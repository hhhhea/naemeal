package mega.naemeal.enrollment.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.enrollment.dto.request.EnrollmentRequestDto;
import mega.naemeal.enrollment.dto.response.EnrollmentResponseDto;
import mega.naemeal.enrollment.entity.Enrollment;
import mega.naemeal.enrollment.repository.EnrollmentRepository;
import mega.naemeal.enums.EnrollmentStatus;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

  private final EnrollmentRepository enrollmentRepository;
  private final CookProgramRepository cookProgramRepository;
  private final RedissonClient redissonClient;

  //참여 신청
  @Override
  public EnrollmentResponseDto attend(Long postId, EnrollmentRequestDto requestDto, String userId) {
    String username = requestDto.getUsername();
    String tel = requestDto.getTel();

    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("이름을 입력해주세요.");
    }
    if (tel == null || tel.isEmpty()) {
      throw new IllegalArgumentException("휴대전화 번호를 입력해주세요.");
    }

    CookProgram post = cookProgramRepository.findByPostId(postId).orElseThrow(
        () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

    //모집기간
    if (post.getDeadline().isBefore(LocalDateTime.now())) {
      throw new IllegalArgumentException("모집 완료된 게시글에는 참여신청을 할 수 없습니다.");
    }
    //참가 인원 수 체크
    int maxEnrollmentNum = post.getMaxEnrollmentNum();
    long enrollmentCount = enrollmentRepository.countByPost_PostId(postId);
    EnrollmentStatus status;
    // 이전 날짜로 변경되었을 때 EnrollmentStatus 업데이트
    if (enrollmentCount < maxEnrollmentNum && post.getDeadline().isAfter(LocalDateTime.now())) {
      status = EnrollmentStatus.APPLIED;
    } else if (enrollmentCount >= maxEnrollmentNum && post.getDeadline().isAfter(LocalDateTime.now())) {
      status = EnrollmentStatus.CANCELLED;
    } else if (enrollmentCount >= maxEnrollmentNum && post.getDeadline().isBefore(LocalDateTime.now())) {
      status = EnrollmentStatus.COMPLETED;
    } else if (post.getDeadline().isBefore(LocalDateTime.now())) { // 이전 날짜로 변경된 경우
      status = EnrollmentStatus.COMPLETED;
    } else {
      status = EnrollmentStatus.CANCELLED; // maxEnrollmentNum 이 설정되지 않은 경우
    }
    //Redisson RLock 객체 생성
    String lockName = "enrollment_lock_" + postId;
    RLock lock = redissonClient.getLock(lockName);

    try {
      lock.lock();

      //이미 참여신청한 경우 중복체크
      List<Enrollment> existingEnrollment = enrollmentRepository.findByUserIdAndPostId(userId, postId);
      if (!existingEnrollment.isEmpty()) {
        throw new IllegalArgumentException("이미 해당 게시글에 참여하셨습니다.");
      }
      // enrollment 엔티티 생성 및 저장
      Enrollment enrollment = new Enrollment(postId, requestDto, userId, post);
      //status 업데이트
      enrollment.updateStatus(status);
      enrollmentRepository.save(enrollment);

      return new EnrollmentResponseDto(enrollment);

    } finally {
      lock.unlock();
    }
  }

  //참여 신청 취소
  @Override
  public void cancel(Long postId, String userId, Long enrollmentId) {
    CookProgram post = cookProgramRepository.findByPostId(postId).orElseThrow(
        () -> new IllegalArgumentException("모집글이 존재하지 않습니다.")
    );
    Enrollment enrollment = enrollmentRepository.findByEnrollmentId(enrollmentId).orElseThrow(
        () -> new IllegalArgumentException("참가 신청한 게시글이 아닙니다,")
    );
    //본인의 참여 신청이 아닌 경우
    if (!enrollment.getUserId().equals(userId)) {
      throw new IllegalArgumentException("게시글 참여신청자와 일치하지 않습니다.");
    }
    //참여 취소 통과
    enrollmentRepository.delete(enrollment);
  }

  // 나의 참여 내역 전체 보기
  @Override
  public List<EnrollmentResponseDto> getAllMyEnrollments(String userId) {

    List<Enrollment> allMyEnrollments = enrollmentRepository.findAllByUserIdAndEnrollmentStatusOrderByCreatedAtDesc(userId,
        EnrollmentStatus.COMPLETED);
    List<EnrollmentResponseDto> responseDto = new ArrayList<>();
    for (Enrollment enrollment : allMyEnrollments) {
      responseDto.add(new EnrollmentResponseDto(enrollment));
    }
    return responseDto;
  }

  //게시글 별 참여 신청 내역 조회
  @Override
  public List<EnrollmentResponseDto> getEnrollmentList(Long postId) {
    List<Enrollment> postEnrollments = enrollmentRepository.findByPostIdOrderByCreatedAtDesc(postId);
    List<EnrollmentResponseDto> responseDto = new ArrayList<>();
    for (Enrollment enrollment : postEnrollments) {
      responseDto.add (new EnrollmentResponseDto(enrollment));
    }
    return responseDto;
  }
}

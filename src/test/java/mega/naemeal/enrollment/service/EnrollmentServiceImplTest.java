package mega.naemeal.enrollment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.enrollment.dto.request.EnrollmentRequestDto;
import mega.naemeal.enrollment.dto.response.EnrollmentResponseDto;
import mega.naemeal.enrollment.entity.Enrollment;
import mega.naemeal.enrollment.repository.EnrollmentRepository;
import mega.naemeal.enums.EnrollmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

  @Mock
  private EnrollmentRepository enrollmentRepository;

  @Mock
  private CookProgramRepository cookProgramRepository;

  @Mock
  private RedissonClient redissonClient;

  @InjectMocks
  private EnrollmentServiceImpl enrollmentService;


  @Test
  @DisplayName("참여신청 성공 테스트")
  public void attend() {
    // given
    Long postId = 1L;
    String userId = "minji1234";
    EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
        .userId(userId)
        .username("minji")
        .tel("010-1234-5678")
        .build();
    CookProgram post = CookProgram.builder()
        .deadline(LocalDateTime.now().plusDays(1))
        .maxEnrollmentNum(10)
        .build();

    // RedissonClient 모의 객체 생성
    RedissonClient mockRedissonClient = Mockito.mock(RedissonClient.class);
    RLock mockLock = Mockito.mock(RLock.class);
//    when(mockRedissonClient.getLock(anyString())).thenReturn(mockLock);

    // EnrollmentServiceImpl 내에서 redissonClient 필드를 사용할 때 모의 객체를 반환하도록 설정
    when(redissonClient.getLock(anyString())).thenReturn(mockLock);

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.of(post));
    when(enrollmentRepository.findByUserIdAndPostId(eq(userId), eq(postId))).thenReturn(new ArrayList<>());
    when(enrollmentRepository.countByPost_PostId(eq(postId))).thenReturn(0L);

    // when
    EnrollmentResponseDto responseDto = enrollmentService.attend(postId, requestDto, userId);

    // then
    assertNotNull(responseDto);
    verify(enrollmentRepository, times(1)).save(any(Enrollment.class));
  }


  @Test
  @DisplayName("참여신청 실패 - 유효하지 않은 사용자명")
  public void attend_InvalidUsername() {
    // given
    Long postId = 1L;
    String userId = "danielle123";
    EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
        .username("") // 빈 이름 설정
        .tel("010-1234-5678")
        .build();

    // when, then
    assertThrows(IllegalArgumentException.class, () ->
        enrollmentService.attend(postId, requestDto, userId)
    );
    verify(enrollmentRepository, never()).save(any(Enrollment.class));
  }


  @Test
  @DisplayName("참여신청 실패 - 유효하지 않은 연락처")
  public void attend_InvalidTel() {
    // given
    Long postId = 1L;
    String userId = "danielle123";
    EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
        .username("danielle")
        .tel("") // 빈 휴대전화 번호 설정
        .build();

    // when, then
    assertThrows(IllegalArgumentException.class, () ->
        enrollmentService.attend(postId, requestDto, userId)
    );
    verify(enrollmentRepository, never()).save(any(Enrollment.class));
  }


  @Test
  @DisplayName("참여신청 실패 - 유효하지 않은 게시글")
  public void attend_InvalidPost() {
    // given
    Long postId = 1L;
    String userId = "danielle123";
    EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
        .username("danielle")
        .tel("010-1234-5678")
        .build();

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.empty());  // 게시글이 존재하지 않는 경우

    // when, then
    assertThrows(IllegalArgumentException.class, () ->
        enrollmentService.attend(postId, requestDto, userId)
    );
    verify(enrollmentRepository, never()).save(any(Enrollment.class));
  }



  @Test
  @DisplayName("참여(신청) 취소 성공 테스트")
  public void cancel() {
    // given
    Long postId = 1L;
    String userId = "pham1234";
    Long enrollmentId = 2L;

    CookProgram post = new CookProgram();

    Enrollment enrollment = Enrollment.builder()
        .userId(userId)
        .build();

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.of(post));
    when(enrollmentRepository.findByEnrollmentId(eq(enrollmentId))).thenReturn(Optional.of(enrollment));
    doNothing().when(enrollmentRepository).delete(eq(enrollment));

    // when
    enrollmentService.cancel(postId, userId, enrollmentId);

    // then
    verify(enrollmentRepository, times(1)).delete(eq(enrollment));
  }


  @Test
  @DisplayName("참여(신청) 취소 실패 - 유효하지 않은 게시글 id")
  public void cancel_InvalidPostId() {
    // given
    Long postId = 1L;
    String userId = "pham1234";
    Long enrollmentId = 2L;

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.empty());

    // when, then
    assertThrows(IllegalArgumentException.class,
        () -> enrollmentService.cancel(postId, userId, enrollmentId));
  }

  @Test
  @DisplayName("참여(신청) 취소 실패 - 유효하지 않은 등록 id")
  public void cancel_InvalidEnrollmentId() {
    // given
    Long postId = 1L;
    String userId = "pham1234";
    Long enrollmentId = 2L;

    CookProgram post = new CookProgram();

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.of(post));
    when(enrollmentRepository.findByEnrollmentId(eq(enrollmentId))).thenReturn(Optional.empty());

    // when, then
    assertThrows(IllegalArgumentException.class,
        () -> enrollmentService.cancel(postId, userId, enrollmentId));
  }

  @Test
  @DisplayName("참여(신청) 취소 실패 - 유효하지 않은 사용자 id")
  public void cancel_InvalidUserId() {
    // given
    Long postId = 1L;
    String userId = "pham1234";
    Long enrollmentId = 2L;

    CookProgram post = new CookProgram();

    Enrollment enrollment = Enrollment.builder()
        .userId("haerin1234")
        .build();

    when(cookProgramRepository.findByPostId(eq(postId))).thenReturn(Optional.of(post));
    when(enrollmentRepository.findByEnrollmentId(eq(enrollmentId))).thenReturn(Optional.of(enrollment));

    // when, then
    assertThrows(IllegalArgumentException.class,
        () -> enrollmentService.cancel(postId, userId, enrollmentId));
  }



  @Test
  @DisplayName("나의 등록(참여) 내역 전체 조회 테스트")
  void getAllMyEnrollments() {
    // given
    String userId = "haerin1234";

    Enrollment myEnroll1 = Enrollment.builder()
        .enrollmentId(1L)
        .userId(userId)
        .enrollmentStatus(EnrollmentStatus.COMPLETED)
        .post(new CookProgram())
        .build();

    Enrollment myEnroll2 = Enrollment.builder()
        .enrollmentId(2L)
        .userId(userId)
        .enrollmentStatus(EnrollmentStatus.COMPLETED)
        .post(new CookProgram())
        .build();

    Enrollment myEnroll3 = Enrollment.builder()
        .enrollmentId(3L)
        .userId(userId)
        .enrollmentStatus(EnrollmentStatus.COMPLETED)
        .post(new CookProgram())
        .build();

    List<Enrollment> enrollments = new ArrayList<>();
    enrollments.add(myEnroll1);
    enrollments.add(myEnroll2);
    enrollments.add(myEnroll3);

    when(enrollmentRepository.findAllByUserIdAndEnrollmentStatusOrderByCreatedAtDesc(eq(userId),
        eq(EnrollmentStatus.COMPLETED))).thenReturn(enrollments);

    // when
    List<EnrollmentResponseDto> result = enrollmentService.getAllMyEnrollments(userId);

    // then
    assertEquals(3, result.size());
    assertEquals(myEnroll1.getEnrollmentId(), result.get(0).getEnrollmentId());
    assertEquals(myEnroll2.getEnrollmentId(), result.get(1).getEnrollmentId());
    assertEquals(myEnroll3.getEnrollmentId(), result.get(2).getEnrollmentId());
  }


  @Test
  @DisplayName("특정 게시물에 대한 참여 내역 조회 테스트")
  void getEnrollment() {
    // given
    Long postId = 1L;
    CookProgram commonPost = new CookProgram();

    Enrollment enrollment1 = Enrollment.builder()
        .enrollmentId(10L)
        .postId(postId)
        .post(commonPost)
        .build();

    Enrollment enrollment2 = Enrollment.builder()
        .enrollmentId(10L)
        .postId(postId)
        .post(commonPost)
        .build();

    List<Enrollment> enrollments = new ArrayList<>();
    enrollments.add(enrollment1);
    enrollments.add(enrollment2);

    when(enrollmentRepository.findByPostIdOrderByCreatedAtDesc(eq(postId))).thenReturn(enrollments);

    // when
    List<EnrollmentResponseDto> result = enrollmentService.getEnrollmentList(postId);

    // then
    assertEquals(2, result.size());
    assertEquals(enrollment1.getEnrollmentId(), result.get(0).getEnrollmentId());
    assertEquals(enrollment2.getEnrollmentId(), result.get(1).getEnrollmentId());
  }
}
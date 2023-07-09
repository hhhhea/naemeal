package mega.naemeal.admin.service;

import lombok.RequiredArgsConstructor;
import mega.naemeal.admin.entity.Notice;
import mega.naemeal.admin.repository.NoticeRepository;
import mega.naemeal.comment.dto.response.CommentCautionResponseDto;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.comment.entity.CommentManage;
import mega.naemeal.comment.repository.CommentManageRepository;
import mega.naemeal.comment.repository.CommentRepository;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.cookprogram.repository.CookProgramRepository;
import mega.naemeal.enums.UserStatus;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminServiceImpl implements AdminService {

  private final NoticeRepository noticeRepository;

  private final CookProgramRepository cookProgramRepository;

  private final CommentRepository commentRepository;

  private final ProfileRepository profileRepository;

  private final CommentManageRepository commentManageRepository;

  //공지사항 작성
  @Override
  public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {
    Notice notice = new Notice(requestDto.getTitle(), requestDto.getContent());
    noticeRepository.save(notice);
    return new NoticeResponseDto(notice);

  }

  //공지사항 전체 조회
  @Override
  public List<NoticeResponseDto> getNoticeList() {

    List<Notice> notices = noticeRepository.findAllByOrderByModifiedAtDesc();
    List<NoticeResponseDto> noticeResponseDtolist = new ArrayList<>();

    for (Notice notice : notices) {
      noticeResponseDtolist.add(new NoticeResponseDto(notice));
    }
    return noticeResponseDtolist;
  }

  //공지사항 선택 조회
  @Override
  public NoticeResponseDto findNotice(Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(
        () -> new IllegalArgumentException("조회하려는 공지사항이 없습니다.")
    );
    return new NoticeResponseDto(notice);
  }


  //공지사항 수정
  @Override
  public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다."));

    notice.update(requestDto.getTitle(), requestDto.getContent());

    return new NoticeResponseDto(notice);
  }


  //공지사항 삭제
  @Override
  public void deleteNotice(Long noticeId) {
    Notice notice = noticeRepository.findById(noticeId).orElseThrow(
        () -> new IllegalArgumentException("삭제하고자 하는 공지사항이 없습니다.")
    );
    noticeRepository.delete(notice);

  }

  //게시글 삭제(관련된 댓글도 모두 삭제)
  @Override
  public void adminDeletePost(Long postId) {
    CookProgram post = cookProgramRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

    cookProgramRepository.delete(post);

    commentRepository.deleteByPostId(postId); // delete from comment where post_id = ?
  }

  //댓글 삭제
  @Override
  public void adminDeleteComment(Long postId, Long commentId) {
    cookProgramRepository.findById(postId).orElseThrow(
        () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
    );

    Comment comment = commentRepository.findById(commentId).orElseThrow(
        () -> new IllegalArgumentException("삭제 할 댓글이 없습니다.")
    );

    commentRepository.delete(comment);

  }

  //유저 활동 정지
  @Override
  public void blockUser(String userId) {
    Profile profile = profileRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("프로필이 존재하지 않습니다.")
    );

    profile.changeUserEnum(UserStatus.BLOCKED);

//    profileRepository.save(profile);
  }

  //유저 활동 재개
  @Override
  public void resumeUser(String userId) {
    Profile profile = profileRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("프로필이 존재하지 않습니다.")
    );
    profile.changeUserEnum(UserStatus.STANDARD);
//    profileRepository.save(profile);
  }

  //신고 유저 조회
  @Override
  public List<CommentCautionResponseDto> getCautionUserList() {

    List<CommentManage> commentManages = commentManageRepository.findAllByOrderByModifiedAtDesc();
    List<CommentCautionResponseDto> CommentCautionResponseDtoList = new ArrayList<>();

    for (CommentManage commentManage : commentManages) {
      CommentCautionResponseDtoList.add(new CommentCautionResponseDto(commentManage));
    }
    return CommentCautionResponseDtoList;
  }

}





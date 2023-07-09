package mega.naemeal.admin.service;

import mega.naemeal.admin.dto.request.NoticeRequestDto;
import mega.naemeal.admin.dto.response.NoticeResponseDto;
import mega.naemeal.comment.dto.response.CommentCautionResponseDto;

import java.util.List;


public interface AdminService {

  NoticeResponseDto createNotice(NoticeRequestDto requestDto);

  List<NoticeResponseDto> getNoticeList();

  NoticeResponseDto findNotice(Long noticeId);

  NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto);

  void deleteNotice(Long noticeId);

  void adminDeletePost(Long postId);

  void adminDeleteComment(Long postId, Long commentId);

  void blockUser(String userId);

  void resumeUser(String userId);

  List<CommentCautionResponseDto> getCautionUserList();


}

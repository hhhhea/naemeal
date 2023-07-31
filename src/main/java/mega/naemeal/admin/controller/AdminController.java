package mega.naemeal.admin.controller;

import lombok.RequiredArgsConstructor;
import mega.naemeal.admin.dto.request.NoticeRequestDto;
import mega.naemeal.admin.dto.response.NoticeResponseDto;
import mega.naemeal.admin.service.AdminService;
import mega.naemeal.comment.dto.response.CommentReportResponseDto;
import mega.naemeal.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/admin", produces = "application/json;charset=UTF-8")
public class AdminController {

  private final AdminService adminService;

  //공지사항 작성
  @PostMapping("/notices")
  public ResponseEntity<ApiResponse> createNotice(@RequestBody NoticeRequestDto requestDto) {
    NoticeResponseDto data = adminService.createNotice(requestDto);
    ApiResponse responseDto = new ApiResponse("(admin) 공지사항 작성이 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

  }

  //공지사항 조회
  @GetMapping("/notices")
  public ResponseEntity<ApiResponse> getNoticeList() {
    List<NoticeResponseDto> data = adminService.getNoticeList();
    ApiResponse responseDto = new ApiResponse("(admin) 공지사항 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //공지사항 선택 조회
  @GetMapping("/notices/{noticeId}")
  public ResponseEntity<ApiResponse> findNotice(@PathVariable Long noticeId) {
    NoticeResponseDto data = adminService.findNotice(noticeId);
    ApiResponse responseDto = new ApiResponse("(admin) 공지사항 선택 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //공지사항 수정
  @PatchMapping("/notices/{noticeId}")
  public ResponseEntity<ApiResponse> updateNotice(@PathVariable Long noticeId,
                                                  @RequestBody NoticeRequestDto requestDto) {
    NoticeResponseDto data = adminService.updateNotice(noticeId, requestDto);
    ApiResponse responseDto = new ApiResponse("(admin) 공지사항 수정이 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //공지사항 삭제
  @DeleteMapping("/notices/{noticeId}")
  public ResponseEntity<ApiResponse> deleteNotice(@PathVariable Long noticeId) {
    adminService.deleteNotice(noticeId);
    ApiResponse responseDto = new ApiResponse("(admin) 공지사항 삭제가 완료되었습니다.");
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //게시글 삭제
  @DeleteMapping("/posts/{postId}")
  public ResponseEntity<ApiResponse> adminDeletePost(@PathVariable Long postId) {
    ApiResponse responseDto = new ApiResponse("(admin) 해당 게시글이 삭제되었습니다.");
    adminService.adminDeletePost(postId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);//responseDto
  }

  //댓글 삭제
  @DeleteMapping("/notices/{noticeId}/comments/{commentId}")
  public ResponseEntity<ApiResponse> adminDeleteComment(@PathVariable Long postId,
                                                        @PathVariable Long commentId) {
    ApiResponse responseDto = new ApiResponse("(admin) 해당 댓글이 삭제되었습니다.");
    adminService.adminDeleteComment(postId, commentId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);//responseDto
  }

  //유저 활동 정지
  @PatchMapping("/users/{userId}/block")
  public ResponseEntity<ApiResponse> blockUser(@PathVariable String userId) {
    ApiResponse responseDto = new ApiResponse("(admin) 해당 유저가 활동 정지되었습니다.");
    adminService.blockUser(userId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //유저 활동 재개
  @PatchMapping("/users/{userId}/resume")
  public ResponseEntity<ApiResponse> resumeUser(@PathVariable String userId) {
    ApiResponse responseDto = new ApiResponse("(admin) 해당 유저가 활동 재개되었습니다.");
    adminService.resumeUser(userId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  // 신고받은 유저 조회
  @GetMapping("/users/blacklist")
  public ResponseEntity<ApiResponse> getCautionUserList(@PathVariable Long postId) {
    List<CommentReportResponseDto> data = adminService.getCautionUserList();
    ApiResponse responseDto = new ApiResponse("(admin) 신고 받은 유저 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

}

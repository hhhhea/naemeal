package mega.naemeal.comment.controller;

import lombok.RequiredArgsConstructor;
import mega.naemeal.comment.dto.request.CommentReportRequestDto;
import mega.naemeal.comment.dto.request.CommentRequestDto;
import mega.naemeal.comment.dto.response.CommentReportResponseDto;
import mega.naemeal.comment.dto.response.CommentResponseDto;
import mega.naemeal.comment.service.CommentServiceImpl;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts/{postId}/comments", produces = "application/json;charset=UTF-8")
public class CommentController {

  private final CommentServiceImpl commentService;

  // /posts/1/comments/2

  // 댓글 작성
  @PostMapping
  public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long postId,
                                                          @RequestBody CommentRequestDto requestDto,
                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
    CommentResponseDto commentResponseDto = commentService.createComment(postId, requestDto, userDetails);
    return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
  }

  // 댓글 수정
  @PatchMapping("/{commentId}")
  public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long postId,
      @RequestBody CommentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long commentId) {
    CommentResponseDto commentResponseDto = commentService.updateComment(postId, requestDto, userDetails, commentId);
    return ResponseEntity.status(HttpStatus.OK).body(commentResponseDto);
  }

  // 댓글 삭제
  @DeleteMapping("/{commentId}")
  public ResponseEntity deleteComment(@PathVariable Long postId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetails,
                                      @PathVariable Long commentId) {
    commentService.deleteComment(postId, userDetails, commentId);

    return new ResponseEntity<>("삭제 완료!", HttpStatus.OK);
  }

  // 게시글의 댓글 조회
  @GetMapping
  public ResponseEntity<ApiResponse> getCommentList(@PathVariable Long postId) {
    List<CommentResponseDto> data = commentService.getCommentList(postId);
    ApiResponse responseDto = new ApiResponse("댓글 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  // 댓글 신고
  @PostMapping("/{commentId}/report")
  public ResponseEntity<CommentReportResponseDto> reportComment(@PathVariable Long postId,
                                                                 @PathVariable Long commentId, @RequestBody CommentReportRequestDto requestDto) {
    CommentReportResponseDto responseDto = new CommentReportResponseDto(postId, commentId, requestDto, "댓글이 신고되었습니다.");
    commentService.reportComment(postId, commentId, requestDto.getReportedReason());
    return ResponseEntity.status(HttpStatus.OK).body(responseDto);
  }
}

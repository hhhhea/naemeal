package mega.naemeal.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.comment.dto.request.CommentReportRequestDto;
import mega.naemeal.comment.entity.CommentReportManage;

@Getter
@NoArgsConstructor
public class CommentReportResponseDto {

  private Long postId;
  private Long commentId;
  private String reportedReason;
  private String message;

  public CommentReportResponseDto(
      CommentReportManage commentReportManage) {
    this.reportedReason = commentReportManage.getReportedReason();
    this.commentId = commentReportManage.getCommentId();
  }

  public CommentReportResponseDto(Long postId, Long commentId,
                                   CommentReportRequestDto requestDto, String message) {
    this.postId = postId;
    this.commentId = commentId;
    this.reportedReason = requestDto.getReportedReason();
    this.message = message;

  }
}
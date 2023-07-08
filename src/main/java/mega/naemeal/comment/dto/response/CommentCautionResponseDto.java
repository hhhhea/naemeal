package mega.naemeal.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.comment.dto.request.CommentCautionRequestDto;
import mega.naemeal.comment.entity.CommentManage;

@Getter
@NoArgsConstructor
public class CommentCautionResponseDto {

  private Long postId;
  private Long commentId;
  private String cautionReason;
  private String message;

  public CommentCautionResponseDto(
      CommentManage commentManage) {
    this.cautionReason = commentManage.getCautionReason();
    this.commentId = commentManage.getCommentId();
  }

  public CommentCautionResponseDto(Long postId, Long commentId,
                                   CommentCautionRequestDto requestDto, String message) {
    this.postId = postId;
    this.commentId = commentId;
    this.cautionReason = requestDto.getCautionReason();
    this.message = message;

  }
}
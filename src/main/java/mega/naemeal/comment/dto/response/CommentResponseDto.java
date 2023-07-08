package mega.naemeal.comment.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.comment.entity.Comment;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {

  private Long commentId;
  private Long postId;
  private String comments;
  private String nickname;
  private LocalDateTime modifiedAt;

  public CommentResponseDto(Comment comment) {
    this.commentId = comment.getCommentId();
    this.postId = comment.getPostId();
    this.comments = comment.getComments();
    this.nickname = comment.getNickname();
    this.modifiedAt = comment.getModifiedAt();
  }
}

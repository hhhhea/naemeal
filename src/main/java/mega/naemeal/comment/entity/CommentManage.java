package mega.naemeal.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;

@Getter
@Entity
@NoArgsConstructor
public class CommentManage extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long cautionId;

  @Column(nullable = false)
  private String cautionUserId;

  @Column(nullable = false)
  private String cautionReason;

  @Column(nullable = false)
  private Long commentId;


  public CommentManage(String cautionUserId, Long commentId, String cautionReason) {
    this.cautionUserId = cautionUserId;
    this.commentId = commentId;
    this.cautionReason = cautionReason;
  }
}

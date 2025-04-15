package mega.naemeal.comment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentReportManage extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long reportedId;

  @Column(nullable = false)
  private String reportedUserId;

  @Column(nullable = false)
  private String reportedReason;

  @Column(nullable = false)
  private Long commentId;


  public CommentReportManage(String reportedUserId, Long commentId, String cautionReason) {
    this.reportedUserId = reportedUserId;
    this.commentId = commentId;
    this.reportedReason = cautionReason;
  }

}

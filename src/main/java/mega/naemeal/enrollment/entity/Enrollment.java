package mega.naemeal.enrollment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.enrollment.dto.request.EnrollmentRequestDto;
import mega.naemeal.enums.EnrollmentStatus;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment extends TimeStamp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long enrollmentId;

  @Enumerated(value = EnumType.STRING)
  @Column(nullable = false)
  private EnrollmentStatus enrollmentStatus = EnrollmentStatus.APPLIED;

  private String userId;

  @Column(nullable = false)
  private String username;

  @Column
  private String tel;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false)
  private CookProgram post;

  @Column(name = "post_id", insertable=false, updatable=false)
  private Long postId;

  @Column
  private int maxEnrollmentNum;
  @Column
  private LocalDateTime deadline;


  public Enrollment(Long postId, EnrollmentRequestDto requestDto, String userId, CookProgram post) {
    this.userId = userId;
    this.username = requestDto.getUsername();
    this.tel = requestDto.getTel();
    this.post = post;
    this.maxEnrollmentNum = post.getMaxEnrollmentNum();
    this.deadline = post.getDeadline();
  }

  public void updateStatus(EnrollmentStatus newStatus) {
    this.enrollmentStatus = newStatus;
  }
}
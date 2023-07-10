package mega.naemeal.enrollment.dto.response;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.enrollment.entity.Enrollment;
import mega.naemeal.enums.EnrollmentStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDto {
  private Long postId;
  private String userId;
  private String title;
  private String area;
  private String username;
  private EnrollmentStatus enrollmentStatus;
  private Long enrollmentId;
  private String tel;

  public EnrollmentResponseDto(Enrollment enrollment) {
    this.postId = enrollment.getPost().getPostId();
    this.userId = enrollment.getUserId();
    this.title = enrollment.getPost().getTitle();
    this.area = enrollment.getPost().getArea();
    this.enrollmentId = enrollment.getEnrollmentId();
    this.username = enrollment.getUsername();
    this.enrollmentStatus = enrollment.getEnrollmentStatus();
    this.tel = enrollment.getTel();
  }
}

package mega.naemeal.enrollment.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.enums.EnrollmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EnrollmentTest {

  @Test
  @DisplayName("업데이트된 상태 확인")
  public void updateStatus() {
    // given
    Enrollment enrollment = new Enrollment();
    EnrollmentStatus newStatus = EnrollmentStatus.APPLIED;

    // when
    enrollment.updateStatus(newStatus);

    // then
    assertEquals(newStatus, enrollment.getEnrollmentStatus());
  }
}
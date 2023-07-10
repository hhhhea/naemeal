package mega.naemeal.enrollment.repository;

import java.util.List;
import java.util.Optional;
import mega.naemeal.enrollment.entity.Enrollment;
import mega.naemeal.enums.EnrollmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {
  Optional<Enrollment> findByEnrollmentId(Long enrollmentId);
  List<Enrollment> findAllByUserIdAndEnrollmentStatusOrderByCreatedAtDesc(String userId, EnrollmentStatus status);
  List<Enrollment> findByUserIdAndPostId(String userId, Long postId);
  List<Enrollment> findByPostIdOrderByCreatedAtDesc(Long postId);
  List<Enrollment> findAllByOrderByCreatedAtDesc();

  long countByPost_PostId(Long postId);
}
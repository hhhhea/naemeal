package mega.naemeal.cookprogram.repository;


import mega.naemeal.cookprogram.entity.CookProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CookProgramRepository extends JpaRepository<CookProgram, Long> {
    Optional<CookProgram> findByPostId(Long postId);
    Optional<CookProgram> findByPostIdAndUserId(Long postId, String userId);
    List<CookProgram> findAllByOrderByCreatedAtDesc();
}

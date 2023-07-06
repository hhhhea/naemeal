package mega.naemeal.comment.repository;

import java.util.List;
import java.util.Optional;

import mega.naemeal.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Override
    Optional<Comment> findById(Long commentId);

    Optional<Comment> findByUserId(String userId);

    List<Comment> findByPostId(Long postId);

    void deleteByPostId(Long postId);

}

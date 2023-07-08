package mega.naemeal.comment.repository;

import mega.naemeal.comment.entity.CommentManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentManageRepository extends JpaRepository<CommentManage, Long> {


  @Override
  Optional<CommentManage> findById(Long cautionId);

  List<CommentManage> findAllByOrderByModifiedAtDesc();


}

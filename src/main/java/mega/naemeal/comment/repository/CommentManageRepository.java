package mega.naemeal.comment.repository;

import mega.naemeal.comment.entity.CommentReportManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentManageRepository extends JpaRepository<CommentReportManage, Long> {


  @Override
  Optional<CommentReportManage> findById(Long cautionId);

  List<CommentReportManage> findAllByOrderByModifiedAtDesc();


}

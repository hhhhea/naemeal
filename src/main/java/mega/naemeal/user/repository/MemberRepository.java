package mega.naemeal.user.repository;

import java.util.Optional;

import mega.naemeal.user.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

  Optional<Member> findByUserId(String userId);

}

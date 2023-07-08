package mega.naemeal.user.repository;

import java.util.Optional;
import mega.naemeal.security.UserDetailsImpl;
import mega.naemeal.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByUserId(String userId);
  Optional<Object> findByUserId(UserDetailsImpl userDetails);
}

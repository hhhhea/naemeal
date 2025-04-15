package mega.naemeal.profile.repository;

import java.util.Optional;
import mega.naemeal.profile.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, String> {

  Optional<Profile> findByUserId(String userId);

}

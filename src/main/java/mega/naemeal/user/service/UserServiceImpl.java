package mega.naemeal.user.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mega.naemeal.enums.UserRoleEnum;
import mega.naemeal.jwt.AuthenticatedUserInfoDto;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
import mega.naemeal.user.dto.PasswordcheckRequestDto;
import mega.naemeal.user.dto.SigninRequestDto;
import mega.naemeal.user.dto.SignupRequestDto;
import mega.naemeal.user.entity.User;
import mega.naemeal.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
  private final ProfileRepository profileRepository;
  private static String basicProfileImage = "profile/profile-basic.jpg";
  public static final String CLOUD_FRONT_DOMAIN_NAME = "https://d261u93iebql1x.cloudfront.net/";

  @Override
  public void signup(SignupRequestDto requestDto) {
    String userId = requestDto.getUserId();
    String password = passwordEncoder.encode(requestDto.getPassword());
    String nickname = requestDto.getNickname();

    Optional<User> found = userRepository.findByUserId(requestDto.getUserId());
    if (found.isPresent()) {
      throw new IllegalArgumentException("중복된 아이디가 존재합니다.");
    }
    UserRoleEnum role = UserRoleEnum.USER;

    if (requestDto.getAdminToken() != null) {
      if (!requestDto.getAdminToken().equals(ADMIN_TOKEN)) {
        throw new IllegalArgumentException("관리자 암호가 일치하지 않아 등록할 수 없습니다.");
      }
      role = UserRoleEnum.ADMIN;
    }

    User user = new User(userId, password, nickname, role);
    Profile profile = new Profile(userId, nickname, CLOUD_FRONT_DOMAIN_NAME+basicProfileImage);
    userRepository.save(user);
    profileRepository.save(profile);
  }

  @Override
  public AuthenticatedUserInfoDto signin(SigninRequestDto requestDto) {
    User user = userRepository.findByUserId(requestDto.getUserId()).orElseThrow(
        () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
    );
    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return new AuthenticatedUserInfoDto(user.getRole(), user.getUserId());
  }

  @Override
  public void signout(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
  }

  @Override
  public void dropout(String userId, PasswordcheckRequestDto requestDto) {
    User user = userRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
    );
    if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    user.changeRole(UserRoleEnum.UNREGISTER);
    userRepository.save(user);
  }

}

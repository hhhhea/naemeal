package mega.naemeal.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mega.naemeal.enums.UserRoleEnum;
import mega.naemeal.jwt.AuthenticatedUserInfoDto;
import mega.naemeal.profile.repository.ProfileRepository;
import mega.naemeal.user.dto.PasswordcheckRequestDto;
import mega.naemeal.user.dto.SigninRequestDto;
import mega.naemeal.user.dto.SignupRequestDto;
import mega.naemeal.user.entity.User;
import mega.naemeal.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private ProfileRepository profileRepository;

  @InjectMocks
  private UserServiceImpl userService;


  @Test
  @DisplayName("회원가입 성공 테스트")
  public void signupTest() {
    // given
    SignupRequestDto requestDto = SignupRequestDto.builder()
        .userId("minji123")
        .nickname("NewJeans")
        .password("newjeans0722")
        .build();

    // when
    userService.signup(requestDto);

    // then
    verify(userRepository, times(1)).save(any(User.class));
  }


  @Test
  @DisplayName("로그인 성공 테스트")
  void siginin() {
    // given
    SigninRequestDto requestDto = SigninRequestDto.builder()
        .userId("baesuzy")
        .password("missa")
        .build();

    String password = requestDto.getPassword();

    User user = User.builder()
        .userId("baesuzy")
        .password(passwordEncoder.encode(password))
        .role(UserRoleEnum.USER)
        .build();

    when(userRepository.findByUserId("baesuzy")).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

    // when
    AuthenticatedUserInfoDto result = userService.signin(requestDto);

    // then
    assertEquals(UserRoleEnum.USER, result.getRole());
    assertEquals("baesuzy", result.getUsername());

    verify(userRepository, times(1)).findByUserId("baesuzy");
  }



  @Test
  @DisplayName("회원 탈퇴 테스트")
  void dropout() {
    // given
    String userId = "miyeoncho";

    PasswordcheckRequestDto requestDto = PasswordcheckRequestDto.builder()
        .password("queencard1")
        .build();

    User user = User.builder()
        .userId(userId)
        .password(passwordEncoder.encode("password"))
        .role(UserRoleEnum.DROPPED)
        .build();

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(requestDto.getPassword(), user.getPassword())).thenReturn(true);

    // when
    UserService userService = new UserServiceImpl(userRepository, passwordEncoder, profileRepository);
    userService.dropout(userId, requestDto);

    // then
    verify(userRepository, times(1)).findByUserId(userId);
    verify(userRepository, times(1)).save(user);
    assertEquals(UserRoleEnum.DROPPED, user.getRole());
  }
}
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
import mega.naemeal.user.entity.Member;
import mega.naemeal.user.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private ProfileRepository profileRepository;

  @InjectMocks
  private MemberServiceImpl userService;


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
    verify(memberRepository, times(1)).save(any(Member.class));
  }


//  @Test
//  @DisplayName("로그인 성공 테스트")
//  void signinTest() {
//    // given
//    SigninRequestDto requestDto = SigninRequestDto.builder()
//        .userId("baesuzy")
//        .password("missa")
//        .build();
//
//    String password = requestDto.getPassword();
//
//    Member member = Member.builder()
//        .userId("baesuzy")
//        .password(passwordEncoder.encode(password))
//        .role(UserRoleEnum.USER)
//        .build();
//
//    when(memberRepository.findByUserId("baesuzy")).thenReturn(Optional.of(member));
//    when(passwordEncoder.matches(password, member.getPassword())).thenReturn(true);
//
//    // when
//    AuthenticatedUserInfoDto result = userService.signin(requestDto);
//
//    // then
//    assertEquals(UserRoleEnum.USER, result.getRole());
//    assertEquals("baesuzy", result.getUsername());
//
//    verify(memberRepository, times(1)).findByUserId("baesuzy");
//  }

  @Test
  @DisplayName("회원 탈퇴 테스트")
  void dropoutTest() {
    // given
    String userId = "miyeoncho";

    PasswordcheckRequestDto requestDto = PasswordcheckRequestDto.builder()
        .password("queencard1")
        .build();

    Member member = Member.builder()
        .userId(userId)
        .password(passwordEncoder.encode("password"))
        .role(UserRoleEnum.USER)
        .build();

    when(memberRepository.findByUserId(userId)).thenReturn(Optional.of(member));
    when(passwordEncoder.matches(requestDto.getPassword(), member.getPassword())).thenReturn(true);

    // when
    userService.dropout(userId, requestDto);

    // then
    verify(memberRepository, times(1)).findByUserId(userId);
    verify(memberRepository, times(1)).save(member);
    assertEquals(UserRoleEnum.DROPPED, member.getRole());
  }
}
package mega.naemeal.user.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import mega.naemeal.enums.UserRoleEnum;
import mega.naemeal.jwt.AuthenticatedUserInfoDto;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
import mega.naemeal.security.UserDetailsServiceImpl;
import mega.naemeal.user.dto.PasswordcheckRequestDto;
import mega.naemeal.user.dto.SigninRequestDto;
import mega.naemeal.user.dto.SignupRequestDto;
import mega.naemeal.user.entity.Member;
import mega.naemeal.user.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsServiceImpl userDetailsService;

  private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";
  private final ProfileRepository profileRepository;
  private static String basicProfileImage = "profile/profile-basic.jpg";
  public static final String CLOUD_FRONT_DOMAIN_NAME = "https://d261u93iebql1x.cloudfront.net/";

  @Override
  public void signup(SignupRequestDto requestDto) {
    String userId = requestDto.getUserId();
    String password = passwordEncoder.encode(requestDto.getPassword());
    String nickname = requestDto.getNickname();

    Optional<Member> found = memberRepository.findByUserId(requestDto.getUserId());
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

    System.out.println(userId+"~~~~1");
    System.out.println(password+"~~~~1");
    System.out.println(nickname+"~~~~1");
    System.out.println(role+"~~~~1");
    Member member = new Member(userId, password, nickname, role);
    Profile profile = new Profile(userId, nickname, CLOUD_FRONT_DOMAIN_NAME+basicProfileImage);
    memberRepository.save(member);
    profileRepository.save(profile);
  }

  @Override
  public AuthenticatedUserInfoDto signin(SigninRequestDto requestDto) {
    Member member = memberRepository.findByUserId(requestDto.getUserId()).orElseThrow(
        () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
    );
    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }

    System.out.println(member.getRole()+"~~~~1");
    System.out.println(member.getUserId()+"~~~~1");
    userDetailsService.loadUserByUsername(member.getUserId());

    return new AuthenticatedUserInfoDto(member.getRole(), member.getUserId());
  }


  @Override
  public void dropout(String userId, PasswordcheckRequestDto requestDto) {
    Member member = memberRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
    );
    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    member.changeRole(UserRoleEnum.DROPPED);
    memberRepository.save(member);
  }

}

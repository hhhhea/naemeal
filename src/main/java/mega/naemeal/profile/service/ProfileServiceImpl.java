package mega.naemeal.profile.service;

import lombok.RequiredArgsConstructor;
import mega.naemeal.profile.dto.request.ProfileRequestDto;
import mega.naemeal.profile.dto.response.ProfileResponseDto;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
import mega.naemeal.user.entity.Member;
import mega.naemeal.user.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProfileServiceImpl implements ProfileService {

  private final MemberRepository memberRepository;
  private final ProfileRepository profileRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public ProfileResponseDto getCustomerProfile(String userId) {
    Profile profile = profileRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("프로필을 작성해주세요.")
    );
    return new ProfileResponseDto(userId, profile);
  }

  @Override
  public String getProfileImage(String userId) {
    Profile profile = profileRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("프로필을 작성해주세요.")
    );
    return profile.getImage();
  }

  @Override
  public ProfileResponseDto updateProfile(String userId, ProfileRequestDto requestDto, String imgPath) {
    Member member = memberRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("등록된 아이디가 없습니다.")
    );
    Profile profile = profileRepository.findByUserId(userId).orElseThrow(
        () -> new IllegalArgumentException("프로필이 존재하지 않습니다.")
    );
    if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    profile.updateProfile(requestDto, imgPath);
    member.changeNickname(requestDto.getNickname());

    return new ProfileResponseDto(userId, profile);
  }

}
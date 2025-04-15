package mega.naemeal.profile.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mega.naemeal.profile.dto.request.ProfileRequestDto;
import mega.naemeal.profile.dto.response.ProfileResponseDto;
import mega.naemeal.profile.entity.Profile;
import mega.naemeal.profile.repository.ProfileRepository;
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
class ProfileServiceImplTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private ProfileRepository profileRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private ProfileServiceImpl profileService;

  private static final String USER_ID = "arinlove123";


  @Test
  @DisplayName("프로필 조회 테스트")
  void getCustomerProfile() {
    // given
    Profile profile = new Profile(USER_ID, "010-1234-5678", "choiarin", "arin-image");
    when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

    // when
    ProfileResponseDto responseDto = profileService.getCustomerProfile(USER_ID);

    // then
    assertEquals(USER_ID, responseDto.getUserId());
    assertEquals("010-1234-5678", responseDto.getTel());
    assertEquals("choiarin", responseDto.getNickname());
    assertEquals("arin-image", responseDto.getImage());
  }


  @Test
  @DisplayName("프로필 조회 실패 테스트")
  void getCustomerProfile_notExistedUser() {
    // given
    when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

    // when, then
    assertThrows(IllegalArgumentException.class, () -> profileService.getCustomerProfile(USER_ID));

  }


  @Test
  @DisplayName("프로필 이미지 조회 테스트")
  void getProfileImageTest() {
    // given
    Profile profile = new Profile(USER_ID, "010-1234-5678", "choiarin", "arin-image");
    when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));

    // when
    String imagePath = profileService.getProfileImage(USER_ID);

    // then
    assertEquals("arin-image", imagePath);

  }


  @Test
  @DisplayName("프로필 이미지 조회 실패 테스트")
  void getProfileImage_nonExistentImage() {
    // Given
    when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());

    // when, then
    assertThrows(IllegalArgumentException.class, () -> profileService.getProfileImage(USER_ID));

  }


  @Test
  @DisplayName("프로필 업데이트 성공 테스트")
  void updateProfile() {
    // given
    Member member = Member.builder()
        .userId(USER_ID)
        .password("12345678")
        .nickname("choiarin")
        .build();

    Profile profile = new Profile(USER_ID, "010-1234-5678", "choiarin");

    String imgPath = "arin-image";

    ProfileRequestDto requestDto = ProfileRequestDto.builder()
        .password("1234567")
        .tel("010-1234-5679")
        .nickname("arin")
        .build();

    when(memberRepository.findByUserId(USER_ID)).thenReturn(Optional.of(member));
    when(profileRepository.findByUserId(USER_ID)).thenReturn(Optional.of(profile));
    when(passwordEncoder.matches(any(), any())).thenReturn(true); // 어떤 타입이든 모킹하고 싶을 때 any() 사용 // 여기선 인자 2개

    // when, then
    ProfileResponseDto responseDto = profileService.updateProfile(USER_ID, requestDto, imgPath);
    assertEquals("arin", responseDto.getNickname());
    assertEquals("010-1234-5679", responseDto.getTel());

  }


  @Test
  @DisplayName("프로필 업데이트 실패 테스트")
  void updateProfile_fail() {
    // given
    ProfileRequestDto requestDto = ProfileRequestDto.builder()
        .nickname("arin")
        .tel("010-1234-5679")
        .password("1234567")
        .build();

    String imgPath = "new_image_path";

    // when, then
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> profileService.updateProfile(USER_ID, requestDto, imgPath));

    assertEquals("등록된 아이디가 없습니다.", exception.getMessage());

  }

}
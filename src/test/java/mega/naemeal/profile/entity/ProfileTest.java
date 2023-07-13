package mega.naemeal.profile.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.enums.UserStatus;
import mega.naemeal.profile.dto.request.ProfileRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class ProfileTest {

  private Profile profile;

  @BeforeEach
  public void setUp() {
    profile = new Profile("minji123", "010-2345-6789", "minji", "image.jpg");
  }

  @Test
  @DisplayName("프로필 업데이트 테스트")
  public void updateProfile() {
    // given
    ProfileRequestDto requestDto = ProfileRequestDto.builder()
        .nickname("kimminji")
        .tel("010-1234-5678")
        .password("123456")
        .build();

    String image = "new_image.jpg";

    // when
    profile.updateProfile(requestDto, image);

    // then
    assertEquals("kimminji", profile.getNickname());
    assertEquals("010-1234-5678", profile.getTel());
    assertEquals("new_image.jpg", profile.getImage());
  }


  @Test
  @DisplayName("프로필 이미지 업데이트 테스트")
  public void updateProfileImage() {
    // given
    String image = "new_image.jpg";

    // when
    profile.updateProfileImage(image);

    // then
    assertEquals("new_image.jpg", profile.getImage());
  }


  @Test
  @DisplayName("유저의 상태 변경 테스트")
  public void changeUserEnum() {
    // given
    UserStatus status = UserStatus.BLOCKED;

    // when
    profile.changeUserEnum(status);

    // then
    assertEquals(UserStatus.BLOCKED, profile.getStatus());
  }
}
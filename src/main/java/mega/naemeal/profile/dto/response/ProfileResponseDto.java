package mega.naemeal.profile.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.profile.entity.Profile;

@Getter
@NoArgsConstructor
public class ProfileResponseDto {

  private String userId;
  private String tel;
  private String nickname;
  private String image;


  public ProfileResponseDto(String userId, Profile profile) {
    this.userId = userId;
    this.tel = profile.getTel();
    this.nickname = profile.getNickname();
    this.image = profile.getImage();
  }
}
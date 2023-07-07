package mega.naemeal.profile.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;
import mega.naemeal.enums.UserStatus;
import mega.naemeal.profile.dto.request.ProfileRequestDto;

@Entity(name = "profile")
@Getter
@NoArgsConstructor
public class Profile extends TimeStamp {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;
  private String tel;
  @Column(nullable = false)
  private String nickname;
  private String image;
  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserStatus status = UserStatus.STANDARD;


  public Profile(String userId, String tel, String nickname,
      String image) {
    this.userId = userId;
    this.tel = tel;
    this.nickname = nickname;
    this.image = image;
  }

  public Profile(String userId, String tel, String nickname) {
    this.userId = userId;
    this.tel = tel;
    this.nickname = nickname;
  }



  public void updateProfile(ProfileRequestDto requestDto, String image) {
    this.nickname = requestDto.getNickname();
    this.tel = requestDto.getTel();
    this.image = image;
  }

  public void updateProfileImage(String image){
    this.image = image;
  }

  public void changeUserEnum(UserStatus status){
    this.status = status;
  }

}

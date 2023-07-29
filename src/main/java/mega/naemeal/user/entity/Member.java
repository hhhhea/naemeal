package mega.naemeal.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.enums.UserRoleEnum;

@Entity(name = "users")
@Getter
@NoArgsConstructor
@Builder
public class Member {

  @Id
  @Column(name = "user_id", nullable = false)
  private String userId;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String nickname;

  @Column(nullable = false)
  @Enumerated(value = EnumType.STRING)
  private UserRoleEnum role;

  public Member(String userId, String password, String nickname, UserRoleEnum role) {
    this.userId = userId;
    this.password = password;
    this.nickname = nickname;
    this.role = role;
  }

  public Member(String userId, String password, String nickname) {
    this.userId = userId;
    this.password = password;
    this.nickname = nickname;
  }

  public void changeRole(UserRoleEnum role) {
    this.role = role;
  }

  public void changeNickname(String nickname){
    this.nickname = nickname;
  }

  public boolean isValidId(String userId) {
    return this.userId.equals(userId);
  }

}


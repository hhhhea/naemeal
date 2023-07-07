package mega.naemeal.jwt;

import lombok.Getter;
import mega.naemeal.enums.UserRoleEnum;

@Getter
public class AuthenticatedUserInfoDto {
  private UserRoleEnum role;
  private String username;

  public AuthenticatedUserInfoDto(UserRoleEnum role, String username) {
    this.role = role;
    this.username = username;
  }
}
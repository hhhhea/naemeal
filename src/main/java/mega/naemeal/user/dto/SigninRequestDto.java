package mega.naemeal.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SigninRequestDto {

  private String userId;
  private String password;
}

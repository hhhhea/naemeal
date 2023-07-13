package mega.naemeal.profile.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileRequestDto {

  private String nickname;
  private String tel;
  private String password;

}

package mega.naemeal.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupRequestDto {

  @Size(min = 10, max = 50, message = "아이디로 사용하실 이메일 주소를 입력해주세요.")
  @Pattern(regexp = "^[a-zA-Z0-9]+@[a-zA-Z0-9]+.[a-z]+$", message = "이메일 형식에 맞춰 작성하세요.")
  private String userId;

  private String password;

  private String nickname;

  private String adminToken;

}

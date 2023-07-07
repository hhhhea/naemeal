package mega.naemeal.user.service;

import jakarta.servlet.http.HttpServletRequest;
import mega.naemeal.jwt.AuthenticatedUserInfoDto;
import mega.naemeal.user.dto.PasswordcheckRequestDto;
import mega.naemeal.user.dto.SigninRequestDto;
import mega.naemeal.user.dto.SignupRequestDto;

public interface UserService {
  void signup(SignupRequestDto requestDto);
  AuthenticatedUserInfoDto signin(SigninRequestDto requestDto);
  void dropout(String userId, PasswordcheckRequestDto requestDto);

  void signout(HttpServletRequest request);
}

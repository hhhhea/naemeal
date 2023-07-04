package mega.naemeal.user.service;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
  void signup(SignupRequestDto requestDto);
  AuthenticatedUserInfoDto signin(SigninRequestDto requestDto);
    void unregister(String userId, PasswordcheckRequestDto requestDto);

}

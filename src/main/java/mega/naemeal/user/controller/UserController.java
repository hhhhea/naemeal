package mega.naemeal.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.config.RedisDao;
import mega.naemeal.jwt.AuthenticatedUserInfoDto;
import mega.naemeal.jwt.JwtUtil;
import mega.naemeal.security.UserDetailsImpl;
import mega.naemeal.user.dto.PasswordcheckRequestDto;
import mega.naemeal.user.dto.SigninRequestDto;
import mega.naemeal.user.dto.SignupRequestDto;
import mega.naemeal.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users", produces = "application/json;charset=UTF-8")
public class UserController {

  private final UserService userService;
  private final JwtUtil jwtUtil;
  private final RedisDao redisDao;

  //회원가입
  @PostMapping("/signup")
  public ResponseEntity<ApiResponse> signup(@RequestBody @Valid SignupRequestDto requestDto) {
    ApiResponse responseDto = new ApiResponse("회원가입이 완료되었습니다.");
    userService.signup(requestDto);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //로그인
  @PostMapping("/signin")
  public ResponseEntity<ApiResponse> signin(@RequestBody @Valid SigninRequestDto requestDto,
      HttpServletResponse response) {
    ApiResponse responseDto = new ApiResponse("로그인이 완료되었습니다.");

    AuthenticatedUserInfoDto userInfoDto = userService.signin(requestDto);
    String accessToken = jwtUtil.createToken(userInfoDto.getUsername(), userInfoDto.getRole());
    String refreshToken = jwtUtil.createRefreshToken(userInfoDto.getUsername(),
        userInfoDto.getRole());
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
    redisDao.setValues(userInfoDto.getUsername(), refreshToken, Duration.ofMinutes(10));

    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //로그아웃
  @PostMapping("/signout")
  public ResponseEntity<ApiResponse> signout(@AuthenticationPrincipal UserDetailsImpl userDetails,
      HttpServletResponse response) {
    ApiResponse responseDto = new ApiResponse("로그아웃이 완료되었습니다.");
    redisDao.deleteValues(userDetails.getUserId());
    response.addHeader(JwtUtil.AUTHORIZATION_HEADER, null);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //회원탈퇴
  @PatchMapping("/dropout")
  public ResponseEntity<ApiResponse> dropout(@RequestBody PasswordcheckRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    userService.dropout(userDetails.getUserId(), requestDto);
    ApiResponse responseDto = new ApiResponse("회원탈퇴가 완료되었습니다.");
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

}
package mega.naemeal.profile.controller;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.profile.dto.request.ProfileRequestDto;
import mega.naemeal.profile.dto.response.ProfileResponseDto;
import mega.naemeal.profile.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/profiles", produces = "application/json;charset=UTF-8")
public class ProfileController {

  private final ProfileService profileService;
  private final S3Service s3Service;
  private static String dirName = "profile";
  private static String imgPath;

  @GetMapping("/my")
  public ResponseEntity<ApiResponse> getCustomerProfileByUserId(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    ProfileResponseDto data = profileService.getCustomerProfile(userDetails.getUserId());
    ApiResponse responseDto = new ApiResponse("프로필 조회 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  @PatchMapping("/my")
  public ResponseEntity<ApiResponse> profileImage
      (@AuthenticationPrincipal UserDetailsImpl userDetails,
          @RequestPart ProfileRequestDto requestDto,
          @RequestPart(value = "file", required = false) MultipartFile file) throws IOException {

    if (file == null) {
      imgPath = profileService.getProfileImage(userDetails.getUserId());
    } else {
      imgPath = s3Service.updateImage(file, dirName);
    }
    ProfileResponseDto data = profileService.updateProfile(userDetails.getUserId(), requestDto,
        imgPath);
    ApiResponse responseDto = new ApiResponse("프로필이 수정되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

}

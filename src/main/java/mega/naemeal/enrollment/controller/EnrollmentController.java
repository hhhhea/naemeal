package mega.naemeal.enrollment.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mega.naemeal.common.ApiResponse;
import mega.naemeal.enrollment.dto.request.EnrollmentRequestDto;
import mega.naemeal.enrollment.dto.response.EnrollmentResponseDto;
import mega.naemeal.enrollment.service.EnrollmentServiceImpl;
import mega.naemeal.security.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class EnrollmentController {

  private final EnrollmentServiceImpl enrollmentService;

  //참여 신청(TRUE(확정) or FALSE(대기))
  @PostMapping("/posts/{postId}/enrollments") //TRUE, FALSE
  public ResponseEntity<ApiResponse> attend(@PathVariable Long postId,
      @Valid @RequestBody EnrollmentRequestDto requestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    EnrollmentResponseDto data = enrollmentService.attend(postId, requestDto,
        userDetails.getUserId());
    ApiResponse responseDto = new ApiResponse("참여신청이 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //참여 취소
  @DeleteMapping("/posts/{postId}/enrollments/{enrollmentId}")
  public ResponseEntity<ApiResponse> cancel(@PathVariable Long postId,
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @PathVariable Long enrollmentId) {
    ApiResponse responseDto = new ApiResponse("참여신청을 취소하였습니다.");
    enrollmentService.cancel(postId, userDetails.getUserId(), enrollmentId);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  // 나의 클래스 목록 조회
  @GetMapping("/enrollments/my")
  public ResponseEntity<ApiResponse> getAllMyEnrollment(
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    List<EnrollmentResponseDto> data = enrollmentService.getAllMyEnrollments(
        userDetails.getUserId());
    ApiResponse responseDto = new ApiResponse("나의 클래스 목록 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }

  //특정 게시물에 대한 참여 신청 목록을 조회
  @GetMapping("/posts/{postId}/enrollments")
  public ResponseEntity<ApiResponse> getEnrollmentList(@PathVariable Long postId) {
    List<EnrollmentResponseDto> data = enrollmentService.getEnrollmentList(postId);
    ApiResponse responseDto = new ApiResponse("참여신청에 대한 조회가 완료되었습니다.", data);
    return new ResponseEntity<>(responseDto, HttpStatus.OK);
  }
}

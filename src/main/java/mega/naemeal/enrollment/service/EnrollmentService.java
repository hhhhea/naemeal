package mega.naemeal.enrollment.service;

import java.util.List;
import mega.naemeal.enrollment.dto.request.EnrollmentRequestDto;
import mega.naemeal.enrollment.dto.response.EnrollmentResponseDto;

public interface EnrollmentService {

  //참여 신청
  EnrollmentResponseDto attend(Long postId, EnrollmentRequestDto requestDto, String userId);

  //참여 신청 취소(삭제)
  void cancel(Long postId, String userId, Long enrollmentId);

  List<EnrollmentResponseDto> getAllMyEnrollments(String userId);

  //참여 신청 내역 조회
  List<EnrollmentResponseDto> getEnrollmentList(Long postId);
}

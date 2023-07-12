package mega.naemeal.enrollment.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollmentRequestDto {

  private String userId;

  private String username;

  @Pattern(regexp = "^01(?:0|1|[6-9])-\\\\d{4}-\\\\d{4}$", message = "핸드폰 번호를 입력해 주세요.")
  private String tel;


}

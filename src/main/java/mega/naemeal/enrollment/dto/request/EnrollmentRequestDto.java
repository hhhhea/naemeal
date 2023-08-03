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

  private String tel;


}

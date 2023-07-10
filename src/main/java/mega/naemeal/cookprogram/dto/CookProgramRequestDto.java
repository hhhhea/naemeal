package mega.naemeal.cookprogram.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class CookProgramRequestDto {

    private String title;

    private String content;

    private String area;

    private String locationName; //세부 주소

    private LocalDateTime deadline; //마감 날짜

    private int maxEnrollmentNum;
}

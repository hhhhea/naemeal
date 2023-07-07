package mega.naemeal.cookprogram.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CookProgramRequestDto {

    private String title;

    private String content;
    //  private PostStatus postStatus;
    private String area;

    private String centerName; //세부 주소

    private LocalDateTime endTime; //마감 날짜

    private int maxEnrollmentNum;

    //모집인원(maxEnrollmentNum)
}

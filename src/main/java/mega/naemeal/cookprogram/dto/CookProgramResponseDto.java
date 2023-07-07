package mega.naemeal.cookprogram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.cookprogram.entity.CookProgram;
import mega.naemeal.enums.PostStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CookProgramResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String area;
    private String centerName; //세부 주소
    private LocalDateTime endTime;
    private int maxEnrollmentNum;
    private PostStatus postStatus;
    //    private LocalDateTime schedule;
    private String image;


    public CookProgramResponseDto(CookProgram post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.area = post.getArea();
        this.centerName = post.getCenterName();
        this.endTime = post.getEndTime();

//        this.schedule = post.getSchedule();

    }
}

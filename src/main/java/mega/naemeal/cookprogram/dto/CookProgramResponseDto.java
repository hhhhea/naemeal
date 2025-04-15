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
    private String locationName; //세부 주소
    private LocalDateTime deadline;
    private int maxEnrollmentNum;
    private PostStatus postStatus;
    private String image;


    public CookProgramResponseDto(CookProgram post){
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.area = post.getArea();
        this.locationName = post.getLocationName();
        this.deadline = post.getDeadline();
        this.maxEnrollmentNum = post.getMaxEnrollmentNum();
        this.image = post.getImage();
        this.postStatus = post.getPostStatus();

    }
}

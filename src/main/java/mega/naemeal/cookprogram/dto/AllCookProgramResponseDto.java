package mega.naemeal.cookprogram.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.cookprogram.entity.CookProgram;

@Getter
@NoArgsConstructor
public class AllCookProgramResponseDto {

    private Long postId;
    private String title;
    private String area;
    private String postStatus;
    private String image;
    private String content;


    public AllCookProgramResponseDto(CookProgram cookProgram) {
        this.postId = cookProgram.getPostId();
        this.title = cookProgram.getTitle();
        this.area = cookProgram.getArea();
        this.postStatus = String.valueOf(cookProgram.getPostStatus());
        this.image = cookProgram.getImage();
        this.content = cookProgram.getContent();

    }

}

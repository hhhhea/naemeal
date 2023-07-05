package mega.naemeal.post.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.post.entity.Post;

@Getter
@NoArgsConstructor
public class AllPostResponseDto {

    private Long postId;
    private String title;
    private String content;
    private String image;

    public AllPostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.image = post.getImage();
        this.content = post.getContent();
        this.title = post.getTitle();
    }
}

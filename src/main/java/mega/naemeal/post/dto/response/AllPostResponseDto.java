package mega.naemeal.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.post.entity.Post;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
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

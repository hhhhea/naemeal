package mega.naemeal.post.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.comment.entity.Comment;
import mega.naemeal.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;
    private String image;
    private List<Comment> commentList = new ArrayList<>();

    public PostResponseDto(Post post) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();

    }

    public PostResponseDto(Post post, List<Comment> comments) {
        this.postId = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.image = post.getImage();
        for(Comment comment : comments){
            this.commentList.add(comment);
        }
    }
}

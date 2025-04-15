package mega.naemeal.comment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.comment.dto.request.CommentRequestDto;
import mega.naemeal.common.TimeStamp;
import mega.naemeal.cookprogram.entity.CookProgram;

@Getter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(nullable = false)
    private String comments;

    private String nickname;

    private String userId;

    private Long postId;

    public Comment(String comments, String userId, String nickname,
                   Long postId) {
        this.comments = comments;
        this.nickname = nickname;
        this.userId = userId;
        this.postId = postId;
    }

    public Comment(long l, String s, CookProgram mockPost) {
        super();
    }

    public void updateComment(CommentRequestDto requestDto) {
        this.comments = requestDto.getComments();
    }

}

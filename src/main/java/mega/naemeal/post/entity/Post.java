package mega.naemeal.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mega.naemeal.common.TimeStamp;
import mega.naemeal.enums.Category;

@Getter
@Entity
@NoArgsConstructor
public class Post extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String userId;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;



    @Column(nullable = false)
    private String image;


    public Post(Category category, String userId, String title, String content, String image) {
        this.category = category;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.image = image;
    }


    public void update(String title, String content, String image) {
        this.content = content;
        this.title = title;
        this.image= image;
    }
}

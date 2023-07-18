package mega.naemeal.post.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.enums.Category;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PostTest {


  @Test
  @DisplayName("게시물 업데이트 테스트")
  void update() {
    //given
    Post post = Post.builder()
        .userId("arin123")
        .title("our cooking contest")
        .category(Category.COMMUNITY)
        .content("please come our contest")
        .image("initialImage")
        .build();

    String updatedTitle = "newTitle";
    String updatedContent = "newContent";
    String updatedImage = "newImage";

    //when
    post.update(updatedTitle, updatedContent, updatedImage);

    // then
    assertEquals(updatedTitle, post.getTitle());
    assertEquals(updatedContent, post.getContent());
    assertEquals(updatedImage, post.getImage());
  }

}

package mega.naemeal.admin.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NoticeTest {


  @Test
  @DisplayName("게시물 업데이트 테스트")
  public void update() {
    // given
    // 초기 게시물 요소
    String initialTitle = "title1";
    String initialContent = "content1";

    // 수정된 게시물 요소
    String updatedTitle = "title2";
    String updatedContent = "content2";

    // 게시물 1개 생성
    Notice notice = new Notice(initialTitle, initialContent);

    // when
    notice.update(updatedTitle, updatedContent);

    // then
    assertEquals(updatedTitle, notice.getTitle());
    assertEquals(updatedContent, notice.getContent());
  }
}
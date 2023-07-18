package mega.naemeal.comment.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.comment.dto.request.CommentRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {


  @Test
  @DisplayName("댓글 업데이트 테스트")
  public void updateComment() {
    // given
    String initialComments = "comment1";
    String updatedComments = "comment2";

    CommentRequestDto requestDto = CommentRequestDto.builder()
        .comments(updatedComments)
        .build();

    Comment comment = new Comment(initialComments, "userId", "nickname", 1L);

    // when
    comment.updateComment(requestDto);

    // then
    assertEquals(updatedComments, comment.getComments());
  }
}
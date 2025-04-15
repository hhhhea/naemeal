package mega.naemeal.cookprogram.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import mega.naemeal.cookprogram.dto.CookProgramRequestDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookProgramTest {

  @Test
  @DisplayName("모집 날짜 종료 여부 확인 - 유효한 기간 내인 경우")
  public void checkEndTime_valid() {
    // given
    LocalDateTime deadline = LocalDateTime.now().plusHours(1);

    CookProgram cookProgram = CookProgram.builder()
        .userId("mother123")
        .title("Let's try a cooking class")
        .content("I believe it is a fantastic program")
        .area("seoul")
        .locationName("coex")
        .deadline(deadline)
        .maxEnrollmentNum(100)
        .image("image")
        .build();

    //when, then
    assertDoesNotThrow(cookProgram::checkEndTime);
  }


  @Test
  @DisplayName("모집 날짜 종료 여부 확인 - 기한 만료된 경우")
  public void checkEndTime_expired() {
    // given
    LocalDateTime deadline = LocalDateTime.now().minusHours(1);
    CookProgram cookProgram = new CookProgram("userId", "title", "content",
        "area", "locationName", deadline, 10, "image");

    // when
    Throwable exception = assertThrows(IllegalArgumentException.class, cookProgram::checkEndTime);

    // then
    assertEquals("모집기간이 이미 지났습니다.", exception.getMessage());
  }


  @Test
  @DisplayName("업데이트 테스트")
  void update() {
    // given
    LocalDateTime deadline = LocalDateTime.now().plusHours(1);
    CookProgram cookProgram = CookProgram.builder()
        .userId("mother123")
        .title("Let's try a cooking class")
        .content("I believe it is a fantastic program")
        .area("seoul")
        .locationName("coex")
        .deadline(deadline)
        .maxEnrollmentNum(100)
        .image("image")
        .build();

    CookProgramRequestDto requestDto = CookProgramRequestDto.builder()
        .title("you can do it")
        .content("I think so")
        .area("Goyang")
        .deadline(LocalDateTime.now().plusDays(1))
        .locationName("kintex")
        .build();

    // when
    cookProgram.update(requestDto, "updatedImage");

    // then
    assertEquals("you can do it", cookProgram.getTitle());
    assertEquals("I think so", cookProgram.getContent());
    assertEquals("Goyang", cookProgram.getArea());
    assertEquals("kintex", cookProgram.getLocationName());
  }

}
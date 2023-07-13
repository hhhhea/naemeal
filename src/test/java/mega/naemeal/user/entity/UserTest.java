package mega.naemeal.user.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.enums.UserRoleEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {
// 유저 엔티티 테스트

  @Test
  @DisplayName("역할(권한) 변경 테스트")
  public void changeRole() {
    // given
    User user = new User("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    user.changeRole(UserRoleEnum.ADMIN);

    // then
    assertEquals(UserRoleEnum.ADMIN, user.getRole());
  }


  @Test
  @DisplayName("닉네임 변경 테스트")
  public void changeNickname() {
    // given
    User user = new User("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    user.changeNickname("choiarin");

    // then
    assertEquals("choiarin", user.getNickname());
  }


  @Test
  @DisplayName("아이디 유효성 검사 - 유효한 경우")
  public void isValidId_valid() {
    // given
    User user = new User("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    boolean isValid = user.isValidId("arinlove123");

    // then
    assertTrue(isValid);
  }


  @Test
  @DisplayName("아이디 유효성 검사 - 유효하지 않은 경우")
  public void isValidId_not_valid() {
    // given
    User user = new User("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    boolean isInvalid = user.isValidId("arin");

    // then
    assertFalse(isInvalid);
  }

}

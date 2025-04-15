package mega.naemeal.user.entity;

import static org.junit.jupiter.api.Assertions.*;

import mega.naemeal.enums.UserRoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {
// 유저 엔티티 테스트

  @Test
  @DisplayName("역할(권한) 변경 테스트")
  public void changeRole() {
    // given
    Member member = new Member("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    member.changeRole(UserRoleEnum.ADMIN);

    // then
    assertEquals(UserRoleEnum.ADMIN, member.getRole());
  }


  @Test
  @DisplayName("닉네임 변경 테스트")
  public void changeNickname() {
    // given
    Member member = new Member("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    member.changeNickname("choiarin");

    // then
    assertEquals("choiarin", member.getNickname());
  }


  @Test
  @DisplayName("아이디 유효성 검사 - 유효한 경우")
  public void isValidId_valid() {
    // given
    Member member = new Member("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    boolean isValid = member.isValidId("arinlove123");

    // then
    assertTrue(isValid);
  }


  @Test
  @DisplayName("아이디 유효성 검사 - 유효하지 않은 경우")
  public void isValidId_not_valid() {
    // given
    Member member = new Member("arinlove123", "123456", "arin", UserRoleEnum.USER);

    // when
    boolean isInvalid = member.isValidId("arin");

    // then
    assertFalse(isInvalid);
  }

}

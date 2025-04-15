package mega.naemeal.common;

import lombok.Getter;

@Getter
public class ApiResponse {

  private String message;
  private Object data;

  public ApiResponse(String message, Object data) {
    this.message = message;
    this.data = data;
  }

  public ApiResponse(String message) {
    this.message = message;
  }

}

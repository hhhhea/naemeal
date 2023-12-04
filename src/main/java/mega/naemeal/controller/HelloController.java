package mega.naemeal.controller;

import lombok.RequiredArgsConstructor;
import mega.naemeal.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
public class HelloController {
    @GetMapping(value = "")
    public ResponseEntity<ApiResponse> hello() {
        ApiResponse responseDto = new ApiResponse("서버 작동 중");
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}

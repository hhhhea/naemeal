package mega.naemeal.admin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mega.naemeal.admin.dto.request.NoticeRequestDto;
import mega.naemeal.admin.dto.response.NoticeResponseDto;
import mega.naemeal.admin.service.AdminService;
import mega.naemeal.jwt.JwtUtil;
import mega.naemeal.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AdminController.class)
class AdminControllerTest {

  @MockBean
  AdminService adminService;

  @MockBean
  JwtUtil jwtUtil;

  @MockBean
  H2ConsoleProperties h2ConsoleProperties;

  @MockBean
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  MockMvc mockMvc;

  @DisplayName("/admin/notices 403 FORBIDDEN")
  @Test
  void t1() throws Exception {
    NoticeRequestDto dto = new NoticeRequestDto("test title", "test content");
    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/admin/notices")
            .content(body))
        .andDo(print())
        .andExpect(status().isForbidden());
  }

  @DisplayName("/admin/notices 201 CREATED")
  @WithMockUser(roles = "ADMIN")
  @Test
  void t2() throws Exception {
    NoticeRequestDto dto = new NoticeRequestDto("test title", "test content");
    ObjectMapper objectMapper = new ObjectMapper();
    String body = objectMapper.writeValueAsString(dto);

    mockMvc.perform(post("/admin/notices")
            .content(body)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().string(containsString("공지사항 작성이 완료")));
  }

  @DisplayName("/admin/notices/{noticeId} 200 OK")
  @WithMockUser(roles = "ADMIN")
  @Test
  void t3() throws Exception {
    Long noticeId = 1L;

    given(adminService.findNotice(noticeId))
        .willReturn(new NoticeResponseDto("title1", "content1", LocalDateTime.now(), LocalDateTime.now(), 1L));

    MvcResult result = mockMvc.perform(get("/admin/notices/{noticeId}", noticeId)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("공지사항 선택 조회가 완료")))
        .andExpect(jsonPath("$.data.title").value("title1"))
        .andExpect(jsonPath("$.data.content").value("content1"))
        .andExpect(jsonPath("$.data.noticeId").value("1"))
        .andReturn();

    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
  }

}
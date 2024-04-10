package com.zerobase.hobbyGroup.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.hobbyGroup.dto.User.SignIn;
import com.zerobase.hobbyGroup.dto.User.SignUpReponse;
import com.zerobase.hobbyGroup.dto.User.SignUpRequest;
import com.zerobase.hobbyGroup.dto.User.UpdateFormRequest;
import com.zerobase.hobbyGroup.dto.User.UpdateFormResponse;
import com.zerobase.hobbyGroup.entity.UserEntity;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.UserService;
import com.zerobase.hobbyGroup.type.Roles;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {

  @MockBean
  private UserService userService;

  @MockBean
  private TokenProvider tokenProvider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)
        .apply(springSecurity())
        .defaultRequest(post("/**").with(csrf()))
        .defaultRequest(patch("/**").with(csrf()))
        .defaultRequest(delete("/**").with(csrf()))
        .build();
  }

  @Test
  @WithMockUser
  public void success_signup() throws Exception {
    //given
    SignUpRequest request = SignUpRequest
        .builder()
        .email("test@naver.com")
        .password("zerobase12!@")
        .userName("테스트")
        .nickname("테스트별명")
        .phone("010-1234-1234")
        .roadAddress("테스트도로명주소")
        .jibunAddress("테스트지번주소")
        .build();

    SignUpReponse signUpResponse = SignUpReponse.builder()
        .email("test@naver.com")
        .userName("테스트")
        .nickname("테스트별명")
        .build();

    given(userService.register(any())).willReturn(signUpResponse);

    //when
    //then
    mockMvc.perform(post("/user/signup")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsBytes(request)))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@naver.com"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("테스트"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("테스트별명"))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password").doesNotExist());
   }

  @Test
  @WithMockUser
  void success_signin() throws Exception{
    //given
    SignIn signIn = SignIn.builder()
        .email("test@naver.com")
        .password("zerobase12!@")
        .build();

    given(userService.authenticate(any())).willReturn(UserEntity
        .builder()
        .email("test@naver.com")
        .password("zerobase12!@")
        .roles(List.of(Roles.USER.getValue()))
        .build());

    String token = "testToken";

    given(tokenProvider.generateToken(any(), any())).willReturn(token);

    //when
    //then
    mockMvc.perform(post("/user/signin")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(signIn)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(token));
  }

  @Test
  @WithMockUser
  void success_update() throws Exception{
    //given
    UpdateFormRequest request = UpdateFormRequest.builder()
        .userId(1L)
        .password("zerobase12!@")
        .userName("테스트")
        .nickname("테스트별명")
        .phone("010-1234-1234")
        .roadAddress("테스트도로명주소")
        .jibunAddress("테스트지번주소")
        .build();

    UpdateFormResponse response = UpdateFormResponse.builder()
            .email("test@naver.com")
            .userName("테스트")
            .nickname("테스트별명")
            .build();

    given(userService.update(any())).willReturn(response);

    String token = "testToken";

    //when
    //then
    mockMvc.perform(put("/user/update")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsBytes(request)))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("test@naver.com"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.userName").value("테스트"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value("테스트별명"));
  }

  @Test
  @WithMockUser
  void success_delete() throws Exception{
    //given
    UpdateFormRequest request = UpdateFormRequest.builder()
        .userId(1L)
        .build();

    String message = "회원 탈퇴가 완료되었습니다.";

    doNothing().when(userService).delete(any());

    String token = "testToken";

    //when
    //then
    mockMvc.perform(delete("/user/delete")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .content(objectMapper.writeValueAsBytes(request)))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().string(message));
  }

  @Test
  @WithMockUser
  void success_logout() throws Exception{
    //given
    String token = "testToken";
    String message = "로그아웃이 완료되었습니다. 다시 로그인 해주세요.";

    //when
    //then
    mockMvc.perform(post("/user/logout")
            .contentType(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string("로그아웃이 완료되었습니다. 다시 로그인 해주세요."));
  }
}
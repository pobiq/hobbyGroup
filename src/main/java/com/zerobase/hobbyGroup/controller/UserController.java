package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.Auth;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  private final TokenProvider tokenProvider;

  /**
   * 회원 가입
   * @param request 성명, 패스워드, 역할 dto
   * @return
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
    var result = this.userService.register(request);
    return ResponseEntity.ok(result);
  }

  /**
   * 로그인
   * @param request 성명, 패스워드 dto
   * @return
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
    var member = this.userService.authenticate(request);
    var token = this.tokenProvider.generateToken(member.getEmail(), member.getRoles());
    return ResponseEntity.ok(token);
  }



}

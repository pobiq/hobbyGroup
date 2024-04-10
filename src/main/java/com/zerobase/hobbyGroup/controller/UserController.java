package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.User;
import com.zerobase.hobbyGroup.dto.User.UpdateFormResponse;
import com.zerobase.hobbyGroup.dto.User.deleteRequest;
import com.zerobase.hobbyGroup.security.TokenProvider;
import com.zerobase.hobbyGroup.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
   * @param request 이메일, 비밀번호, 이름, 핸드폰 번호, 도로명 주소, 지번 주소 dto
   * @return
   */
  @PostMapping("/signup")
  public ResponseEntity<?> signup(@RequestBody @Valid User.SignUpRequest request) {
    var result = this.userService.register(request);
    return ResponseEntity.ok(result);
  }

  /**
   * 로그인
   * @param request 이메일, 비밀번호 dto
   * @return
   */
  @PostMapping("/signin")
  public ResponseEntity<?> signin(@RequestBody @Valid User.SignIn request) {
    var user = this.userService.authenticate(request);
    var token = this.tokenProvider.generateToken(user.getEmail(), user.getRoles());
    return ResponseEntity.ok(token);
  }

  /**
   * 회원 정보 수정
   * @param request 회원 정보 수정 dto
   * @param token jwt 토큰
   * @return
   */
  @PutMapping("/update")
  public ResponseEntity<?> updateUser(@RequestBody @Valid User.UpdateFormRequest request, @RequestHeader("Authorization") String token) {

    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    UpdateFormResponse response = this.userService.update(request);
    return ResponseEntity.ok(response);
  }

  /**
   * 회원 탈퇴
   * @param request 회원 탈퇴 dto
   * @return
   */
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUser(@RequestBody User.deleteRequest request, @RequestHeader("Authorization") String token) {
    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.tokenProvider.invalidateToken(token.substring(7));
    this.userService.delete(request);

    return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
  }

  /**
   * 로그 아웃
   * @param token jwt 토큰
   * @return 알림 메세지
   */
  @PostMapping("/logout")
  public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
    ResponseEntity<?> responseEntity = this.tokenProvider.validToken(token);
    if (responseEntity != null) {
      return responseEntity; // 유효하지 않은 토큰 또는 이미 로그아웃된 토큰일 경우 해당 응답을 그대로 반환
    }

    this.tokenProvider.invalidateToken(token.substring(7));
    return ResponseEntity.ok("로그아웃이 완료되었습니다. 다시 로그인 해주세요.");
  }

}

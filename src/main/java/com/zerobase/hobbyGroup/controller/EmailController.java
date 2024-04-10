package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.Email;
import com.zerobase.hobbyGroup.service.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailController {

  private final MailService mailService;

  /**
   * 인증코드 이메일 전송
   * @param request 이메일 dto
   * @return
   */
  @PostMapping("/send")
  public ResponseEntity<?> sendMessage(@RequestBody @Valid Email.SendRequest request) {
    mailService.sendCodeToEmail(request);

    return ResponseEntity.ok("인증코드를 보냈습니다." + request.getEmail() + "을 확인해주세요.");
  }

  /**
   * 인증
   * @param request 이메일, 인증코드 dto
   * @return
   */
  @PostMapping("/verification")
  public ResponseEntity<?> verificationEmail(@RequestBody @Valid Email.VerificationRequest request) {
    Email.VerificationReponse response = mailService.verifiedCode(request.getEmail(), request.getCode());
    return ResponseEntity.ok("인증이 완료되었습니다.");
  }

}

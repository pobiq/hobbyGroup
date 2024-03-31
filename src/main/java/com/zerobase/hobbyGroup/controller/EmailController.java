package com.zerobase.hobbyGroup.controller;

import com.zerobase.hobbyGroup.dto.Email;
import com.zerobase.hobbyGroup.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

  @PostMapping("/send")
  public ResponseEntity<?> sendMessage(@RequestBody Email.SendRequest request) {
    mailService.sendCodeToEmail(request);

    return ResponseEntity.ok(request);
  }

  @PostMapping("/verification")
  public ResponseEntity<?> verificationEmail(@RequestBody Email.VerificationRequest request) {
    Email.VerificationReponse response = mailService.verifiedCode(request.getEmail(), request.getCode());

    return ResponseEntity.ok(response);
  }

}

package com.zerobase.hobbyGroup.service;

import com.zerobase.hobbyGroup.dto.Email;
import com.zerobase.hobbyGroup.entity.UserEntity;
import com.zerobase.hobbyGroup.exception.impl.auth.NotEqualAuthCodeException;
import com.zerobase.hobbyGroup.exception.impl.email.AlreadyEmailAuthException;
import com.zerobase.hobbyGroup.exception.impl.email.AlreadySendEmailException;
import com.zerobase.hobbyGroup.exception.impl.email.NoEmailException;
import com.zerobase.hobbyGroup.exception.impl.email.NotEmailFormException;
import com.zerobase.hobbyGroup.exception.impl.other.LogicException;
import com.zerobase.hobbyGroup.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

  private final JavaMailSender javaMailSender;

  private final UserRepository userRepository;

  private final RedisService redisService;

  private final long authCodeExpirationMillis = 300000; // 5분

  public void sendCodeToEmail(Email.SendRequest request) {
    String toEmail = request.getEmail();

    //vaildation
    //정상적인 이메일 형식 인지 검증
    String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(toEmail);

    if(!matcher.matches()) {
      throw new NotEmailFormException();
    }
    this.check(toEmail);

    // 이미 인증 메일을 보냈는지 확인
    if(redisService.existsKey(toEmail)) {
      throw new AlreadySendEmailException();
    }

    String title = "이메일 인증";
    String authCode = this.createCode();
    this.sendEmail(toEmail, title, "인증번호 : " + authCode);
    // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "Email / value = 6자리 숫자 )
    redisService.setValues(toEmail, authCode, Duration.ofMillis(this.authCodeExpirationMillis));
  }

  /**
   * 이메일 관련 validation
   * @param email 보낼 이메일 주소
   */
  private void check(String email) {
    Optional<UserEntity> userEntity = userRepository.findByEmail(email);
    // 회원 가입된 이메일인지 확인
    if(userEntity.isEmpty()) {
      throw new NoEmailException();
    }

    // 이메일 인증했는지 확인
    if(userEntity.get().getEmailAuth()) {
      throw new AlreadyEmailAuthException();
    }
  }

  private String createCode() {
    int length = 6;
    try {
      Random random = SecureRandom.getInstanceStrong();
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < length; i++) {
        builder.append(random.nextInt(10));
      }
      return builder.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new LogicException();
    }
  }

  /**
   * 이메일 발송하는 메서드
   * @param toEmail 보낼 이메일 주소
   * @param title 이메일 제목
   * @param text 이메일 내용
   */
  public void sendEmail(String toEmail, String title, String text) {
    SimpleMailMessage emailForm = createEmailForm(toEmail, title, text);
    try {
      javaMailSender.send(emailForm);
    } catch (RuntimeException e) {
      throw e;
    }
  }

  /**
   * 인증 코드 검증 메서드
   * @param email
   * @param authCode
   * @return
   */
  public Email.VerificationReponse verifiedCode(String email, String authCode) {
    this.check(email);
    String redisAuthCode = redisService.getValues(email);
    boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);

    Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(email);
    UserEntity userEntity = optionalUserEntity.get();
    if(authResult) {
      userEntity.setEmailAuth(true);
      userRepository.save(userEntity);
    } else {
      throw new NotEqualAuthCodeException();
    }

    return Email.VerificationReponse.builder()
        .email(userEntity.getEmail())
        .emailAuth(true)
        .build();
  }

  /**
   * 발송할 이메일 데이터를 설정하는 메서드
   * @param toEmail 보낼 이메일 주소
   * @param title 이메일 제목
   * @param text 이메일 내용
   * @return
   */
  private SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(toEmail);
    message.setSubject(title);
    message.setText(text);

    return message;
  }
}

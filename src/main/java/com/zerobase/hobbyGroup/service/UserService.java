package com.zerobase.hobbyGroup.service;

import com.zerobase.hobbyGroup.dto.User;
import com.zerobase.hobbyGroup.dto.User.SignIn;
import com.zerobase.hobbyGroup.dto.User.UpdateFormResponse;
import com.zerobase.hobbyGroup.entity.UserEntity;
import com.zerobase.hobbyGroup.exception.impl.auth.NotEqualPasswordException;
import com.zerobase.hobbyGroup.exception.impl.email.AlreadyExistUserException;
import com.zerobase.hobbyGroup.exception.impl.email.NoEmailException;
import com.zerobase.hobbyGroup.exception.impl.email.NotEmailAuthorization;
import com.zerobase.hobbyGroup.exception.impl.user.NoUserException;
import com.zerobase.hobbyGroup.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws NoEmailException {
    return this.userRepository.findByEmail(email)
        .orElseThrow(NoUserException::new);
  }

  /**
   * 회원 가입
   * @param user 유저 dto
   * @return
   */
  public User.SignUpReponse register(User.SignUpRequest user) {
    log.info("user create started");
    // 이메일이 존재하는 경우 exception 발생
    boolean exists = this.userRepository.existsByEmail(user.getEmail());
    if (exists) {
      throw new AlreadyExistUserException();
    }

    // ID 생성 가능한 경우, 멤버 테이블에 저장
    // 비밀번호는 암호화 되어서 저장되어야함
    user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    var result = this.userRepository.save(user.toEntity());
    log.info("user create finished");
    return User.SignUpReponse.fromEntity(result);
  }

  /**
   * 회원 수정
   * @param request 회원 수정 dto
   * @return
   */
  public UpdateFormResponse update(User.UpdateFormRequest request) {
    log.info("user update started");

    var user = this.userRepository.findById(request.getUserId())
            .orElseThrow(NoUserException::new);

    UserEntity userEntity = UserEntity.builder()
          .userId(user.getUserId())
          .email(user.getEmail())
          .password(this.passwordEncoder.encode(request.getPassword()))
          .userName(request.getUserName())
          .nickname(request.getNickname())
          .phone(request.getPhone())
          .roadAddress(request.getRoadAddress())
          .jibunAddress(request.getJibunAddress())
          .createdAt(user.getCreatedAt())
          .updatedAt(LocalDateTime.now())
          .emailAuth(user.getEmailAuth())
          .roles(user.getRoles())
          .build();

    var result = this.userRepository.save(userEntity);
    log.info("user update finished");
    return UpdateFormResponse.fromEntity(result);
  }

  /**
   * 회원 수정
   * @param request 회원 수정 dto
   * @return
   */
  public void delete(User.deleteRequest request) {
    log.info("user delete started");

    var user = this.userRepository.findById(request.getUserId())
        .orElseThrow(NoUserException::new);

    this.userRepository.delete(user);
    log.info("user delete finished");
  }

  /**
   * 로그인 인증
   * @param user 로그인 dto
   * @return 멤버 엔티티 반환
   */
  public UserEntity authenticate(SignIn user) {
    log.info("user login started");
    // id 로 멤버 조회
    var userentity = this.userRepository.findByEmail(user.getEmail())
        .orElseThrow(NoEmailException::new);

    // 이메일 인증 확인
    if(!userentity.getEmailAuth()) {
      throw new NotEmailAuthorization();
    }

    // 패스워드 일치 여부 확인
    // 일치하지 않는 경우 400 status 코드와 적합한 에러 메시지 반환
    if (!this.passwordEncoder.matches(user.getPassword(), userentity.getPassword())) {
      throw new NotEqualPasswordException();
    }

    log.info("user login finished");
    // 일치하는 경우, 해당 멤버 엔티티 반환
    return userentity;
  }

}

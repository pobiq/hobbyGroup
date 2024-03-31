package com.zerobase.hobbyGroup.dto;

import com.zerobase.hobbyGroup.entity.UserEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

public class Auth {
  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignIn {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUp {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 10, message = "이름은 최대 10자 까지 입니다.")
    private String userName;

    private String nickname;

    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
    private String phone;

    private String roadAddress;

    private String jibunAddress;

    public UserEntity toEntity() {
      return UserEntity.builder()
          .email(this.email)
          .password(this.password)
          .userName(this.userName)
          .nickname(this.nickname)
          .phone(this.phone)
          .roadAddress(this.roadAddress)
          .jibunAddress(this.jibunAddress)
          .createdAt(LocalDateTime.now())
          .emailAuth(false)
          .roles(List.of("ROLE_USER"))
          .build();
    }
  }
}

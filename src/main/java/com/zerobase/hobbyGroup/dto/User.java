package com.zerobase.hobbyGroup.dto;

import com.zerobase.hobbyGroup.entity.UserEntity;
import com.zerobase.hobbyGroup.type.Roles;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class User {
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
  public static class SignUpRequest {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 10, message = "이름은 최대 10자 까지 입니다.")
    private String userName;

    @Size(max = 20, message = "별명은 최대 20자 까지 입니다.")
    private String nickname;

    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
    private String phone;

    @Size(max = 100, message = "도로명 주소는 최대 100자 까지 입니다.")
    private String roadAddress;

    @Size(max = 100, message = "지번 주소는 최대 100자 까지 입니다.")
    private String jibunAddress;

    @NotBlank(message = "역할은 필수 입력 값입니다.")
    private String role;

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
          .build();
    }
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUpReponse {

    private String email;

    private String userName;

    private String nickname;

    public static SignUpReponse fromEntity(UserEntity userEntity) {
      return SignUpReponse.builder()
          .email(userEntity.getEmail())
          .userName(userEntity.getUsername())
          .nickname(userEntity.getNickname())
          .build();
    }
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateFormRequest {

    @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "유저 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "유저 아이디는 2147483647 이하여야 합니다.")
    private Long userId;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    @Size(max = 10, message = "이름은 최대 10자 까지 입니다.")
    private String userName;

    @Size(max = 20, message = "별명은 최대 20자 까지 입니다.")
    private String nickname;

    @NotBlank(message = "핸드폰 번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "핸드폰 번호 형식이 올바르지 않습니다.")
    private String phone;

    @Size(max = 100, message = "도로명 주소는 최대 100자 까지 입니다.")
    private String roadAddress;

    @Size(max = 100, message = "지번 주소는 최대 100자 까지 입니다.")
    private String jibunAddress;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class UpdateFormResponse {

    private String email;

    private String userName;

    private String nickname;

    public static UpdateFormResponse fromEntity(UserEntity userEntity) {
      return UpdateFormResponse.builder()
          .email(userEntity.getEmail())
          .userName(userEntity.getUsername())
          .nickname(userEntity.getNickname())
          .build();
    }
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class deleteRequest {

    @NotNull(message = "유저 아이디는 필수 입력 값입니다.")
    @Min(value = 1, message = "유저 아이디는 1이상이어야 합니다.")
    @Max(value = 2147483647, message = "유저 아이디는 2147483647 이하여야 합니다.")
    private Long userId;

  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class logoutForm {
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 올바르지 않습니다.")
    private String email;
  }
}

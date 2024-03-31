package com.zerobase.hobbyGroup.dto;

import com.zerobase.hobbyGroup.entity.UserEntity;
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
    private String email;
    private String password;
  }

  @Getter
  @Setter
  @ToString
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class SignUp {
    private String email;
    private String password;
    private String userName;
    private String nickname;
    private String phone;
    private String roadAddress;
    private String jibunAddress;
    private List<String> roles;

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
          .roles(this.roles)
          .build();
    }
  }
}

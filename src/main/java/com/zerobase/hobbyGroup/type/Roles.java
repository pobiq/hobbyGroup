package com.zerobase.hobbyGroup.type;


import lombok.Getter;

@Getter
public enum Roles {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String value;

  Roles(String value) {
    this.value = value;
  }

}

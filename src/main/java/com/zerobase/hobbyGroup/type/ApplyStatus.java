package com.zerobase.hobbyGroup.type;

import lombok.Getter;

@Getter
public enum ApplyStatus {
  APPLY("APPLY"),
  ACCEPT("ACCEPT"),
  REJECT("REJECT");

  private final String value;

  ApplyStatus(String value) {
    this.value = value;
  }
}

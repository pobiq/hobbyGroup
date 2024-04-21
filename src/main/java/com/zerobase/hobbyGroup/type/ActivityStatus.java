package com.zerobase.hobbyGroup.type;

import lombok.Getter;

@Getter
public enum ActivityStatus {
  GENERAL("GENERAL"),
  NOTICE("NOTICE");

  private final String value;

  ActivityStatus(String value) {
    this.value = value;
  }

}

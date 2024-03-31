package com.zerobase.hobbyGroup.exception;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorException {

  private String statusCode;
  private String errorContent;
  private List<String> messages;

  public ErrorException(String statusCode, String errorContent, String messages) {
    this.statusCode = statusCode;
    this.errorContent = errorContent;
    this.messages = new ArrayList<>();
    this.messages.add(messages);
  }

  public ErrorException(String statusCode, String errorContent, List<String> messages) {
    this.statusCode = statusCode;
    this.errorContent = errorContent;
    this.messages = messages;
  }
}

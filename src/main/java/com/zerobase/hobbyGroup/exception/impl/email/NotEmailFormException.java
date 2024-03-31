package com.zerobase.hobbyGroup.exception.impl.email;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotEmailFormException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "올바른 이메일 형식이 아닙니다.";
  }
}

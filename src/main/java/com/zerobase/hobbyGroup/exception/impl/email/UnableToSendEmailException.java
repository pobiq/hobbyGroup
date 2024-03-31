package com.zerobase.hobbyGroup.exception.impl.email;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class UnableToSendEmailException extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "이메일을 보낼수 없습니다.";
  }
}

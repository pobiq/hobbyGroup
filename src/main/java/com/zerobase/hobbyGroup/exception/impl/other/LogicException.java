package com.zerobase.hobbyGroup.exception.impl.other;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class LogicException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  @Override
  public String getMessage() {
    return "서버에 문제가 있습니다.";
  }
}

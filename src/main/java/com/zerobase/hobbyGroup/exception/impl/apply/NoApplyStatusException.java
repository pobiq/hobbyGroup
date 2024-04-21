package com.zerobase.hobbyGroup.exception.impl.apply;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoApplyStatusException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "신청한 상태가 아닙니다.";
  }
}

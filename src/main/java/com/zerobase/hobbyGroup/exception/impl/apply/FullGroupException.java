package com.zerobase.hobbyGroup.exception.impl.apply;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class FullGroupException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "인원이 다 찬 상태입니다.";
  }
}

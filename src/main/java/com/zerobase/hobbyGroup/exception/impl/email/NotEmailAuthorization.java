package com.zerobase.hobbyGroup.exception.impl.email;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotEmailAuthorization extends AbstractException {

  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "이메일 인증을 하지 않았습니다.";
  }

}

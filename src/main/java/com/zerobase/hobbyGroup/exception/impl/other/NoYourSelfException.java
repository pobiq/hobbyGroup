package com.zerobase.hobbyGroup.exception.impl.other;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoYourSelfException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.INTERNAL_SERVER_ERROR.value();
  }

  @Override
  public String getMessage() {
    return "자기 자신이 쓴(게시물, 모임 신청, 모임 찜, 댓글) 이 아닙니다.";
  }
}

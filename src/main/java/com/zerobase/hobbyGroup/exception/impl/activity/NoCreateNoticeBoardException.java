package com.zerobase.hobbyGroup.exception.impl.activity;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoCreateNoticeBoardException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "공지 게시글은 2개 이상 생성 할수 없습니다.";
  }
}

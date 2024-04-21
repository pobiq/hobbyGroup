package com.zerobase.hobbyGroup.exception.impl.file;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoFileException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "일치하는 파일이 없습니다.";
  }
}

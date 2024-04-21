package com.zerobase.hobbyGroup.exception.impl.file;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotCreationFolderException extends AbstractException {
  @Override
  public int getStatusCode() {
    return HttpStatus.BAD_REQUEST.value();
  }

  @Override
  public String getMessage() {
    return "파일 폴더를 만들지 못했습니다.";
  }
}

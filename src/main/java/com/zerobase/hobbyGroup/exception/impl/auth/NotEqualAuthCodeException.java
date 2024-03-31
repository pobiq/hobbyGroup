package com.zerobase.hobbyGroup.exception.impl.auth;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NotEqualAuthCodeException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "인증번호가 일치하지 않습니다.";
    }
}

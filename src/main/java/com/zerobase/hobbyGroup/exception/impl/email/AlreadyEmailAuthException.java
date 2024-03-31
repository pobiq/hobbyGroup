package com.zerobase.hobbyGroup.exception.impl.email;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyEmailAuthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이메일 인증한 사용자 입니다.";
    }
}

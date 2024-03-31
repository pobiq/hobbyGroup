package com.zerobase.hobbyGroup.exception.impl.email;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadySendEmailException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 인증 메일을 보냈습니다.";
    }
}

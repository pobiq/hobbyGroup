package com.zerobase.hobbyGroup.exception.impl.auth;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoAuthException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "자기자신 이거나 관리자만 할수 있습니다.";
    }
}

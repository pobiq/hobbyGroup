package com.zerobase.hobbyGroup.exception.impl.role;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoRoleException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "역할 형식이 올바르지 않습니다.";
    }
}

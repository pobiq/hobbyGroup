package com.zerobase.hobbyGroup.exception.impl.apply;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyRejectStatusException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 철회한 상태입니다.";
    }
}

package com.zerobase.hobbyGroup.exception.impl.apply;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoGroupLeaderException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "모임장 만 할수 있습니다.";
    }
}

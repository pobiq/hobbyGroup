package com.zerobase.hobbyGroup.exception.impl.group;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoGroupException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "일치하는 그룹이 없습니다.";
    }
}

package com.zerobase.hobbyGroup.exception.impl.apply;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoApplyGroupException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "일치하는 그룹 신청이 없습니다.";
    }
}

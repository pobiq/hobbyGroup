package com.zerobase.hobbyGroup.exception.impl.apply;


import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyApplyGroupException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "이미 신청 하였거나 수락되었습니다.";
    }
}

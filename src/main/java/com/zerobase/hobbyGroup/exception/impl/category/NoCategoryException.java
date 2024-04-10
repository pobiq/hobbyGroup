package com.zerobase.hobbyGroup.exception.impl.category;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoCategoryException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "일치하는 카테고리가 없습니다.";
    }
}

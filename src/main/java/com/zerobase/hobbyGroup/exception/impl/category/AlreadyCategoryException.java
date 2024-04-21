package com.zerobase.hobbyGroup.exception.impl.category;

import com.zerobase.hobbyGroup.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class AlreadyCategoryException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "입력하신 카테고리명이 이미 있습니다.";
    }
}

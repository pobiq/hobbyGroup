package com.zerobase.hobbyGroup.exception;

import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(AbstractException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code(e.getStatusCode())
                                                    .message(e.getMessage())
                                                    .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }

    /**
     * 유효성 검사 처리
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorException> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<String> errorsList = ex.getAllErrors().stream().
            map(DefaultMessageSourceResolvable::getDefaultMessage).toList();
        return ResponseEntity.ok(new ErrorException("400", "Validation failure", errorsList));
    }

}

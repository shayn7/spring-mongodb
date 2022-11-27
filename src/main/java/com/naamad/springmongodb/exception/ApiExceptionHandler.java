package com.naamad.springmongodb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;


@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {PersonRequestException.class})
    public ResponseEntity<Object> personNotFoundException(PersonRequestException e){
        ApiException apiException = ApiException
                .builder()
                .message(e.getMessage())
                .localDateTime(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(apiException, HttpStatus.NOT_FOUND);
    }
}

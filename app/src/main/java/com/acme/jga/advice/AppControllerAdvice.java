package com.acme.jga.advice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class AppControllerAdvice {
    private static final String INSTRUMENTATION_NAME = AppControllerAdvice.class.getCanonicalName();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleInternal(Exception exception, HttpServletRequest request) {
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(exception.getMessage());
    }


}

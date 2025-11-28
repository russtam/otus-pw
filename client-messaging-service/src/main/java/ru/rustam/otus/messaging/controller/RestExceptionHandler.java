package ru.rustam.otus.messaging.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rustam.otus.common.model.ErrorDto;
import ru.rustam.otus.messaging.exceptions.MessagingException;


@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error("Exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("INTERNAL_ERROR", e.toString()));
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorDto> handleNotFoundException(MessagingException e) {
        log.error("Exception", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("ORDER_NOT_FOUND", e.toString()));
    }

}

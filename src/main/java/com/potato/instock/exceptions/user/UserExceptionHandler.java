package com.potato.instock.exceptions.user;

import com.potato.instock.exceptions.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(value = {UserExistsException.class})
    public ResponseEntity<Object> handleUserExistsException(UserExistsException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        GeneralException userException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(userException, httpStatus);
    }

    @ExceptionHandler(value = {UserInfoException.class})
    public ResponseEntity<Object> handleUserMissingInfoException(UserInfoException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        GeneralException userException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(userException, httpStatus);
    }
}

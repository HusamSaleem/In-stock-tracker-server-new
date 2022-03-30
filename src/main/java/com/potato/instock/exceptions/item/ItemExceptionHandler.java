package com.potato.instock.exceptions.item;

import com.potato.instock.exceptions.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ItemExceptionHandler {

    @ExceptionHandler(value = {ItemNotFoundException.class})
    public ResponseEntity<Object> handleItemNotFoundException(ItemNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        GeneralException itemException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(itemException, httpStatus);
    }

    @ExceptionHandler(value = {ItemInvalidIdException.class})
    public ResponseEntity<Object> handleItemInvalidIdException(ItemInvalidIdException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        GeneralException itemException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(itemException, httpStatus);
    }
}

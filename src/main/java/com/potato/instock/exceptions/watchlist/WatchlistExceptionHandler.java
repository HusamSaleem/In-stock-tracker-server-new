package com.potato.instock.exceptions.watchlist;

import com.potato.instock.exceptions.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class WatchlistExceptionHandler {

    @ExceptionHandler(value = {WatchlistNotFoundException.class})
    public ResponseEntity<Object> handleWatchlistNotFound(WatchlistNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        GeneralException userException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(userException, httpStatus);
    }

    @ExceptionHandler(value = {WatchlistDuplicateEntryException.class})
    public ResponseEntity<Object> handleDuplicateWatchlistEntry(WatchlistDuplicateEntryException e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        GeneralException userException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(userException, httpStatus);
    }

    @ExceptionHandler(value = {WatchlistEntryNotFoundException.class})
    public ResponseEntity<Object> handleWatchlistEntryNotFound(WatchlistEntryNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        GeneralException userException = new GeneralException(
                e.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );

        return new ResponseEntity<>(userException, httpStatus);
    }
}

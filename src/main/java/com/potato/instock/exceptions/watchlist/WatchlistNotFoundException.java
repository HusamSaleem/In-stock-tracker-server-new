package com.potato.instock.exceptions.watchlist;

public class WatchlistNotFoundException extends RuntimeException {
    public WatchlistNotFoundException(String message) {
        super(message);
    }

    public WatchlistNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.potato.instock.exceptions.watchlist;

public class WatchlistEntryNotFoundException extends RuntimeException {
    public WatchlistEntryNotFoundException(String message) {
        super(message);
    }

    public WatchlistEntryNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

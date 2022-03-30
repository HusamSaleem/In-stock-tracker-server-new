package com.potato.instock.exceptions.watchlist;

public class WatchlistDuplicateEntryException extends RuntimeException {
    public WatchlistDuplicateEntryException(String message) {
        super(message);
    }

    public WatchlistDuplicateEntryException(String message, Throwable cause) {
        super(message, cause);
    }
}

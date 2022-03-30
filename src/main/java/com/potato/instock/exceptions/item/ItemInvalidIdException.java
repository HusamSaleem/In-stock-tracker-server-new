package com.potato.instock.exceptions.item;

public class ItemInvalidIdException extends RuntimeException {
    public ItemInvalidIdException(String message) {
        super(message);
    }

    public ItemInvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }
}

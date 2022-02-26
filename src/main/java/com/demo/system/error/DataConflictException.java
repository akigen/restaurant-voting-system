package com.demo.system.error;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }
}

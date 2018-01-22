package com.kloudtek.unpack;

public class UnpackException extends Exception {
    public UnpackException() {
    }

    public UnpackException(String message) {
        super(message);
    }

    public UnpackException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnpackException(Throwable cause) {
        super(cause);
    }

    public UnpackException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

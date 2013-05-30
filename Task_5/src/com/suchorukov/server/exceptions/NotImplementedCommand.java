package com.suchorukov.server.exceptions;

public class NotImplementedCommand extends Exception {
    public NotImplementedCommand() {
    }

    public NotImplementedCommand(String message) {
        super(message);
    }

    public NotImplementedCommand(String message, Throwable cause) {
        super(message, cause);
    }

    public NotImplementedCommand(Throwable cause) {
        super(cause);
    }

    public NotImplementedCommand(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

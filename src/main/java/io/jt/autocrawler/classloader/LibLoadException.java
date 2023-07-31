package io.jt.autocrawler.classloader;

import io.jt.autocrawler.exception.BaseException;

public class LibLoadException extends BaseException {
    public LibLoadException() {
    }

    public LibLoadException(String message) {
        super(message);
    }

    public LibLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibLoadException(Throwable cause) {
        super(cause);
    }
}

package io.jt.autocrawler.progress;

import io.jt.autocrawler.exception.BaseException;

public class ProgressException extends BaseException {
    public ProgressException() {
    }

    public ProgressException(String message) {
        super(message);
    }
}

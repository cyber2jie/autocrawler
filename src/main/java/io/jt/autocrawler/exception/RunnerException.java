package io.jt.autocrawler.exception;

public class RunnerException extends BaseException {
    public RunnerException() {
    }

    public RunnerException(String message) {
        super(message);
    }

    public RunnerException(String message, Throwable cause) {
        super(message, cause);
    }

    public RunnerException(Throwable cause) {
        super(cause);
    }
}

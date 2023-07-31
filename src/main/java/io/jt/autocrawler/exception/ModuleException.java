package io.jt.autocrawler.exception;

public class ModuleException extends BaseException {
    public ModuleException() {
    }

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModuleException(Throwable cause) {
        super(cause);
    }
}

package io.jt.autocrawler.crawler;

import io.jt.autocrawler.exception.BaseException;

public class CrawlerException extends BaseException {
    public CrawlerException() {
    }

    public CrawlerException(String message) {
        super(message);
    }

    public CrawlerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrawlerException(Throwable cause) {
        super(cause);
    }
}

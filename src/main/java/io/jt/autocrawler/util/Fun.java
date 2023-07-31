package io.jt.autocrawler.util;

public interface Fun<T, R> {
    void call(T o, R r);
}

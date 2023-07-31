package io.jt.autocrawler.parser;

import io.jt.autocrawler.model.Out;
import io.jt.autocrawler.model.Request;

public interface OutConsumer {
    void consume(Request request, Out out, byte[] bytes);
}

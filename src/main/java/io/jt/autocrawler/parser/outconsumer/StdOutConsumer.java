package io.jt.autocrawler.parser.outconsumer;

import io.jt.autocrawler.model.Out;
import io.jt.autocrawler.model.Request;
import io.jt.autocrawler.parser.OutConsumer;

import java.nio.charset.Charset;

public class StdOutConsumer implements OutConsumer {
    @Override
    public void consume(Request request, Out out, byte[] bytes) {
        String content = new String(bytes, Charset.defaultCharset());
        System.out.println(content);
    }
}

package io.jt.autocrawler.parser.model;

import io.jt.autocrawler.parser.OutConsumer;

public class OutParsedData {
    private OutConsumer consumer;

    public OutParsedData(OutConsumer consumer) {
        this.consumer = consumer;
    }

    public OutConsumer getConsumer() {
        return consumer;
    }

    public void setConsumer(OutConsumer consumer) {
        this.consumer = consumer;
    }
}

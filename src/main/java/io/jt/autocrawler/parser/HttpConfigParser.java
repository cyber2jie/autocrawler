package io.jt.autocrawler.parser;

import io.jt.autocrawler.config.HttpConfig;
import io.jt.autocrawler.model.InType;
import io.jt.autocrawler.model.OutType;
import io.jt.autocrawler.parser.insupplier.FileLineSupplier;
import io.jt.autocrawler.parser.insupplier.ListSupplier;
import io.jt.autocrawler.parser.insupplier.ScriptSupplier;
import io.jt.autocrawler.parser.model.InParsedData;
import io.jt.autocrawler.parser.model.OutParsedData;
import io.jt.autocrawler.parser.outconsumer.FileConsumer;
import io.jt.autocrawler.parser.outconsumer.HoleConsumer;
import io.jt.autocrawler.parser.outconsumer.ScriptConsumer;
import io.jt.autocrawler.parser.outconsumer.StdOutConsumer;

import java.util.ArrayList;
import java.util.List;

public class HttpConfigParser {
    private HttpConfig config;

    public HttpConfigParser(HttpConfig config) {
        this.config = config;
    }

    public static class HttpConfigParsedData {
        private InParsedData in;
        private OutParsedData out;

        public HttpConfigParsedData(InParsedData in, OutParsedData out) {
            this.in = in;
            this.out = out;
        }

        public InParsedData getIn() {
            return in;
        }

        public void setIn(InParsedData in) {
            this.in = in;
        }

        public OutParsedData getOut() {
            return out;
        }

        public void setOut(OutParsedData out) {
            this.out = out;
        }
    }

    public HttpConfigParsedData parse() {
        return new HttpConfigParsedData(parseInData(), parseOutData());
    }

    private InParsedData parseInData() {
        List<String> urls = new ArrayList<>();
        InType inType = config.getIn().getType();
        if (inType != null) {
            switch (inType) {
                case script:
                    urls.addAll(new ScriptSupplier().get(config.getIn()));
                    break;
                case list:
                    urls.addAll(new ListSupplier().get(config.getIn()));
                    break;
                case fileLine:
                    urls.addAll(new FileLineSupplier().get(config.getIn()));
                    break;
            }
        }
        return new InParsedData(urls);
    }

    private OutParsedData parseOutData() {
        OutType outType = config.getOut().getType();
        OutConsumer consumer = null;
        if (outType != null) {
            switch (outType) {
                case hole:
                    consumer = new HoleConsumer();
                    break;
                case file:
                    consumer = new FileConsumer();
                    break;
                case stdout:
                    consumer = new StdOutConsumer();
                    break;
                case script:
                    consumer = new ScriptConsumer();
                    break;
                default:
                    consumer = new StdOutConsumer();
            }
        } else consumer = new StdOutConsumer();
        return new OutParsedData(consumer);
    }
}

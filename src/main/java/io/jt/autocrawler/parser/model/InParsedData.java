package io.jt.autocrawler.parser.model;

import java.util.List;

public class InParsedData {
    private List<String> urls;

    public InParsedData(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}

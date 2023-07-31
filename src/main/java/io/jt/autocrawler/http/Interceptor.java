package io.jt.autocrawler.http;

import org.apache.http.client.methods.HttpUriRequest;

public interface Interceptor {
    public void handle(String url, HttpUriRequest request);
}

package io.jt.autocrawler.model;

import io.jt.autocrawler.http.Http;

public class Request {
    private String url;
    private Http.HttpBuilder.Response response;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Http.HttpBuilder.Response getResponse() {
        return response;
    }

    public void setResponse(Http.HttpBuilder.Response response) {
        this.response = response;
    }
}

package io.jt.autocrawler.config;

import io.jt.autocrawler.constants.ConfigConstants;
import io.jt.autocrawler.doc.Doc;
import io.jt.autocrawler.doc.SubDoc;
import io.jt.autocrawler.model.In;
import io.jt.autocrawler.model.Out;

import java.util.Map;

@Doc("工具http请求配置,使用json配置")
public class HttpConfig {
    @Doc("请求url输入配置")
    @SubDoc
    private In in;
    @Doc("结果输出配置")
    @SubDoc
    private Out out;

    @Doc("请求配置headers")
    private Map headers;

    @Doc("请求线程配置")
    private int threads = ConfigConstants.DEFAULT_THREADS;

    @Doc(("url请求分组大小"))

    private int page = ConfigConstants.DEFAULT_HTTP_PAGE;


    public In getIn() {
        return in;
    }

    public void setIn(In in) {
        this.in = in;
    }

    public Out getOut() {
        return out;
    }

    public void setOut(Out out) {
        this.out = out;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }

    public Map getHeaders() {
        return headers;
    }

    public void setHeaders(Map headers) {
        this.headers = headers;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}

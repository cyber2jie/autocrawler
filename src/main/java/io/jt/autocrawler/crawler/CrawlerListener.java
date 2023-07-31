package io.jt.autocrawler.crawler;

import io.jt.autocrawler.config.CrawlerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.SpiderListener;

public class CrawlerListener implements SpiderListener {
    private CrawlerConfig crawlerConfig;
    private ClassLoader classLoader;
    private final Logger logger = LoggerFactory.getLogger(Logger.class);

    public CrawlerListener(CrawlerConfig crawlerConfig, ClassLoader classLoader) {
        this.crawlerConfig = crawlerConfig;
        this.classLoader = classLoader;
    }

    @Override
    public void onSuccess(Request request) {
        logger.info("{}请求已完成", request.getUrl());
    }

    @Override
    public void onError(Request request, Exception e) {
        logger.error("{}请求错误，错误原因{}", request.getUrl(), e.getMessage());
    }
}

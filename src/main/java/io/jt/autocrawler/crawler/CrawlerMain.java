package io.jt.autocrawler.crawler;

import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import io.jt.autocrawler.classloader.PathLibClassLoader;
import io.jt.autocrawler.config.CrawlerConfig;
import io.jt.autocrawler.constants.ConfigConstants;
import io.jt.autocrawler.constants.CrawlerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Spider;

import java.util.Arrays;

public class CrawlerMain {
    private final Logger logger = LoggerFactory.getLogger(CrawlerMain.class);

    private CrawlerConfig crawlerConfig;

    public CrawlerMain(CrawlerConfig crawlerConfig) {
        this.crawlerConfig = crawlerConfig;
    }

    public void start() {

        if (StrUtil.isBlank(crawlerConfig.getBaseUrl())) throw new CrawlerException("需配置baseUrl");
        ClassLoader classLoader = CrawlerMain.class.getClassLoader();
        if (StrUtil.isNotBlank(crawlerConfig.getLibPath())) {
            classLoader = PathLibClassLoader.create(crawlerConfig.getLibPath());
        }

        Spider spider = Spider.create(new GroovyScriptPageProcessor(crawlerConfig, classLoader));
        spider.addUrl(crawlerConfig.getBaseUrl());
        int thread = crawlerConfig.getThread();
        if (thread <= 0) thread = ConfigConstants.DEFAULT_THREADS;
        spider.thread(thread);
        spider.setSpiderListeners(Arrays.asList(new CrawlerListener(crawlerConfig, classLoader)));
        spider.setExitWhenComplete(true);

        if (StrUtil.isNotBlank(crawlerConfig.getBeforeStartScript())) {

            Binding spiderBinding = new Binding();
            spiderBinding.setProperty(CrawlerConstants.BINDING_SPIDER, spider);
            new CrawlerScriptExecutor(crawlerConfig.getBeforeStartScript(), spiderBinding, classLoader).execute();

        }

        spider.run();

    }


}

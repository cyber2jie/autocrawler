package io.jt.autocrawler.crawler;

import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import io.jt.autocrawler.config.CrawlerConfig;
import io.jt.autocrawler.constants.CrawlerConstants;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

public class GroovyScriptPageProcessor implements PageProcessor {
    private CrawlerConfig crawlerConfig;
    private ClassLoader classLoader;
    private Site site;
    private CrawlerScriptExecutor pageScriptExecutor;

    public GroovyScriptPageProcessor(CrawlerConfig crawlerConfig, ClassLoader classLoader) {
        this.crawlerConfig = crawlerConfig;
        this.classLoader = classLoader;
        site = new Site();
        if (StrUtil.isNotBlank(crawlerConfig.getPageProcessorSiteScript())) {
            Binding siteBinding = new Binding();
            siteBinding.setProperty(CrawlerConstants.BINDING_SITE, site);
            new CrawlerScriptExecutor(crawlerConfig.getPageProcessorSiteScript(), siteBinding, classLoader).execute();
        }

        pageScriptExecutor = new CrawlerScriptExecutor(crawlerConfig.getPageProcessorProcessScript(), null, classLoader);

    }

    @Override
    public void process(Page page) {
        if (StrUtil.isNotBlank(crawlerConfig.getPageProcessorProcessScript())) {
            Binding pageBinding = new Binding();
            pageBinding.setProperty(CrawlerConstants.BINDING_PAGE, page);
            pageScriptExecutor.setBinding(pageBinding);
            pageScriptExecutor.execute();
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}

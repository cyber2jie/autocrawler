package io.jt.autocrawler.config;


import io.jt.autocrawler.constants.ConfigConstants;
import io.jt.autocrawler.constants.CrawlerConstants;
import io.jt.autocrawler.doc.Doc;

@Doc("爬虫任务配置")
public class CrawlerConfig {
    @Doc("站点URL")
    private String baseUrl;
    @Doc("扩展库路径")
    private String libPath;
    @Doc("线程数")
    private int thread = ConfigConstants.DEFAULT_THREADS;

    @Doc("PageProcessor process执行脚本,脚本内置属性:" + CrawlerConstants.BINDING_PAGE)
    private String pageProcessorProcessScript;

    @Doc("PageProcessor site执行脚本,脚本内置属性:" + CrawlerConstants.BINDING_SITE)
    private String pageProcessorSiteScript;

    @Doc("启动前执行脚本,用于添加pipeline等操作,脚本内置属性:" + CrawlerConstants.BINDING_SPIDER)
    private String beforeStartScript;


    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getLibPath() {
        return libPath;
    }

    public void setLibPath(String libPath) {
        this.libPath = libPath;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getPageProcessorProcessScript() {
        return pageProcessorProcessScript;
    }

    public void setPageProcessorProcessScript(String pageProcessorProcessScript) {
        this.pageProcessorProcessScript = pageProcessorProcessScript;
    }

    public String getPageProcessorSiteScript() {
        return pageProcessorSiteScript;
    }

    public void setPageProcessorSiteScript(String pageProcessorSiteScript) {
        this.pageProcessorSiteScript = pageProcessorSiteScript;
    }

    public String getBeforeStartScript() {
        return beforeStartScript;
    }

    public void setBeforeStartScript(String beforeStartScript) {
        this.beforeStartScript = beforeStartScript;
    }
}

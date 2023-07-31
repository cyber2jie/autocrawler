package io.jt.autocrawler.crawler;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import io.jt.autocrawler.util.GroovyScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class CrawlerScriptExecutor {
    private String scriptPath;
    private Binding binding;
    private ClassLoader classLoader;
    private String script;

    private final Logger logger = LoggerFactory.getLogger(Logger.class);

    public CrawlerScriptExecutor(String scriptPath, Binding binding, ClassLoader classLoader) {
        this.scriptPath = scriptPath;
        this.binding = binding;
        this.classLoader = classLoader;
        loadScript();
    }

    private void loadScript() {

        try {
            script = FileUtil.readString(scriptPath, Charset.defaultCharset());
        } catch (Exception e) {
            logger.error("加载脚本错误{}", e.getMessage());
        }

    }

    public void execute() {

        execute(true);

    }

    public void execute(boolean ignoreError) {
        try {
            if (StrUtil.isNotBlank(script)) {
                GroovyScriptUtil.evaluate(classLoader, binding, script);
            }
        } catch (Exception e) {
            if (!ignoreError) throw new CrawlerException(e);
        }
    }

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public Binding getBinding() {
        return binding;
    }

    public void setBinding(Binding binding) {
        this.binding = binding;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}

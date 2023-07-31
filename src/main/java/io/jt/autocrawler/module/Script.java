package io.jt.autocrawler.module;

import cn.hutool.core.io.FileUtil;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import io.jt.autocrawler.classloader.PathLibClassLoader;
import io.jt.autocrawler.config.ScriptConfig;
import io.jt.autocrawler.crawler.CrawlerException;
import io.jt.autocrawler.exception.BaseException;
import io.jt.autocrawler.util.ArgParser;
import io.jt.autocrawler.util.GroovyScriptUtil;
import io.jt.autocrawler.util.JSONUtil;
import io.jt.autocrawler.util.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Objects;

public class Script extends Module {
    private final Logger logger = LoggerFactory.getLogger(Script.class);

    @Override
    public void run(String[] args) {

        new ArgParser(args, (parser, cmds) -> {

            ScriptConfig scriptConfig = null;
            String conf = cmds.getOptionValue("conf");
            try {
                String confStr = FileUtil.readString(conf, Charset.defaultCharset());
                scriptConfig = JSONUtil.parse(confStr, ScriptConfig.class);
            } catch (Exception e) {
                logger.error("加载配置文件{}错误", conf);
                throw new BaseException(e);
            }
            Objects.requireNonNull(scriptConfig, "加载配置文件错误");
            logger.info("开始执行脚本");

            try {
                ClassLoader classLoader = PathLibClassLoader.create(scriptConfig.getLibPath());
                GroovyShell groovyShell = GroovyScriptUtil.evaluateReturnShell(classLoader, new Binding(), "");
                scriptConfig.getScripts().stream().forEach(script -> {
                    groovyShell.evaluate(FileUtil.readString(script, Charset.defaultCharset()));
                });

            } catch (Exception e) {
                logger.error("脚本执行错误", e.getMessage());
                e.printStackTrace();
                throw new CrawlerException(e);
            }

            logger.info("脚本执行结束");

        }, (parser, error) -> {
            logger.error(error.getMessage());
            printUsage();
        }).option("conf", "配置文件路径", "")
                .parse();
    }

    private void printUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("使用 -conf 指定配置文件");
        Print.print(sb.toString());
    }
}

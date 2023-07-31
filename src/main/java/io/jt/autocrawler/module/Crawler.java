package io.jt.autocrawler.module;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import io.jt.autocrawler.config.CrawlerConfig;
import io.jt.autocrawler.crawler.CrawlerException;
import io.jt.autocrawler.crawler.CrawlerMain;
import io.jt.autocrawler.util.ArgParser;
import io.jt.autocrawler.util.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class Crawler extends Module {
    private final Logger logger = LoggerFactory.getLogger(Crawler.class);

    @Override
    public void run(String[] args) {

        new ArgParser(args, (parser, cmds) -> {

            CrawlerConfig crawlerConfig = null;
            String conf = cmds.getOptionValue("conf");
            try {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(FileUtil.readBytes(conf));
                Yaml yaml = new Yaml();
                crawlerConfig = yaml.loadAs(IoUtil.getBomReader(inputStream), CrawlerConfig.class);
            } catch (Exception e) {
                logger.error("加载配置文件{}错误", conf);
                throw new CrawlerException(e);
            }
            Objects.requireNonNull(crawlerConfig, "加载配置文件错误");
            logger.info("开始启动爬虫任务");
            try {
                new CrawlerMain(crawlerConfig).start();
            } catch (Exception e) {
                logger.error("任务运行错误", e.getMessage());
                e.printStackTrace();
                throw new CrawlerException(e);
            }
            logger.info("任务已结束");

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

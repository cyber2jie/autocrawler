package io.jt.autocrawler.module;

import io.jt.autocrawler.config.CrawlerConfig;
import io.jt.autocrawler.config.HttpConfig;
import io.jt.autocrawler.config.ScriptConfig;
import io.jt.autocrawler.doc.GenDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Doc extends Module {
    private final Logger logger = LoggerFactory.getLogger(Doc.class);

    @Override
    public void run(String[] args) {
        String path = "docs";
        if (args.length > 0) path = args[0];
        logger.info("获取到生成路径{}", path);
        GenDoc.genDoc(path, CrawlerConfig.class, HttpConfig.class, ScriptConfig.class);
        logger.info("文档已生成到路径{}", path);
    }
}

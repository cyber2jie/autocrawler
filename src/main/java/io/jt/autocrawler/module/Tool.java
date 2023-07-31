package io.jt.autocrawler.module;

import io.jt.autocrawler.exception.RunnerException;
import io.jt.autocrawler.runner.Runner;
import io.jt.autocrawler.runner.Runners;
import io.jt.autocrawler.util.ArgParser;
import io.jt.autocrawler.util.Print;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Tool extends Module {
    private final Logger logger = LoggerFactory.getLogger(Tool.class);

    @Override
    public void run(String[] args) {
        new ArgParser(args, (parser, cmds) -> {
            String tool = cmds.getOptionValue("t");
            String conf = cmds.getOptionValue("conf");
            Runner runner = Runners.get(tool);
            if (runner == null) throw new RunnerException("无法使用的工具");
            try {
                runner.run(conf);
            } catch (Exception e) {
                logger.error("运行工具错误{}", e.getMessage());
                throw new RunnerException(e);
            }
        }, (parser, error) -> {
            logger.error(error.getMessage());
            printUsage();
        }).option("t", "使用工具", "http").option("conf", "配置文件路径", "")
                .parse();
    }

    private void printUsage() {
        StringBuilder sb = new StringBuilder();
        sb.append("使用 -t 指定工具  -conf 指定配置文件");
        Print.print(sb.toString());
    }
}

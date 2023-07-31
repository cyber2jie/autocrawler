package io.jt.autocrawler;

import io.jt.autocrawler.env.Environment;
import io.jt.autocrawler.exception.ModuleException;
import io.jt.autocrawler.module.*;
import io.jt.autocrawler.util.ArgParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class AutoCrawler {
    private static final Logger logger = LoggerFactory.getLogger(AutoCrawler.class);
    private static final Map<String, Module> modules = new LinkedHashMap<>();

    static {
        modules.put("crawler", new Crawler());
        modules.put("repl", new Repl());
        modules.put("script", new Script());
        modules.put("tool", new Tool());
        modules.put("doc", new Doc());
    }

    public static void main(String[] args) {

        Environment.put(Environment.EnvID.ARGV, args);

        new ArgParser(args, (parser, cmds) -> {
            String m = cmds.getOptionValue("m");
            Module module = modules.get(m);
            if (module == null) throw new ModuleException(String.format("无法找到模块【%s】", m));
            logger.debug("运行模块{}", m);
            Environment.put(module);
            module.run(ArgParser.removeArgs(args, "m", "module"));
        }, (parser, error) -> {
            logger.error(error.getMessage());
            parser.getHelpFormatter().printHelp(Version.version, parser.getOptions());
        }).option("m", "module", "使用模块", modules.keySet().toString()).parse();
    }
}

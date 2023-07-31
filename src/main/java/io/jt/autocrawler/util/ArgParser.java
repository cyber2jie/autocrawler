package io.jt.autocrawler.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import org.apache.commons.cli.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArgParser {

    private final static CommandLineParser parser = DefaultParser.builder()
            .setStripLeadingAndTrailingQuotes(true)
            .build();
    private final static HelpFormatter helpFormatter = new HelpFormatter();
    private String args[];
    private Fun<ArgParser, CommandLine> onSuccess;
    private Fun<ArgParser, Exception> onError;
    private Options options;

    public ArgParser(String[] args, Fun<ArgParser, CommandLine> onSuccess, Fun<ArgParser, Exception> onError) {
        this.args = args;
        this.onSuccess = onSuccess;
        this.onError = onError;
        options = new Options();
    }

    public ArgParser option(String option, String desc, String argNames) {
        return option(option, option, desc, argNames);
    }

    public ArgParser option(String option, String longOption, String desc, String argNames) {
        return option(option, longOption, true, true, desc, argNames);
    }

    public ArgParser option(String option, String longOption, boolean required, boolean hasArg, String desc, String argNames) {
        Option op = Option.builder().option(option)
                .hasArg(hasArg)
                .required(required)
                .longOpt(longOption)
                .desc(desc).argName(argNames).build();
        options.addOption(op);
        return this;
    }

    public static HelpFormatter getHelpFormatter() {
        return helpFormatter;
    }

    public String[] getArgs() {
        return args;
    }

    public Options getOptions() {
        return options;
    }

    public void parse() {
        try {
            CommandLine cmds = parser.parse(getOptions(), getArgs(), true);
            onSuccess.call(this, cmds);
        } catch (Exception e) {
            onError.call(this, e);
        }
    }


    public static String[] removeArgs(String[] args, String... argName) {
        ArrayList<String> argList = new ArrayList<>();
        List<String> argNameList = Arrays.asList(argName);
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                String ag = StrUtil.sub(arg, 1, arg.length());
                if (argNameList.contains(ag)) {
                    int idx = i + 1;
                    if (args.length > idx) {
                        String nextArg = args[idx];
                        if (!nextArg.startsWith("-")) {
                            i++;
                        }
                    }
                    continue;
                }
            }
            argList.add(arg);
        }
        return ArrayUtil.toArray(argList, String.class);
    }
}

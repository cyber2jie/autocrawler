package io.jt.autocrawler.module;

import io.jt.autocrawler.util.GroovyScriptUtil;

public class Repl extends Module {
    @Override
    public void run(String[] args) {
        GroovyScriptUtil.repl();
    }
}

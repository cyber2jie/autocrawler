package io.jt.autocrawler.config;


import io.jt.autocrawler.doc.Doc;

import java.util.List;

@Doc("groovy执行脚本配置")
public class ScriptConfig {
    @Doc("扩展库路径")
    private String libPath;
    @Doc("脚本路径，json集合，可配置多个。")
    private List<String> scripts;

    public String getLibPath() {
        return libPath;
    }

    public void setLibPath(String libPath) {
        this.libPath = libPath;
    }

    public List<String> getScripts() {
        return scripts;
    }

    public void setScripts(List<String> scripts) {
        this.scripts = scripts;
    }
}
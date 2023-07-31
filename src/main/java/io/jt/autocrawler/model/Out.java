package io.jt.autocrawler.model;

import io.jt.autocrawler.doc.Doc;

import java.util.Map;

public class Out {
    @Doc("可配置hole,stdout,file,script。file需指定参数path(path支持velocity替换参数，参数为配置的参数),script方式需指定script参数或scriptPath参数，优先使用script。")
    private OutType type;
    @Doc("参数")
    private Map params;

    public OutType getType() {
        return type;
    }

    public void setType(OutType type) {
        this.type = type;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }

}

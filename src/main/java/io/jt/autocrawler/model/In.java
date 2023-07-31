package io.jt.autocrawler.model;

import io.jt.autocrawler.doc.Doc;

import java.util.Map;

public class In {
    @Doc("输入方式配置,list方式，在参数中列出请求的url。fileLine方式,在文件中的每行是一个url。script方式需指定script参数或scriptPath参数，优先使用script，脚本需返回url集合。")
    private InType type;
    @Doc("list方式，配置urls参数，类型为数组。fileLine方式配置path参数，类型为字符串。")
    private Map params;

    public InType getType() {
        return type;
    }

    public void setType(InType type) {
        this.type = type;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
    }
}

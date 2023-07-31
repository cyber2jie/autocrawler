package io.jt.autocrawler.parser.insupplier;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import io.jt.autocrawler.model.In;
import io.jt.autocrawler.parser.Const;
import io.jt.autocrawler.parser.InSupplier;
import io.jt.autocrawler.util.GroovyScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ScriptSupplier implements InSupplier {
    private Logger logger = LoggerFactory.getLogger(ScriptSupplier.class);

    @Override
    public List<String> get(In in) {
        List<String> urls = new ArrayList<>();
        String script = (String) in.getParams().get("script");
        if (StrUtil.isBlank(script)) {
            String scriptPath = (String) in.getParams().get("scriptPath");
            try {
                script = FileUtil.readString(scriptPath, Charset.defaultCharset());
            } catch (IORuntimeException e) {
                logger.warn("输出获取{}脚本错误,{}", scriptPath, e.getMessage());
            }

        }
        Binding binding = new Binding();
        binding.setProperty(Const.BINDDING_VAR_PREFIX + "in", in);
        Object urlsObject = GroovyScriptUtil.evaluate(binding, script);
        if (urlsObject instanceof Collection) {
            urls.addAll(Arrays.asList(ArrayUtil.toArray((Collection) urlsObject, String.class)));
        } else if (urlsObject instanceof String) {
            urls.add(Convert.toStr(urlsObject));
        }
        return urls;
    }
}

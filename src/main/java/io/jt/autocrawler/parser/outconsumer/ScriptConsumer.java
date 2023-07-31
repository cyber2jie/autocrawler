package io.jt.autocrawler.parser.outconsumer;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.StrUtil;
import groovy.lang.Binding;
import io.jt.autocrawler.model.Out;
import io.jt.autocrawler.model.Request;
import io.jt.autocrawler.parser.Const;
import io.jt.autocrawler.parser.OutConsumer;
import io.jt.autocrawler.util.GroovyScriptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class ScriptConsumer implements OutConsumer {
    private Logger logger = LoggerFactory.getLogger(ScriptConsumer.class);

    @Override
    public void consume(Request request, Out out, byte[] bytes) {
        String script = (String) out.getParams().get("script");
        if (StrUtil.isBlank(script)) {
            String scriptPath = (String) out.getParams().get("scriptPath");
            try {
                script = FileUtil.readString(scriptPath, Charset.defaultCharset());
            } catch (IORuntimeException e) {
                logger.warn("输出获取{}脚本错误,{}", scriptPath, e.getMessage());
            }

        }
        Binding binding = new Binding();
        binding.setProperty(Const.BINDDING_VAR_PREFIX + "out", out);
        binding.setProperty(Const.BINDDING_VAR_PREFIX + "request", request);
        binding.setProperty(Const.BINDDING_VAR_PREFIX + "bytes", bytes);
        GroovyScriptUtil.evaluate(binding, script);

    }
}

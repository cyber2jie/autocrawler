package io.jt.autocrawler.parser.outconsumer;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.jt.autocrawler.model.Out;
import io.jt.autocrawler.model.Request;
import io.jt.autocrawler.parser.Const;
import io.jt.autocrawler.parser.OutConsumer;
import io.jt.autocrawler.util.VelocityUtil;

import java.util.HashMap;
import java.util.Map;

public class FileConsumer implements OutConsumer {
    @Override
    public void consume(Request request, Out out, byte[] bytes) {
        String path = (String) out.getParams().get("path");
        if (StrUtil.isNotBlank(path)) {
            Map params = out.getParams();
            Map<String, Object> context = new HashMap<>();
            for (Object o : params.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                String k = Convert.toStr(entry.getKey(), "");
                if (StrUtil.isNotBlank(k)) {
                    context.put(k, entry.getValue());
                }
            }

            context.put(Const.PARAM_VAR_URL, request.getUrl());

            path = VelocityUtil.render(context, path);
            FileUtil.writeBytes(bytes, path);
        }
    }
}

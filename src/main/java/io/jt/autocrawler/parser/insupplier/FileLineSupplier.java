package io.jt.autocrawler.parser.insupplier;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.jt.autocrawler.model.In;
import io.jt.autocrawler.parser.InSupplier;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileLineSupplier implements InSupplier {
    @Override
    public List<String> get(In in) {
        List<String> urls = new ArrayList<>();
        String path = (String) in.getParams().get("path");
        String content = FileUtil.readString(path, Charset.defaultCharset());
        urls.addAll(StrUtil.split(content, "\n"));
        return urls;
    }
}

package io.jt.autocrawler.parser.insupplier;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import io.jt.autocrawler.model.In;
import io.jt.autocrawler.parser.InSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListSupplier implements InSupplier {
    @Override
    public List<String> get(In in) {
        List<String> urls = new ArrayList<>();
        Object urlsObject = in.getParams().get("urls");
        if (urlsObject instanceof Collection) {
            urls.addAll(Arrays.asList(ArrayUtil.toArray((Collection) urlsObject, String.class)));
        } else if (urlsObject instanceof String) {
            urls.add(Convert.toStr(urlsObject));
        }
        return urls;
    }
}

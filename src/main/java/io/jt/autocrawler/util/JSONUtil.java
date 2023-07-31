package io.jt.autocrawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;

public class JSONUtil {
    public static <T> T parse(String json, Class<T> cls) {
        return JSON.parseObject(json, cls, Feature.AllowSingleQuotes, Feature.AllowArbitraryCommas, Feature.AllowComment, Feature.SupportAutoType);
    }
}

package io.jt.autocrawler.util;

import ognl.Ognl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class OgnlUtil {
    private static final Logger logger = LoggerFactory.getLogger(OgnlUtil.class);

    public static Object getValue(String expression, Map<String, Object> context, Object root) {
        Object val = null;
        try {
            val = Ognl.getValue(expression, context, root);
        } catch (Exception e) {
            logger.warn("ognl get value fail,cause by {}", e.getMessage());
        }
        return val;
    }


}

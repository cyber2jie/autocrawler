package io.jt.autocrawler.lang;

public class StrUtilX {
    public static String nullToBlank(String str) {
        if (str == null || "null".equals(str.trim())) {
            return "";
        }
        return str;
    }
}


package io.jt.autocrawler.runner;

import java.util.HashMap;
import java.util.Map;

public class Runners {
    private static final Map<String, Runner> runners = new HashMap<>();

    static {
        runners.put("http", new HttpRunner());
    }

    public static Runner get(String id) {
        return runners.get(id);
    }
}

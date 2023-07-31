package io.jt.autocrawler.util;

import java.io.PrintStream;

public class Print {
    private final static PrintStream printStream = System.out;

    public static void print(String msg) {
        printStream.println(msg);
    }
}

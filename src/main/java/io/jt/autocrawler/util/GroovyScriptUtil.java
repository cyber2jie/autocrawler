package io.jt.autocrawler.util;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.apache.groovy.groovysh.Main;

public class GroovyScriptUtil {
    public static Object evaluate(String script) {
        return evaluate(new Binding(), script);
    }

    public static Object evaluate(Binding binding, String script) {
        return evaluate(GroovyScriptUtil.class.getClassLoader(), binding, script);
    }

    public static Object evaluate(ClassLoader classLoader, Binding binding, String script) {
        GroovyShell shell = new GroovyShell(classLoader, binding);
        return shell.evaluate(script);
    }

    public static GroovyShell evaluateReturnShell(Binding binding, String script) {
        return evaluateReturnShell(GroovyScriptUtil.class.getClassLoader(), binding, script);
    }

    public static GroovyShell evaluateReturnShell(ClassLoader classLoader, Binding binding, String script) {
        GroovyShell shell = new GroovyShell(classLoader, binding);
        shell.evaluate(script);
        return shell;
    }

    public static void repl() {
        Main.main();
    }
}

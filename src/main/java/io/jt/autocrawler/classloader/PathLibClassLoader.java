package io.jt.autocrawler.classloader;

import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class PathLibClassLoader extends URLClassLoader {
    public PathLibClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public PathLibClassLoader(URL[] urls) {
        this(urls, PathLibClassLoader.class.getClassLoader());
    }

    public static PathLibClassLoader create(String libPath) {
        List<URL> libs = new ArrayList<>();
        getJarFile(libPath).stream().forEach((lib) -> {
            try {
                libs.add(new File(lib).toURI().toURL());
            } catch (IOException e) {
                throw new LibLoadException("加载库(" + lib + ")错误", e);
            }
        });
        URL[] urls = libs.toArray(new URL[libs.size()]);
        return new PathLibClassLoader(urls);
    }

    private static List<String> getJarFile(String libPath) {
        List<String> jars = new ArrayList<>();
        File libFile = new File(libPath);

        if (libFile.exists()) {
            if (libFile.isDirectory()) {
                String[] libs = libFile.list((dir, file) -> {
                    return file.endsWith(".jar");
                });
                for (String lib : libs) {
                    jars.add(libPath + File.separator + lib);
                }

            } else {
                if (StrUtil.endWith(libPath, ".jar")) jars.add(libPath);
            }
        }

        return jars;
    }

}

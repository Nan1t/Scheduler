package edu.zieit.scheduler.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LibLoader {

    private final List<URL> urls;
    private ClassLoader loader;

    public LibLoader() {
        urls = new ArrayList<>();
    }

    public ClassLoader classLoader() {
        return loader;
    }

    public void loadAll(Path dir) {
        try {
            Files.list(dir).forEach(this::loadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadFile(Path file) {
        try {
            load(file.toUri().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void load(URL url) {
        urls.add(url);
    }

    public void finishLoad() {
        URL[] urls = this.urls.toArray(new URL[0]);
        loader = URLClassLoader.newInstance(urls, getClass().getClassLoader());
    }

}

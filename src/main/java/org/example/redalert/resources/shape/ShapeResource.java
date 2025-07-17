package org.example.redalert.resources.shape;

import org.example.redalert.resources.GameResource;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ShapeResource {
    private final ConcurrentHashMap<String, List<ShapeFrame>> mCache = new ConcurrentHashMap<>();

    {
        Enumeration<URL> resources = GameResource.getResources("shp/");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File file = new File(url.getFile());
            String name = file.getName();
            mCache.put(name, new ArrayList<>());
        }
    }

    public List<ShapeFrame> load(String name) {
        List<ShapeFrame> frames;
        try {
            if (!mCache.containsKey(name)) {
                frames = new ArrayList<>();
            } else {
                frames = mCache.get(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return frames;
    }
}

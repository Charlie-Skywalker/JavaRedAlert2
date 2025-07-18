package org.example.redalert.resources.audio;

import org.example.redalert.resources.GameResource;
import org.example.redalert.resources.palette.PaletteFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * 音频资源文件
 */
public class AudioResource {
    public final HashMap<String, AudioFile> mCache = new HashMap<>();
    
    {
        Enumeration<URL> resources = GameResource.getResources("wav/");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File file = new File(url.getFile());
            byte[] bytes = new byte [PaletteFile.LENGTH];
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.read(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String name = file.getName();
            mCache.put(name, new AudioFile(bytes));
        }
    }
    
    public AudioFile get(String name) {
        AudioFile file;
        try {
            if (mCache.containsKey(name)) {
                file = mCache.get(name);
            } else {
                file = new AudioFile(new byte[0]);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}

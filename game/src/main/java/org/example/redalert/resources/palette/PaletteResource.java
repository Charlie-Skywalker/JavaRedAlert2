package org.example.redalert.resources.palette;

import org.example.redalert.resources.GameResource;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

public class PaletteResource {
    public final HashMap<String, PaletteFile> mCache = new HashMap<>();
    
    {
        Enumeration<URL> resources = GameResource.getResources("pal/");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            File file = new File(url.getFile());
            byte[] bytes = new byte [PaletteFile.LENGTH];
            try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
                raf.read(bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            PaletteFile palette = PaletteParser.parse(bytes);
            String name = file.getName();
            mCache.put(name, palette);
        }
    }
}

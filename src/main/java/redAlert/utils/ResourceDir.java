package redAlert.utils;

import java.net.URL;
import java.io.File;
import java.net.URLDecoder;


public class ResourceDir {
    public static String getResourceDir(String category) {
        URL url = PalFileReader.class.getClassLoader().getResource(".");
        
        String resourceDir = null; 
        if(url != null) {
            resourceDir = url.getPath();//当前目录  也就是与classes文件夹所在目录  变量以"/"或"\"结尾
        }
        else {
            resourceDir = System.getProperty("user.dir");
        }

        try {
            resourceDir= URLDecoder.decode(resourceDir, "UTF-8");
        }catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(resourceDir, category);
        return file.getAbsolutePath();
    }
}



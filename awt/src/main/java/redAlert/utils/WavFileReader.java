package redAlert.utils;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.HashMap;

/**
 * 音乐文件读取类
 */
public class WavFileReader {
    public static String wavPath = WavFileReader.class.getClassLoader().getResource("wav/").getFile();
    
    /**
     * 音乐缓存,避免播放音乐时一直从硬盘中读取
     */
    private static final HashMap<String, ByteArrayInputStream> musicCache = new HashMap<>();
    
    /**
     * 获取音乐文件的字节流
     * 有缓存从缓存中加载,没缓存从硬盘加载
     */
    public static ByteArrayInputStream getMusicInputStream(String wavPrefix) {
        try {
            ByteArrayInputStream musicInputStream;
            if (!musicCache.containsKey(wavPrefix)) {
                File musicFile = new File(wavPath + "/" + wavPrefix + ".wav");
                byte[] musicBytes = FileUtils.readFileToByteArray(musicFile);// 获取字节数组
                musicInputStream = new ByteArrayInputStream(musicBytes);
                musicCache.put(wavPrefix, musicInputStream);
            } else {
                musicInputStream = musicCache.get(wavPrefix);
                musicInputStream.reset();
            }
            return musicInputStream;
        } catch (Exception e) {
			throw new RuntimeException(e);
        }
    }
}

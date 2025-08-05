package redAlert.resourceCenter;

import org.apache.commons.io.FileUtils;
import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.utils.ShpFileReader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Shape文件资源管理中心
 * <p>
 * 向外部提供SHP帧数据的读取接口
 */
public class ShpResourceCenter {
    
    /**
     * SHP文件前缀和路径名称缓存
     */
    private static HashMap<String, String> shpPrefixToPathMap = new HashMap<String, String>();
    
    /**
     * SHP帧数据缓存
     * 已加载过的SHP在此缓存,不在硬盘重复加载
     */
    private static ConcurrentHashMap<String, List<ShapeUnitFrame>> shpCache = new ConcurrentHashMap<>();
    
    /**
     * 加载ClassPath目录下的所有SHP文件名和文件目录
     */
    static {
        String classPath = ShpResourceCenter.class.getClassLoader().getResource("shp/").getFile();
        File file = new File(classPath);
        Iterator<File> files = FileUtils.iterateFiles(file, new String[] { "shp" }, true);
        while (files.hasNext()) {
            File shpFile = files.next();
            String name = shpFile.getName();
            String shpPrefix = name.substring(0, name.indexOf("."));
            String path = shpFile.getAbsolutePath();
            shpPrefixToPathMap.put(shpPrefix, path);
        }
    }
    
    /**
     * 根据SHP文件前缀名找文件路径
     */
    private static String getShpByPrefix(String shpPrefix) {
        return shpPrefixToPathMap.get(shpPrefix);
    }
    
    /**
     * 资源是否已加载过
     */
    private static boolean isLoaded(String shpPrefix, String palPrefix) {
        return shpCache.containsKey(shpPrefix + "__" + palPrefix);
    }
    
    /**
     * 从缓存中加载shp帧数据
     */
    private static List<ShapeUnitFrame> loadFromCache(String shpPrefix, String palPrefix) {
        return shpCache.get(shpPrefix + "__" + palPrefix);
    }
    
    /**
     * 将shp帧数据加入缓存
     */
    private static List<ShapeUnitFrame> addToCache(String shpPrefix, String palPrefix, List<ShapeUnitFrame> frames) {
        return shpCache.put(shpPrefix + "__" + palPrefix, frames);
    }
    
    /**
     * 加载SHP文件的工作由此类来完成,避免重复加载资源
     * 第一次加载的内容会加入缓存,加载时先查找缓存中是否有数据
     *
     * @param shpPrefix shp文件名前缀
     * @param palPrefix pal文件名前缀
     * @return
     */
    public static List<ShapeUnitFrame> loadShpResource(String shpPrefix, String palPrefix, boolean half) {
        
        List<ShapeUnitFrame> frames = null;
        try {
            String shpFilePath = getShpByPrefix(shpPrefix);
            if (isLoaded(shpPrefix, palPrefix)) {
                return loadFromCache(shpPrefix, palPrefix);
            } else {
                frames = ShpFileReader.convertShpFileToBuildingFrames(shpFilePath, palPrefix, half);
                addToCache(shpPrefix, palPrefix, frames);
                return frames;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return frames;
    }
    
    /**
     * 鉴于大部分SHP只加载一半,提供一个这个方法,方便以后
     *
     * @param shpPrefix shp文件名前缀
     * @param palPrefix
     * @return
     */
    public static List<ShapeUnitFrame> loadShpResource(String shpPrefix, String palPrefix) {
        return loadShpResource(shpPrefix, palPrefix, true);
    }
    
    /**
     * 加载工作动画
     */
    public static List<List<ShapeUnitFrame>> loadWorkingFrames(List<String> workingShpPrefixLs, SceneType scene) {
        List<List<ShapeUnitFrame>> result = new ArrayList<>();
        for (String prefix : workingShpPrefixLs) {
            List<ShapeUnitFrame> list = loadShpResource(prefix, scene.getPalPrefix());
            result.add(list.subList(0, list.size() / 2));
        }
        return result;
    }
    
    /**
     * 加载受损动画
     */
    public static List<List<ShapeUnitFrame>> loadDamagedFrames(List<String> workingShpPrefixLs, SceneType scene) {
        List<List<ShapeUnitFrame>> result = new ArrayList<>();
        for (String prefix : workingShpPrefixLs) {
            List<ShapeUnitFrame> list = loadShpResource(prefix, scene.getPalPrefix());
            result.add(list.subList(list.size() / 2, list.size()));
        }
        return result;
    }
}

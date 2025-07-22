package org.example.redalert.resources;

import org.example.redalert.resources.audio.AudioResource;
import org.example.redalert.resources.palette.PaletteResource;
import org.example.redalert.resources.shape.ShapeResource;
import org.example.redalert.utils.JavaResourcesUtil;

import java.net.URL;
import java.util.Enumeration;

/**
 * 游戏资源类
 */
public class GameResource {
    /**
     * 色盘资源
     */
    public static final PaletteResource sPalette =  new PaletteResource();
    /**
     * 图形资源
     */
    public static final ShapeResource sShape =  new ShapeResource();
    /**
     * 音频资源
     */
    public static final AudioResource sAudio =  new AudioResource();
    
    /**
     * 获取游戏资源
     * @param name 资源名称
     * @return 所有资源链接，不存在时返回空枚举
     */
    public static Enumeration<URL> getResources(String name) {
        // TODO 此处应该扩展方法，实现资源覆盖
        return JavaResourcesUtil.getResources(name);
    }
}

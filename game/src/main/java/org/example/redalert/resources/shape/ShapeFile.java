package org.example.redalert.resources.shape;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形文件
 */
public class ShapeFile {
    /**
     * 标签
     */
    public int mFlag;
    /**
     * 图片宽度
     */
    public int mWidth;
    /**
     * 图片高度
     */
    public int mHeight;
    /**
     * 帧数
     */
    public int mFrameCount;
    /**
     * 图形文件的所有帧
     */
    public final List<ShapeFrame> mFrames = new ArrayList<>();
}

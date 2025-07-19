package org.example.redalert.resources.shape;

import java.util.ArrayList;
import java.util.List;

/**
 * 图形帧数据
 */
public class ShapeFrame {
    public int mMinX;
    public int mMinY;
    public int mWidth;
    public int mHeight;
    public int mType;
    public int mTypeAlign;
    public int mColor;
    public int mReversed;
    public int mOffset;
    public final List<ShapeFramePixel> mPixels = new ArrayList<>();
}

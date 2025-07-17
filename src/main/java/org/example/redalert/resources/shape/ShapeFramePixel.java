package org.example.redalert.resources.shape;

/**
 * 图形帧像素点
 */
public class ShapeFramePixel {
    /**
     * X 坐标
     */
    public int mX;
    /**
     * Y 坐标
     */
    public int mY;
    /**
     * 颜色索引
     */
    public int mColorIndex;
    /**
     * 颜色值
     */
    public int mColor;

    public ShapeFramePixel(int x, int y, int colorIndex) {
        this(x, y, colorIndex, 0);
    }

    public ShapeFramePixel(int x, int y, int colorIndex, int color) {
        mX = x;
        mY = y;
        mColorIndex = colorIndex;
        mColor = color;
    }
}

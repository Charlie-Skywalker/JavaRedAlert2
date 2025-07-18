package org.example.redalert.ui.element;

/**
 * 定义游戏中的一个地块
 */
public class Cell {
    /**
     * 菱形横轴长
     */
    public static final int sWidth = 40;
    
    /**
     * 菱形竖轴长
     */
    public static final int sHeight = 20;
    
    /**
     * 中心点横坐标
     */
    public int mCenterX;
    
    /**
     * 中心点纵坐标
     */
    public int mCenterY;
    
    /**
     * 左上角 X 坐标
     */
    public int mWestNorthX;
    
    /**
     * 左上角 Y 坐标
     */
    public int mWestNorthY;
    
    /**
     * 右上角 X 坐标
     */
    public int mEastNorthX;
    
    /**
     * 右上角 Y 坐标
     */
    public int mEastNorthY;
    
    /**
     * 左下角 X 坐标
     */
    public int mWestSouthX;
    
    /**
     * 左下角 Y 坐标
     */
    public int mWestSouthY;
    
    /**
     * 右下角 X 坐标
     */
    public int mEastSouthX;
    
    /**
     * 右下角 Y 坐标
     */
    public int mEastSouthY;
    
    /**
     * 左顶点 X 坐标
     */
    public int mWestX;
    
    /**
     * 左顶点 Y 坐标
     */
    public int mWestY;
    
    /**
     * 上顶点 X 坐标
     */
    public int mNorthX;
    
    /**
     * 上顶点 Y 坐标
     */
    public int mNorthY;
    
    /**
     * 右顶点 X 坐标
     */
    public int mEastX;
    
    /**
     * 右顶点 Y 坐标
     */
    public int mEastY;
    
    /**
     * 下顶点 X 坐标
     */
    public int mSouthX;
    
    /**
     * 下顶点 Y 坐标
     */
    public int mSouthY;
    
    public Cell(int centerX, int centerY) {
        this.mCenterX = centerX;
        this.mCenterY = centerY;
        this.mWestNorthX = centerX - sWidth / 2;
        this.mWestNorthY = centerY - sHeight / 2;
        this.mEastNorthX = centerX + sWidth / 2;
        this.mEastNorthY = centerY - sHeight / 2;
        this.mWestSouthX = centerX - sWidth / 2;
        this.mWestSouthY = centerY + sHeight / 2;
        this.mEastSouthX = centerX + sWidth / 2;
        this.mEastSouthY = centerY + sHeight / 2;
        this.mWestX = centerX - sWidth / 2;
        this.mWestY = centerY;
        this.mNorthX = centerX;
        this.mNorthY = centerY - sHeight / 2;
        this.mEastX = centerX + sWidth / 2;
        this.mEastY = centerY;
        this.mSouthX = centerX;
        this.mSouthY = centerY + sHeight / 2;
    }
}

package org.example.redalert.resources.palette;

/**
 * 色盘颜色包装类
 */
public class PaletteColor {
    /**
     * 透明度
     */
    public byte mAlpha;
    
    /**
     * 红色
     */
    public byte mRed;
    
    /**
     * 绿色
     */
    public byte mGreen;
    
    /**
     * 蓝色
     */
    public byte mBlue;
    
    public PaletteColor() {
        this((byte) 255, (byte) 255, (byte) 255, (byte) 255);
    }
    
    public PaletteColor(byte red, byte green, byte blue) {
        this((byte) 255, red, green, blue);
    }
    
    public PaletteColor(byte alpha, byte red, byte green, byte blue) {
        this.mAlpha = alpha;
        this.mRed = red;
        this.mGreen = green;
        this.mBlue = blue;
    }
}

package org.example.redalert.controller;

import org.example.redalert.ui.element.Cell;

import java.util.ArrayList;

public class MapController {
    /**
     * 地块信息
     */
    public static final ArrayList<Cell> sCells = new ArrayList<>();
    
    /**
     * 根据地图尺寸创建地块
     * @param width 地图宽
     * @param height 地图高
     */
    public static void createTiles(int width, int height) {
        sCells.clear();
        
        // 每个地图的地块从界面左上角原点 (0, 0) 开始绘制，一直绘制到不能绘制完整地块
        int maxX = width - Cell.sWidth;
        int maxY = height - Cell.sHeight;
        
        // 绘制菱形时，从 (width / 2, height / 2) 开绘制才能得到完整菱形
        for (int x = Cell.sWidth / 2; x < width; x += Cell.sWidth) {
            for (int y = Cell.sHeight / 2; y < height; y += Cell.sHeight) {
                sCells.add(new Cell(x, y));
            }
        }
    }
}

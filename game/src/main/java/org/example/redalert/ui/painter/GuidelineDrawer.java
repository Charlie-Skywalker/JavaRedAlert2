package org.example.redalert.ui.painter;

import org.example.redalert.controller.MapController;
import org.example.redalert.map.Cell;
import org.example.redalert.map.Map;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 绘制地块分割线
 */
public class GuidelineDrawer implements Drawer {
    @Override
    public void draw(BufferedImage image) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.black); // 直线颜色
        // 绘制单元格边框
        Map map = MapController.getMap();
        for (Cell cell : map.getCells()) {
            // 绘制 West-North 方向边
            graphics.drawLine(cell.getWestX(), cell.getWestY(), cell.getNorthX(), cell.getNorthY());
            // 绘制 East-North 方向边
            graphics.drawLine(cell.getNorthX(), cell.getNorthY(), cell.getEastX(), cell.getEastY());
            // 绘制 East-South 方向边
            graphics.drawLine(cell.getEastX(), cell.getEastY(), cell.getSouthX(), cell.getSouthY());
            // 绘制 West-South 方向边
            graphics.drawLine(cell.getSouthX(), cell.getSouthY(), cell.getWestX(), cell.getWestY());
        }
    }
}

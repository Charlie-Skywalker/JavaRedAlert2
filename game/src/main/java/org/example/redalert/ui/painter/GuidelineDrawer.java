package org.example.redalert.ui.painter;

import org.example.redalert.controller.MapController;
import org.example.redalert.ui.element.Cell;

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
        for (Cell cell : MapController.sCells) {
            // 绘制 West-North 方向边
            graphics.drawLine(cell.mWestX, cell.mWestY, cell.mNorthX, cell.mNorthY);
            // 绘制 East-North 方向边
            graphics.drawLine(cell.mNorthX, cell.mNorthY, cell.mEastX, cell.mEastY);
            // 绘制 East-South 方向边
            graphics.drawLine(cell.mEastX, cell.mEastY, cell.mSouthX, cell.mSouthY);
            // 绘制 West-South 方向边
            graphics.drawLine(cell.mSouthX, cell.mSouthY, cell.mWestX, cell.mWestY);
        }
    }
}

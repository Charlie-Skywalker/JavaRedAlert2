package org.example.redalert.ui.panel;

import org.example.redalert.GameContext;
import org.example.redalert.controller.MapController;
import org.example.redalert.controller.MouseCursorController;
import org.example.redalert.ui.painter.Drawer;
import org.example.redalert.ui.painter.GuidelineDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BattlefieldPanel extends JPanel {
    /**
     * 游戏主画面宽高
     * 宽高比设为2比1是因为菱形格子横竖对角线长度比为2比1
     */
    public static final int sWidth = 800;
    public static final int sHeight = 400;
    /**
     * JPanel 在 JFrame 中的坐落位置
     */
    private static final int sLocationX = 0;
    private static final int sLocationY = 0;
    public final BufferedImage mImage = new BufferedImage(sWidth, sHeight, BufferedImage.TYPE_INT_ARGB);
    /**
     * TODO 绘制器，暂时没有想好如何抽象
     */
    public final ArrayList<Drawer> mDrawers = new ArrayList<>();
    
    public BattlefieldPanel() {
        // 设置尺寸
        Dimension dimension = new Dimension(sWidth, sHeight);
        setSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        
        // 设置坐标
        setLocation(sLocationX, sLocationY);
        // JPanel 的布局默认是 FlowLayout
        setLayout(null);
        // 设置一个看不见的鼠标
        setCursor(MouseCursorController.sEmpty.mCursor);
        // 在上下文中注册
        GameContext.sBattlefield = this;
        // 初始化任务
        new Thread(new InitTask()).start();
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(mImage, 0, 0, this);
    }
    
    private class InitTask implements Runnable {
        @Override
        public void run() {
            MapController.createTiles(sWidth, sHeight);
            GuidelineDrawer drawer = new GuidelineDrawer();
            drawer.draw(mImage);
        }
    }
}

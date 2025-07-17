package org.example.redalert.ui.panel;

import org.example.redalert.GameContext;
import org.example.redalert.controller.MouseCursorController;

import javax.swing.*;
import java.awt.*;

public class BattlefieldPanel extends JPanel {
    /**
     * 游戏主画面宽高
     * 宽高比设为2比1是因为菱形格子横竖对角线长度比为2比1
     */
    public static final int sWidth = 1800;
    public static final int sHeight = 900;
    /**
     * JPanel在JFrame中的坐落位置
     */
    private static final int sLocationX = 0;
    private static final int sLocationY = 0;
    
    public BattlefieldPanel() {
        Dimension dimension = new Dimension(sWidth,sHeight);
        // 设置尺寸
        super.setSize(dimension);
        // 最小尺寸
        super.setMinimumSize(dimension);
        // 首选尺寸
        super.setPreferredSize(dimension);
        // 设置坐标
        super.setLocation(sLocationX, sLocationY);
        // JPanel 的布局默认是 FlowLayout
        super.setLayout(null);
        // 设置一个看不见的鼠标
        setCursor(MouseCursorController.sEmpty.mCursor);
        // 在上下文中注册
        GameContext.sBattlefield = this;
    }
}

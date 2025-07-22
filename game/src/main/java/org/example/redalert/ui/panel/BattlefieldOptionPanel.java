package org.example.redalert.ui.panel;

import org.example.redalert.GameContext;
import org.example.redalert.controller.MouseCursorController;

import javax.swing.*;
import java.awt.*;

public class BattlefieldOptionPanel extends JPanel {
    /**
     * 选项卡页面宽高
     */
    public static final int sWidth = 168;
    public static final int sHeight = BattlefieldPanel.sHeight;
    
    /**
     * JPanel 在 JFrame 中的坐落位置
     */
    private static final int sLocationX = BattlefieldPanel.sWidth;
    private static final int sLocationY = 0;
    
    public BattlefieldOptionPanel() {
        // JFrame 是边界布局，只有这个方法可以设置大小，setSize 和设置最大最小都没用
        Dimension dimension = new Dimension(sWidth, sHeight);
        setSize(dimension);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);
        
        // 设置坐标
        setLocation(sLocationX, sLocationY);
        // 背景色
        setBackground(Color.green);
        // 设置一个看不见的鼠标
        setCursor(MouseCursorController.sEmpty.mCursor);
        // 在上下文中注册
        GameContext.sBattlefieldOption = this;
    }
}

package org.example.redalert;

import org.example.redalert.ui.panel.BattlefieldOptionPanel;
import org.example.redalert.ui.panel.BattlefieldPanel;

import javax.swing.*;
import java.awt.*;

public class Launcher {
    /**
     * 窗口宽度
     */
    public static final int WINDOW_WIDTH = BattlefieldPanel.sWidth + BattlefieldOptionPanel.sWidth;
    /**
     * 窗口高度
     * <p>
     * 32 是微软建议的标题栏高度
     */
    public static final int WINDOW_HEIGHT = BattlefieldPanel.sHeight + 32;
    
    public static void main(String[] args) {
        // 程序窗口
        JFrame frame = new JFrame("红色警戒");
        
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.setResizable(false); // 不可调整大小
        frame.setAlwaysOnTop(false); // 总是在最上层
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // 屏幕居中
        frame.setVisible(true); // JFrame 默认不可见，设置为可见
        frame.pack();
        
        // 游戏主界面
        BattlefieldPanel battlefield = new BattlefieldPanel();
        frame.add(BorderLayout.CENTER, battlefield); // 格式布局放中间
        
        // 选项卡页面
        BattlefieldOptionPanel option = new BattlefieldOptionPanel();
        frame.add(BorderLayout.EAST, option);
        
        // 初始化鼠标指针形状图片
        // MouseCursorController.init();
        // battlefield.setCursor(MouseCursorController.getNone()); // 设置一个看不见的鼠标
        
        // 鼠标事件的处理
        // MouseEventDeal.init(battlefield);
        // 键盘事件的处理
        // KeyBoardEventDeal.init(battlefield);
        // 伤害计算器
        // ShapeUnitResourceCenter.startDamageCalculate();
    }
}

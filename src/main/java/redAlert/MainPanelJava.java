package redAlert;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


import javax.swing.*;

import redAlert.manager.draw.DrawManager;

/**
 * 游戏场景界面
 * 基于JavaSwing渲染的游戏场景画板
 */
public class MainPanelJava extends JPanel{

	private static final long serialVersionUID = 1L;
	

	/**
	 * 最终给SWT线程绘制用的画板
	 */
	public BufferedImage canvas = new BufferedImage(SysConfig.viewportWidth,SysConfig.viewportHeight,BufferedImage.TYPE_INT_ARGB);

	public DrawManager drawManager;
	/**
	 * 执行画板初始化
	 */
	public MainPanelJava() {
		drawManager = new DrawManager(canvas,this);
		drawManager.panelInit();
	}

	/**
	 * 重绘方法  将主画板的内容绘制在窗口中
	 * Swing的组件,应该重写paintComponent方法  这样没有闪屏问题
	 *
	 */
	@Override
	public void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			//获取视口偏移,由于这两个变量变化频繁,所以需要获取一个快照,否则移动视口内容会抖动
			int theSightOffX = RuntimeParameter.viewportOffX;
			int theSightOffY = RuntimeParameter.viewportOffY;

			//绘制方法放在这比放在repaint()之前效果更流畅
			//绘制地形（地形的代码块覆盖全图,所以就不用重新清空画板了）
			drawManager.drawTerrain(theSightOffX,theSightOffY,null);
			//绘制游戏内的ShapeUnit
			drawManager.drawMainInterface(theSightOffX,theSightOffY,null);
			//绘制预建造菱形红绿块
			drawManager.drawRhombus(theSightOffX, theSightOffY,null);
			//绘制选择框
			drawManager.drawSelectRect(null);
			//绘制鼠标指针
			drawManager.drawMouseCursor(null);
			RuntimeParameter.frameCount++;
			g.drawImage(canvas, 0, 0, this);
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

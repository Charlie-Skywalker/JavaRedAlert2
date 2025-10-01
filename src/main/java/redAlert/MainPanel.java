package redAlert;


import java.awt.image.BufferedImage;

import com.jogamp.opengl.awt.GLJPanel;

import redAlert.manager.draw.DrawManager;


/**
 * 游戏场景界面
 * 基于OpenGL渲染的游戏场景画板
 *
 */
public class MainPanel extends GLJPanel {

	private static final long serialVersionUID = 1L;


	public DrawManager drawManager;

	/**
	 * 临时画板   最终将移除此画板,但是现在还没改完
	 */
	BufferedImage canvas = new BufferedImage(SysConfig.viewportWidth, SysConfig.viewportHeight, BufferedImage.TYPE_INT_ARGB);


	/**
	 * 执行画板初始化
	 */
	public MainPanel() {
		drawManager = new DrawManager(canvas, this);
		drawManager.panelInit();
	}
}
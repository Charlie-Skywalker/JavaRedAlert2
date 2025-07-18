package redAlert.mapEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import redAlert.utils.CanvasPainter;

/**
 * 地图编辑器的界面
 */
public class MapEditorPanel extends JPanel{

	private static final long serialVersionUID = -682168191460964814L;
	/**
	 * JPanel在JFrame中的坐落位置
	 */
	private final int locationX = 0;
	private final int locationY = 0;
	/**
	 * 游戏主画面宽高
	 * 宽高比设为2比1是因为菱形格子横竖对角线长度比为2比1
	 */
	public static final int viewportWidth = 1800;
	public static final int viewportHeight = 900;
	/**
	 * 视口在地图上偏移量
	 * 视野偏移量,用于确认渲染的范围
	 */
	public static int viewportOffX = 0;
	public static int viewportOffY = 0;
	/**
	 * 战场地图的宽高
	 */
	public static final int gameMapWidth = 6000;
	public static final int gameMapHeight = 4000;
	/**
	 * 主画板绘画间隔66毫秒
	 * 红警的默认帧率是15帧/秒,这样设置更接近红警2
	 * 
	 * 速度5下  红警的默认帧率是60帧
	 */
	private final long paintPeriod = 17;
	
	public static long frameCount = 0;
	
	
	/**
	 * 背景
	 */
	public BufferedImage background = new BufferedImage(viewportWidth,viewportHeight,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 辅助线格
	 */
	public BufferedImage guidelinesCanvas = new BufferedImage(viewportWidth,viewportHeight,BufferedImage.TYPE_INT_ARGB);
	
	
	public MapEditorPanel myself;
	
	public MapEditorPanel() {
		myself = this;
		super.setLocation(locationX, locationY);
//		super.setLayout(null);//JPanel的布局默认是FlowLayout
		super.setSize(viewportWidth, viewportHeight);
		super.setMinimumSize(new Dimension(viewportWidth,viewportHeight));//最小尺寸
		super.setPreferredSize(new Dimension(viewportWidth,viewportHeight));//首选尺寸
//		super.setMaximumSize(new Dimension(panelWidth,panelHeight));
		
		drawGuidelinesCanvas();//初始化辅助线格
		TilesSourceCenter.loadResource();
		startPainterThread();//启动绘画线程
	}
	
	/**
	 * TODO 绘制左侧地形的线程
	 * 
	 */
	private void startPainterThread() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			
			
			@Override
			public void run() {
				
				//画背景
				Graphics g = background.createGraphics();
				g.setColor(new Color(128,128,128));
				g.fillRect(0, 0, viewportWidth, viewportHeight);
				g.dispose();
				
				//画生成的地图
				if(MapEditorTest.isMapComplete) {
					int viewportOffX = MapEditorPanel.viewportOffX;
					int viewportOffY = MapEditorPanel.viewportOffY;
					Graphics g2d = guidelinesCanvas.getGraphics();
					List<MapCenterPoint> allMpcs = RandomMapGenerate.allMcps;
					for(MapCenterPoint mcp:allMpcs) {
						Tile tile = mcp.getTile();
						g2d.drawImage(tile.getImage(), mcp.getX()-30-viewportOffX, mcp.getY()-15-viewportOffY, null);
					}
					g2d.dispose();
				}
				
				
				
				
				
				myself.repaint();
				frameCount++;
			}
		};
		
		timer.schedule(refreshTask, 1L, paintPeriod);
	}
	
	/**
	 * 初始化辅助线格
	 */
	private void drawGuidelinesCanvas() {
		CanvasPainter.drawGuidelines(guidelinesCanvas);//辅助线网格
	}
	
	/**
	 * 重绘方法  将主画板的内容绘制在窗口中
	 */
	@Override
	public void paint(Graphics g) {
		try {
			super.paint(g);
			g.clearRect(0, 0, gameMapWidth, gameMapHeight);
			g.drawImage(background, 0, 0, this);//画背景
			g.drawImage(guidelinesCanvas, 0, 0, this);//画地形
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

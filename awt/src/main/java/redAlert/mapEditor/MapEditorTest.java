package redAlert.mapEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;

import redAlert.other.Mouse;

/**
 * 地图编辑器
 */
public class MapEditorTest {

	
	/**
	 * 游戏窗口的宽高
	 */
	public static final int frameWidth = MapEditorPanel.viewportWidth + TilesSelectPanel.optionWidth;
	public static final int frameHeight = MapEditorPanel.viewportHeight;
	
	public static boolean isMapComplete = false;
	
	public static void main(String[] args) throws Exception{
		//程序窗口
		JFrame jf = new JFrame("红色警戒地图编辑器");
		
		
		//初始化鼠标指针形状图片
		Mouse.initMouseCursor();
		
		//编辑器主界面
		MapEditorPanel eidtorPanel = new MapEditorPanel();
		jf.add(BorderLayout.WEST,eidtorPanel);//格式布局放左边
		
		//选项卡页面
		TilesSelectPanel optionsPanel = new TilesSelectPanel();
		jf.add(BorderLayout.EAST,optionsPanel);
		
		jf.setSize(frameWidth,frameHeight);
		jf.setMaximumSize(new Dimension(frameWidth, frameHeight));
		jf.setResizable(false);//不可调整大小
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见,设置为可见
		
		/**
		 * 鼠标事件的处理
		 */
		MapMouseEventDeal.init(eidtorPanel);
		
		MapKeyBoardEventDeal.init(eidtorPanel);
		
		Thread.sleep(3000);
		RandomMapGenerate.randomGenerate();
//		isMapComplete = true;
	}
}

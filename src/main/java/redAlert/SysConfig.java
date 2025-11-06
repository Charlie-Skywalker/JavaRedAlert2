package redAlert;

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * 系统参数
 * 一般确定后在游戏过程中不再变化
 */
public class SysConfig {
	
	public static void initSysConfig() {
		//初始化屏幕尺寸
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		SysConfig.screenWidth = dimension.width;
		SysConfig.screenHeight = dimension.height;
		SysConfig.frameWidth = (int)(screenWidth*0.8);
		SysConfig.frameHeight = (int)(screenHeight*0.70);
		
		SysConfig.viewportWidth = frameWidth-OptionsPanel.optionWidth;
		SysConfig.viewportHeight = frameHeight-32;//32是微软建议的标题栏高度
		
		//计算应显示的双人位的个数
		
		//钱板 16像素
		//顶板 32像素
		//雷达 110像素
		//背板1 69像素
		//底板+下选板 26+63   但是最少应该展示一半
		
		OptionsPanel.side2Num = (SysConfig.viewportHeight-16-32-110-69-89)/50+1;
	}
	
	public static int screenWidth;
	public static int screenHeight;
	/**
	 * 游戏场景面板位置
	 * 游戏场景JPanel在JFrame中的坐落位置
	 */
	public static final int locationX = 0;
	public static final int locationY = 0;
	/**
	 * 游戏主画面宽高
	 * 主画面包括MainPanel+OptionsPanel
	 */
	public static int viewportWidth;
	public static int viewportHeight;
	/**
	 * 游戏窗口的宽高
	 */
	public static int frameWidth;
	public static int frameHeight;
	/**
	 * 战场地图的宽高
	 * 
	 * 以后可能会增加可视区域的宽高,所以这个值是地图的最大值,所以认为不可变
	 */
	public static final int gameMapWidth = 6000;
	public static final int gameMapHeight = 4000;
	
}

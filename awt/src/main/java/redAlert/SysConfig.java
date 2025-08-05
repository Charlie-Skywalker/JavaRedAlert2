package redAlert;

/**
 * 系统参数
 * 一般确定后在游戏过程中不再变化
 */
public class SysConfig {
	
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
	public static final int viewportWidth = 1800;
	public static final int viewportHeight = 900;
	/**
	 * 游戏窗口的宽高
	 */
	public static final int frameWidth = viewportWidth + OptionsPanel.optionWidth;
	public static final int frameHeight = viewportHeight+32;//32是微软建议的标题栏高度
	/**
	 * 战场地图的宽高
	 * 
	 * 以后可能会增加可视区域的宽高,所以这个值是地图的最大值,所以认为不可变
	 */
	public static final int gameMapWidth = 6000;
	public static final int gameMapHeight = 4000;
	
}

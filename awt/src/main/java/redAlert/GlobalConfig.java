package redAlert;

import redAlert.enums.Country;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building.SceneType;

/**
 * 对于玩家来说的全局配置
 */
public class GlobalConfig {

	/**
	 * 雪地 野外 城市
	 */
	public static SceneType sceneType = SceneType.SNOW;
	/**
	 * 阵营颜色
	 */
	public static UnitColor unitColor = UnitColor.Blue;
	/**
	 * 国家
	 */
	public static Country country = Country.USA;
	/**
	 * 地图文件路径
	 */
	public static String mapFilePath = "E:/gmap.text";
	
	
	
	
}

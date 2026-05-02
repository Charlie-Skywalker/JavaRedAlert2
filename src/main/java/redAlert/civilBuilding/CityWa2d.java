package redAlert.civilBuilding;

import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.CivilBuilding;
import redAlert.utilBean.CenterPoint;

/**
 * 五角大楼  上边
 */
public class CityWa2d extends CivilBuilding{

	public CityWa2d(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.CityWa2d);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.height = 50;
	}
}

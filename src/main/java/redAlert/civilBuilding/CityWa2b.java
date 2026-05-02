package redAlert.civilBuilding;

import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.CivilBuilding;
import redAlert.utilBean.CenterPoint;

/**
 * 五角大楼  下边
 */
public class CityWa2b extends CivilBuilding{

	public CityWa2b(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.CityWa2b);
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

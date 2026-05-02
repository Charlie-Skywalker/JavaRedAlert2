package redAlert.civilBuilding;

import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.CivilBuilding;
import redAlert.utilBean.CenterPoint;

/**
 * 美国电报电话大厦(原AT&T总部大楼)
 * 
 */
public class CityWash07 extends CivilBuilding{

	public CityWash07(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.CityWash07);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.height = 115;
	}
}

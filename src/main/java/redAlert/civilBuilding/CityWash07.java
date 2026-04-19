package redAlert.civilBuilding;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.CivilBuilding;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

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
	
	@Override
	public List<CenterPoint> getNoConstCpList() {
		int centerX = centerOffX + super.getPositionX();
		int centerY = centerOffY + super.getPositionY();
		List<CenterPoint> result = new ArrayList<>();
		CenterPoint center = PointUtil.getCenterPoint(centerX, centerY);
		result.add(center);
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
		result.add(center.getRight());
		result.add(center.getRightUp());
		result.add(center.getUp());
		result.add(center.getLeftUp());
		
		return result;
	}

	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		return getNoConstCpList();
	}
}

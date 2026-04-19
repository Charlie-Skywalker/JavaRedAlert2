package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.MilitaryBuilding;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军裂缝产生器
 */
public class AfGap extends MilitaryBuilding{
	
	public AfGap(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.AfGap);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		if(sceneType==SceneType.TEM) {
			super.aniShpPrefixLs.add("gggap");
		}
		if(sceneType==SceneType.URBAN) {
			super.aniShpPrefixLs.add("gggap");
		}
		if(sceneType==SceneType.SNOW) {
			super.aniShpPrefixLs.add("gagap");
		}
	}
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 */
	@Override
	public List<CenterPoint> getNoConstCpList() {
		int centerX = centerOffX + super.getPositionX();
		int centerY = centerOffY + super.getPositionY();
		List<CenterPoint> result = new ArrayList<>();
		CenterPoint center = PointUtil.getCenterPoint(centerX, centerY);
		result.add(center);
		return result;
	}
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 */
	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		return getNoConstCpList();
	}
	
}

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
 * 盟军矿石精炼厂
 */
public class AfOrep extends MilitaryBuilding{
	
	public AfOrep(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.AfOrep);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		if(sceneType==SceneType.TEM) {
			super.aniShpPrefixLs.add("ggorep");
			super.aniShpPrefixLs.add("ggorep_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.aniShpPrefixLs.add("ggorep");
			super.aniShpPrefixLs.add("ggorep_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.aniShpPrefixLs.add("gaorep");
			super.aniShpPrefixLs.add("gaorep_a");
		}
	}
	
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 */
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
	
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 */
	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		return getNoConstCpList();
	}
	
	/**
	 * 获取建筑的阴影区域
	 */
	@Override
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp().getLeftUp();
		CenterPoint cp2 = cp.getUp().getUp();
		CenterPoint cp3 = cp.getUp().getLeftUp();
		
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		return list;
	}
	
}

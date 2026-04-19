package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.building.MilitaryBuilding;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军巨炮
 * 巨炮的gg和ga文件只有一个底座
 */
public class AfGcan extends MilitaryBuilding{
	
	public AfGcan(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.AfGcan);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
		
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2 = new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		if(sceneType==SceneType.TEM) {
			super.aniShpPrefixLs.add("gggcan");
		}
		if(sceneType==SceneType.URBAN) {
			super.aniShpPrefixLs.add("gggcan");
		}
		if(sceneType==SceneType.SNOW) {
			super.aniShpPrefixLs.add("gagcan");
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
		result.add(center.getLeftDn());
		result.add(center.getRightDn());
		result.add(center.getDn());
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

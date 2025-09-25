package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军作战实验室
 */
public class AfTech extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "tech";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfTech(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfTech;
		super.height = 190;
		
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggtech");
			super.aniShpPrefixLs.add("ggtech_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggtech");
			super.aniShpPrefixLs.add("ggtech_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gatech");
			super.aniShpPrefixLs.add("gatech_a");
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
		result.add(center.getLeftUp());
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
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
		CenterPoint cp2 = cp.getLeftUp().getUp();
		CenterPoint cp3 = cp.getUp();
		CenterPoint cp4 = cp.getRightUp();
		CenterPoint cp5 = cp.getUp().getLeftUp().getLeftUp();
		CenterPoint cp6 = cp.getUp().getRightUp();
		CenterPoint cp7 = cp.getUp().getUp();
		CenterPoint cp8 = cp.getUp().getUp().getLeftUp();
		CenterPoint cp9 = cp.getUp().getUp().getLeftUp().getLeftUp();
		CenterPoint cp10 = cp.getUp().getUp().getRightUp();
		CenterPoint cp11 = cp.getUp().getUp().getUp();
		CenterPoint cp12 = cp.getUp().getUp().getUp().getUp();
		CenterPoint cp13 = cp.getUp().getUp().getUp().getLeftUp();
		CenterPoint cp14 = cp.getUp().getUp().getUp().getLeftUp();
		CenterPoint cp15 = cp.getUp().getUp().getUp().getUp().getLeftUp();
		CenterPoint cp16 = cp.getUp().getUp().getUp().getUp().getUp();
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		list.add(cp6);
		list.add(cp7);
		list.add(cp8);
		list.add(cp9);
		list.add(cp10);
		list.add(cp11);
		list.add(cp12);
		list.add(cp13);
		list.add(cp14);
		list.add(cp15);
		list.add(cp16);
		return list;
	}
}

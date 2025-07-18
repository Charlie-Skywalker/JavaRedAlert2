package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军兵营
 *
 */
public class AfPile extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "pile";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfPile(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfPile(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfPile(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfPile;
		setCenterOffX(156);
		setCenterOffY(102);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggpile");
			super.aniShpPrefixLs.add("ggpile_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggpile");
			super.aniShpPrefixLs.add("ggpile_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gapile");
			super.aniShpPrefixLs.add("gapile_a");
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
		CenterPoint center = PointUtil.fetchCenterPoint(centerX, centerY);
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
		CenterPoint cp3 = cp.getLeftUp().getUp().getUp();
		CenterPoint cp4 = cp.getUp();
		CenterPoint cp5 = cp.getRightUp();
		CenterPoint cp6 = cp.getRight();
		CenterPoint cp7 = cp.getUp().getUp();
		
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		list.add(cp6);
		list.add(cp7);
		return list;
	}
}

package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军船厂
 */
public class AfYard extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "yard";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfYard(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfYard(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfYard(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfYard;
		super.height = 130;
		setCenterOffX(210);
		setCenterOffY(190);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggyard");
			super.aniShpPrefixLs.add("ggyard_c");
			super.aniShpPrefixLs.add("ggyard_d");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggyard");
			super.aniShpPrefixLs.add("ggyard_c");
			super.aniShpPrefixLs.add("ggyard_d");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gayard");
			super.aniShpPrefixLs.add("ggyard_c");
			super.aniShpPrefixLs.add("ggyard_d");
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
		CenterPoint center = PointUtil.fetchCenterPoint(centerX, centerY);
		result.add(center);
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
		result.add(center.getRight());
		result.add(center.getRightUp());
		result.add(center.getUp());
		result.add(center.getLeftUp());
		
		result.add(center.getLeft().getLeftDn());
		result.add(center.getLeftDn().getLeftDn());
		result.add(center.getLeftDn().getDn());
		result.add(center.getRight().getRightDn());
		result.add(center.getRightDn().getRightDn());
		result.add(center.getDn().getRightDn());
		result.add(center.getDn().getDn());
		return result;
	}
	
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 * 现在还没考虑船只的问题  船只也可以进入船坞的一部分，但是现在还处理不了水面  先不处理
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
		CenterPoint cp2 = cp.getUp().getLeftUp();
		CenterPoint cp3 = cp.getUp().getUp();
		CenterPoint cp4 = cp.getUp().getRightUp();
		CenterPoint cp5 = cp.getRightUp().getRightUp();
		CenterPoint cp6 = cp.getRightUp().getRight();
		
		CenterPoint cp7 = cp.getUp().getUp().getLeftUp();
		CenterPoint cp8 = cp.getUp().getUp().getUp();
		CenterPoint cp9 = cp.getUp().getUp().getRightUp();
		CenterPoint cp10 = cp.getUp().getRightUp().getRightUp();
		CenterPoint cp11 = cp.getRightUp().getRightUp().getRightUp();
		
		CenterPoint cp12 = cp.getRightUp().getRightUp().getRight();
		
		CenterPoint cp13 = cp.getUp().getUp().getUp().getUp();
		CenterPoint cp14 = cp.getUp().getUp().getUp().getLeftUp();
		CenterPoint cp15 = cp.getUp().getUp().getUp().getRightUp();
		
		CenterPoint cp16 = cp.getUp().getUp().getUp().getRightUp().getRightUp();
		
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

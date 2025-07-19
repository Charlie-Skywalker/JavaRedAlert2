package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军矿场
 *
 */
public class AfRefn extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "refn";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfRefn(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfRefn(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfRefn(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfRefn;
		setCenterOffX(108);
		setCenterOffY(144);
		
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggrefn");
			super.aniShpPrefixLs.add("ggrefnbb");
			super.aniShpPrefixLs.add("ggrefnl3");
			super.aniShpPrefixLs.add("ggrefnl2");
			super.aniShpPrefixLs.add("ggrefnl1");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggrefn");
			super.aniShpPrefixLs.add("ggrefnbb");
			super.aniShpPrefixLs.add("ggrefnl3");
			super.aniShpPrefixLs.add("ggrefnl2");
			super.aniShpPrefixLs.add("ggrefnl1");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("garefn");
			super.aniShpPrefixLs.add("garefnbb");
			super.aniShpPrefixLs.add("garefnl3");
			super.aniShpPrefixLs.add("garefnl2");
			super.aniShpPrefixLs.add("garefnl1");
		}
	}
	
	/**
	 * 获取建筑的所有菱形中心
	 */
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
		result.add(center.getRight().getRightDn());
		result.add(center.getRightDn().getRightDn());
		result.add(center.getRightDn().getDn());
		
		return result;
	}
	
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 * 矿场  倒矿的一边是可以载具进入的
	 */
	@Override
	public List<CenterPoint> getNoVehicleCpList() {
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
		
		return result;
	}
	
	/**
	 * 获取建筑的阴影区域
	 */
	@Override
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp().getLeftUp();
		CenterPoint cp2 = cp.getLeftUp().getLeftUp().getRightUp();
		CenterPoint cp3 = cp.getLeftUp().getLeftUp().getRightUp().getRightUp();
		CenterPoint cp4 = cp.getLeftUp().getLeftUp().getRightUp().getRightUp().getRightDn();
		
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		return list;
	}
}

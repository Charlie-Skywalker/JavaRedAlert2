package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军 间谍卫星
 */
public class AfSpst extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "spst";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfSpst(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfSpst(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfSpst(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfSpst;
		super.height = 90;
		setCenterOffX(90);
		setCenterOffY(66);
		
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggspst");
			super.aniShpPrefixLs.add("ggspst_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggspst");
			super.aniShpPrefixLs.add("ggspst_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gaspst");
			super.aniShpPrefixLs.add("gaspst_a");
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
		CenterPoint cp1 = cp.getLeft();
		CenterPoint cp2 = cp.getLeftUp();
		CenterPoint cp3 = cp.getLeftUp().getLeftUp();
		CenterPoint cp4 = cp.getLeftUp().getUp();
		CenterPoint cp5 = cp.getUp();
		CenterPoint cp6 = cp.getUp().getRightUp();
		CenterPoint cp7 = cp.getRightUp();
		CenterPoint cp8 = cp.getRightUp().getRightUp();
		CenterPoint cp9 = cp.getRight();
		
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		list.add(cp6);
		list.add(cp7);
		list.add(cp8);
		list.add(cp9);
		return list;
	}
	
}

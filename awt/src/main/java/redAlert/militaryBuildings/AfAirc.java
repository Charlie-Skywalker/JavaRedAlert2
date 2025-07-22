package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军空指部
 *
 */
public class AfAirc extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "airc";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfAirc(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfAirc(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfAirc(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfAirc;
		super.height = 100;
		setCenterOffX(122);
		setCenterOffY(110);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggairc");
			super.aniShpPrefixLs.add("ggairc_c");
			super.aniShpPrefixLs.add("ggaircbb");
			super.aniShpPrefixLs.add("ggairc_b");
			super.aniShpPrefixLs.add("ggairc_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggairc");
			super.aniShpPrefixLs.add("ggairc_c");
			super.aniShpPrefixLs.add("ggaircbb");
			super.aniShpPrefixLs.add("ggairc_b");
			super.aniShpPrefixLs.add("ggairc_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gaairc");
			super.aniShpPrefixLs.add("gaairc_c");
			super.aniShpPrefixLs.add("gaaircbb");
			super.aniShpPrefixLs.add("gaairc_b");
			super.aniShpPrefixLs.add("gaairc_a");
		}
	}
	
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 * 
	 * 实际上就是建筑占地   但是建筑占地的地方,坦克有可能能开进去
	 */
	@Override
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
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp().getLeftUp().getRightUp().getLeftUp().getRightUp().getLeftUp();
		CenterPoint cp2 = cp.getLeftUp().getLeftUp().getRightUp().getLeftUp().getRightUp();
		CenterPoint cp3 = cp.getLeftUp().getLeftUp().getRightUp().getLeftUp();
		CenterPoint cp4 = cp.getLeftUp().getLeftUp().getRightUp();
		CenterPoint cp5 = cp.getLeftUp().getLeftUp();
		CenterPoint cp6 = cp.getLeftUp().getRightUp();
		CenterPoint cp7 = cp.getLeftUp().getLeftDn().getLeftUp();//勉强算一个
		
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

package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军 天气控制器
 */
public class AfWeth extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "weth";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfWeth(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
		
		super.workingFrames.clear();
		super.workingFrames.add(super.constructFrames.subList(constructFrames.size()-1, constructFrames.size()));//工作动画展示建造动画的最后一帧
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfWeth;
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggweth");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggweth");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gaweth");
			super.aniShpPrefixLs.add("gaweth_e");
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
		CenterPoint cp2 = cp.getUp().getLeftUp();
		CenterPoint cp3 = cp.getUp().getUp();
		CenterPoint cp4 = cp.getUp().getRightUp();
		CenterPoint cp5 = cp.getRightUp().getRightUp();
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		return list;
	}
}

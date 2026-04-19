package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.building.MilitaryBuilding;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军 爱国者飞弹
 * 游戏中爱国者飞弹是盟军的建筑，但名称是苏军格式的
 * 例如建造动画nasammk.shp、ntsammk.shp、ngsammk.shp  运行动画nasam.shp ngsam.shp
 * ng na 动画只有基座
 */
public class AfSam extends MilitaryBuilding{
	
	public AfSam(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		super(sceneType,ConstConfig.AfSam);
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
		
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2= new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.height = 60;
		if(sceneType==SceneType.TEM) {
			super.aniShpPrefixLs.add("ngsam");
		}
		if(sceneType==SceneType.URBAN) {
			super.aniShpPrefixLs.add("ngsam");
		}
		if(sceneType==SceneType.SNOW) {
			super.aniShpPrefixLs.add("nasam");
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
	
	/**
	 * 获取建筑的阴影区域
	 */
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp();
		CenterPoint cp2 = cp.getUp();
		CenterPoint cp3 = cp.getRightUp();
		
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		return list;
	}
	
}

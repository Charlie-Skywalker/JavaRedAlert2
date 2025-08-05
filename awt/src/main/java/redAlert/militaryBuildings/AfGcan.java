package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军巨炮
 * 巨炮的gg和ga文件只有一个底座
 */
public class AfGcan extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "gcan";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfGcan(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfGcan(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2 = new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
		
		super.scene = sceneType;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
//		super.constructFrameIndex = 0;
//		super.workingFrameIndex = 0;
//		super.damagedFrameIndex = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		curFrame = constructFrames.get(0);
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "cnst";
	}
	public AfGcan(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2 = new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
		
		super.scene = sceneType;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
//		super.constructFrameIndex = 0;
//		super.workingFrameIndex = 0;
//		super.damagedFrameIndex = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		curFrame = constructFrames.get(0);
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "cnst";
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfGcan;
		setCenterOffX(10);
		setCenterOffY(10);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("gggcan");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("gggcan");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
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
		CenterPoint center = PointUtil.fetchCenterPoint(centerX, centerY);
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

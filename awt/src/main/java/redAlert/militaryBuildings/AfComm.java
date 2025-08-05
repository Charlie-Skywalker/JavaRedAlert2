package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 通信中心  一个只有一半的建筑
 * 只有gt和gu的建造动画  没有工作动画
 */
public class AfComm extends Building{

	/**
	 * 地理放置中心偏离图片左上角positionX的距离
	 */
	public static int centerOffX = 10;
	/**
	 * 地理放置中心偏离图片左上角positionY的距离
	 */
	public static int centerOffY = 10;
	
	public String basicName = "comm";
	public String team = "g";
	
	public AfComm(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfComm(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, scene.getPalPrefix());
		
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		super.workingFrames = workingFrames;
		super.damagedFrames = workingFrames;
		
		super.scene = scene;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
//		super.constructFrameIndex = 0;
//		super.workingFrameIndex = 0;
//		super.damagedFrameIndex = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		curFrame = super.calculateFirstFrame();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "comm";
	}
	
	public AfComm(int positionX,int positionY,SceneType scene,UnitColor unitColor) {
		if(scene==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
		}
		if(scene==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
		}
		
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, scene.getPalPrefix());
		
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		super.workingFrames = workingFrames;
		super.damagedFrames = workingFrames;
		
		super.scene = scene;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
//		super.constructFrameIndex = 0;
//		super.workingFrameIndex = 0;
//		super.damagedFrameIndex = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		curFrame = super.calculateFirstFrame();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "comm";
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		setCenterOffX(10);
		setCenterOffY(10);
		if(scene==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
		}
		if(scene==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
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
}

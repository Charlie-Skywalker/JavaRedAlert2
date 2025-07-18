package redAlert.militaryBuildings;

import java.util.ArrayList;
import java.util.List;

import redAlert.Constructor;
import redAlert.GameContext;
import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.other.BuildingBloodBar;
import redAlert.other.BuildingBone;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军 爱国者飞弹
 * gg ga动画只有基座
 */
public class AfSam extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "sam";
	/**
	 * 阵营 苏军
	 */
	public String team = "n";
	
	public AfSam(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfSam(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2= new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
		
		super.scene = sceneType;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
		super.frameNum = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		this.curFrame = calculateFirstFrame();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		this.curCenterPoint = PointUtil.getCenterPoint(super.positionX+super.centerOffX, super.positionY+super.centerOffY);
		
		//初始化血条的信息
		bloodBar = new BuildingBloodBar(this);
		Constructor.putOneShapeUnit(bloodBar, GameContext.scenePanel);
		
		//初始化骨架的信息
		bone = new BuildingBone(this);
		Constructor.putOneShapeUnit(bone, GameContext.scenePanel);
		
	}
	
	public AfSam(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		
		workingFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list1 = new ArrayList<>();
		list1.add(constructFrames.get(constructFrames.size()-1));
		workingFrames.add(list1);
		
		damagedFrames = new ArrayList<List<ShapeUnitFrame>>(5);
		List<ShapeUnitFrame> list2= new ArrayList<>();
		list2.add(constructFrames.get(constructFrames.size()-1));
		damagedFrames.add(list2);
		
		
		super.scene = sceneType;
		super.unitColor = unitColor;
		super.positionX = positionX;
		super.positionY = positionY;
		super.frameNum = 0;
		super.status = BuildingStatus.UNDEMAGED;
		super.stage = BuildingStage.UnderConstruct;
		this.curFrame = calculateFirstFrame();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		this.curCenterPoint = PointUtil.getCenterPoint(super.positionX+super.centerOffX, super.positionY+super.centerOffY);
		
		//初始化血条的信息
		bloodBar = new BuildingBloodBar(this);
		Constructor.putOneShapeUnit(bloodBar, GameContext.scenePanel);
		
		//初始化骨架的信息
		bone = new BuildingBone(this);
		Constructor.putOneShapeUnit(bone, GameContext.scenePanel);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfSam;
		super.height = 60;
		setCenterOffX(50);
		setCenterOffY(50);
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
		//定义显示名称
		super.unitName = "爱国者飞弹";
		
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

package redAlert.civilBuilding;

import java.util.ArrayList;
import java.util.List;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.enums.BuildingStatus;
import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.enums.UnitColor;
import redAlert.other.BuildingBloodBar;
import redAlert.other.BuildingBone;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 美国电报电话大厦(原AT&T总部大楼)
 * 
 */
public class CityNewy10 extends Building{

	/**
	 * shp文件基础名
	 */
	public String basicName = "newy10";
	/**
	 * 阵营：城市建筑
	 */
	public String teamMark = "c";
	
	public CityNewy10(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		initBuildingValue(centerPoint,sceneType,unitColor);
		System.out.println(super.centerOffX+","+super.centerOffY);
	}
	
	/**
	 * 由子类调用初始化Building的成员变量
	 */
	@Override
	public void initBuildingValue(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		List <ShapeUnitFrame> all = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix(), true);
		this.constructFrames = all.subList(0, 1);
		this.workingFrames = new ArrayList<List <ShapeUnitFrame>>();
		workingFrames.add(all.subList(0, 1));
		this.damagedFrames = new ArrayList<List <ShapeUnitFrame>>();
		damagedFrames.add(all.subList(1, 2));
		
		super.setCenterOffXY(constructFrames);
		
		this.scene = sceneType;
		this.unitColor = unitColor;
		this.positionX = centerPoint.getX()-centerOffX;
		this.positionY = centerPoint.getY()-centerOffY;
		this.frameNum = 0;
		this.status = BuildingStatus.UNDEMAGED;
		this.stage = BuildingStage.UnderConstruct;
		this.curFrame = calculateFirstFrame();
		this.positionMinX = curFrame.getMinX()+positionX;
		this.positionMinY = curFrame.getMinY()+positionY;
		this.curCenterPoint = PointUtil.getCenterPoint(super.positionX+super.centerOffX, super.positionY+super.centerOffY);
		
		//初始化血条的信息
		bloodBar = new BuildingBloodBar(this);
		Constructor.putOneShapeUnit(bloodBar);
		
		//初始化骨架的信息
		bone = new BuildingBone(this);
		Constructor.putOneShapeUnit(bone);
		
	}
	
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.CityNewy10;
		super.height = 200;
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = teamMark + sceneType.getSceneMark() + basicName;
			super.aniShpPrefixLs.add("ctnewy10");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = teamMark + sceneType.getSceneMark() + basicName;
			super.aniShpPrefixLs.add("ctnewy10");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = teamMark + sceneType.getSceneMark() + basicName;
			super.aniShpPrefixLs.add("canewy10");
		}
	}
	
	
	@Override
	public List<CenterPoint> getNoConstCpList() {
		int centerX = centerOffX + super.getPositionX();
		int centerY = centerOffY + super.getPositionY();
		List<CenterPoint> result = new ArrayList<>();
		CenterPoint center = PointUtil.getCenterPoint(centerX, centerY);
		result.add(center);
		result.add(center.getLeftUp());
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
		return result;
	}

	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		return getNoConstCpList();
	}

}

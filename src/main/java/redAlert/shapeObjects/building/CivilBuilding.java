package redAlert.shapeObjects.building;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
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
import redAlert.utilBean.CenterPoint;
import redAlert.utils.CanvasPainter;
import redAlert.utils.PointUtil;

/**
 * 民用建筑的抽象
 * 
 * 
 * 民用建筑的特点是  没有建造动画、没有工作动画，全程是静止的
 * 
 * 有崭新、驻兵、破损状态，有些特殊建筑有倒塌动画
 */
public abstract class CivilBuilding extends Building{

	
	/**
	 * 建筑使用状态
	 */
	public enum BuildingUsingStatus{
		
		Default("默认"),
		Damaged("受损"),
		ArmyIn("驻兵"),
		Ruined("废墟");
		
		private final String cnDesc;
		
		BuildingUsingStatus(String cnDesc){
			this.cnDesc = cnDesc;
		}
	}
	
	public String shpFilePrefix = "";
	
	
	public CivilBuilding(SceneType sceneType,ConstConfig config) {
		super(sceneType,config);
		this.shpFilePrefix = constConfig.faction + sceneType.getSceneMark() + constConfig.shpBasicName;
	}
	
	/**
	 * 表示建筑原貌
	 */
	public ShapeUnitFrame defaultFrame = null;
	/**
	 * 表示建筑受损
	 */
	public ShapeUnitFrame damagedFrame = null;
	/**
	 * 表示驻兵状态
	 */
	public ShapeUnitFrame armyInFrame = null;
	/**
	 * 表示废墟状态
	 */
	public ShapeUnitFrame ruinedFrame = null;
	/**
	 * 建筑阶段  正在建设中或建设完成(动态展示中)
	 */
	public BuildingUsingStatus usingStatus = BuildingUsingStatus.Default;
	
	
	/**
	 * 由子类调用初始化Building的成员变量
	 */
	public void initBuildingValue(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		List <ShapeUnitFrame> all = ShpResourceCenter.loadShpResource(shpFilePrefix, sceneType.getPalPrefix(), true);
		defaultFrame = all.get(0);
		damagedFrame = all.get(1);
		armyInFrame = all.get(2);
		List <ShapeUnitFrame> all2 = ShpResourceCenter.loadShpResource(shpFilePrefix, sceneType.getPalPrefix().replace("unit", "iso"), true);
		ruinedFrame = all2.get(3);
		ShpResourceCenter.removeCache(shpFilePrefix, sceneType.getPalPrefix().replace("unit", "iso"));
		
		super.setCenterOffXY(all);
		super.unitColor = unitColor;
		super.positionX = centerPoint.getX()-centerOffX;
		super.positionY = centerPoint.getY()-centerOffY;
		super.frameNum = 0;
		super.status = BuildingStatus.UNDEMAGED;
		this.curFrame = calculateFirstFrame();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		super.curCenterPoint = PointUtil.getCenterPoint(super.positionX+super.centerOffX, super.positionY+super.centerOffY);
		
		//初始化血条的信息
		bloodBar = new BuildingBloodBar(this);
		Constructor.putOneShapeUnit(bloodBar);
		
		//初始化骨架的信息
		bone = new BuildingBone(this);
		Constructor.putOneShapeUnit(bone);
	}
	
	/**
	 * 由于新建建筑是直接扔进缓存队列的,所以需要计算好第一帧的颜色
	 * 计算第一帧
	 */
	public ShapeUnitFrame calculateFirstFrame() {
		
		if(usingStatus==BuildingUsingStatus.Default) {
			ShapeUnitFrame newFrame = defaultFrame.copy();
			BufferedImage image = newFrame.getImg();
			giveFrameUnitColor(image,newFrame);//上阵营色
			return newFrame;
		}
		if(usingStatus==BuildingUsingStatus.Damaged) {
			ShapeUnitFrame newFrame = damagedFrame.copy();
			BufferedImage image = newFrame.getImg();
			giveFrameUnitColor(image,newFrame);//上阵营色
			return newFrame;
		}
		if(usingStatus==BuildingUsingStatus.ArmyIn) {
			ShapeUnitFrame newFrame = armyInFrame.copy();
			BufferedImage image = newFrame.getImg();
			giveFrameUnitColor(image,newFrame);//上阵营色
			return newFrame;
		}
		if(usingStatus==BuildingUsingStatus.Ruined) {
			ShapeUnitFrame newFrame = ruinedFrame.copy();
			BufferedImage image = newFrame.getImg();
			giveFrameUnitColor(image,newFrame);//上阵营色
			return newFrame;
		}
		return null;
	}
	
	@Override
	public void calculateNextFrame() {
		if(bloodBar.getCurHp()>bloodBar.getMaxHp()*0.5){
			status = BuildingStatus.UNDEMAGED;
		}else if(bloodBar.getCurHp()<=bloodBar.getMaxHp()*0.5 && bloodBar.getCurHp()>=1){
			status = BuildingStatus.DEMAGED;
		}else if(bloodBar.getCurHp()<1) {
			super.end = true;
		}
		
		if(usingStatus==BuildingUsingStatus.Default) {
			ShapeUnitFrame constFrame = defaultFrame;
			BufferedImage curImg = curFrame.getImg();
			CanvasPainter.clearImage(curImg);
			curFrame.setMinX(constFrame.getMinX());
			curFrame.setMaxX(constFrame.getMaxX());
			curFrame.setMinY(constFrame.getMinY());
			curFrame.setMaxY(constFrame.getMaxY());
			curFrame.setRealPartHeight(constFrame.getRealPartHeight());
			curFrame.setRealPartWidth(constFrame.getRealPartWidth());
			Graphics2D curG2d = curImg.createGraphics();
			curG2d.drawImage(constFrame.getImg(), 0, 0, null);
			curG2d.dispose();
			giveFrameUnitColor(curImg,constFrame);//上阵营色
			super.positionMinX = curFrame.getMinX()+positionX;
			super.positionMinY = curFrame.getMinY()+positionY;
		}else if(usingStatus==BuildingUsingStatus.Damaged) {
			ShapeUnitFrame constFrame = damagedFrame;
			BufferedImage curImg = curFrame.getImg();
			CanvasPainter.clearImage(curImg);
			curFrame.setMinX(constFrame.getMinX());
			curFrame.setMaxX(constFrame.getMaxX());
			curFrame.setMinY(constFrame.getMinY());
			curFrame.setMaxY(constFrame.getMaxY());
			curFrame.setRealPartHeight(constFrame.getRealPartHeight());
			curFrame.setRealPartWidth(constFrame.getRealPartWidth());
			Graphics2D curG2d = curImg.createGraphics();
			curG2d.drawImage(constFrame.getImg(), 0, 0, null);
			curG2d.dispose();
			giveFrameUnitColor(curImg,constFrame);//上阵营色
			super.positionMinX = curFrame.getMinX()+positionX;
			super.positionMinY = curFrame.getMinY()+positionY;
		}else if(usingStatus==BuildingUsingStatus.ArmyIn) {
			ShapeUnitFrame constFrame = armyInFrame;
			BufferedImage curImg = curFrame.getImg();
			CanvasPainter.clearImage(curImg);
			curFrame.setMinX(constFrame.getMinX());
			curFrame.setMaxX(constFrame.getMaxX());
			curFrame.setMinY(constFrame.getMinY());
			curFrame.setMaxY(constFrame.getMaxY());
			curFrame.setRealPartHeight(constFrame.getRealPartHeight());
			curFrame.setRealPartWidth(constFrame.getRealPartWidth());
			Graphics2D curG2d = curImg.createGraphics();
			curG2d.drawImage(constFrame.getImg(), 0, 0, null);
			curG2d.dispose();
			giveFrameUnitColor(curImg,constFrame);//上阵营色
			super.positionMinX = curFrame.getMinX()+positionX;
			super.positionMinY = curFrame.getMinY()+positionY;
		}else if(usingStatus==BuildingUsingStatus.Ruined) {
			ShapeUnitFrame constFrame = ruinedFrame;
			BufferedImage curImg = curFrame.getImg();
			CanvasPainter.clearImage(curImg);
			curFrame.setMinX(constFrame.getMinX());
			curFrame.setMaxX(constFrame.getMaxX());
			curFrame.setMinY(constFrame.getMinY());
			curFrame.setMaxY(constFrame.getMaxY());
			curFrame.setRealPartHeight(constFrame.getRealPartHeight());
			curFrame.setRealPartWidth(constFrame.getRealPartWidth());
			Graphics2D curG2d = curImg.createGraphics();
			curG2d.drawImage(constFrame.getImg(), 0, 0, null);
			curG2d.dispose();
			giveFrameUnitColor(curImg,constFrame);//上阵营色
			super.positionMinX = curFrame.getMinX()+positionX;
			super.positionMinY = curFrame.getMinY()+positionY;
		}
	}

	public BuildingUsingStatus getUsingStatus() {
		return usingStatus;
	}

	public void setUsingStatus(BuildingUsingStatus usingStatus) {
		this.usingStatus = usingStatus;
	}
	
	
}

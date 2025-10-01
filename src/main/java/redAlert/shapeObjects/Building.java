package redAlert.shapeObjects;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.enums.BuildingAreaType;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.militaryBuildings.AfCnst;
import redAlert.other.BloodBar;
import redAlert.other.BuildingBloodBar;
import redAlert.other.BuildingBone;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.vehicle.Mcv;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.CanvasPainter;
import redAlert.utils.PointUtil;

/**
 * 对一个建筑对象的抽象 
 *
 */
public abstract class Building extends ShapeUnit implements Bloodable{
	
	/**
	 * 建筑风格
	 */
	public enum SceneType{
		URBAN("城市","uniturb"),SNOW("雪地","unitsno"),TEM("野外","unittem"),ANIM("动画","anim");
		
		private final String cnDesc;
		private final String palPrefix;
		SceneType(String cnDesc,String palPrefix){
			this.cnDesc = cnDesc;
			this.palPrefix = palPrefix;
		}
		public String getCnDesc() {
			return cnDesc;
		}
		public String getPalPrefix() {
			return palPrefix;
		}
	}
	
	/**
	 * 建筑状态
	 */
	public enum BuildingStatus{
		DEMAGED("受损"),UNDEMAGED("完好");
		private final String cnDesc;
		
		BuildingStatus(String cnDesc){
			this.cnDesc = cnDesc;
		}
	}
	/**
	 * 建设阶段
	 */
	public enum BuildingStage{
		UnderConstruct("建设中"),ConstructComplete("建设完成"),Selling("贱卖中");
		private final String cnDesc;
		
		BuildingStage(String cnDesc){
			this.cnDesc = cnDesc;
		}
	}
	
	/**
	 * 根据SHP文件的命名规律
	 * 建造名称的规律
	 * ga**mk gt**mk gu**mk
	 */
	public String snoMark = "a";
	public String temMark = "t";
	public String urbMark = "u";
	
	/**
	 * 建造动画SHP文件前缀
	 */
	public String constShpFilePrefix = "";
	/**
	 * 运行动画SHP文件前缀集合
	 */
	public List<String> aniShpPrefixLs = new ArrayList<>();
	/**
	 * 建筑建造动画帧集合
	 *   因为建造是一个单向连续的过程，不包括多个组成部分，所以是一个简单数组
	 */
	public List<ShapeUnitFrame> constructFrames;
	/**
	 * 建筑工作动画帧集合
	 *   建筑工作可能分为多个shp文件协同，所以List中有多个shp帧序列
	 */
	public List<List<ShapeUnitFrame>> workingFrames;
	/**
	 * 建筑受损动画帧集合
	 *   与建筑工作动画帧类似
	 */
	public List<List<ShapeUnitFrame>> damagedFrames;
	/**
	 * 建筑状态    受损或完好
	 */
	public BuildingStatus status = BuildingStatus.UNDEMAGED;
	/**
	 * 建筑阶段  正在建设中或建设完成(动态展示中)
	 */
	public BuildingStage stage = BuildingStage.UnderConstruct;
	/**
	 * 用于决定加载三种动画帧时使用哪套皮肤
	 */
	public SceneType scene;
	/**
	 * SHP帧下标
	 */
	public int frameIndex = 0;
	/**
	 * 血条对象
	 */
	public BloodBar bloodBar;
	/**
	 * 建筑的配置信息
	 */
	public ConstConfig constConfig = null;//给一个默认值
	/**
	 * 建筑的虚拟高度
	 * 这个变量涉及到血条和模型棱的显示位置
	 */
	public int height = 70;
	/**
	 * 骨架对象
	 */
	public BuildingBone bone;
	
	/**
	 * 默认的构造方法
	 */
	public Building() {
		super.frameSpeed = 4;
		
	}
	
	/**
	 * 由子类调用初始化Building的成员变量
	 */
	public void initBuildingValue(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		this.constructFrames = ShpResourceCenter.loadShpResource(constShpFilePrefix, sceneType.getPalPrefix());
		this.workingFrames = ShpResourceCenter.loadWorkingFrames(aniShpPrefixLs, sceneType);
		this.damagedFrames = ShpResourceCenter.loadDamagedFrames(aniShpPrefixLs, sceneType);
		
		this.scene = sceneType;
		this.unitColor = unitColor;
		this.positionX = positionX;
		this.positionY = positionY;
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
	 * 由于新建建筑是直接扔进缓存队列的,所以需要计算好第一帧的颜色
	 * 计算第一帧
	 */
	public ShapeUnitFrame calculateFirstFrame() {
		ShapeUnitFrame firstConstFrame = constructFrames.get(0);
		
		ShapeUnitFrame newFrame = firstConstFrame.copy();
		BufferedImage image = newFrame.getImg();
		giveFrameUnitColor(image,newFrame);//上阵营色
		return newFrame;
	}
	
	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	public void calculateNextFrame() {
		
		if(bloodBar.getCurHp()>bloodBar.getMaxHp()*0.5){
			status = BuildingStatus.UNDEMAGED;
		}else if(bloodBar.getCurHp()<=bloodBar.getMaxHp()*0.5 && bloodBar.getCurHp()>=1){
			status = BuildingStatus.DEMAGED;
		}else if(bloodBar.getCurHp()<1) {
			super.end = true;
		}
		
		if(stage==BuildingStage.UnderConstruct) {//建造动画
//			frameIndex++;
			if(frameIndex> (constructFrames.size()-1)) {
				stage = BuildingStage.ConstructComplete;
				frameIndex=0;
				
				//找到基地并展示夹箱子动画
				if(!this.getClass().equals(AfCnst.class)) {
					ShapeUnitResourceCenter.exeCnstFetchAni();
				}
				
				
				//....展示第一幅画面
				calculateNextFrame();
			}else {
				ShapeUnitFrame constFrame = constructFrames.get(frameIndex);
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
				frameIndex++;
			}
		}else if(stage==BuildingStage.ConstructComplete) {//建设完成
			
			if(status==BuildingStatus.UNDEMAGED){
				BufferedImage curImg = curFrame.getImg();
				CanvasPainter.clearImage(curImg);
				Graphics curG2d = curImg.createGraphics();
				
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				for(int i=0;i<workingFrames.size();i++) {
					List<ShapeUnitFrame> workingFrameLs = workingFrames.get(i);
					ShapeUnitFrame frame = workingFrameLs.get( frameIndex%workingFrameLs.size());
					BufferedImage oriImage = frame.getImg();
					curG2d.drawImage(oriImage, 0, 0, null);
					
					giveFrameUnitColor(curImg,frame);//上阵营色
					
					if(i==0) {
						minX = frame.getMinX();
						minY = frame.getMinY();
						maxX = frame.getMaxX();
						maxY = frame.getMaxY();
					}else {
						if(frame.getMinX()<minX) {
							minX = frame.getMinX();
						}
						if(frame.getMinY()<minY) {
							minY = frame.getMinY();
						}
						if(frame.getMaxX()>maxX) {
							maxX = frame.getMaxX();
						}
						if(frame.getMaxY()>maxY) {
							maxY = frame.getMaxY();
						}
					}
				}
				curG2d.dispose();
				super.positionMinX = positionX+minX;
				super.positionMinY = positionY+minY;
				curFrame.setMinX(minX);
				curFrame.setMaxX(maxX);
				curFrame.setMinY(minY);
				curFrame.setMaxY(maxY);
				if(ShapeUnitResourceCenter.isPowerOn || this.constConfig.lowPowerWorkable) {
					frameIndex++;
				}
			}else if(status==BuildingStatus.DEMAGED){
				BufferedImage curImg = curFrame.getImg();
				CanvasPainter.clearImage(curImg);
				Graphics curG2d = curImg.createGraphics();
				
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				for(int i=0;i<damagedFrames.size();i++) {
					List<ShapeUnitFrame> damagedFrameLs = damagedFrames.get(i);
					ShapeUnitFrame frame = damagedFrameLs.get(frameIndex%damagedFrameLs.size());
					BufferedImage oriImage = frame.getImg();
					curG2d.drawImage(oriImage, 0, 0, null);
					
					giveFrameUnitColor(curImg,frame);//上阵营色
					
					if(i==0) {
						minX = frame.getMinX();
						minY = frame.getMinY();
						maxX = frame.getMaxX();
						maxY = frame.getMaxY();
					}else {
						if(frame.getMinX()<minX) {
							minX = frame.getMinX();
						}
						if(frame.getMinY()<minY) {
							minY = frame.getMinY();
						}
						if(frame.getMaxX()>maxX) {
							maxX = frame.getMaxX();
						}
						if(frame.getMaxY()>maxY) {
							maxY = frame.getMaxY();
						}
					}
				}
				curG2d.dispose();
				super.positionMinX = positionX+minX;
				super.positionMinY = positionY+minY;
				curFrame.setMinX(minX);
				curFrame.setMaxX(maxX);
				curFrame.setMinY(minY);
				curFrame.setMaxY(maxY);
				if(ShapeUnitResourceCenter.isPowerOn || this.constConfig.lowPowerWorkable) {
					frameIndex++;
				}
			}
		}else if(stage==BuildingStage.Selling){//卖掉
			if(frameIndex>=constructFrames.size()) {
				frameIndex = constructFrames.size()-1;

				ShapeUnitFrame constFrame = constructFrames.get(frameIndex);
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
				frameIndex--;
			}else if(frameIndex>=0 && frameIndex<constructFrames.size()) {
				ShapeUnitFrame constFrame = constructFrames.get(frameIndex);
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
				frameIndex--;
			}else {
				endBuilding();
			}
		}
	}

	public void endBuilding(){
		this.end = true;
		setVisible(false);
		setEnd(true);
		getCurCenterPoint().setBuilding(null);//上边的物品
		ShapeUnitResourceCenter.removeOneBuilding(this);
		this.getBloodBar().setVisible(false);
		this.getBloodBar().setEnd(true);
		ShapeUnitResourceCenter.removeOneUnit(bloodBar);
		this.getBone().setVisible(false);
		this.getBone().setEnd(true);


		ShapeUnitResourceCenter.removeOneUnit(bone);
		getCurCenterPoint().buildingAreaType = BuildingAreaType.None;

		afterBuildingDestroy();
	}
	/**
	 * 留给子类重写
	 * 建筑被摧毁（卖掉）后做一些事情,默认播放语音
	 * building应该有生命周期的概念
	 */
	public void  afterBuildingDestroy() {
		Constructor.playOneMusic("ceva058");
	}





	//获取最左边的中心块
	public CenterPoint getLeftFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint leftFirst = null;
		int x = Integer.MAX_VALUE;
		for(CenterPoint cp:ls) {
			int cpx = cp.getX();
			if(cpx<x) {
				x = cp.getX();
				leftFirst = cp;
			}
		}
		return leftFirst;
	}
	//获取最上边的中心块
	public CenterPoint getTopFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint topFirst = null;
		int y = Integer.MAX_VALUE;
		for(CenterPoint cp:ls) {
			int cpy = cp.getY();
			if(cpy<y) {
				y = cp.getY();
				topFirst = cp;
			}
		}
		return topFirst;
	}
	//获取最右边的中心块
	public CenterPoint getRightFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint rightFirst = null;
		int x = 0;
		for(CenterPoint cp:ls) {
			int cpx = cp.getX();
			if(cpx>x) {
				x = cp.getX();
				rightFirst = cp;
			}
		}
		return rightFirst;
	}
	
	
	
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 */
	public abstract List<CenterPoint> getNoConstCpList();
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 */
	public abstract List<CenterPoint> getNoVehicleCpList();
	/**
	 * 或者这个建筑的阴影区域
	 */
	public List<CenterPoint> getShadownCpList(){
		return new ArrayList<>();
	}
	
	
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public int getPositionMinX() {
		return positionMinX;
	}
	public void setPositionMinX(int positionMinX) {
		this.positionMinX = positionMinX;
	}
	public int getPositionMinY() {
		return positionMinY;
	}
	public void setPositionMinY(int positionMinY) {
		this.positionMinY = positionMinY;
	}
	public ShapeUnitFrame getCurFrame() {
		return curFrame;
	}
	public void setCurFrame(ShapeUnitFrame curFrame) {
		this.curFrame = curFrame;
	}
	public SceneType getScene() {
		return scene;
	}
	public void setScene(SceneType scene) {
		this.scene = scene;
	}

	public BuildingStatus getStatus() {
		return status;
	}
	public void setStatus(BuildingStatus status) {
		this.status = status;
	}
	public BuildingStage getStage() {
		return stage;
	}
	public void setStage(BuildingStage stage) {
		this.stage = stage;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public BloodBar getBloodBar() {
		return bloodBar;
	}
	public void setBloodBar(BloodBar bloodBar) {
		this.bloodBar = bloodBar;
	}
	public BuildingBone getBone() {
		return bone;
	}
	public void setBone(BuildingBone bone) {
		this.bone = bone;
	}
	public ConstConfig getConstConfig() {
		return constConfig;
	}
	public void setConstConfig(ConstConfig constConfig) {
		this.constConfig = constConfig;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + unitNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(obj==this) {
			return true;
		}
		if(obj instanceof Building) {
			Building o = (Building)obj;
			if(o.getUnitNo()==this.getUnitNo()) {
				return true;
			}
		}
		return false;
	}
	
}

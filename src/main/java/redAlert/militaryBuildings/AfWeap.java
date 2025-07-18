package redAlert.militaryBuildings;

import java.awt.image.BufferedImage;
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
 * 盟军 建设工厂
 *
 */
public class AfWeap extends Building{
	/**
	 * 有坦克生产时需要的SHP图
	 */
	public List<List<ShapeUnitFrame>> tankFramesWorkingFrames = null;
	public int tankFrameWorkingIndex = 0;
	public List<List<ShapeUnitFrame>> tankFramesDamagedFrames = null;
	public int tankFrameDamagedIndex = 0;
	
	public List<List<ShapeUnitFrame>> tankFramesWorkingFrames2 = null;
	public int tankFrameWorkingIndex2 = 0;
	public List<List<ShapeUnitFrame>> tankFramesDamagedFrames2 = null;
	public int tankFrameDamagedIndex2 = 0;
	/**
	 * 有飞行物生产时需要的SHP图
	 */
	public List<List<ShapeUnitFrame>> flyFramesWorkingFrames = null;
	public int flyFrameWorkingIndex = 0;
	public List<List<ShapeUnitFrame>> flyFramesDamagedFrames = null;
	public int flyFrameDamagedIndex = 0;
	
	public List<List<ShapeUnitFrame>> flyFramesWorkingFrames2 = null;
	public int flyFrameWorkingIndex2 = 0;
	public List<List<ShapeUnitFrame>> flyFramesDamagedFrames2 = null;
	public int flyFrameDamagedIndex2 = 0;
	
	private boolean isMakingVehicle = false;
	private boolean isMakingFly = false;
	private boolean isPartOfWeap = false;//是否是组件部分  chaifen方法会造组件
	/**
	 * 建筑设置为isMaking=true时 isPutChildIn不会立刻改变  所以需要这个变量
	 */
	private volatile boolean isPutChildIn = false;//是否把子组件放入了缓存队列  避免时间差导致建筑消失
	
	/**
	 * 建设工厂的特殊方法
	 * 需要将建设工厂拆分成两个部分   两个部分的优先级和战车的优先级在上下之分
	 * 当有战车生产完毕  未完全走出建设工厂时   需要分别绘图
	 * 提供给计算线程使用 
	 */
	public List<Building> tankChaifen() {
		List<Building> chaifenLs = new ArrayList<>();
		
		AfWeap afweap1 = new AfWeap();//工厂身体部分
		afweap1.setScene(this.scene);
		afweap1.setPositionX(this.positionX);
		afweap1.setPositionY(this.positionY);
		afweap1.setStatus(this.status);
		afweap1.setStage(this.stage);
		afweap1.setPriority(40);//设置优先级  这行代码很重要  afweap2的优先级应低于战车
		afweap1.setPartOfWeap(true);
		afweap1.setMakingVehicle(true);
		afweap1.setUnitNo(new Random().nextInt());
		afweap1.setUnitName("weapPart1");
		
		if(status==BuildingStatus.UNDEMAGED){
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<tankFramesWorkingFrames.size();i++) {
				List<ShapeUnitFrame> workingFrameLs = tankFramesWorkingFrames.get(i);
				ShapeUnitFrame frame = workingFrameLs.get(tankFrameWorkingIndex%workingFrameLs.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
				
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
			afweap1.setPositionMinX(afweap1.getPositionX()+minX);
			afweap1.setPositionMinY(afweap1.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap1.setCurFrame(bf);
			tankFrameWorkingIndex++;
		}else {
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<tankFramesDamagedFrames.size();i++) {
				List<ShapeUnitFrame> damagedFrameLs = tankFramesDamagedFrames.get(i);
				ShapeUnitFrame frame = damagedFrameLs.get(tankFrameDamagedIndex%damagedFrameLs.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
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
			afweap1.setPositionMinX(afweap1.getPositionX()+minX);
			afweap1.setPositionMinY(afweap1.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap1.setCurFrame(bf);
			tankFrameDamagedIndex++;
		}
		chaifenLs.add(afweap1);
		
		
		
		AfWeap afweap2 = new AfWeap();//工厂的顶棚部分
		afweap2.setScene(this.scene);
		afweap2.setPositionX(this.positionX);
		afweap2.setPositionY(this.positionY);
		afweap2.setStatus(this.status);
		afweap2.setStage(this.stage);
		afweap2.setPriority(60);//设置优先级  这行代码很重要  afweap2的优先级应低于战车
		afweap2.setPartOfWeap(true);
		afweap2.setMakingVehicle(true);
		afweap2.setUnitNo(new Random().nextInt());
		afweap2.setUnitName("weapPart2");
		
		if(status==BuildingStatus.UNDEMAGED){
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<tankFramesWorkingFrames2.size();i++) {
				List<ShapeUnitFrame> workingFrameLs2 = tankFramesWorkingFrames2.get(i);
				ShapeUnitFrame frame = workingFrameLs2.get(tankFrameWorkingIndex2%workingFrameLs2.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
				
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
			afweap2.setPositionMinX(afweap2.getPositionX()+minX);
			afweap2.setPositionMinY(afweap2.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap2.setCurFrame(bf);
			tankFrameWorkingIndex2++;
		}else {
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<tankFramesDamagedFrames2.size();i++) {
				List<ShapeUnitFrame> damagedFrameLs2 = tankFramesDamagedFrames2.get(i);
				ShapeUnitFrame frame = damagedFrameLs2.get(tankFrameDamagedIndex2%damagedFrameLs2.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
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
			afweap2.setPositionMinX(afweap2.getPositionX()+minX);
			afweap2.setPositionMinY(afweap2.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap2.setCurFrame(bf);
			tankFrameDamagedIndex2++;
		}
		chaifenLs.add(afweap2);
		
		
		return chaifenLs;
	}
	
	/**
	 * 建设工厂的特殊方法
	 * 需要将建设工厂拆分成两个部分   两个部分的优先级和战车的优先级在上下之分
	 * 当有战车生产完毕  未完全走出建设工厂时   需要分别绘图
	 * 提供给计算线程使用 
	 */
	public List<Building> flyChaifen() {
		List<Building> chaifenLs = new ArrayList<>();
		
		AfWeap afweap1 = new AfWeap();//工厂的一部分
		afweap1.setScene(this.scene);
		afweap1.setPositionX(this.positionX);
		afweap1.setPositionY(this.positionY);
		afweap1.setStatus(this.status);
		afweap1.setStage(this.stage);
		afweap1.setPriority(40);//设置优先级  这行代码很重要  afweap2的优先级应低于飞行物
		afweap1.setPartOfWeap(true);
		afweap1.setMakingVehicle(true);
		afweap1.setUnitNo(new Random().nextInt());
		afweap1.setUnitName("weapPart1");
		
		if(status==BuildingStatus.UNDEMAGED){
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<flyFramesWorkingFrames.size();i++) {
				List<ShapeUnitFrame> workingFrameLs = flyFramesWorkingFrames.get(i);
				ShapeUnitFrame frame = workingFrameLs.get(flyFrameWorkingIndex%workingFrameLs.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
				
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
			afweap1.setPositionMinX(afweap1.getPositionX()+minX);
			afweap1.setPositionMinY(afweap1.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap1.setCurFrame(bf);
			flyFrameWorkingIndex++;
		}else {
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<flyFramesDamagedFrames.size();i++) {
				List<ShapeUnitFrame> damagedFrameLs = flyFramesDamagedFrames.get(i);
				ShapeUnitFrame frame = damagedFrameLs.get(flyFrameDamagedIndex%damagedFrameLs.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
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
			afweap1.setPositionMinX(afweap1.getPositionX()+minX);
			afweap1.setPositionMinY(afweap1.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap1.setCurFrame(bf);
			flyFrameDamagedIndex++;
		}
		chaifenLs.add(afweap1);
		
		
		
		AfWeap afweap2 = new AfWeap();//工厂的一部分
		afweap2.setScene(this.scene);
		afweap2.setPositionX(this.positionX);
		afweap2.setPositionY(this.positionY);
		afweap2.setStatus(this.status);
		afweap2.setStage(this.stage);
		afweap2.setPriority(60);//设置优先级  这行代码很重要  afweap2的优先级应低于战车
		afweap2.setPartOfWeap(true);
		afweap2.setMakingVehicle(true);
		afweap2.setUnitNo(new Random().nextInt());
		afweap2.setUnitName("weapPart2");
		
		if(status==BuildingStatus.UNDEMAGED){
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<flyFramesWorkingFrames2.size();i++) {
				List<ShapeUnitFrame> workingFrameLs2 = flyFramesWorkingFrames2.get(i);
				ShapeUnitFrame frame = workingFrameLs2.get(flyFrameWorkingIndex2%workingFrameLs2.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
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
			afweap2.setPositionMinX(afweap2.getPositionX()+minX);
			afweap2.setPositionMinY(afweap2.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap2.setCurFrame(bf);
			flyFrameWorkingIndex2++;
		}else {
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<flyFramesDamagedFrames2.size();i++) {
				List<ShapeUnitFrame> damagedFrameLs2 = flyFramesDamagedFrames2.get(i);
				ShapeUnitFrame frame = damagedFrameLs2.get(flyFrameDamagedIndex2%damagedFrameLs2.size());
				BufferedImage oriImg = frame.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = oriImg.getRGB(w, h);
						if(rgb1!=0)newImg.setRGB(w, h, rgb1);
					}
				}
				giveFrameUnitColor(newImg,frame);//上阵营色
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
			afweap2.setPositionMinX(afweap2.getPositionX()+minX);
			afweap2.setPositionMinY(afweap2.getPositionY()+minY);
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(newImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			afweap2.setCurFrame(bf);
			flyFrameDamagedIndex2++;
		}
		chaifenLs.add(afweap2);
		
		
		return chaifenLs;
	}
	
	
	public AfWeap() {
		
	}
	/**
	 * shp文件基础名
	 */
	public String basicName = "weap";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	public AfWeap(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfWeap(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
		//目前只加载了非雪地的部分   雪地的部分还没有加载  应该加判断！！！
		List<String> tankMakeShpPreFix = new ArrayList<>();
		tankMakeShpPreFix.add("gaweap_a");
		tankMakeShpPreFix.add("gaweap_1");
		tankMakeShpPreFix.add("gaweap_b");
		tankMakeShpPreFix.add("gaweapbb");
		tankFramesWorkingFrames = ShpResourceCenter.loadWorkingFrames(tankMakeShpPreFix, sceneType);
		tankFramesDamagedFrames = ShpResourceCenter.loadDamagedFrames(tankMakeShpPreFix, sceneType);
		
		List<String> tankMakeShpPreFix2 = new ArrayList<>();
		tankMakeShpPreFix2.add("gaweap_2");
		tankFramesWorkingFrames2 = ShpResourceCenter.loadWorkingFrames(tankMakeShpPreFix2, sceneType);
		tankFramesDamagedFrames2 = ShpResourceCenter.loadDamagedFrames(tankMakeShpPreFix2, sceneType);
		
		List<String> flysMakeShpPreFix = new ArrayList<>();
		flysMakeShpPreFix.add("gaweap_4");
		flysMakeShpPreFix.add("gaweap_b");
		flysMakeShpPreFix.add("gaweapbb");
		flysMakeShpPreFix.add("gaweap_a");
		flyFramesWorkingFrames = ShpResourceCenter.loadWorkingFrames(flysMakeShpPreFix, sceneType);
		flyFramesDamagedFrames = ShpResourceCenter.loadDamagedFrames(flysMakeShpPreFix, sceneType);
		List<String> flysMakeShpPreFix2 = new ArrayList<>();
		flysMakeShpPreFix2.add("gaweap_3");
		flyFramesWorkingFrames2 = ShpResourceCenter.loadWorkingFrames(flysMakeShpPreFix2, sceneType);
		flyFramesDamagedFrames2 = ShpResourceCenter.loadDamagedFrames(flysMakeShpPreFix2, sceneType);
	}
	
	public AfWeap(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
		//目前只加载了非雪地的部分   雪地的部分还没有加载  应该加判断！！！
		List<String> tankMakeShpPreFix = new ArrayList<>();
		tankMakeShpPreFix.add("gaweap_a");
		tankMakeShpPreFix.add("gaweap_1");
		tankMakeShpPreFix.add("gaweap_b");
		tankMakeShpPreFix.add("gaweapbb");
		tankFramesWorkingFrames = ShpResourceCenter.loadWorkingFrames(tankMakeShpPreFix, sceneType);
		tankFramesDamagedFrames = ShpResourceCenter.loadDamagedFrames(tankMakeShpPreFix, sceneType);
		
		List<String> tankMakeShpPreFix2 = new ArrayList<>();
		tankMakeShpPreFix2.add("gaweap_2");
		tankFramesWorkingFrames2 = ShpResourceCenter.loadWorkingFrames(tankMakeShpPreFix2, sceneType);
		tankFramesDamagedFrames2 = ShpResourceCenter.loadDamagedFrames(tankMakeShpPreFix2, sceneType);
		
		List<String> flysMakeShpPreFix = new ArrayList<>();
		flysMakeShpPreFix.add("gaweap_4");
		flysMakeShpPreFix.add("gaweap_b");
		flysMakeShpPreFix.add("gaweapbb");
		flysMakeShpPreFix.add("gaweap_a");
		flyFramesWorkingFrames = ShpResourceCenter.loadWorkingFrames(flysMakeShpPreFix, sceneType);
		flyFramesDamagedFrames = ShpResourceCenter.loadDamagedFrames(flysMakeShpPreFix, sceneType);
		List<String> flysMakeShpPreFix2 = new ArrayList<>();
		flysMakeShpPreFix2.add("gaweap_3");
		flyFramesWorkingFrames2 = ShpResourceCenter.loadWorkingFrames(flysMakeShpPreFix2, sceneType);
		flyFramesDamagedFrames2 = ShpResourceCenter.loadDamagedFrames(flysMakeShpPreFix2, sceneType);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfWeap;
		setCenterOffX(160);
		setCenterOffY(170);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggweap");
			super.aniShpPrefixLs.add("ggweap_2");
			super.aniShpPrefixLs.add("ggweap_1");
			super.aniShpPrefixLs.add("ggweap_b");
			super.aniShpPrefixLs.add("ggweap_a");
			super.aniShpPrefixLs.add("ggweapbb");
			
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggweap");
			super.aniShpPrefixLs.add("ggweap_2");
			super.aniShpPrefixLs.add("ggweap_1");
			super.aniShpPrefixLs.add("ggweap_b");
			super.aniShpPrefixLs.add("ggweap_a");
			super.aniShpPrefixLs.add("ggweapbb");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gaweap");
			super.aniShpPrefixLs.add("gaweap_2");
			super.aniShpPrefixLs.add("gaweap_1");
			super.aniShpPrefixLs.add("gaweap_b");
			super.aniShpPrefixLs.add("gaweap_a");
			super.aniShpPrefixLs.add("gaweapbb");
		}
	}
	
	public boolean isMakingVehicle() {
		return isMakingVehicle;
	}

	public void setMakingVehicle(boolean isMakingVehicle) {
		this.isMakingVehicle = isMakingVehicle;
	}

	public boolean isPartOfWeap() {
		return isPartOfWeap;
	}

	public void setPartOfWeap(boolean isPartOfWeap) {
		this.isPartOfWeap = isPartOfWeap;
	}

	public boolean isMakingFly() {
		return isMakingFly;
	}

	public void setMakingFly(boolean isMakingFly) {
		this.isMakingFly = isMakingFly;
	}
	public boolean isPutChildIn() {
		return isPutChildIn;
	}
	public void setPutChildIn(boolean isPutChildIn) {
		this.isPutChildIn = isPutChildIn;
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
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
		result.add(center.getRight());
		result.add(center.getRightUp());
		result.add(center.getUp());
		result.add(center.getLeftUp());
		result.add(center.getRight().getRightDn());
		result.add(center.getRightDn().getRightDn());
		result.add(center.getRightDn().getDn());
		result.add(center.getLeft().getLeftUp());
		result.add(center.getLeftUp().getLeftUp());
		result.add(center.getUp().getLeftUp());
		
		return result;
	}
	
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 * 建设工厂中心和门口是可以进入的
	 */
	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		int centerX = centerOffX + super.getPositionX();
		int centerY = centerOffY + super.getPositionY();
		List<CenterPoint> result = new ArrayList<>();
		
		CenterPoint center = PointUtil.fetchCenterPoint(centerX, centerY);
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRight());
		result.add(center.getRightUp());
		result.add(center.getUp());
		result.add(center.getLeft().getLeftUp());
		result.add(center.getLeftUp().getLeftUp());
		result.add(center.getUp().getLeftUp());
		
		return result;
	}
	
	/**
	 * 获取建筑的阴影区域
	 */
	@Override
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp().getLeftUp().getLeftUp();
		CenterPoint cp2 = cp.getUp().getLeftUp().getLeftUp();
		CenterPoint cp3 = cp.getUp().getUp().getLeftUp();
		CenterPoint cp4 = cp.getUp().getUp().getLeftUp().getLeftUp();
		CenterPoint cp5 = cp.getUp().getUp();
		CenterPoint cp6 = cp.getUp().getRightUp();
		CenterPoint cp7 = cp.getRightUp().getRightUp();
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

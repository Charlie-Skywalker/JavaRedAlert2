package redAlert.militaryBuildings;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.Constructor;
import redAlert.GameContext;
import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.other.NuclearBombDown;
import redAlert.other.NuclearBombUp;
import redAlert.other.Nuketo;
import redAlert.other.YiShu;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 苏军核弹井
 */
public class SfMisl extends Building{
	
	public SfMisl() {
		
	}
	
	/**
	 * 用于核弹井展开的动画
	 */
	protected List<List<ShapeUnitFrame>> buildingFrameExpandList;
	/**
	 * 核弹井展开的下标
	 */
	public int expandingIndex = 0;
	/**
	 * 核弹准备发射的动画
	 */
	protected List<List<ShapeUnitFrame>> buildingFrameReadyList;
	/**
	 * 核弹准备发射的下标
	 */
	protected int readyIndex = 0;
	/**
	 * 用户核弹井关闭的动画
	 */
	protected List<List<ShapeUnitFrame>> buildingFrameCloseList;
	/**
	 * 核弹井关闭的下标
	 */
	protected int closeIndex = 0;
	
	/**
	 * 核弹井的状态
	 */
	public int nuclearSiloStatus = 0;//0表示核弹未准备   1表示需要展开核弹井  2表示已经展开核弹井  3已发射需要关闭   4发射状态
	/**
	 * 核弹是否发射了
	 */
	public boolean isNuclearLaunched = false;
	/**
	 * 核弹是否正在发射
	 */
	public boolean isNuclearLaunching = false;
	/**
	 * 发射帧计数器
	 * 前几帧  发射井是不关闭的
	 */
	public int launchingCounter = 0;
	/**
	 * shp文件基础名
	 */
	public String basicName = "misl";
	/**
	 * 阵营 盟军
	 */
	public String team = "n";
	
	public SfMisl(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public SfMisl(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
		
		List<String> list = new ArrayList<>();
		list.add("ngmisl_f");
		list.add("ngmisl");
		this.buildingFrameExpandList = ShpResourceCenter.loadWorkingFrames(list,sceneType);
		List<String> list2 = new ArrayList<>();
		list2.add("ngmisl_g");
		list2.add("ngmisl");
		this.buildingFrameReadyList = ShpResourceCenter.loadWorkingFrames(list2, sceneType);
		List<String> list3 = new ArrayList<>();
		list3.add("ngmisl_h");
		list3.add("ngmisl");
		this.buildingFrameCloseList = ShpResourceCenter.loadWorkingFrames(list3, sceneType);
	}
	
	public SfMisl(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
		
		List<String> list = new ArrayList<>();
		list.add("ngmisl_f");
		list.add("ngmisl");
		this.buildingFrameExpandList = ShpResourceCenter.loadWorkingFrames(list,sceneType);
		List<String> list2 = new ArrayList<>();
		list2.add("ngmisl_g");
		list2.add("ngmisl");
		this.buildingFrameReadyList = ShpResourceCenter.loadWorkingFrames(list2, sceneType);
		List<String> list3 = new ArrayList<>();
		list3.add("ngmisl_h");
		list3.add("ngmisl");
		this.buildingFrameCloseList = ShpResourceCenter.loadWorkingFrames(list3, sceneType);
		
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.SfMisl;
		setCenterOffX(118);
		setCenterOffY(145);
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ngmisl_e");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ngmisl_e");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("namisl_e");
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
	 * 重写父类的计算下一帧方法
	 */
	public void calculateNextFrame() {
		
		if(stage==BuildingStage.UnderConstruct) {
			frameIndex++;
			if(frameIndex> constructFrames.size()-1  ) {
				stage = BuildingStage.ConstructComplete;
				frameIndex=0;
				//....展示第一幅画面
				calculateNextFrame();
			}else {
				curFrame = constructFrames.get(frameIndex);
				
				ShapeUnitFrame newFrame = curFrame.copy();
				BufferedImage image = newFrame.getImg();
				giveFrameUnitColor(image,newFrame);//上阵营色
				super.curFrame = newFrame;
			}
		}else if(status==BuildingStatus.UNDEMAGED) {
			if(nuclearSiloStatus==0) {//核弹井关闭状态
				int width = curFrame.getImg().getWidth();
				int height = curFrame.getImg().getHeight();
				BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				for(int i=0;i<workingFrames.size();i++) {
					List<ShapeUnitFrame> workingFrameLs = workingFrames.get(i);
					ShapeUnitFrame frame = workingFrameLs.get(frameIndex%workingFrameLs.size());
					BufferedImage oriImage = frame.getImg();
					for(int w=0;w<width;w++) {
						for(int h=0;h<height;h++) {
							int rgb1 = oriImage.getRGB(w, h);
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
				this.positionMinX = positionX+minX;
				this.positionMinY = positionY+minY;
				ShapeUnitFrame bf = new ShapeUnitFrame();
				bf.setImg(newImg);
				bf.setMinX(minX);
				bf.setMaxX(maxX);
				bf.setMinY(minY);
				bf.setMaxY(maxY);
				super.curFrame = bf;
				frameIndex++;
			}else if(nuclearSiloStatus==1){//需要展开的状态
				int width = curFrame.getImg().getWidth();
				int height = curFrame.getImg().getHeight();
				BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				for(int i=0;i<buildingFrameExpandList.size();i++) {
					List<ShapeUnitFrame> expandFrameLs = buildingFrameExpandList.get(i);
					ShapeUnitFrame frame = null;
					if( expandingIndex <=expandFrameLs.size()-1) {
						frame = expandFrameLs.get( expandingIndex%expandFrameLs.size());
					}else {
						frame = expandFrameLs.get(expandFrameLs.size()-1);
						if(expandingIndex>=15) {//核弹井展开一共16帧
							nuclearSiloStatus = 2;
							expandingIndex = 0;
						}
					}
					
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
				this.positionMinX = positionX+minX;
				this.positionMinY = positionY+minY;
				ShapeUnitFrame bf = new ShapeUnitFrame();
				bf.setImg(newImg);
				bf.setMinX(minX);
				bf.setMaxX(maxX);
				bf.setMinY(minY);
				bf.setMaxY(maxY);
				curFrame = bf;
				expandingIndex++;
			}else if(nuclearSiloStatus==2){//已经展开的状态
				int width = curFrame.getImg().getWidth();
				int height = curFrame.getImg().getHeight();
				BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				for(int i=0;i<buildingFrameReadyList.size();i++) {
					List<ShapeUnitFrame> readyFrameLs = buildingFrameReadyList.get(i);
					ShapeUnitFrame frame = readyFrameLs.get( readyIndex%readyFrameLs.size());
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
				this.positionMinX = positionX+minX;
				this.positionMinY = positionY+minY;
				ShapeUnitFrame bf = new ShapeUnitFrame();
				bf.setImg(newImg);
				bf.setMinX(minX);
				bf.setMaxX(maxX);
				bf.setMinY(minY);
				bf.setMaxY(maxY);
				curFrame = bf;
				readyIndex++;
			}else if(nuclearSiloStatus==3) {//需要关闭的状态
				int width = curFrame.getImg().getWidth();
				int height = curFrame.getImg().getHeight();
				BufferedImage newImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
				int minX = 0;
				int minY = 0;
				int maxX = 0;
				int maxY = 0;
				if(isNuclearLaunching && launchingCounter<1) {//刚发射   需要加载第一帧动画
					launchingCounter++;
				}else if(isNuclearLaunching && launchingCounter>0 && launchingCounter<10) {//停留在第一帧
					launchingCounter++;
					return;
				}else if(isNuclearLaunching && launchingCounter>=10) {//继续加载别的动画
					isNuclearLaunching = false;
					isNuclearLaunched = false;
					launchingCounter = 0;
				}
				
				for(int i=0;i<buildingFrameCloseList.size();i++) {
					List<ShapeUnitFrame> closeFrameLs = buildingFrameCloseList.get(i);
					ShapeUnitFrame frame = null;
					if(closeIndex <=closeFrameLs.size()-1) {
						frame = closeFrameLs.get( closeIndex%closeFrameLs.size());
					}else {
						frame = closeFrameLs.get(closeFrameLs.size()-1);
						if(closeIndex>=15) {//核弹井展开一共16帧
							nuclearSiloStatus = 0;
							closeIndex= 0;
						}
					}
					
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
				this.positionMinX = positionX+minX;
				this.positionMinY = positionY+minY;
				ShapeUnitFrame bf = new ShapeUnitFrame();
				bf.setImg(newImg);
				bf.setMinX(minX);
				bf.setMaxX(maxX);
				bf.setMinY(minY);
				bf.setMaxY(maxY);
				curFrame = bf;
				closeIndex++;
			}else if(nuclearSiloStatus==4) {//核弹发射状态
				
				//此处不需要画核弹井  仍然保持核弹井展开动画  curFrame不做修改
				
				//加载核弹的图片  然后把核弹绘制在图片上
				if(!isNuclearLaunched) {
					NuclearBombUp nuclearBomb = new NuclearBombUp(this);
					Constructor.putOneShapeUnit(nuclearBomb,GameContext.getMainPanel());
					Nuketo nuketo = new Nuketo(this.positionX+38,this.positionY+100);
					Constructor.putOneShapeUnit(nuketo,GameContext.getMainPanel());
					isNuclearLaunched = true;
					isNuclearLaunching = true;
					nuclearSiloStatus = 3;
					Constructor.playOneMusic("snuksire");//防空警报
					Constructor.playOneMusic("snuklaun");//发射的声音
					
					//核弹下落后的一切行为，都在任务中体现
					NuclearBombDown nbd = new NuclearBombDown(500,500,this.unitColor);
					YiShu yishu = new YiShu(nbd);
					yishu.start();
				}
				
			}
		}else {
			int width = curFrame.getImg().getWidth();
			int height = curFrame.getImg().getHeight();
			BufferedImage theImg = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			int minX = 0;
			int minY = 0;
			int maxX = 0;
			int maxY = 0;
			for(int i=0;i<damagedFrames.size();i++) {
				List<ShapeUnitFrame> ls = damagedFrames.get(i);
				ShapeUnitFrame bf = ls.get(frameIndex%damagedFrames.size());
				BufferedImage bi1 = bf.getImg();
				for(int w=0;w<width;w++) {
					for(int h=0;h<height;h++) {
						int rgb1 = bi1.getRGB(w, h);
						if(rgb1!=0)theImg.setRGB(w, h, rgb1);
					}
				}
				if(i==0) {
					minX = bf.getMinX();
					minY = bf.getMinY();
					maxX = bf.getMaxX();
					maxY = bf.getMaxY();
				}else {
					if(bf.getMinX()<minX) {
						minX = bf.getMinX();
					}
					if(bf.getMinY()<minY) {
						minY = bf.getMinY();
					}
					if(bf.getMaxX()>maxX) {
						maxX = bf.getMaxX();
					}
					if(bf.getMaxY()>maxY) {
						maxY = bf.getMaxY();
					}
				}
			}
			this.positionMinX = positionX+minX;
			this.positionMinY = positionY+minY;
			ShapeUnitFrame bf = new ShapeUnitFrame();
			bf.setImg(theImg);
			bf.setMinX(minX);
			bf.setMaxX(maxX);
			bf.setMinY(minY);
			bf.setMaxY(maxY);
			curFrame = bf;
			frameIndex++;
		}
	}
	
	/**
	 * 核弹井打开的动作
	 */
	public void nuclearSiloExpand() {
		try{
			if(this.nuclearSiloStatus==0){//关闭状态
				Constructor.playOneMusic("ceva003");//nuclear missile ready
				Thread.sleep(1000);
				this.nuclearSiloStatus = 1;
				this.expandingIndex = 0;
				Constructor.playOneMusic("snukread");
			}else {
				System.out.println("核弹井当前状态不允许打开");
			}
		}catch (Exception e) {
			
		}
	}
	/**
	 * 核弹井关闭的动作
	 */
	public void nuclearSiloClose() {
		try{
			if(this.nuclearSiloStatus==2) {//打开状态
				Constructor.playOneMusic("ceva002");//warning nuclear missile launched
				Thread.sleep(1000);
				this.nuclearSiloStatus = 3;
				this.closeIndex = 0;
				this.isNuclearLaunched = false;
			}else {
				System.out.println("当前状态不允许关闭核弹井");
			}
			
		}catch (Exception e) {
			
		}
	}
	
	/**
	 * 核弹井发射！
	 */
	public void nuclearSiloLaunch() {
		if(this.nuclearSiloStatus==2) {
			this.nuclearSiloStatus = 4;
		}else{
			System.out.println("当前状态无法发射核弹");
		}
		
	}
	
	/**
	 * 获取建筑的阴影区域
	 */
	@Override
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getUp().getLeftUp();
		CenterPoint cp2 = cp.getUp().getUp();
		CenterPoint cp3 = cp.getUp().getRightUp();
		
		CenterPoint cp4 = cp.getUp().getUp().getLeftUp();
		CenterPoint cp5 = cp.getUp().getUp().getUp();
		CenterPoint cp6 = cp.getUp().getUp().getRightUp();
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		list.add(cp6);
		
		return list;
	}
	
}

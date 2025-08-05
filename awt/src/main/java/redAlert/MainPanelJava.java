package redAlert;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import redAlert.enums.MouseStatus;
import redAlert.militaryBuildings.AfWeap;
import redAlert.other.Mouse;
import redAlert.other.MouseCursorObject;
import redAlert.other.MoveLine;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.task.ShapeUnitCalculateTask;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utilBean.MovePlan;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.PointUtil;
import redAlert.utils.TmpFileReader;

/**
 * 游戏场景界面
 * 基于JavaSwing渲染的游戏场景画板
 */
public class MainPanelJava extends JPanel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 自身引用
	 */
	public MainPanelJava myself;
	/**
	 * 最终给SWT线程绘制用的画板
	 */
	public BufferedImage canvas = new BufferedImage(SysConfig.viewportWidth,SysConfig.viewportHeight,BufferedImage.TYPE_INT_ARGB);
	
	/**
	 * 执行画板初始化
	 */
	public MainPanelJava() {
		super.setLocation(SysConfig.locationX, SysConfig.locationY);
//		super.setLayout(null);//JPanel的布局默认是FlowLayout
		super.setSize(SysConfig.viewportWidth, SysConfig.viewportHeight);
		super.setMinimumSize(new Dimension(SysConfig.viewportWidth,SysConfig.viewportHeight));//最小尺寸
		super.setPreferredSize(new Dimension(SysConfig.viewportWidth,SysConfig.viewportHeight));//首选尺寸
		
		this.myself = this;
		
		//游戏场景物品计算任务
		ShapeUnitCalculateTask calculateTask = new ShapeUnitCalculateTask(RuntimeParameter.shapeUnitBlockingQueue);
		calculateTask.startCalculateTask();
		
		this.setCursor(Mouse.getNoneCursor());//隐藏鼠标
		
		int theSightOffX = RuntimeParameter.viewportOffX;
		int theSightOffY = RuntimeParameter.viewportOffY;
		
		initGuidelinesCanvas(theSightOffX,theSightOffY);//初始化辅助线格
		startPainterThread();//启动绘画线程
	}
	
	/**
	 * 地形菱形块列表
	 */
	public List<BufferedImage> terrainImageList = new ArrayList<>();
	/**
	 * 地形菱形块名称列表
	 */
	public List<String> terrainNameList = new ArrayList<>();
	/**
	 * 初始化辅助线格
	 */
	public void initGuidelinesCanvas(int theSightOffX,int theSightOffY) {
		CanvasPainter.drawGuidelines(canvas,theSightOffX,theSightOffY);//辅助线网格
		
		//读取地形文件
		try {
			File mapFile = new File(GlobalConfig.mapFilePath);
			if(mapFile.exists()) {
				//加载tmp文件
				terrainImageList.add(TmpFileReader.test("clat01.sno"));
				terrainImageList.add(TmpFileReader.test("clat02.sno"));
				terrainImageList.add(TmpFileReader.test("clat03.sno"));
				terrainImageList.add(TmpFileReader.test("clat04.sno"));
				terrainImageList.add(TmpFileReader.test("clat05.sno"));
				terrainImageList.add(TmpFileReader.test("clat06.sno"));
				terrainImageList.add(TmpFileReader.test("clat07.sno"));
				terrainImageList.add(TmpFileReader.test("clat08.sno"));
				terrainImageList.add(TmpFileReader.test("clat09.sno"));
				terrainImageList.add(TmpFileReader.test("clat10.sno"));
				terrainImageList.add(TmpFileReader.test("clat11.sno"));
				terrainImageList.add(TmpFileReader.test("clat12.sno"));
				terrainImageList.add(TmpFileReader.test("clat13.sno"));
				terrainImageList.add(TmpFileReader.test("clat14.sno"));
				terrainImageList.add(TmpFileReader.test("clat15.sno"));
				terrainImageList.add(TmpFileReader.test("clat16.sno"));
				
				terrainImageList.add(TmpFileReader.test("clat01a.sno"));
				terrainImageList.add(TmpFileReader.test("clat02a.sno"));
				terrainImageList.add(TmpFileReader.test("clat03a.sno"));
				terrainImageList.add(TmpFileReader.test("clat04a.sno"));
				terrainImageList.add(TmpFileReader.test("clat05a.sno"));
				terrainImageList.add(TmpFileReader.test("clat06a.sno"));
				terrainImageList.add(TmpFileReader.test("clat07a.sno"));
				terrainImageList.add(TmpFileReader.test("clat08a.sno"));
				terrainImageList.add(TmpFileReader.test("clat09a.sno"));
				terrainImageList.add(TmpFileReader.test("clat10a.sno"));
				terrainImageList.add(TmpFileReader.test("clat11a.sno"));
				terrainImageList.add(TmpFileReader.test("clat12a.sno"));
				terrainImageList.add(TmpFileReader.test("clat13a.sno"));
				terrainImageList.add(TmpFileReader.test("clat14a.sno"));
				terrainImageList.add(TmpFileReader.test("clat15a.sno"));
				terrainImageList.add(TmpFileReader.test("clat16a.sno"));
				
				terrainNameList.add(("clat01.sno"));
				terrainNameList.add(("clat02.sno"));
				terrainNameList.add(("clat03.sno"));
				terrainNameList.add(("clat04.sno"));
				terrainNameList.add(("clat05.sno"));
				terrainNameList.add(("clat06.sno"));
				terrainNameList.add(("clat07.sno"));
				terrainNameList.add(("clat08.sno"));
				terrainNameList.add(("clat09.sno"));
				terrainNameList.add(("clat10.sno"));
				terrainNameList.add(("clat11.sno"));
				terrainNameList.add(("clat12.sno"));
				terrainNameList.add(("clat13.sno"));
				terrainNameList.add(("clat14.sno"));
				terrainNameList.add(("clat15.sno"));
				terrainNameList.add(("clat16.sno"));
				
				terrainNameList.add(("clat01a.sno"));
				terrainNameList.add(("clat02a.sno"));
				terrainNameList.add(("clat03a.sno"));
				terrainNameList.add(("clat04a.sno"));
				terrainNameList.add(("clat05a.sno"));
				terrainNameList.add(("clat06a.sno"));
				terrainNameList.add(("clat07a.sno"));
				terrainNameList.add(("clat08a.sno"));
				terrainNameList.add(("clat09a.sno"));
				terrainNameList.add(("clat10a.sno"));
				terrainNameList.add(("clat11a.sno"));
				terrainNameList.add(("clat12a.sno"));
				terrainNameList.add(("clat13a.sno"));
				terrainNameList.add(("clat14a.sno"));
				terrainNameList.add(("clat15a.sno"));
				terrainNameList.add(("clat16a.sno"));
				
				
				//读取地图文件
				String mapText = FileUtils.readFileToString(new File(GlobalConfig.mapFilePath), "UTF-8");
				String [] strs = StringUtils.split(mapText,"$");
				
				Graphics2D g2d = canvas.createGraphics();
				
				for(int i=0;i<strs.length;i++) {
					String info = strs[i];
					String [] infos = StringUtils.split(info,",");
					int x = Integer.valueOf(infos[0]);
					int y = Integer.valueOf(infos[1]);
					String name = infos[2];
					
					int index = terrainNameList.indexOf(name);
					CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
					cp.setTileIndex(index);
					BufferedImage image = terrainImageList.get(index);
					g2d.drawImage(image, cp.getX()-30, cp.getY()-15, null);
					
				}
				g2d.dispose();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * TODO 启动画板绘制线程
	 * 画板绘制线程将按照指定的时间间隔,定期绘制成员变量中的3个图片对象
	 * 绘制鼠标、绘制地形、绘制游戏内单位
	 * 
	 * 
	 */
	private void startPainterThread() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			
			public boolean prioritySetFlag = false;
			@Override
			public void run() {
				try {
					//将绘制线程的优先级调整为最高
					if(!prioritySetFlag) {
						Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
						prioritySetFlag = true;
					}
					
					//获取视口偏移,由于这两个变量变化频繁,所以需要获取一个快照,否则移动视口内容会抖动
					int theSightOffX = RuntimeParameter.viewportOffX;
					int theSightOffY = RuntimeParameter.viewportOffY;
					
					//绘制地形（地形的代码块覆盖全图,所以就不用重新清空画板了）
					drawTerrain(theSightOffX,theSightOffY);
					//绘制游戏内的ShapeUnit
					drawMainInterface(theSightOffX,theSightOffY);
					//绘制预建造菱形红绿块
					drawRhombus(theSightOffX, theSightOffY);
					//绘制选择框
					drawSelectRect();
					//绘制鼠标指针
					drawMouseCursor();
					
					myself.repaint();
					RuntimeParameter.frameCount++;
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(refreshTask, 1L, 1000/RuntimeParameter.fps);
		
	}
	
	/**
	 *  绘制地形terrain
	 *  
	 *  有地形画地形
	 *  没地形画网格
	 */
	public void drawTerrain(int viewportOffX,int viewportOffY) {
		
		if(!terrainImageList.isEmpty()) {
			Graphics2D g2d = canvas.createGraphics();
			//一类中心点
			for(int m=0;m<50;m++) {
				int y = 15+30*m;
				for(int n=0;n<50;n++) {
					int x = 30+60*n;
					CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
					int cpx = cp.getX();
					int cpy = cp.getY();
					if(  cpx>= viewportOffX-100 && cpx<=viewportOffX+SysConfig.viewportWidth+100 && cpy>=viewportOffY-100 && cpy< viewportOffY+SysConfig.viewportHeight+100) {
						g2d.drawImage( terrainImageList.get(cp.getTileIndex()), cp.getX()-30-viewportOffX, cp.getY()-15-viewportOffY, null);
					}
				}
			}
			
			//二类中心点
			for(int m=0;m<50;m++) {
				int y = 30*m;
				for(int n=0;n<50;n++) {
					int x = 60*n;
					CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
					int cpx = cp.getX();
					int cpy = cp.getY();
					if(  cpx>= viewportOffX-100 && cpx<=viewportOffX+SysConfig.viewportWidth+100 && cpy>=viewportOffY-100 && cpy< viewportOffY+SysConfig.viewportHeight+100) {
						g2d.drawImage( terrainImageList.get(cp.getTileIndex()), cp.getX()-30-viewportOffX, cp.getY()-15-viewportOffY, null);
					}
				}
			}
			g2d.dispose();
		}else {
			CanvasPainter.drawGuidelines(canvas, viewportOffX, viewportOffY);//辅助线网格
		}
	}
	
	/**
	 * 画板绘制线程会不停调用此方法,从绘制队列中拿取方块(ShapeUnit),绘制到主画板上
	 * 绘制完毕后,会把方块再放入SHP方块阻塞队列,由方块帧计算线程计算下一帧,从而实现游戏画面循环
	 * 
	 * 其中调用repaint方法后,系统SWT线程会稍后更新JPanel中显示的内容
	 */
	public void drawMainInterface(int viewportOffX,int viewportOffY) {
		PriorityQueue<ShapeUnit> drawShapeUnitList  = null;
		
		
		/**
		 * 这样保证获取缓存队列与获取绘制队列间不冲突
		 * 保证在绘制时,其他线程可以向缓存队列中放置内容
		 * 保证其他线程向缓存队列放置方块过程中,缓存队列不会突然变成绘制队列,导致线程向绘制队列中放置方块
		 */
		while(true) {
			if(RuntimeParameter.casLock.compareAndSet(0, 1)) {
				RuntimeParameter.queueFlag.addAndGet(1);//先把缓存队列切换成绘制队列(队列身份互换)
				drawShapeUnitList = RuntimeParameter.getDrawShapeUnitList();
				RuntimeParameter.casLock.compareAndSet(1, 0);
				break;
			}
		}
			
		if(!drawShapeUnitList.isEmpty()) {
			
			Graphics2D g2d = canvas.createGraphics();
			
			while(!drawShapeUnitList.isEmpty()) {
				ShapeUnit shp = drawShapeUnitList.poll();
				if(shp instanceof AfWeap) {
					AfWeap afweap = (AfWeap)shp;
					/**
					 * 解决正在建造车辆的问题
					 * 战车工厂的主建筑标记为正在造车辆  则不绘制这个建筑
					 */
					if(!afweap.isPartOfWeap()) {//主建筑
						if(afweap.isMakingVehicle() && afweap.isPutChildIn()) {
							RuntimeParameter.addUnitToQueue(shp);//放回规划队列,不进行绘制
							continue;
						}else if(afweap.isMakingFly() && afweap.isPutChildIn()) {
							RuntimeParameter.addUnitToQueue(shp);//放回规划队列,不进行绘制
							continue;
						}else {//正常
							ShapeUnitFrame bf = shp.getCurFrame();
							BufferedImage img = bf.getImg();
							int positionX = shp.getPositionX();
							int positionY = shp.getPositionY();
							int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
							int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
							g2d.drawImage(img, viewX, viewY, this);
							
							RuntimeParameter.addUnitToQueue(shp);//放回规划队列
						}
					}else{//子建筑
						ShapeUnitFrame bf = shp.getCurFrame();
						BufferedImage img = bf.getImg();
						int positionX = shp.getPositionX();
						int positionY = shp.getPositionY();
						int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
						int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
						g2d.drawImage(img, viewX, viewY, this);
						
						RuntimeParameter.addUnitToQueue(shp);//放回规划队列
					}
					
					
				}else {
					
					if(shp.isVisible()) {
						ShapeUnitFrame bf = shp.getCurFrame();
						BufferedImage img = bf.getImg();
						int positionX = shp.getPositionX();
						int positionY = shp.getPositionY();
						int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
						int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
						g2d.drawImage(img, viewX, viewY, this);
						
						//画移动线
						if(shp instanceof MoveLine) {
							MoveLine ml = (MoveLine)shp;
							List<MovePlan> movePlanLs = ml.getMovePlans();
							for(MovePlan plan:movePlanLs) {
								int startx = plan.getUnit().getPositionX()+ plan.getUnit().getCenterOffX();
								int starty = plan.getUnit().getPositionY()+ plan.getUnit().getCenterOffY();
								int endx = plan.getTargetCp().getX();
								int endy = plan.getTargetCp().getY();
								
								int startViewX = CoordinateUtil.getViewportX(startx, viewportOffX);
								int startViewY = CoordinateUtil.getViewportY(starty, viewportOffY);
								int endxViewX = CoordinateUtil.getViewportX(endx, viewportOffX);
								int endxViewY = CoordinateUtil.getViewportY(endy, viewportOffY);
								
								g2d.setColor(MoveLine.lineColor);
								g2d.setStroke(MoveLine.stroke);
								g2d.drawLine(startViewX, startViewY, endxViewX, endxViewY);//画连接线
								g2d.fillRect(startViewX-1, startViewY-1, MoveLine.radius, MoveLine.radius);//画端点
								g2d.fillRect(endxViewX-1, endxViewY-1, MoveLine.radius, MoveLine.radius);//画端点
								
								
							}
						}
					}
					RuntimeParameter.addUnitToQueue(shp);//放回规划队列
				}
				
			}
			g2d.dispose();
		}
			
	}
	
	/**
	 * 绘制鼠标指针
	 */
	public void drawMouseCursor() {
		Point mousePoint = myself.getMousePosition();
		if(mousePoint!=null) {
			MouseCursorObject cursor = Mouse.getMouseCursor(RuntimeParameter.mouseStatus);
			int positionX = mousePoint.x-cursor.getOffX();
			int positionY = mousePoint.y-cursor.getOffY();
			
			Graphics2D g2d = canvas.createGraphics();
			BufferedImage cursorImage = cursor.getMouse().getImg();
			g2d.drawImage(cursorImage, positionX, positionY, null);
			g2d.dispose();
		}
	}
	
	/**
	 * 画建造菱形块的方法
	 */
	public void drawRhombus(int viewportOffX,int viewportOffY) {
		
		if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {
			Point mousePoint = myself.getMousePosition();
			if(mousePoint!=null) {
				Coordinate coord = CoordinateUtil.getCoordinate(mousePoint.x, mousePoint.y);
				CenterPoint centerPoint = coord.getCenterPoint();
				
				int fxNum = MouseEventDeal.constName.fxNum;
				int fyNum = MouseEventDeal.constName.fyNum;
				
				CanvasPainter.drawRhombus(centerPoint, fxNum, fyNum, canvas);
			}
		}
	}
	
	/**
	 * 画选择框(按下鼠标后拖动呈现的白色选择框)
	 */
	public void drawSelectRect() {
		
		if(RuntimeParameter.mouseStatus == MouseStatus.Select) {
			int pressX = RuntimeParameter.pressX;
			int pressY = RuntimeParameter.pressY;
			Point mousePoint = myself.getMousePosition();
			if(mousePoint!=null) {
				int endMouseX = mousePoint.x;
				int endMouseY = mousePoint.y;
				CanvasPainter.drawSelectRect(pressX, pressY, endMouseX, endMouseY, canvas);
			}
		}
	}
	
	/**
	 * 重绘方法  将主画板的内容绘制在窗口中
	 * Swing的组件,应该重写paintComponent方法  这样没有闪屏问题
	 */
	@Override
	public void paintComponent(Graphics g) {
		try {
			super.paintComponent(g);
			g.drawImage(canvas, 0, 0, this);
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

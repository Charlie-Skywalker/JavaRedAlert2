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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import redAlert.MainTest.MouseStatus;
import redAlert.militaryBuildings.AfWeap;
import redAlert.other.Mouse;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.FrameCalculateThread;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.PointUtil;
import redAlert.utils.TmpFileReader;

/**
 * 游戏场景界面
 *
 */
public class MainPanel extends JPanel{
	
	private static final long serialVersionUID = -682168191460964814L;
	/**
	 * JPanel在JFrame中的坐落位置
	 */
	private final int locationX = 0;
	private final int locationY = 0;
	/**
	 * 游戏主画面宽高
	 * 宽高比设为2比1是因为菱形格子横竖对角线长度比为2比1
	 */
	public static final int viewportWidth = 1800;
	public static final int viewportHeight = 900;
	/**
	 * 视口在地图上偏移量
	 * 视野偏移量,用于确认渲染的范围
	 */
	public static int viewportOffX = 0;
	public static int viewportOffY = 0;
	/**
	 * 战场地图的宽高
	 */
	public static final int gameMapWidth = 6000;
	public static final int gameMapHeight = 4000;
	
	/**
	 * 主画板绘画间隔66毫秒
	 * 红警的默认帧率是15帧/秒,这样设置更接近红警2
	 * 
	 * 速度5下  红警的默认帧率是60帧
	 */
	private final long paintPeriod = 17;
	
	/**
	 * SHP方块阻塞队列
	 * 新增的ShapeUnit都需要先放入此队列,再由规划线程计算渲染次序
	 * 注意:此队列只用于渲染画面,不参与游戏的逻辑运算,逻辑运算由资源中心负责处理
	 * 为什么用阻塞队列:
	 *   当队列中的元素处理完毕放入规划队列时，此时会阻塞，避免建筑规划线程不停的轮询，减少CPU占用
	 */
	public ArrayBlockingQueue<ShapeUnit> shapeUnitBlockingQueue = new ArrayBlockingQueue<ShapeUnit>(100);
	/**
	 * 主画板
	 */
	private BufferedImage mainInterface = new BufferedImage(viewportWidth,viewportHeight,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 用来画鼠标、选择框等主画板层面上的内容
	 */
	private BufferedImage canvasFirst = new BufferedImage(viewportWidth,viewportHeight,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 辅助线格
	 */
	private BufferedImage guidelinesCanvas = new BufferedImage(viewportWidth,viewportHeight,BufferedImage.TYPE_INT_ARGB);
	
	
	/**
	 * SHP方块规划队列1
	 * 当buildingFlag=偶数  此队列为绘制队列
	 * 当buildingFlag=奇数  此队列为缓存队列
	 */
	private PriorityQueue<ShapeUnit> unitList = new PriorityQueue<ShapeUnit>();
	/**
	 * SHP方块规划队列2
	 * 两个建筑队列,一个用于绘制画面时，另一个用于缓存下一帧画面
	 * 当buildingFlag=奇数  此队列为缓存队列
	 * 当buildingFlag=偶数  此队列为绘制队列
	 */
	private PriorityQueue<ShapeUnit> unitList2 = new PriorityQueue<ShapeUnit>();
	/**
	 * SHP方块队列标识
	 * 用于决定使用1.2哪个队列
	 */
	public AtomicInteger queueFlag = new AtomicInteger(0);
	/**
	 * 0表示空闲  1表示正在使用缓存队列
	 * 为使用缓存队列而设计的CAS锁
	 */
	private AtomicInteger casLock = new AtomicInteger(0);
	/**
	 * 鼠标上次所在中心点
	 */
	private CenterPoint lastMoveCenterPoint = null;
	/**
	 * 鼠标上次所在点坐标
	 */
	private int lastMoveX;
	private int lastMoveY;
	
	/**
	 * 帧计数
	 */
	public static long frameCount = 0;
	
	
	
	
	
	/**
	 * 
	 */
	public MainPanel() {
		super.setLocation(locationX, locationY);
//		super.setLayout(null);//JPanel的布局默认是FlowLayout
		super.setSize(viewportWidth, viewportHeight);
		super.setMinimumSize(new Dimension(viewportWidth,viewportHeight));//最小尺寸
		super.setPreferredSize(new Dimension(viewportWidth,viewportHeight));//首选尺寸
		
		GameContext.setMainPanel(this);//暴露一个外部引用
		startFrameImageCalculate();//场景物品计算线程启动
		initGuidelinesCanvas();//初始化辅助线格
		startPainterThread();//启动绘画线程
	}
	
	
	/**
	 * 获取绘制队列
	 */
	public PriorityQueue<ShapeUnit> getDrawShapeUnitList() {
		if(queueFlag.get()%2==0) {
			return this.unitList;
		}else {
			return this.unitList2;
		}
	}
	/**
	 * 获取缓存队列
	 */
	public PriorityQueue<ShapeUnit> getCacheShapeUnitList() {
		if(queueFlag.get()%2==0) {
			return this.unitList2;
		}else {
			return this.unitList;
		}
	}
	
	/**
	 * 向缓存队列中添加建筑
	 */
	public void addBuildingToQueue(ShapeUnit unit) {
		while(true) {
			if(casLock.compareAndSet(0, 1)) {
				 PriorityQueue<ShapeUnit> cacheShapeUnitList = getCacheShapeUnitList();
				if(cacheShapeUnitList.contains(unit)) {
					System.out.println("有重复移除");
					cacheShapeUnitList.remove(unit);
				}
				cacheShapeUnitList.offer(unit);
				casLock.compareAndSet(1, 0);
				break;
			}
		}
	}
	
	/**
	 * 向规划队列中添加建筑（实验）
	 */
	public void addBuildingToCalQueue(ShapeUnit unit) {
		while(true) {
			if(casLock.compareAndSet(0, 1)) {
				if(shapeUnitBlockingQueue.contains(unit)) {
					System.out.println("有重复移除");
					shapeUnitBlockingQueue.remove(unit);
				}
				shapeUnitBlockingQueue.offer(unit);
				casLock.compareAndSet(1, 0);
				break;
			}
		}
	}
	
	public ArrayBlockingQueue<Runnable> threadQueue = new ArrayBlockingQueue<Runnable>(10);
	public ArrayBlockingQueue<Runnable> abq = new ArrayBlockingQueue<>(3);
    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(20,20+5,60, TimeUnit.SECONDS,abq,new ThreadPoolExecutor.CallerRunsPolicy());
	
	
	
	/**
	 * TODO 负责计算画面的线程,计算所有应绘制的内容的画面,放入队列
	 * 注意take方法会阻塞
	 */
	private void startFrameImageCalculate() {
		
		for(int i=0;i<10;i++) {
			FrameCalculateThread fcThread = new FrameCalculateThread(this,threadQueue);
			threadQueue.offer(fcThread);
		}
		
		/**
		 * 建筑规划线程
		 * 只有一个线程在负责计算下一帧的内容，如果后续有多个线程，性能将大大提升
		 * 
		 * 必须明确  建筑规划线程主要职责是计算下一帧和移除   不应该处理其他业务逻辑
		 * 重工的逻辑  必须改成由重工自己确定状态  计算下一帧
		 */
		Thread thread = new Thread() {
			public void run() {
				while(true) {
					try {
						ShapeUnit shp = shapeUnitBlockingQueue.take();
						//多线程规划   
						FrameCalculateThread fcThread = (FrameCalculateThread)threadQueue.take();
						fcThread.setShp(shp);
						threadPoolExecutor.execute(fcThread);
					}catch(Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			}
		};
		thread.start();
	}
	
	
	
	/**
	 * TODO 负责绘制画面的线程,将队列中计算好的内容,画进画板
	 * 
	 */
	private void startPainterThread() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			
			public boolean prioritySetFlag = false;
			@Override
			public void run() {
				try {
					if(!prioritySetFlag) {
						Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
						prioritySetFlag = true;
					}
					
					//绘制鼠标图片
					Point mousePoint = GameContext.scenePanel.getMousePosition();
					if(mousePoint==null) {
						/*
						int lastMoveX = GameContext.scenePanel.getLastMoveX();
						int lastMoveY = GameContext.scenePanel.getLastMoveY();
						if(lastMoveX>0) {
							lastMoveX-=20;
						}
						if(lastMoveY>0) {
							lastMoveY-=15;
						}
						mousePoint = new Point(lastMoveX,lastMoveY); 
						*/
						CanvasPainter.clearImage(GameContext.scenePanel.mouseCursorImage);
					}else {
						if(MainTest.mouseStatus==MouseStatus.Idle) {
							drawDefaultMouseCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.PreSingleSelect) {
							drawPreSingleSelectMouseCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.Construct) {
							drawDefaultMouseCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.Select) {
							drawDefaultMouseCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.UnitMove) {
							drawUnitMoveMouseCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.UnitExpand) {
							drawUnitExpandCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.UnitNoExpand) {
							drawUnitNoExpandCursor(mousePoint);
						}
						if(MainTest.mouseStatus==MouseStatus.UnitNoMove) {
							drawUnitNoMoveCursor(mousePoint);
						}
					}
					
					int theSightOffX = viewportOffX;//由于这两个变量变化频繁,所以需要获取一个快照,否则移动视口内容会抖动
					int theSightOffY = viewportOffY;
					
					//绘制地形
					drawTerrain(theSightOffX,theSightOffY);
					
					//绘制游戏内的ShapeUnit
					drawMainInterface(theSightOffX,theSightOffY);
					
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		timer.schedule(refreshTask, 1L, paintPeriod);
		
	}
	
	
	/**
	 * 地形菱形块列表
	 */
	public List<BufferedImage> terrainImageList = new ArrayList<>();
	
	public List<String> terrainNameList = new ArrayList<>();
	/**
	 * 初始化辅助线格
	 */
	private void initGuidelinesCanvas() {
		CanvasPainter.drawGuidelines(guidelinesCanvas);//辅助线网格
		
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
				
				Graphics2D g2d = guidelinesCanvas.createGraphics();
				
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
	 *  绘制地形terrain
	 */
	private void drawTerrain(int viewportOffX,int viewportOffY) {
		
		if(!terrainImageList.isEmpty()) {
			Graphics2D g2d = guidelinesCanvas.createGraphics();
			//一类中心点
			for(int m=0;m<50;m++) {
				int y = 15+30*m;
				for(int n=0;n<50;n++) {
					int x = 30+60*n;
					CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
					int cpx = cp.getX();
					int cpy = cp.getY();
					if(  cpx>= viewportOffX-100 && cpx<=viewportOffX+viewportWidth+100 && cpy>=viewportOffY-100 && cpy< viewportOffY+viewportHeight+100) {
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
					if(  cpx>= viewportOffX-100 && cpx<=viewportOffX+viewportWidth+100 && cpy>=viewportOffY-100 && cpy< viewportOffY+viewportHeight+100) {
						g2d.drawImage( terrainImageList.get(cp.getTileIndex()), cp.getX()-30-viewportOffX, cp.getY()-15-viewportOffY, null);
					}
				}
			}
			g2d.dispose();
		}
	}
	
	
	
	/**
	 * 主类中的TimerTask会不停调用此方法,实现画面的刷新
	 * 根据建筑队列绘制主画板,每个建筑在绘制完毕后,会返回
	 */
	private void drawMainInterface(int viewportOffX,int viewportOffY) {
		PriorityQueue<ShapeUnit> drawShapeUnitList  = null;
		
		
		/**
		 * 这样保证获取缓存队列与获取绘制队列间不冲突
		 * 保证在绘制时,其他线程可以向缓存队列中放置内容
		 * 保证其他线程获取缓存队列后,缓存队列不会突然变成绘制队列,导致线程向绘制队列中放置物品
		 */
		while(true) {
			if(casLock.compareAndSet(0, 1)) {
				queueFlag.addAndGet(1);//先把缓存队列切换成绘制队列
				drawShapeUnitList = getDrawShapeUnitList();
				casLock.compareAndSet(1, 0);
				break;
			}
		}
			
		if(!drawShapeUnitList.isEmpty()) {
			
			CanvasPainter.clearImage(mainInterface);
			Graphics2D g2d = mainInterface.createGraphics();
			
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
							shapeUnitBlockingQueue.add(shp);//再放回,不进行绘画
							continue;
						}else if(afweap.isMakingFly() && afweap.isPutChildIn()) {
							shapeUnitBlockingQueue.add(shp);//再放回,不进行绘画
							continue;
						}else {//正常
							ShapeUnitFrame bf = shp.getCurFrame();
							BufferedImage img = bf.getImg();
							int positionX = shp.getPositionX();
							int positionY = shp.getPositionY();
							int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
							int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
							g2d.drawImage(img, viewX, viewY, this);
							
							shapeUnitBlockingQueue.add(shp);//放入
						}
					}else{//子建筑
						ShapeUnitFrame bf = shp.getCurFrame();
						BufferedImage img = bf.getImg();
						int positionX = shp.getPositionX();
						int positionY = shp.getPositionY();
						int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
						int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
						g2d.drawImage(img, viewX, viewY, this);
						
						shapeUnitBlockingQueue.add(shp);//放入
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
					}
					shapeUnitBlockingQueue.add(shp);//放入规划队列
				}
				
			}
			g2d.dispose();
			
			this.repaint();
			
			frameCount++;
			
		}else {
//			防止空屏   这里就不再repaint();
//			this.repaint();
		}
			
	}
	
	
	
	
	
	public BufferedImage getMainInterface() {
		return mainInterface;
	}

	public void setMainInterface(BufferedImage mainInterface) {
		this.mainInterface = mainInterface;
	}

	public BufferedImage getCanvasFirst() {
		return canvasFirst;
	}

	public void setCanvasFirst(BufferedImage canvasFirst) {
		this.canvasFirst = canvasFirst;
	}

	public BufferedImage getGuidelinesCanvas() {
		return guidelinesCanvas;
	}

	public void setGuidelinesCanvas(BufferedImage guidelinesCanvas) {
		this.guidelinesCanvas = guidelinesCanvas;
	}

	public CenterPoint getLastMoveCenterPoint() {
		return lastMoveCenterPoint;
	}
	public void setLastMoveCenterPoint(CenterPoint lastMoveCenterPoint) {
		this.lastMoveCenterPoint = lastMoveCenterPoint;
	}
	public int getLastMoveX() {
		return lastMoveX;
	}
	public void setLastMoveX(int lastMoveX) {
		this.lastMoveX = lastMoveX;
	}
	public int getLastMoveY() {
		return lastMoveY;
	}
	public void setLastMoveY(int lastMoveY) {
		this.lastMoveY = lastMoveY;
	}
	
	
	/**
	 * 表示鼠标指针的图片
	 */
	private BufferedImage mouseCursorImage = new BufferedImage(55,43,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 表示鼠标图片应该被放置的坐标
	 */
	private int positionX,positionY;
	
	/**
	 * 绘制默认样式的鼠标
	 */
	private void drawDefaultMouseCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getDefaultCursorImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x;
		positionY = mouesePoint.y;
		g2d.dispose();
	}
	/**
	 * 绘制预单选样式的鼠标
	 */
	private void drawPreSingleSelectMouseCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getPreSingleSelectCursorImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x-27;
		positionY = mouesePoint.y-21;
		g2d.dispose();
	}
	/**
	 * 绘制单位移动指针样式的鼠标
	 */
	private void drawUnitMoveMouseCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getUnitMoveCursorImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x-27;
		positionY = mouesePoint.y-21;
		g2d.dispose();
	}
	/**
	 * 绘制单位部署样式的鼠标
	 */
	private void drawUnitExpandCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getUnitExpandImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x-27;
		positionY = mouesePoint.y-21;
		g2d.dispose();
	}
	/**
	 * 绘制单位禁止部署样式的鼠标
	 */
	private void drawUnitNoExpandCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getUnitNoExpandImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x-27;
		positionY = mouesePoint.y-21;
		g2d.dispose();
	}
	/**
	 * 绘制单位禁止移动样式的鼠标
	 */
	private void drawUnitNoMoveCursor(Point mouesePoint) {
		CanvasPainter.clearImage(this.mouseCursorImage);
		Graphics2D g2d = this.mouseCursorImage.createGraphics();
		BufferedImage defaultMouse = Mouse.getUnitNoMoveImage();
		g2d.drawImage(defaultMouse, 0, 0, null);
		positionX = mouesePoint.x-27;
		positionY = mouesePoint.y-21;
		g2d.dispose();
	}
	
	
	
	/**
	 * 重绘方法  将主画板的内容绘制在窗口中
	 */
	@Override
	public void paint(Graphics g) {
		try {
			super.paint(g);
			g.clearRect(0, 0, gameMapWidth, gameMapHeight);
			
			g.drawImage(guidelinesCanvas, 0, 0, this);//画地形
			g.drawImage(mainInterface, 0, 0, this);//画场景内物品
			g.drawImage(canvasFirst, 0, 0, this);//画指令框和移动线
			g.drawImage(mouseCursorImage, positionX, positionY, this);//画鼠标
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	
}

package redAlert;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import javax.swing.SwingUtilities;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.util.FPSAnimator;

import redAlert.enums.MouseStatus;
import redAlert.militaryBuildings.AfWeap;
import redAlert.other.Mouse;
import redAlert.other.MouseCursorObject;
import redAlert.other.MoveLine;
import redAlert.other.Place;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.task.ShapeUnitCalculateTask;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utilBean.MovePlan;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.DrawableUtil;
import redAlert.utils.PointUtil;
import redAlert.utils.TmpFileReader;

/**
 * 游戏场景界面
 * 基于OpenGL渲染的游戏场景画板
 *
 */
public class MainPanel extends GLJPanel{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 自身引用
	 */
	public MainPanel myself;
	/**
	 * 临时画板   最终将移除此画板,但是现在还没改完
	 */
	public BufferedImage canvas = new BufferedImage(SysConfig.viewportWidth,SysConfig.viewportHeight,BufferedImage.TYPE_INT_ARGB);
	
	
	/**
	 * 执行画板初始化
	 */
	public MainPanel() {
		
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capabilities = new GLCapabilities(profile);
		this.setRequestedGLCapabilities(capabilities);
		PanelGlListener listener = new PanelGlListener(this);//处理页面渲染的
		this.addGLEventListener(listener);
		
		
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
		
		FPSAnimator animator = new FPSAnimator(this, RuntimeParameter.fps, true);
	    SwingUtilities.invokeLater(new Runnable() {
	    	public void run() {
	    		animator.start(); // 开始动画线程
	    	}
	    });
		
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
	 *  绘制地形terrain
	 *
	 *  有地形画地形
	 *  没地形画网格
	 */
	public void drawTerrain(GLAutoDrawable drawable,int viewportOffX,int viewportOffY) {
		
		
		
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
			
			DrawableUtil.drawOneImgAtPosition(drawable, canvas, 0, 0, 0, 0);
			
		}else {
			CanvasPainter.drawGuidelines(canvas, viewportOffX, viewportOffY);//辅助线网格
			
			DrawableUtil.drawOneImgAtPosition(drawable, canvas, 0, 0, 0, 0);
		}
	}
	
	
	
	/**
	 * 画板绘制线程会不停调用此方法,从绘制队列中拿取方块(ShapeUnit),绘制到主画板上
	 * 绘制完毕后,会把方块再放入SHP方块阻塞队列,由方块帧计算线程计算下一帧,从而实现游戏画面循环
	 *
	 * 其中调用repaint方法后,系统SWT线程会稍后更新JPanel中显示的内容
	 */
	public void drawMainInterface(GLAutoDrawable drawable,int viewportOffX,int viewportOffY) {
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
						}else {
							DrawableUtil.drawOneShpAtPosition(drawable, shp, viewportOffX, viewportOffY);
							RuntimeParameter.addUnitToQueue(shp);//放回规划队列
						}
					}else{//子建筑
						
						DrawableUtil.drawOneShpAtPosition(drawable, shp, viewportOffX, viewportOffY);
						RuntimeParameter.addUnitToQueue(shp);//放回规划队列
					}
				}else {
					
					if(shp.isVisible()) {
						
						DrawableUtil.drawOneShpAtPosition(drawable, shp, viewportOffX, viewportOffY);
						
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
								
								DrawableUtil.drawMoveLine(drawable, startViewX, startViewY, endxViewX, endxViewY);
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
	public void drawMouseCursor(GLAutoDrawable drawable) {
		
		Point mousePoint = myself.getMousePosition();
		if(mousePoint!=null) {
			MouseCursorObject cursor = Mouse.getMouseCursor(RuntimeParameter.mouseStatus);
			int positionX = mousePoint.x-cursor.getOffX();
			int positionY = mousePoint.y-cursor.getOffY();
			DrawableUtil.drawOneSufAtPosition(drawable, cursor.getMouse(), positionX, positionY,0,0);
		}
	}
	
	/**
	 * 画建造菱形块的方法
	 */
	public void drawRhombus(GLAutoDrawable drawable,int viewportOffX,int viewportOffY) {
		
		if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {
			Point mousePoint = myself.getMousePosition();
			if(mousePoint!=null) {
				Coordinate coord = CoordinateUtil.getCoordinate(mousePoint.x, mousePoint.y);
				CenterPoint centerPoint = coord.getCenterPoint();
				
				int fxNum = MouseEventDeal.constName.fxNum;
				int fyNum = MouseEventDeal.constName.fyNum;
				
				drawRhombus(drawable, centerPoint, fxNum, fyNum, viewportOffX, viewportOffY);
			}
		}
		
	}
	
	/**
	 * 画建筑建造预占地方块
	 * centerPoint 菱形中心点
	 * int fxNum 从西南向东北数占几个菱形（此方向即纺射X轴）
	 * int fyNum 从东南向西北数占几个菱形（此方向即仿射Y轴）
	 */
	private void drawRhombus(GLAutoDrawable drawable,CenterPoint centerPoint,int fxNum,int fyNum,int viewportOffX,int viewportOffY) {
		if(fxNum==1 && fyNum==1) {
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
		}
		if(fxNum==2 && fyNum==2) {//发电厂 间谍卫星
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==3) {//核弹井 维修厂 天气控制 矿石精炼
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
		}
		if(fxNum==2 && fyNum==3) {//兵营 空指部 实验室
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==4) {//矿场 超时空
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
			
			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==4 && fyNum==4) {//基地  船坞
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
			
			drawRhombus(drawable,centerPoint.getLeft().getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn().getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn().getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==5) {//建设工厂
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
			
			drawRhombus(drawable,centerPoint.getLeft().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
		}
	}
	
	/**
	 * 画最小单位菱形块
	 */
	private void drawRhombus(GLAutoDrawable drawable,CenterPoint centerPoint,int viewportOffX,int viewportOffY) {
		ShapeUnitFrame suf = null;
		if(!centerPoint.isBuildingCanPutOn()) {
			suf = Place.getRedRect();
		}else {
			suf = Place.getGreenRect();
		}
		
		int positionX = centerPoint.getX()-30;
		int positionY = centerPoint.getY()-14;
		
		DrawableUtil.drawOneSufAtPosition(drawable, suf, positionX, positionY, viewportOffX, viewportOffY);
	}
	
	/**
	 * 画选择框(按下鼠标后拖动呈现的白色选择框)
	 */
	public void drawSelectRect(GLAutoDrawable drawable) {
		
		if(RuntimeParameter.mouseStatus == MouseStatus.Select) {
			int pressX = RuntimeParameter.pressX;
			int pressY = RuntimeParameter.pressY;
			Point mousePoint = myself.getMousePosition();
			if(mousePoint!=null) {
				int endMouseX = mousePoint.x;
				int endMouseY = mousePoint.y;
				DrawableUtil.drawLine(drawable, pressX, pressY, endMouseX, pressY);
				DrawableUtil.drawLine(drawable, endMouseX, pressY, endMouseX, endMouseY);
				DrawableUtil.drawLine(drawable, endMouseX, endMouseY, pressX, endMouseY);
				DrawableUtil.drawLine(drawable, pressX, endMouseY, pressX, pressY);
			}
		}
	}
	
}

/**
 * JOGL负责渲染画面
 */
class PanelGlListener implements GLEventListener{

	
	public MainPanel panel = null;
	
	public PanelGlListener(MainPanel panel) {
		this.panel = panel;
	}
	
	/**
	 * OpenGL上下文初始化工作
	 */
	@Override
	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glEnable(GL2.GL_TEXTURE_2D);//开启纹理
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置glClear函数调用时覆盖颜色缓冲区的颜色值
		gl.glEnable(GL2.GL_BLEND);//启用颜色混合功能
		gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);//表示使用源颜色的alpha值来作为因子  表示用1.0减去源颜色的alpha值来作为因子
		gl.glLoadIdentity();//重置当前指定的矩阵为单位矩阵,与glMatrixMode函数一起调用
		gl.glOrtho(0, SysConfig.viewportWidth, SysConfig.viewportHeight, 0, 1, -1);//坐标系统的设置 X方向从左到右  Y方向从上到下
		gl.glMatrixMode(GL2.GL_MODELVIEW);//对模型视景矩阵堆栈应用随后的矩阵操作
	}

	
	
	/**
	 * 渲染每一帧的画面  由此方法实现
	 */
	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置glClear函数调用时覆盖颜色缓冲区的颜色值
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);//清除颜色缓冲区和深度缓冲区
		
        //获取视口偏移,由于这两个变量变化频繁,所以需要获取一个快照,否则移动视口内容会抖动
		int theSightOffX = RuntimeParameter.viewportOffX;
		int theSightOffY = RuntimeParameter.viewportOffY;
		
		//绘制地形（地形的代码块覆盖全图,所以就不用重新清空画板了）
		panel.drawTerrain(drawable,theSightOffX,theSightOffY);
		//绘制游戏内的ShapeUnit
		panel.drawMainInterface(drawable,theSightOffX,theSightOffY);
		//绘制预建造菱形红绿块
		panel.drawRhombus(drawable,theSightOffX,theSightOffY);
		//绘制选择框
		panel.drawSelectRect(drawable);
		//绘制鼠标指针
		panel.drawMouseCursor(drawable);
		
		RuntimeParameter.frameCount++;
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
	
	}
	@Override
	public void dispose(GLAutoDrawable drawable) {
	
	}
}

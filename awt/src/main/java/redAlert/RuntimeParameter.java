package redAlert;

import java.util.PriorityQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

import redAlert.enums.MouseStatus;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.CenterPoint;

/**
 * 运行时参数
 * 游戏过程中会不断变化的参数
 */
public class RuntimeParameter {
	/**
	 * 视口在地图上偏移量
	 * 视野偏移量,用于确认渲染的范围
	 */
	public static int viewportOffX = 0;
	public static int viewportOffY = 0;
	/**
	 * 帧率
	 * 
	 * 红警2游戏速度5时帧率为60帧/秒
	 */
	public static int fps = 60;
	/**
	 * SHP方块阻塞队列
	 * 新增的ShapeUnit都需要先放入此队列,再由规划线程计算渲染次序
	 * 注意:此队列只用于渲染画面,不参与游戏的逻辑运算,逻辑运算由资源中心负责处理
	 * 为什么用阻塞队列:
	 *   当队列中的元素处理完毕放入规划队列时，此时会阻塞，避免建筑规划线程不停的轮询，减少CPU占用
	 */
	public static ArrayBlockingQueue<ShapeUnit> shapeUnitBlockingQueue = new ArrayBlockingQueue<ShapeUnit>(150);
	
	/**
	 * 缓存队列中添加方块
	 */
	public static void addUnitToQueue(ShapeUnit shapeUnit) {
		shapeUnitBlockingQueue.add(shapeUnit);
	}
	
	/**
	 * SHP方块规划队列1
	 * 当buildingFlag=偶数  此队列为绘制队列
	 * 当buildingFlag=奇数  此队列为缓存队列
	 */
	private static PriorityQueue<ShapeUnit> unitList = new PriorityQueue<ShapeUnit>(150);
	/**
	 * SHP方块规划队列2
	 * 两个建筑队列,一个用于绘制画面时，另一个用于缓存下一帧画面
	 * 当buildingFlag=奇数  此队列为缓存队列
	 * 当buildingFlag=偶数  此队列为绘制队列
	 */
	private static PriorityQueue<ShapeUnit> unitList2 = new PriorityQueue<ShapeUnit>(150);
	
	/**
	 * SHP方块队列标识
	 * 用于决定使用1.2哪个队列
	 */
	public static AtomicInteger queueFlag = new AtomicInteger(0);
	/**
	 * 0表示空闲  1表示正在使用缓存队列
	 * 为使用缓存队列而设计的CAS锁
	 * 拿到锁才能向缓存队列中添加单位或切换队列身份
	 */
	public static AtomicInteger casLock = new AtomicInteger(0);
	/**
	 * 获取绘制队列
	 */
	public static PriorityQueue<ShapeUnit> getDrawShapeUnitList() {
		if(queueFlag.get()%2==0) {
			return unitList;
		}else {
			return unitList2;
		}
	}
	/**
	 * 获取缓存队列
	 */
	public static PriorityQueue<ShapeUnit> getCacheShapeUnitList() {
		if(queueFlag.get()%2==0) {
			return unitList2;
		}else {
			return unitList;
		}
	}
	
	/**
	 * 向缓存队列中添加建筑
	 */
	public static void addBuildingToQueue(ShapeUnit unit) {
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
	 * 上次鼠标指针在MainPanel的中心点
	 */
	public static CenterPoint lastMoveCenterPoint = null;
	/**
	 * 上次鼠标指针在MainPanel的坐标
	 */
	public static int lastMoveX,lastMoveY;
	/**
	 * 帧计数
	 */
	public static long frameCount = 0;
	/**
	 * 鼠标在游戏场景界面按下时坐标
	 * 触发mousePressed事件时更新
	 */
	public static int pressX=0,pressY=0;
	/**
	 * 当前鼠标状态
	 */
	public static MouseStatus mouseStatus = MouseStatus.Idle;
}

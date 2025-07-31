package redAlert.task;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.FrameCalculateThread;

/**
 * 方块帧计算调度任务
 * 
 * 由此类管理的帧计算线程进行逻辑计算,计算后,再由场景渲染任务根据计算结果绘制画面
 * 
 * 也可以说是游戏场景内物品计算任务
 */
public class ShapeUnitCalculateTask extends Thread{

	
	/**
	 * 方块(ShapeUnit)帧计算线程数量
	 */
	public int workerNum = 10;
	/**
	 * 方块(ShapeUnit)帧计算线程队列
	 */
	public ArrayBlockingQueue<Runnable> workersQueue = new ArrayBlockingQueue<Runnable>(workerNum);
	/**
	 * 线程池中的阻塞队列
	 * 几乎无实际作用
	 */
	public ArrayBlockingQueue<Runnable> abq = new ArrayBlockingQueue<>(3);
	/**
	 * 方块(ShapeUnit)帧计算线程池
	 */
    public ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(workerNum,workerNum+5,60, TimeUnit.SECONDS,abq,new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * SHP方块阻塞队列的引用
     */
    public ArrayBlockingQueue<ShapeUnit> shapeUnitBlockingQueue = null;
    
    
    /**
     * 
     */
    public ShapeUnitCalculateTask(ArrayBlockingQueue<ShapeUnit> shapeUnitBlockingQueue) {
    	this.shapeUnitBlockingQueue = shapeUnitBlockingQueue;
    	initCalculateQueue();
    }
    
    /**
     * 初始化方块帧计算线程队列
     * 
     * 生成若干个方块帧计算线程worker
     * worker循环使用,方块的逻辑计算结束后再放回workersQueue
     *  
     */
    public void initCalculateQueue() {
		
		for(int i=0;i<workerNum;i++) {
			FrameCalculateThread worker = new FrameCalculateThread(workersQueue);
			workersQueue.offer(worker);
		}
    }
    
    /**
     * 启动方法
     */
    public void startCalculateTask() {
    	this.start();
    }

    /**
     * 调度线程启动
     * 
     * 调度线程监测SHP方块阻塞队列,若发现有方块计算任务,则取出;若没有,则阻塞
     * 调度线程若发现帧计算线程队列中有空闲的worker,则分配worker进行计算
     * 若没有空闲的worker则阻塞直到有空闲的worker
     */
	@Override
	public void run() {
		while(true) {
			try {
				ShapeUnit shp = shapeUnitBlockingQueue.take();//当队列为空时,会阻塞,降低CPU占用
				FrameCalculateThread worker = (FrameCalculateThread)workersQueue.take();//当没有空闲的帧计算线程时,会阻塞
				worker.setShp(shp);//分配任务
				threadPoolExecutor.execute(worker);
			}catch(Exception e) {
				e.printStackTrace();
				continue;
			}
		}
	}
    
    
}

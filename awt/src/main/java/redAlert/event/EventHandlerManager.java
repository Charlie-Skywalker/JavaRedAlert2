package redAlert.event;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 事件管理器
 */
public class EventHandlerManager {

	/**
	 * 事件发布池
	 */
	public static ArrayBlockingQueue<RaEvent> eventBlockingQueue = new ArrayBlockingQueue<RaEvent>(10);
	
	/**
	 * 处理事件的线程池
	 */
	public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	
	/**
	 * 发布一个红警事件
	 */
	public static void publishOneEvent(RaEvent raEvent) {
		eventBlockingQueue.offer(raEvent);
	}
	
	/**
	 * 初始化红警事件管理器
	 */
	public static void init() {
		new Thread(()->{
			while(true) {
				try {
					RaEvent raEvent = eventBlockingQueue.take();
					if(raEvent instanceof ConstructEvent) {//建筑建造事件
						ConstructEvent constEvent = (ConstructEvent)raEvent;
						ConstructEventHandler handler = new ConstructEventHandler(constEvent);
						threadPoolExecutor.execute(handler);
						continue;
					}
					if(raEvent instanceof ConstIconClickEvent) {
						ConstIconClickEvent iconClickEvent = (ConstIconClickEvent)raEvent;
						ConstIconClickEventHandler handler = ConstIconClickEventHandler.getInstance(iconClickEvent);
						threadPoolExecutor.execute(handler);
						continue;
					}
					
					
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		}).start();
	}
	
	
}

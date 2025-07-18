package redAlert.utilBean;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import redAlert.MainPanel;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.ShapeUnit;

/**
 * 方块(ShapeUnit)帧计算线程
 */
public class FrameCalculateThread implements Runnable{
	
	
	public ArrayBlockingQueue<Runnable> threadQueue;
	public ShapeUnit shp = null;
	public MainPanel mp = null;
	public List<ShapeUnit> shapeUnitQueryList;
	
	
	
	public FrameCalculateThread(MainPanel mp,ArrayBlockingQueue<Runnable> threadQueue) {
		this.mp = mp;
		this.threadQueue = threadQueue;
	}
	
	public void setShp(ShapeUnit shp) {
		this.shp = shp;
	}

	@Override
	public void run() {
		try {
			if(shp instanceof AfWeap) {
				AfWeap afweap = (AfWeap)shp;
				//解决正在建造车辆的问题
				/*
				 * 子建筑  只用一次
				 */
				if(afweap.isPartOfWeap()) {
					afweap.setEnd(true);
					threadQueue.offer(this);
					return;
				}
				if(afweap.isPartOfWeap()) {
					afweap.setEnd(true);
					threadQueue.offer(this);
					return;
				}
				
				/**
				 * 用主建筑来产生两个子建筑
				 * 主建筑不再绘制,而两个子建筑代替主建筑的渲染
				 */
				if(!afweap.isPartOfWeap()) {
					if(afweap.isMakingVehicle()) {
						List<Building> ls = afweap.tankChaifen();
						for(Building building : ls) {
							mp.addBuildingToQueue(building);
						}
						afweap.setPutChildIn(true);
					}else if(afweap.isMakingFly()){
						List<Building> ls = afweap.flyChaifen();
						for(Building building : ls) {
							mp.addBuildingToQueue(building);
						}
						afweap.setPutChildIn(true);
					}else {
						afweap.setPutChildIn(false);
					}
					
					
					shp.setFrameNum(shp.getFrameNum()+1);
					if(shp.getFrameNum()%shp.getFrameSpeed()==0) {
						shp.calculateNextFrame();
					}
					
					mp.addBuildingToQueue(afweap);
				}
				
			}else {
				
				if(shp.isEnd()) {
					ShapeUnitResourceCenter.removeOneUnit(shp);
				}else {
					//帧率控制  不同的方块有不同的帧率
					shp.setFrameNum(shp.getFrameNum()+1);
					
					if(shp.getFrameNum()%shp.getFrameSpeed()==0) {
						shp.calculateNextFrame();
					}
					mp.addBuildingToQueue(shp);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
		threadQueue.offer(this);
		
	}
	
	
	
}

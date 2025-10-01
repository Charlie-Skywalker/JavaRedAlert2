package redAlert.resourceCenter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import redAlert.Constructor;
import redAlert.GlobalConfig;
import redAlert.OptionsPanel;
import redAlert.enums.BuildingAreaType;
import redAlert.enums.SoldierEnum;
import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfWeap;
import redAlert.other.MoveLine;
import redAlert.other.OneDamage;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.PowerPlant;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Vehicle.EngineStatus;
import redAlert.shapeObjects.soldier.Adog;
import redAlert.shapeObjects.soldier.Engn;
import redAlert.shapeObjects.soldier.Gi;
import redAlert.shapeObjects.soldier.Sniper;
import redAlert.shapeObjects.soldier.Tany2;
import redAlert.shapeObjects.vehicle.GrizTank;
import redAlert.shapeObjects.vehicle.Ifv;
import redAlert.shapeObjects.vehicle.Sref;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utils.LittleCenterPointUtil;
import redAlert.utils.PointUtil;
import redAlert.utils.RandomUtil;

/**
 * 管理地图上资源的类
 * 
 * 这个资源不包括渲染相关   只处理对战的业务逻辑
 */
public class ShapeUnitResourceCenter {

	/**
	 * 选中、添加、删除移动单位时使用的锁
	 */
	public static ReentrantLock readLock = new ReentrantLock();
	
	/**
	 * 被指挥官鼠标选中的建筑
	 */
	public static Building selectedBuilding = null;
	/**
	 * 存放所有军事建筑的资源列表
	 */
	public static List<Building> buildingList = new ArrayList<>();
	/**
	 * 建筑操作锁
	 */
	public static ReentrantLock buildingLock = new ReentrantLock();
	/**
	 * 存放所有可以移动单位的资源列表
	 * 这个列表是专门用于游戏逻辑运算的,不参与渲染画面
	 * 游戏中新增单位、移除单位、单位血量变化等，都通过该列表处理
	 */
	public static List<MovableUnit> movableUnitQueryList = new ArrayList<>();
	/**
	 * 临时被选中的单位
	 */
	public static List<MovableUnit> selectedMovableUnits = new ArrayList<>();
	/**
	 * 移动线
	 * 移动线不是终点单位,不考虑线程同步
	 */
	public static List<MoveLine> moveLineUnitList = new ArrayList<>();
	/**
	 * 存放一些杂七杂八东西的
	 */
	public static List<ShapeUnit> shapeUnitList = new ArrayList<ShapeUnit>();
	/**
	 * 移动线锁
	 */
	public static ReentrantLock moveLineLock = new ReentrantLock();
	
	/**
	 * 添加一个军事建筑
	 */
	public static void addBuilding(Building building) {
		try {
			buildingLock.lock();
			if(!buildingList.contains(building)) {
				buildingList.add(building);
			}
			
			
			
			//=============处理电力相关逻辑================
			boolean beforePowerStatus = true;//true表示电力够  false表示电力不足
			if(powerGeneration>powerLoad) {
				beforePowerStatus = true;
			}else {
				beforePowerStatus = false;
			}
			calculatePowerInfo();
			boolean afterPowerStatus = true;
			if(powerGeneration>powerLoad) {
				afterPowerStatus = true;
			}else {
				afterPowerStatus = false;
			}
			
			if(!beforePowerStatus && afterPowerStatus) {
				//播放电力增强的声音
				Constructor.playOneMusic("gpowon");
				
				if(containsBuildingClass(AfAirc.class)) {
					OptionsPanel.radarLabel.triggleRadarShow();//雷达的显示屏监控放在这里肯定是不合适的  要放在雷达的监控器中  现在先放这里
				}
				
			}
			if(beforePowerStatus && !afterPowerStatus) {
				//播放电力不足的声音
				//low power
				Constructor.playOneMusic("gpowof");
				Constructor.playOneMusic("ceva053");//lowpower
				
				if(containsBuildingClass(AfAirc.class) && OptionsPanel.radarLabel.isPlayed) {
					OptionsPanel.radarLabel.turnOffRadarShow();
				}
			}
			//=============处理电力相关逻辑================
			
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	/**
	 * 移除一个军事建筑
	 */
	public static void removeOneBuilding(Building building) {
		try {
			buildingLock.lock();
			if(buildingList.contains(building)) {
				buildingList.remove(building);
			}
			
			//移除建筑的占地引用
			List<CenterPoint> areas = building.getNoConstCpList();
			for(CenterPoint cp:areas) {
				cp.setBuilding(null);
				cp.setBuildingAreaType(BuildingAreaType.None);
			}
			
			//=============处理电力相关逻辑================
			boolean beforePowerStatus = true;//true表示电力够  false表示电力不足
			if(powerGeneration>powerLoad) {
				beforePowerStatus = true;
			}else {
				beforePowerStatus = false;
			}
			calculatePowerInfo();
			boolean afterPowerStatus = true;
			if(powerGeneration>powerLoad) {
				afterPowerStatus = true;
			}else {
				afterPowerStatus = false;
			}
			
			if(!beforePowerStatus && afterPowerStatus) {
				//播放电力增强的声音
				Constructor.playOneMusic("gpowon");
			}
			if(beforePowerStatus && !afterPowerStatus) {
				//播放电力不足的声音
				//low power
				Constructor.playOneMusic("gpowof");
				Constructor.playOneMusic("ceva053");//lowpower
			}
			//=============处理电力相关逻辑================
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	
	
	/**
	 * 是否已建设某种建筑
	 */
	public static boolean containsBuildingClass(Class clazz) {
		try {
			buildingLock.lock();
			for(Building building: buildingList) {
				if(clazz.equals(building.getClass())) {
					return true;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
		return false;
	}
	
	/**
	 * 找到盟军基地  并释放夹箱子动画
	 */
	public static void exeCnstFetchAni() {
		try {
			buildingLock.lock();
			for(Building building: buildingList) {
				if(building.getClass().equals(AfCnst.class)) {
					AfCnst afCnst = (AfCnst)building;
					afCnst.setToFetchCrate(true);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	
	
	
	/**
	 * 选中一个建筑
	 */
	public static void selectOneBuilding(Building building) {
		try {
			buildingLock.lock();
			selectedBuilding = building;
			selectedBuilding.getBloodBar().setVisible(true);
			selectedBuilding.getBone().setVisible(true);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	/**
	 * 取消选中的建筑
	 */
	public static void unselectBuilding() {
		try {
			buildingLock.lock();
			if(selectedBuilding!=null) {
				selectedBuilding.getBloodBar().setVisible(false);
				selectedBuilding.getBone().setVisible(false);
				selectedBuilding = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	
	/**
	 * 获取选中矩形框内的所有可移动的单位
	 */
	public static List<MovableUnit> getMovableUnitFromWarMap(int startx,int starty,int endx,int endy){
		int x1,x2,y1,y2;
		if(startx<endx) {
			x1 = startx;x2=endx;
		}else {
			x1 = endx;x2 = startx;
		}
		if(starty<endy) {
			y1 = starty;y2 = endy;
		}else {
			y1 = endy;y2 = starty;
		}
		
		List<MovableUnit> units = new ArrayList<>();
		try {
			readLock.lock();
			Iterator<MovableUnit> iterator = movableUnitQueryList.iterator();
			while(iterator.hasNext()) {
				MovableUnit shapeUnit = iterator.next();
				if(shapeUnit!=null) {
					int centerX = shapeUnit.getCenterOffX()+shapeUnit.getPositionX();
					int centerY = shapeUnit.getCenterOffY()+shapeUnit.getPositionY();
					
					if(ifIn(x1,y1,x2,y2,centerX,centerY)) {
						units.add((MovableUnit)shapeUnit);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
		return units;
	}
	/**
	 * 向选中单位列表中添加单位
	 */
	public static List<MovableUnit> addAll(List<MovableUnit> newSelect){
		try {
			readLock.lock();
			for(MovableUnit newUnit:newSelect) {
				if(!selectedMovableUnits.contains(newUnit)) {
					selectedMovableUnits.add(newUnit);
					newUnit.getBloodBar().setVisible(true);
				}
			}
			return selectedMovableUnits;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
		
		return selectedMovableUnits;
	}

	/**
	 * 选中单位列表是否为空
	 */
	public static boolean isSelectedUnitsEmpty() {
		try {
			readLock.lock();
			return selectedMovableUnits.isEmpty();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
		return true;
	}
	
	/**
	 * 添加一个可移动单位
	 */
	public static void addMovableUnit(MovableUnit unit) {
		try {
			readLock.lock();
			if(!movableUnitQueryList.contains(unit)) {
				movableUnitQueryList.add(unit);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 从兵营添加一个步兵
	 */
	public static void addSoldierFromPile(SoldierEnum soldier) {
		//找到兵营
		try {
			readLock.lock();
			for(Building building: buildingList) {
				if(AfPile.class.equals(building.getClass())) {
					int x = building.getPositionX()+building.getCenterOffX();
					int y = building.getPositionY()+building.getCenterOffY();
					CenterPoint cp = PointUtil.getCenterPoint(x, y);
					CenterPoint bornCp = cp.getLeft();
					
					LittleCenterPoint bornLcp = bornCp.getDownLittleCenterPoint();
					LittleCenterPoint targetMoveLcp = bornCp.getLeftDn().getLeftDn().getLeftLittleCenterPoint();
					LittleCenterPoint target = LittleCenterPointUtil.findSoldierCanOnLcpNearBy(targetMoveLcp);
					
					
					if(soldier==SoldierEnum.AfSnip) {
						Sniper sniper = new Sniper(bornLcp,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(sniper);
						addMovableUnit(sniper);
						sniper.moveToTarget(target);
					}
					if(soldier==SoldierEnum.AfGi) {
						Gi gi = new Gi(bornLcp,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(gi);
						addMovableUnit(gi);
						gi.moveToTarget(target);
					}
					if(soldier==SoldierEnum.Engn) {
						Engn engn = new Engn(bornLcp,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(engn);
						addMovableUnit(engn);
						engn.moveToTarget(target);
					}
					if(soldier==SoldierEnum.AfAdog) {
						Adog adog = new Adog(bornLcp,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(adog);
						addMovableUnit(adog);
						adog.moveToTarget(target);
					}
					if(soldier==SoldierEnum.AfTany) {
						Tany2 tany = new Tany2(bornLcp,GlobalConfig.unitColor);
						Constructor.playOneMusic("itanatb");//哈哈哈哈哈哈哈
						Constructor.putOneShapeUnit(tany);
						addMovableUnit(tany);
						tany.moveToTarget(target);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 从战车工厂添加一个载具
	 */
	public static void addVehicleFromWeap(VehicleEnum vehicle) {
		try {
			readLock.lock();
			for(Building building: buildingList) {
				if(AfWeap.class.equals(building.getClass())) {
					int x = building.getPositionX()+building.getCenterOffX();
					int y = building.getPositionY()+building.getCenterOffY();
					CenterPoint bornCp = PointUtil.getCenterPoint(x, y);
					
					CenterPoint targetMoveLcp = bornCp.getRightDn().getRightDn().getRightDn().getRightDn();
					CenterPoint target = PointUtil.findVehicleCanOnCpNearBy(targetMoveLcp);
					System.out.println("初次坐标"+target);
					
					
					if(vehicle==VehicleEnum.AfGtnk) {
						AfWeap weap = (AfWeap)building;
						weap.setMakingVehicle(true);//正在生产坦克
						
						GrizTank gtank = new GrizTank(bornCp.getX()-64,bornCp.getY()-64,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(gtank);
//						addMovableUnit(gtank);
						gtank.moveToTarget(target);
						
						while(true) {
							CenterPoint tankCp = gtank.getCurCenterPoint();
							Thread.sleep(5);
							if(!target.isVehicleCanOn() || gtank.engineStatus==EngineStatus.Stopped) {
								System.out.println("二次坐标"+target);
								target = PointUtil.findVehicleCanOnCpNearBy(targetMoveLcp);
								gtank.moveToTarget(target);
								
							}
							if(!tankCp.equals(bornCp) && !tankCp.equals(bornCp.getRightDn()) && !tankCp.equals(bornCp.getRightDn().getRightDn())) {
								break;
							}
						}
						weap.setMakingVehicle(false);
					}
					
					if(vehicle==VehicleEnum.AfIfv) {
						AfWeap weap = (AfWeap)building;
						weap.setMakingVehicle(true);//正在生产坦克
						
						Ifv fv = new Ifv(bornCp.getX()-64,bornCp.getY()-64,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(fv);
//						addMovableUnit(fv);
						fv.moveToTarget(target);
						
						while(true) {
							CenterPoint tankCp = fv.getCurCenterPoint();
							Thread.sleep(0);
							if(!tankCp.equals(bornCp) && !tankCp.equals(bornCp.getRightDn()) && !tankCp.equals(bornCp.getRightDn().getRightDn())) {
								break;
							}
						}
						weap.setMakingVehicle(false);
					}
					
					if(vehicle==VehicleEnum.AfSref) {
						AfWeap weap = (AfWeap)building;
						weap.setMakingVehicle(true);//正在生产坦克
						
						Sref sref = new Sref(bornCp.getX()-64,bornCp.getY()-64,GlobalConfig.unitColor);
						Constructor.putOneShapeUnit(sref);
//						addMovableUnit(sref);
						sref.moveToTarget(target);
						Constructor.randomPlayOneMusic(new String[] {"vpristaa","vpristab","vpristac"});
						
						while(true) {
							CenterPoint tankCp = sref.getCurCenterPoint();
							Thread.sleep(0);
							if(!tankCp.equals(bornCp) && !tankCp.equals(bornCp.getRightDn()) && !tankCp.equals(bornCp.getRightDn().getRightDn())) {
								break;
							}
						}
						weap.setMakingVehicle(false);
					}
					
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
	}
	
	/**
	 * 移除一个可移动单位
	 */
	public static void removeOneMovableUnit(MovableUnit unit) {
		try {
			readLock.lock();
			if(movableUnitQueryList.contains(unit)) {
				movableUnitQueryList.remove(unit);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			readLock.unlock();
		}
	}
	
	
	/**
	 * 添加一个单位
	 */
	public static void addUnit(ShapeUnit unit) {		
		if(unit instanceof MoveLine) {
			addOneMoveLine((MoveLine)unit);
		}
		else if(unit instanceof MovableUnit) {
			addMovableUnit((MovableUnit)unit);
		}
		else {
			try {
				readLock.lock();
				if(!shapeUnitList.contains(unit)) {
					shapeUnitList.add(unit);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				readLock.unlock();
			}
		}
	}
	/**
	 * 移除一个单位
	 */
	public static void removeOneUnit(ShapeUnit unit) {
		if(unit instanceof MoveLine) {
			removeOneMoveLine((MoveLine)unit);
		}
		else if(unit instanceof MovableUnit) {
			removeOneMovableUnit((MovableUnit)unit);
		}
		else if(unit instanceof Building) {
			removeOneBuilding((Building)unit);
		}
		
		else {
			try {
				readLock.lock();
				if(shapeUnitList.contains(unit)) {
					shapeUnitList.remove(unit);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				readLock.unlock();
			}
		}
	}
	
	
	
	/**
	 * 单选一个单位
	 */
	public static void selectOneUnit(MovableUnit movableUnit) {
		synchronized (readLock) {
			cancelSelect();
			selectedMovableUnits.add(movableUnit);
			movableUnit.getBloodBar().setVisible(true);
		}
	}
	
	
	/**
	 * 取消所有选中
	 */
	public static void cancelSelect(){
		synchronized (readLock) {
			Iterator<MovableUnit> iterator = selectedMovableUnits.iterator();
			while(iterator.hasNext()) {
				MovableUnit movableUnit = iterator.next();
				if(movableUnit.getBloodBar()!=null){
					movableUnit.getBloodBar().setVisible(false);
				}
				iterator.remove();
			}
		}
	}
	
	/**
	 * 判断一个点是否在矩形内
	 */
	public static boolean ifIn(int startx,int starty,int endx,int endy,int centerX,int centerY) {
		if(centerX>=startx && centerX<=endx && centerY>=starty && centerY<=endy) {
			return true;
		}else {
			return false;
		}
	}
	/**
	 * 添加移动线
	 */
	public static void addOneMoveLine(MoveLine moveLine) {
		try {
			moveLineLock.lock();
			moveLineUnitList.add(moveLine);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			moveLineLock.unlock();
		}
	}
	
	/**
	 * 删除移除所有移动线
	 */
	public static void removeAllMoveLine() {
		try {
			moveLineLock.lock();
			for(MoveLine movLine:moveLineUnitList) {
				movLine.setVisible(false);
				movLine.setEnd(true);
			}
			moveLineUnitList.clear();
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			moveLineLock.unlock();
		}
	}
	/**
	 * 移除一条移动线
	 */
	public static void removeOneMoveLine(MoveLine moveLine) {
		try {
			moveLineLock.lock();
			moveLine.setVisible(false);
			moveLine.setEnd(true);
			moveLineUnitList.remove(moveLine);
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			moveLineLock.unlock();
		}
	}
	
	/**
	 * 造成的伤害的阻塞队列  
	 * 造成的伤害将放入此队列中   由特定的线程处理伤害
	 * 
	 */
	public static ArrayBlockingQueue<OneDamage> damageBlockingQueue = new ArrayBlockingQueue<OneDamage>(50);
	/**
	 * TODO 负责计算掉血情况的的线程
	 */
	public static void startDamageCalculate() {
		
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
						OneDamage damage = damageBlockingQueue.take();
						damage.settle();
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
	 * 发电量
	 */
	public static int powerGeneration = 0;
	/**
	 * 电力负载
	 */
	public static int powerLoad = 0;
	/**
	 * 是否有电
	 */
	public static boolean isPowerOn = true;
	
	/**
	 * 计算发电量与负载
	 */
	public static void calculatePowerInfo() {
		try {
			buildingLock.lock();
			int powerGenerationCopy = 0;
			int powerLoadCopy = 0;
			for(Building building: buildingList) {
				if(building instanceof PowerPlant) {
					PowerPlant pp =	(PowerPlant)building;
					powerGenerationCopy+=pp.getPowerGeneration();
				}else {
					powerLoadCopy+=building.getConstConfig().powerLoad;
				}
			}
			powerLoad = powerLoadCopy;
			powerGeneration = powerGenerationCopy;
			if(powerLoad>powerGeneration) {
				isPowerOn = false;
			}else {
				isPowerOn = true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			buildingLock.unlock();
		}
	}
	
}

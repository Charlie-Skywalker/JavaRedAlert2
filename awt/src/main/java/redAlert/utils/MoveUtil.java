package redAlert.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import redAlert.Constructor;
import redAlert.other.MoveLine;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.shapeObjects.Vehicle;
import redAlert.shapeObjects.Vehicle.EngineStatus;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utilBean.MovePlan;

/**
 * 有关单位移动的工具类
 */
public class MoveUtil {

	
	/**
	 * 控制群体移动的方法，先不解决平移后的位置不可抵达的问题
	 * movableUnits 选中单位
	 * targetCp 鼠标点击确定的目标点
	 *
	 * 先找到单位群中最靠近中心的单位，以它的中心点作为中心，对其他单位平移至目标中心点
	 *
	 */
	public static void move(List<MovableUnit> movableUnits,int targetX,int targetY) {
		MovableUnit centerUnit = getCenterMovableUnit(movableUnits);//找到中心的单位
		CenterPoint center = centerUnit.getCurCenterPoint();//单位群中心点坐标
		
		List<CenterPoint> all = new ArrayList<>();//平移后的点阵群
		List<LittleCenterPoint> allLcp = new ArrayList<>();//平移后的点阵群
		CenterPoint targetCp = PointUtil.getCenterPoint(targetX, targetY);
		LittleCenterPoint targetLcp = LittleCenterPointUtil.getLittleCenterPoint(targetX, targetY);
		
		/*
		 * 平移算法
		 *
		 * 假设将单位都平移到目标地点，计算最终的移动点集合
		 * 载具计算载具的CenterPoint
		 * 步兵计算CenterPoint和LittleCenterPoint
		 * 如果平移到的地点不能放置步兵或载具  将在附近找可以使用的点
		 *
		 */
		List<MovePlan> movePlanLs = new ArrayList<>();
		Set <LittleCenterPoint> exceptLcps = new HashSet<>();//步兵不能将这些点作为平移点
		Set<CenterPoint> exceptCps = new HashSet<>();//载具不能将这些点作为平移点
		for(MovableUnit unit: movableUnits) {
			//根据实验结果,红警2中用的是中心点平移 而不是位于靠近中心的单位的平移
			int deltaX = unit.getCenterOffX()+unit.getPositionX()-center.getX();
			int deltaY = unit.getCenterOffY()+unit.getPositionY()-center.getY();
			int x1 = targetCp.getX()+deltaX;
			int y1 = targetCp.getY()+deltaY;
			CenterPoint transCp = PointUtil.getCenterPoint(x1, y1);
			LittleCenterPoint transLcp = LittleCenterPointUtil.getLittleCenterPoint(x1, y1);
			
			if(unit instanceof Soldier) {
				if(!transLcp.isSoldierCanOn()) {
					transLcp = LittleCenterPointUtil.findSoldierCanOnLcpNearBy(transLcp,exceptLcps);//如果平移到的目标点有单位,那就在附近找一个可用的点
					exceptLcps.add(transLcp);
					exceptCps.add(transLcp.getCenterPoint());
				}
			}
			if(unit instanceof Vehicle) {
				if(!transCp.isVehicleCanOn()) {
					transCp = PointUtil.findVehicleCanOnCpNearBy(transCp,exceptCps);//如果平移到的目标点有单位,那就在附近找一个可用的点
					exceptCps.add(transCp);
					exceptLcps.add(transCp.getLeftLittleCenterPoint());
					exceptLcps.add(transCp.getRightLittleCenterPoint());
					exceptLcps.add(transCp.getUpLittleCenterPoint());
					exceptLcps.add(transCp.getDownLittleCenterPoint());
				}
			}
			
			
			MovePlan plan = new MovePlan();
			plan.setTargetCp(transCp);
			plan.setUnit(unit);
			plan.setTargetLCP(transLcp);
			
			movePlanLs.add(plan);
			all.add(transCp);
			allLcp.add(targetLcp);
		}
		
		
		
		/*
		 * 聚合算法
		 *
		 * 因为平移到目标地点后,单位未聚合在一起,仍然是分散的,所以需要向中心聚合
		 *
		 * 这个写的一定有问题   某些特殊情况下可能会使单位重叠
		 */
		List<CenterPoint> norNeibors = PointUtil.getNorNeighborsCollection(all,targetCp);//未聚合在中心点区域的点集合
		List<CenterPoint> neibors = PointUtil.getNeighborsCollection(all,targetCp);//已经聚合在中心点区域的点集合
		for(CenterPoint lonelyCp:norNeibors) {
			CenterPoint movedCp = PointUtil.selectAndSelect(neibors, lonelyCp);//最短距离法
			
			for(MovePlan plan: movePlanLs) {
				if(plan.getTargetCp().equals(lonelyCp)) {
					plan.setTargetCp(movedCp);
					if(plan.getUnit() instanceof Soldier) {
						LittleCenterPoint minDisLcp = PointUtil.getMinDisLCP(lonelyCp.getX(), lonelyCp.getY(), movedCp);
						plan.setTargetLCP(minDisLcp);
					}
					neibors.add(movedCp);
				}
			}
		}
		
		//画多条移动线
		createManyMoveLine(movePlanLs);
		
		/*
		 * 向选中的载具发出停止指令
		 * 等载具们都停止移动后  再发出新的移动指令
		 */
		//发出停止指令
		for(MovePlan plan:movePlanLs) {
			MovableUnit unit = plan.getUnit();
			if(unit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle)unit;
				vehicle.stopFlag = true;
			}
		}
		
		//确认已停止
		while(true) {
			boolean allStop = true;
			for(MovePlan plan:movePlanLs) {
				MovableUnit unit = plan.getUnit();
				if(unit instanceof Vehicle) {
					Vehicle vehicle = (Vehicle)unit;
					if(vehicle.nextTarget!=null) {
						allStop = false;
						break;
					}
				}
			}
			if(allStop) {
				System.out.println("确认停止");
				break;
			}
		}
		//重置停止符号
		for(MovePlan plan:movePlanLs) {
			MovableUnit unit = plan.getUnit();
			if(unit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle)unit;
				vehicle.stopFlag = false;
			}
		}
		
		
		//发动机引擎启动
		for(MovePlan plan:movePlanLs) {
			MovableUnit unit = plan.getUnit();
			if(unit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle)unit;
				vehicle.setEngineStatus(EngineStatus.Started);
			}
		}
		
		//发出移动命令
		for(MovePlan plan:movePlanLs) {
			MovableUnit unit = plan.getUnit();
			CenterPoint cp = plan.getTargetCp();
			
			if(unit instanceof Vehicle) {
				unit.moveToTarget(cp);
			}
			if(unit instanceof Soldier) {
				LittleCenterPoint lcp = plan.getTargetLCP();
				Soldier s = (Soldier)unit;
				s.moveToTarget(lcp);
			}
		}
		
		
		
		
	}
	
	/**
	 * 单个单位移动
	 */
	public static void move(MovableUnit moveUnit,CenterPoint targetCp) {
		createOneMoveLine(targetCp,moveUnit);
		
		moveUnit.stopFlag = true;
		//确认已停止
		while(true) {
			if(moveUnit instanceof Vehicle) {
				Vehicle vehicle = (Vehicle)moveUnit;
				if(vehicle.nextTarget==null) {
					break;
				}
			}else {
				//步兵的以后再写
				break;
			}
		}
		//重置停止符号
		moveUnit.stopFlag = false;
		//发动机引擎启动  //发出移动命令
		if(moveUnit instanceof Vehicle) {
			Vehicle vehicle = (Vehicle)moveUnit;
			vehicle.setEngineStatus(EngineStatus.Started);
			moveUnit.moveToTarget(targetCp);
		}
		
		if(moveUnit instanceof Soldier) {
			LittleCenterPoint lcp = PointUtil.getMinDisLCP(moveUnit.getPositionX()+moveUnit.getCenterOffX(), moveUnit.getPositionY()+moveUnit.getCenterOffY(), targetCp);
			moveUnit.moveToTarget(lcp);
		}
	}
	
	/**
	 * 画一条移动线
	 */
	public static void createOneMoveLine(CenterPoint moveTarget,MovableUnit moveUnit) {
		MovePlan plan = new MovePlan();
		plan.setUnit(moveUnit);
		plan.setTargetCp(moveTarget);
		ArrayList<MovePlan> planList = new ArrayList<>();
		planList.add(plan);
		ShapeUnitResourceCenter.removeAllMoveLine();
		MoveLine moveLine = new MoveLine(planList);
		Constructor.putOneShapeUnit(moveLine);
	}
	/**
	 * 画多条移动线
	 */
	public static void createManyMoveLine(List<MovePlan> movePlans) {
		ShapeUnitResourceCenter.removeAllMoveLine();
		MoveLine moveLine = new MoveLine(movePlans);
		Constructor.putOneShapeUnit(moveLine);
	}
	
	
	
	/**
	 * 获取一群可移动单位中位于几何中心的单位
	 */
	public static MovableUnit getCenterMovableUnit(List<MovableUnit> units) {
		int xtotal = 0;
		int ytotal = 0;
		for(MovableUnit unit: units) {
			xtotal+= unit.getCenterOffX()+unit.getPositionX();
			ytotal+= unit.getCenterOffY()+unit.getPositionY();
		}
		int aveX = xtotal/units.size();
		int aveY = ytotal/units.size();
		
		MovableUnit centerUnit = null;
		int min = 99999999;
		
		for(MovableUnit unit: units) {
			int tx = unit.getCenterOffX()+unit.getPositionX();
			int ty = unit.getCenterOffY()+unit.getPositionY();
			
			int distance = Math.abs(tx-aveX)* Math.abs(tx-aveX)+ Math.abs(aveY-ty)*Math.abs(aveY-ty);
			if(distance<min) {
				min = distance;
				centerUnit = unit;
			}
		}
		return centerUnit;
	}
}

package redAlert.test;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utilBean.MovePlan;
import redAlert.utils.MoveUtil;
import redAlert.utils.PointUtil;

public class Atest1 {

	/**
	 * 获取一群可移动单位中位于几何中心的单位
	 */
	public static MovableUnit getCenterMovableUnit(List<MovableUnit> units) {
		int xtotal = 0;
		int ytotal = 0;
		for(MovableUnit unit: units) {
			xtotal+= unit.getCurCenterPoint().getX();
			ytotal+= unit.getCurCenterPoint().getY();
		}
		int aveX = xtotal/units.size();
		int aveY = ytotal/units.size();
		
		MovableUnit centerUnit = null;
		int min = 9999999;
		
		for(MovableUnit unit: units) {
			int tx = unit.getCenterOffX()+unit.getPositionX();
			int ty = unit.getCenterOffY()+unit.getPositionY();
			
			int distance = Math.abs(tx-aveX)+ Math.abs(aveY-ty);
			if(distance<min) {
				min = distance;
				centerUnit = unit;
			}
		}
		return centerUnit;
	}
	
	
	/**
	 * 重理思路  重写选点算法
	 */
	public static void test3(List<MovableUnit> movableUnits,CenterPoint targetCp) throws Exception{
		List<CenterPoint> vehicleCp = new ArrayList<>();
		List<LittleCenterPoint> soldierCp = new ArrayList<>();
		
		MovableUnit centerUnit = getCenterMovableUnit(movableUnits);
		CenterPoint center = centerUnit.getCurCenterPoint();
		
		List<CenterPoint> all = new ArrayList<>();
		/*
		 * 平移算法
		 *
		 * 假设将单位都整体平移到目标地点，计算最终的移动点集合
		 * 
		 */
		List<MovePlan> movePlanLs = new ArrayList<>();
		for(MovableUnit unit: movableUnits) {
			if(unit.equals(centerUnit)) {//中心点直接定在目标点
				MovePlan plan = new MovePlan();
				plan.setTargetCp(targetCp);
				plan.setUnit(unit);
				movePlanLs.add(plan);
				all.add(targetCp);
				continue;
			}
			
			int deltaX = unit.getCurCenterPoint().getX() - center.getX();
			int deltaY = unit.getCurCenterPoint().getY() - center.getY();
			
			int x1 = targetCp.getX()+deltaX;
			int y1 = targetCp.getY()+deltaY;
			CenterPoint transCp = PointUtil.getCenterPoint(x1, y1);
			
			MovePlan plan = new MovePlan();
			plan.setTargetCp(transCp);
			plan.setUnit(unit);
			movePlanLs.add(plan);
			all.add(transCp);
		}
		
		/**
		 * 聚合算法 
		 * 
		 * 因为平移到目标地点后,单位未聚合在一起,仍然是分散的,所以需要向中心聚合
		 */
		List<CenterPoint> norNeibors = PointUtil.getNorNeighborsCollection(all,targetCp);//未聚合在中心点区域的点集合
		List<CenterPoint> neibors = PointUtil.getNeighborsCollection(all,targetCp);//已经聚合在中心点区域的点集合
		for(CenterPoint lonelyCp:norNeibors) {
//			CenterPoint movedCp = PointUtil.walk(neibors, targetCp, ncp);//游走算法显然不靠谱
			CenterPoint movedCp = PointUtil.selectAndSelect(neibors, lonelyCp);//最短距离法
			
			for(MovePlan plan: movePlanLs) {
				if(plan.getTargetCp().equals(lonelyCp)) {
					plan.setTargetCp(movedCp);
					neibors.add(movedCp);
				}
			}
		}
		
		MoveUtil.createManyMoveLine(movePlanLs);
		for(MovePlan plan:movePlanLs) {
			MovableUnit unit = plan.getUnit();
			CenterPoint cp = plan.getTargetCp();
			if(unit instanceof Vehicle) {
				unit.moveToTarget(cp);
			}
			if(unit instanceof Soldier) {
				LittleCenterPoint lcp = PointUtil.getMinDisLCP(unit.getCenterOffX()+unit.getPositionX(), unit.getCenterOffY()+unit.getPositionY(), cp);
				unit.moveToTarget(lcp);
			}
		}
		
		
		
		
	}
	

	
	public static void main(String[] args) throws Exception{
		
//		String a = URLDecoder.decode("%e4%b8%ad%e6%96%87", "UTF-8");
//		System.out.println(a);
		
//		long a = 1;
//		int b = 1;
//		
//		long d = -1;
//		long a1 = a<<32;
//		long b1 = b;
//		
//		System.out.println(Integer.MAX_VALUE);
//		System.out.println(a1 | b1);
//		System.out.println(a1);
		int a = 1;
		long al = a;
		long b = al<<32;
		
		System.out.println(b);
	}
}

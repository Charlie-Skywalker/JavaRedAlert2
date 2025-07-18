package redAlert.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;

import redAlert.enums.Direction;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;

/**
 * 新的菱形格算法
 */
public class PointUtil {
	
	/**
	 * 中心点坐标缓存
	 * 所有的中心点,原则上应该从缓存中获取,避免新建
	 */
	public static Map<String,CenterPoint> centerPointMap = new HashMap<>();
	
	/**
	 * 小中心点缓存
	 */
	public static Map<String,LittleCenterPoint> littleCenterPointMap = new HashMap<>();
	
	static {
		initCenterPointCache();
	}
	/**
	 * 获取中心点坐标集合
	 * 中心点用的是右中心点
	 */
	public static void initCenterPointCache() {
		//一类中心点
		for(int m=0;m<50;m++) {
			int y = 15+30*m;
			for(int n=0;n<50;n++) {
				int x = 30+60*n;
				centerPointMap.put(x+","+y, new CenterPoint(x,y));
				
				littleCenterPointMap.put( (x-16)+","+y, new LittleCenterPoint(x-16,y,Direction.Left));
				littleCenterPointMap.put( (x+16)+","+y, new LittleCenterPoint(x+16,y,Direction.Right));
				littleCenterPointMap.put( x+","+(y-8), new LittleCenterPoint(x,y-8,Direction.Up));
				littleCenterPointMap.put( x+","+(y+8), new LittleCenterPoint(x,y+8,Direction.Down));
				
			}
		}
		
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 30*m;
			for(int n=0;n<50;n++) {
				int x = 60*n;
				centerPointMap.put(x+","+y, new CenterPoint(x,y));
				
				littleCenterPointMap.put( (x-16)+","+y, new LittleCenterPoint(x-16,y,Direction.Left));
				littleCenterPointMap.put( (x+16)+","+y, new LittleCenterPoint(x+16,y,Direction.Right));
				littleCenterPointMap.put( x+","+(y-8), new LittleCenterPoint(x,y-8,Direction.Up));
				littleCenterPointMap.put( x+","+(y+8), new LittleCenterPoint(x,y+8,Direction.Down));
			}
		}
	}
	/**
	 * 是否包含某个点
	 */
	public static boolean contains(CenterPoint p) {
		return centerPointMap.containsKey(p.getX()+","+p.getY());
	}
	/**
	 * 放入,以后可能会更改Key值
	 */
	public static void putIntoMap(CenterPoint p) {
		centerPointMap.put(p.getX()+","+p.getY(), p);
	}
	/**
	 * 判断一个坐标点是否是菱形中心点
	 */
	public static boolean isCenterPoint(int x,int y) {
		return centerPointMap.containsKey(x+","+y);
	}
	
	/**
	 * 获取小中心点
	 * 需要确认参数是小中心点坐标后才能使用
	 */
	public static LittleCenterPoint fetchLittleCenterPoint(int littleCenterX,int littleCenterY) {
		return littleCenterPointMap.get(littleCenterX+","+littleCenterY);
	}
	
	
	
	/**
	 * 找到一个中心点周围一个载具可进入的点   逐渐向外围搜索
	 * 这个点上不能有别的载具  这个是出兵用的
	 */
	public static CenterPoint findVehicleCanOnCpNearBy(CenterPoint cp) {
		ArrayDeque<CenterPoint> rest = new ArrayDeque<>();
		rest.add(cp);
		Set<CenterPoint> haveGet = new HashSet<>();
		haveGet.add(cp);
		
		CenterPoint result = null;
		while(!rest.isEmpty()) {
			CenterPoint start = rest.poll();
			if(start.isVehicleCanOn()) {
				return start;
			}
			
			List<CenterPoint> neighbors = PointUtil.getNeighbors(start);
			for(CenterPoint neighbor:neighbors) {
				if(neighbor.isVehicleCanOn()) {
					return neighbor;
				}
				
				if(!haveGet.contains(neighbor)) {
					haveGet.add(neighbor);
					rest.add(neighbor);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 从指定中心点的4个小中心点里找到与目标点(x1,y1)距离最近的一个
	 */
	public static LittleCenterPoint getMinDisLCP(int x1,int y1,CenterPoint centerPoint) {
		LittleCenterPoint lcpLeft = centerPoint.getLeftLittleCenterPoint();
		int lx1 = lcpLeft.getX();
		int ly1 = lcpLeft.getY();
		int dis1 = (lx1-x1)*(lx1-x1)+(ly1-y1)*(ly1-y1);
		LittleCenterPoint lcpDown = centerPoint.getDownLittleCenterPoint();
		int lx2 = lcpDown.getX();
		int ly2 = lcpDown.getY();
		int dis2 = (lx2-x1)*(lx2-x1)+(ly2-y1)*(ly2-y1);
		LittleCenterPoint lcpUp = centerPoint.getUpLittleCenterPoint();
		int lx3 = lcpUp.getX();
		int ly3 = lcpUp.getY();
		int dis3 = (lx3-x1)*(lx3-x1)+(ly3-y1)*(ly3-y1);
		LittleCenterPoint lcpRight = centerPoint.getRightLittleCenterPoint();
		int lx4 = lcpRight.getX();
		int ly4 = lcpRight.getY();
		int dis4 = (lx4-x1)*(lx4-x1)+(ly4-y1)*(ly4-y1);
		
		int min = NumberUtils.min(dis1,dis2,dis3,dis4);
		if(min==dis1) {
			return lcpLeft;
		}else if(min==dis2) {
			return lcpDown;
		}else if(min==dis3) {
			return lcpUp;
		}else {
			return lcpRight;
		}
	}
	/**
	 * 判断两个中心点是否相邻
	 */
	public static boolean isNeighbor(CenterPoint cp1,CenterPoint cp2) {
		if(cp1.getX()-cp2.getX()==0  && Math.abs(cp1.getY()-cp2.getY())==30) {
			return true;
		}
		if(Math.abs(cp1.getX()-cp2.getX())==30 && Math.abs(cp1.getY()-cp2.getY())==15 ) {
			return true;
		}
		if(Math.abs(cp1.getX()-cp2.getX())==60 && cp1.getY()-cp2.getY()==0 ) {
			return true;
		}
		return false;
	}
	/**
	 * 判断中心点与一个区域是否相邻
	 */
	public static boolean isNeignborOfArea(CenterPoint cp1,List<CenterPoint> area) {
		for(CenterPoint cp : area) {
			if(isNeighbor(cp,cp1)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 从区域area中找到与点startCp距离最近的点，再从找到的点的周围找与startCp距离最近的点
	 * 如果找不到可用的点  就用内部这个minDisInCp点
	 */
	public static CenterPoint selectAndSelect(List<CenterPoint> area,CenterPoint startCp) {
		int min = 9999999;
		CenterPoint minDisInCp = null;
		for(CenterPoint cp:area) {
			int dis = Math.abs(cp.getX()-startCp.getX()) +2*Math.abs(cp.getY()-startCp.getY());
			if(dis<min) {
				min = dis;
				minDisInCp = cp;
			}
		}
		List<CenterPoint> neignbors = getNeighbors(minDisInCp);
		min = 999999;
		CenterPoint minDisNeighborCp = null;
		for(CenterPoint cp:neignbors) {
			int dis = Math.abs(cp.getX()-startCp.getX()) +2*Math.abs(cp.getY()-startCp.getY());
			if(dis<min) {
				min = dis;
				minDisNeighborCp = cp;
			}
		}
		
		if(minDisNeighborCp.isExistSingleSelectUnit()) {
			return minDisInCp;
		}else {
			return minDisNeighborCp;
		}
		
		
	}
	
	
	/**
	 * 获取一个点周围的8个点
	 */
	public static List<CenterPoint> getNeighbors(CenterPoint center){
		CenterPoint newCp1 = center.getRight();
		CenterPoint newCp2 = center.getRightDn();
		CenterPoint newCp3 = center.getDn();
		CenterPoint newCp4 = center.getLeftDn();
		CenterPoint newCp5 = center.getLeft();
		CenterPoint newCp6 = center.getLeftUp();
		CenterPoint newCp7 = center.getUp();
		CenterPoint newCp8 = center.getRightUp();
		
		List<CenterPoint> list = new ArrayList<>();
		if(newCp1!=null) list.add(newCp1);
		if(newCp2!=null) list.add(newCp2);
		if(newCp3!=null) list.add(newCp3);
		if(newCp4!=null) list.add(newCp4);
		if(newCp5!=null) list.add(newCp5);
		if(newCp6!=null) list.add(newCp6);
		if(newCp7!=null) list.add(newCp7);
		if(newCp8!=null) list.add(newCp8);
		
		return list;
	}
	
	/**
	 * 获取一个点所在区域的所有点
	 */
	public static List<CenterPoint> getNeighborsCollection(List<CenterPoint> all, CenterPoint center){
		Queue<CenterPoint> queue = new LinkedList<>();//可使用的起始点
		queue.add(center);
		List<CenterPoint> result = new ArrayList<>();//结果集
		result.add(center);
		
		while(!queue.isEmpty()) {
			CenterPoint cp = queue.poll();
			
			List<CenterPoint> neighbors = getNeighbors(cp);
			for(CenterPoint neighbor :neighbors) {
				if(!result.contains(neighbor) && all.contains(neighbor)) {
					queue.offer(neighbor);
					result.add(neighbor);
				}
			}
		}
		return result;
	}
	
	/**
	 * 获取与点center不在一个区域的点
	 */
	public static List<CenterPoint> getNorNeighborsCollection(List<CenterPoint> all, CenterPoint center){
		List<CenterPoint> neignbors = getNeighborsCollection(all,center);
		List<CenterPoint> result = new ArrayList<>();
		for(CenterPoint cp:all) {
			if(!neignbors.contains(cp)) {
				result.add(cp);
			}
		}
		return result;
	}
	
	/**
	 * 找到一个中心点周围一个载具可进入的点   逐渐向外围搜索
	 * 找到的这个点不能是exceptLs中的点
	 */
	public static CenterPoint findVehicleCanOnCpNearBy(CenterPoint cp,Set<CenterPoint> exceptLs) {
		ArrayDeque<CenterPoint> rest = new ArrayDeque<>();
		rest.add(cp);
		Set<CenterPoint> haveGet = new HashSet<>();
		haveGet.add(cp);
		
		CenterPoint result = null;
		while(!rest.isEmpty()) {
			CenterPoint start = rest.poll();
			List<CenterPoint> neighbors = PointUtil.getNeighbors(start);
			for(CenterPoint neighbor:neighbors) {
				if(neighbor.isVehicleCanOn() && !exceptLs.contains(neighbor)) {
					return neighbor;
				}
				
				if(!haveGet.contains(neighbor)) {
					haveGet.add(neighbor);
					rest.add(neighbor);
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 获取中心点
	 * 需要确认参数是中心点坐标后才能使用
	 */
	public static CenterPoint fetchCenterPoint(int centerX,int centerY) {
		return centerPointMap.get(centerX+","+centerY);
	}
	/**
	 * 查询一个建筑是否在阴影中
	 */
	public static boolean isBuidingInShadow(Building building) {
		List<CenterPoint> buildingAreas = building.getNoConstCpList();
		for(CenterPoint cp:buildingAreas) {
			if(cp.isInShadow()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 红警核心方法之一:获取一个普通坐标对应的中心点坐标
	 * 与旧方法类似都是游走搜索算法  计算量比旧的还要少
	 */
	public static CenterPoint getCenterPoint(int x1,int y1){
		//中心点所在的Y坐标都是30的倍数
		
		//先向上向下搜索,找到两个Y坐标
		int y_d = y1-y1%15;
		int y_u = y_d+15;
		//双向搜索
		CenterPoint pointLeftCenter = null; 
		CenterPoint pointRightCenter = null;
		//左向搜索
		for(int i=0;i<30;i++) {
			int x11 = x1-i;
			if(x11%15==0) {
				if(isCenterPoint(x11,y_d)) {
					pointLeftCenter = fetchCenterPoint(x11,y_d);
					break;
				}
				if(isCenterPoint(x11,y_u)) {
					pointLeftCenter = fetchCenterPoint(x11,y_u);
					break;
				}
			}
		}
		//右向搜索
		for(int i=1;i<31;i++) {
			int x11 = x1+i;
			if(x11%15==0) {
				if(isCenterPoint(x11,y_d)) {
					pointRightCenter = fetchCenterPoint(x11,y_d);
					break;
				}
				if(isCenterPoint(x11,y_u)) {
					pointRightCenter = fetchCenterPoint(x11,y_u);
					break;
				}
			}
		}
		
		//看看谁上谁下
		
		//左上右下
		if(pointLeftCenter.y<pointRightCenter.y) {
			int deltaY = y1-pointLeftCenter.y;
			int deltaX = pointRightCenter.x-x1;
			
			if(deltaX>=2*deltaY) {
				return pointLeftCenter;
			}else {
				return pointRightCenter;
			}
		}
		//左下右上
		if(pointLeftCenter.y>pointRightCenter.y) {
			int deltaY = y1-pointRightCenter.y;
			int deltaX = x1-pointLeftCenter.x;
			if(deltaX>=2*deltaY) {
				return pointRightCenter;
			}else {
				return pointLeftCenter;
			}
		}
		
		
		return null;
	}
	
}

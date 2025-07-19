package redAlert.mapEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapCenterPointUtil {
	/**
	 * 中心点坐标缓存
	 * 所有的中心点,原则上应该从缓存中获取,避免新建
	 */
	public static Map<String,MapCenterPoint> centerPointMap = new HashMap<>();
	
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
				centerPointMap.put(x+","+y, new MapCenterPoint(x,y));
				
			}
		}
		
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 30*m;
			for(int n=0;n<50;n++) {
				int x = 60*n;
				centerPointMap.put(x+","+y, new MapCenterPoint(x,y));
			}
		}
	}
	
	/**
	 * 判断一个坐标点是否是菱形中心点
	 */
	public static boolean isCenterPoint(int x,int y) {
		return centerPointMap.containsKey(x+","+y);
	}
	
	/**
	 * 获取一个点周围的8个点
	 */
	public static List<MapCenterPoint> getNeighbors(MapCenterPoint center){
		MapCenterPoint newCp1 = center.getRight();
		MapCenterPoint newCp2 = center.getRightDn();
		MapCenterPoint newCp3 = center.getDn();
		MapCenterPoint newCp4 = center.getLeftDn();
		MapCenterPoint newCp5 = center.getLeft();
		MapCenterPoint newCp6 = center.getLeftUp();
		MapCenterPoint newCp7 = center.getUp();
		MapCenterPoint newCp8 = center.getRightUp();
		
		List<MapCenterPoint> list = new ArrayList<>();
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
	 * 获取中心点
	 * 需要确认参数是中心点坐标后才能使用
	 */
	public static MapCenterPoint fetchCenterPoint(int centerX,int centerY) {
		return centerPointMap.get(centerX+","+centerY);
	}
	
	
	/**
	 * 红警核心方法之一:获取一个普通坐标对应的中心点坐标
	 * 与旧方法类似都是游走搜索算法  计算量比旧的还要少
	 */
	public static MapCenterPoint getCenterPoint(int x1,int y1){
		//中心点所在的Y坐标都是30的倍数
		
		//先向上向下搜索,找到两个Y坐标
		int y_d = y1-y1%15;
		int y_u = y_d+15;
		//双向搜索
		MapCenterPoint pointLeftCenter = null; 
		MapCenterPoint pointRightCenter = null;
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

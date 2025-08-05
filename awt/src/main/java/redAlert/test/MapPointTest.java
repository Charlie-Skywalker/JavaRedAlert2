package redAlert.test;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * 重新定义中心点算法
 *
 * 平铺地面的不是标准菱形
 */
public class MapPointTest {

	
	/**
	 * 是否是左中心点
	 */
	public void isLeftCenterPoint() {
		/**
		 * 一类 左中心点  规律
		 *
		 *
		 * x=29+30*n
		 * y=15+60*m
		 *
		 */
	}
	
	/**
	 * 是否是右中心点
	 * @return
	 */
	public static boolean isRightCenterPoint(int x,int y) {
		return rightPoints.containsKey(x+","+y);
	}
	/**
	 * 获取菱形中心点
	 * 需要确认参数是中心点坐标后才能使用
	 */
	public static Point fetchCenterPoint(int centerX,int centerY) {
		return rightPoints.get(centerX+","+centerY);
	}
	
	/**
	 * 左中心点集合
	 */
	public static Map<String,Point> LeftPoints = new HashMap<String,Point>();
	/**
	 * 右中心点集合
	 */
	public static Map<String,Point> rightPoints = new HashMap<String,Point>();
	/**
	 * 初始化左中心点
	 * 左中心点不好用,改用右中心点,该方法保留但不使用
	 */
	public static void initLeftCenterPoint() {
		//一类中心点
		for(int m=0;m<50;m++) {
			int y = 15+30*m;
			for(int n=0;n<50;n++) {
				int x = 29+60*n;
				LeftPoints.put(x+","+y, new Point(x,y));
			}
		}
		
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 30*m;
			for(int n=0;n<50;n++) {
				int x = -1+60*n;
				LeftPoints.put(x+","+y, new Point(x,y));
			}
		}
		
		
	}
	/**
	 * 初始化右中心点
	 */
	public static void initRightCenterPoint() {
		//一类中心点
		for(int m=0;m<50;m++) {
			int y = 15+30*m;
			for(int n=0;n<50;n++) {
				int x = 30+60*n;
				rightPoints.put(x+","+y, new Point(x,y));
			}
		}
		
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 30*m;
			for(int n=0;n<50;n++) {
				int x = 60*n;
				rightPoints.put(x+","+y, new Point(x,y));
			}
		}
	}
	
	/**
	 * 给出一个点  判断它的左中心点或者右中心点
	 */
	public static Point getPoint(int x1,int y1){
		//中心点所在的Y坐标都是30的倍数
		
		//先向上向下搜索,找到两个Y坐标
		int y_d = y1-y1%15;
		int y_u = y_d+15;
		//双向搜索
		Point pointLeftCenter = null;
		Point pointRightCenter = null;
		//左向搜索
		for(int i=0;i<30;i++) {
			int x11 = x1-i;
			if(x11%15==0) {
				if(isRightCenterPoint(x11,y_d)) {
					pointLeftCenter = fetchCenterPoint(x11,y_d);
					break;
				}
				if(isRightCenterPoint(x11,y_u)) {
					pointLeftCenter = fetchCenterPoint(x11,y_u);
					break;
				}
			}
		}
		//右向搜索
		for(int i=1;i<31;i++) {
			int x11 = x1+i;
			if(x11%15==0) {
				if(isRightCenterPoint(x11,y_d)) {
					pointRightCenter = fetchCenterPoint(x11,y_d);
					break;
				}
				if(isRightCenterPoint(x11,y_u)) {
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
	
	public static void main(String[] args) {
		initRightCenterPoint();
		
//		System.out.println(getPoint(45, 45));
		System.out.println(getPoint(29, 30));
	}
	
}

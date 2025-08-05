package redAlert.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;

/**
 * 菱形网格工具类
 * 
 * 由于地图想要铺满  不可能用标准菱形  此类废弃   改为测试类
 */
public class PointUtilTest {
	
	/**
	 * 中心点坐标缓存
	 * 所有的中心点,原则上应该从缓存中获取,避免新建
	 */
	public static Map<String,CenterPoint> centerPointMap = new HashMap<>();
	
	static {
		initCenterPointCache();
	}
	/**
	 * 获取中心点坐标集合
	 */
	public static void initCenterPointCache() {
		//一类中心点
		for(int m=0;m<50;m++) {
			int y = 32*m;
			for(int n=0;n<50;n++) {
				int x = 64*n;
				putIntoMap(new CenterPoint(x,y));
			}
		}
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 16+32*m;
			for(int n=0;n<50;n++) {
				int x = 32+64*n;
				putIntoMap(new CenterPoint(x,y));
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
	 * 获取菱形中心点
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
	 * 自己想到的向量法
	 * 速度要快很多,解决了多边形法速度慢,边上无法判断的问题
	 * 搜索偏向是向下向右的
	 */
	public static CenterPoint getCenterPoint(int x1,int y1) {
		int y_d = y1-y1%16;
		int y_u = y_d+16;
		//双向搜索
		CenterPoint pointLeftCenter = null; 
		CenterPoint pointRightCenter = null;
		//左向搜索
		for(int i=0;i<32;i++) {
			int x11 = x1-i;
			if(x11%16==0) {
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
		for(int i=1;i<33;i++) {
			int x11 = x1+i;
			if(x11%16==0) {
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
		
//      矩形  注意y坐标轴向下  x坐标轴向右
		//		B      D
		//		A      C
		
		//左上 右下模式
		if(pointLeftCenter.getY()<pointRightCenter.getY()) {
			int pDx = pointRightCenter.x;
			int pDy = pointLeftCenter.y;
			//找到向量  pD->p
			//若向量  向量y分量绝对值 除以x分量绝对值 >0.5则在右下   <0.5则在左上
			if(pDx==x1) {
				return pointRightCenter;
			}
			if(pDy==y1) {
				return pointLeftCenter;
			}
			
			double xd = Math.abs(pDx-x1);
			double yd = Math.abs(pDy-y1);
			if(yd/xd>0.5) {
				return pointRightCenter;
			}else if(yd/xd<0.5) {
				return pointLeftCenter;
			}else {
				return pointLeftCenter;
			}
			
		}
		
		//左下 右上模式
		if(pointLeftCenter.getY()>pointRightCenter.getY()) {
			int pBx = pointLeftCenter.x;
			int pBy = pointRightCenter.y;
			
			//找到向量  pB->p
			//若向量  向量y分量绝对值 除以x分量绝对值 >0.5则在右下   <0.5则在左上
			if(pBx==x1) {
				return pointLeftCenter;
			}
			if(pBy==y1) {
				return pointRightCenter;
			}
			
			double xd = Math.abs(pBx-x1);
			double yd = Math.abs(pBy-y1);
			if(yd/xd>0.5) {
				return pointLeftCenter;
			}else if(yd/xd<0.5) {
				return pointRightCenter;
			}else {
				return pointLeftCenter;
			}
			
		}
		
		return null;
		
	}
	
}

package redAlert.test;

import java.awt.Color;
import java.util.HashMap;
import java.util.Objects;

/**
 * 红警中心点
 */
public class RaPoint {

	
	//获取中心点坐标集合
	public static HashMap<String,RaPoint> myPointMap = new HashMap<>();
	
	static {
		for(int m=0;m<500;m++) {//一类
			int y = 32*m;
			for(int n=0;n<50;n++) {
				int x = 64*n;
				myPointMap.put(x+","+y, new RaPoint(x,y));
			}
		}
		
		for(int m=0;m<500;m++) {//二类
			int y = 16+32*m;
			for(int n=0;n<50;n++) {
				int x = 32+64*n;
				myPointMap.put(x+","+y, new RaPoint(x,y));
			}
		}
	}
	
	public int x;
	public int y;
	
	public RaPoint(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public boolean canUse = true;//是否是能走的格子
	public int curPrice = 0;//当前代价  已经走过的路的距离
	/**
	 * 欧拉距离
	 */
	public int euDistance = 9999;
	/**
	 * 这个点的上一个点
	 * 在寻路成功后,需要回溯路径,需要设置和获取此变量
	 */
	public RaPoint lastPoint;
	
	public Color color = Color.green;
	
	
	
	
	public RaPoint getLastPoint() {
		return lastPoint;
	}

	public void setLastPoint(RaPoint lastPoint) {
		this.lastPoint = lastPoint;
	}

	public int getEuDistance() {
		return euDistance;
	}

	public void setEuDistance(int euDistance) {
		this.euDistance = euDistance;
	}

	public int getCurPrice() {
		return curPrice;
	}

	public void setCurPrice(int curPrice) {
		this.curPrice = curPrice;
	}

	public boolean isCanUse() {
		return canUse;
	}

	public void setCanUse(boolean canUse) {
		this.canUse = canUse;
		if(canUse) {
			this.color = Color.green;
		}else {
			this.color = Color.red;
		}
		
	}

	public RaPoint getLeft(){
		if(x-64>=0) {
			return myPointMap.get( (x-64)+","+y);
		}else {
			return null;
		}
	}
	
	public RaPoint getRight(){
		if(x+64<10000) {
			return myPointMap.get( (x+64)+","+y);
		}else {
			return null;
		}
	}
	
	public RaPoint getUp(){
		if(y-32>=0) {
			return myPointMap.get( x+","+(y-32));
		}else {
			return null;
		}
	}
	
	public RaPoint getDown(){
		if(y+32<10000) {
			return myPointMap.get( x+","+(y+32));
		}else {
			return null;
		}
	}
	
	public RaPoint getLeftUp(){
		if(x-32>=0 && y-16>=0) {
			return myPointMap.get( (x-32)+","+ (y-16) );
		}else {
			return null;
		}
	}
	
	public RaPoint getLeftDown(){
		if(x-32>=0 && y+16<=10000) {
			return myPointMap.get( (x-32)+","+ (y+16) );
		}else {
			return null;
		}
	}
	
	public RaPoint getRightDown(){
		if(x+32<=10000 && y+16<=10000) {
			return myPointMap.get( (x+32)+","+ (y+16) );
		}else {
			return null;
		}
	}
	
	public RaPoint getRightUp(){
		if(x+32<=10000 && y-16>=0) {
			return myPointMap.get( (x+32)+","+ (y-16) );
		}else {
			return null;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RaPoint other = (RaPoint) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return "(" +x + "," + y + ")";
	}
	
}

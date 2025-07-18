package redAlert.test;

import java.awt.Color;
import java.util.Objects;

import redAlert.Atest;

public class MyPoint{
	public int x;
	public int y;
	
	public boolean canUse = true;//是否是能走的格子
	public Color color = Color.yellow;
	public int curPrice = 0;//当前代价
	public int mhd = 9999;//曼哈顿距离
	public MyPoint lastPoint;
	public int totalPrice = 9999;
	
	public MyPoint(int x,int y){
		this.x = x;
		this.y = y;
	}
	
	public void ban() {
		this.canUse = false;
		this.color = Color.black;
	}
	
	public MyPoint getLeft(){
		if(x-1>=0) {
			return Atest.myPointMap.get( (x-1)+","+y);
		}else {
			return null;
		}
	}
	
	public MyPoint getRight(){
		if(x+1<30) {
			return Atest.myPointMap.get( (x+1)+","+y);
		}else {
			return null;
		}
	}
	
	public MyPoint getUp(){
		if(y-1>=0) {
			return Atest.myPointMap.get( x+","+(y-1));
		}else {
			return null;
		}
	}
	
	public MyPoint getDown(){
		if(y+1<30) {
			return Atest.myPointMap.get( x+","+(y+1));
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
		MyPoint other = (MyPoint) obj;
		return x == other.x && y == other.y;
	}
	
	
	
}

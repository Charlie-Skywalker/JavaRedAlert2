package redAlert.utilBean;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import redAlert.enums.Direction;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.utils.PointUtil;

/**
 * 小中心点
 * 
 * 步兵单位站立位置
 * 
 * 
 * 中心点的四周  有四个小中心点
 * 
 * 左  x-16 y
 * 右  x+16 y
 * 上  x    y-8
 * 下  x    y+8
 * 
 * 
 * 不提供一个点坐标  获取其对应小中心点的方法
 * 
 * 获取小中心点  一定从中心点的方法获取  且获取的中心点要么指定  要么随机
 */
@Data
public class LittleCenterPoint {

	/**
	 * 中心点横坐标
	 */
	public int x;
	/**
	 * 中心点纵坐标
	 */
	public int y;
	/**
	 * 是哪一块方块  上下左右
	 * 上0
	 * 左1
	 * 下2
	 * 右3
	 */
	public Direction direction;
	
	/**
	 * 这个区块上的步兵引用
	 * 
	 * 暂时认为小中心块上只能由步兵单位
	 */
	public Soldier soldier;
	/**
	 * 被提前预定
	 */
	public volatile AtomicBoolean preBooked = new AtomicBoolean(false);
	
	public LittleCenterPoint(int x, int y,Direction direction) {
		this.x = x;
		this.y = y;
		this.direction = direction;
	}

	/**
	 * 步兵是否可进入此LCP
	 */
	public boolean isSoldierCanOn(){
		CenterPoint cp = PointUtil.getCenterPoint(x, y);
		if(cp!=null) {
			boolean flag = cp.isSoldierCanOn();
			if(flag) {
				//预占用模式
				if(preBooked.get()) {
					return false;
				}else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 获取此小中心点所在的中心点
	 */
	public CenterPoint getCenterPoint() {
		return PointUtil.getCenterPoint(x, y);
	}
	
	/**
	 * 获取一个小中心点的左上小中心点
	 */
	public LittleCenterPoint getLeftUp() {
		int x1,y1;
		if(direction==Direction.Up || direction==Direction.Left) {
			x1 = x - 14;
			y1 = y - 7;
		}else {
			x1 = x - 16;
			y1 = y - 8;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的左小中心点
	 */
	public LittleCenterPoint getLeft() {
		int x1,y1;
		if(direction==Direction.Up) {
			x1 = x - 30;
			y1 = y + 1;
		}else if(direction==Direction.Down){
			x1 = x - 30;
			y1 = y - 1;
		}else if(direction==Direction.Left) {
			x1 = x - 28;
			y1 = y;
		}else{
			x1 = x - 32;
			y1 = y;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的左下小中心点
	 */
	public LittleCenterPoint getLeftDn() {
		int x1,y1;
		if(direction==Direction.Up || direction==Direction.Right) {
			x1 = x - 16;
			y1 = y + 8;
		}else {
			x1 = x -14;
			y1 = y + 7;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的下小中心点
	 */
	public LittleCenterPoint getDn() {
		int x1,y1;
		if(direction==Direction.Up) {
			x1 = x;
			y1 = y + 16;
		}else if(direction==Direction.Down){
			x1 = x;
			y1 = y + 14;
		}else if(direction==Direction.Left) {
			x1 = x + 2;
			y1 = y + 15;
		}else {
			x1 = x - 2;
			y1 = y + 15;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的右下小中心点
	 */
	public LittleCenterPoint getRightDn() {
		int x1,y1;
		if(direction==Direction.Down || direction==Direction.Right) {
			x1 = x + 14;
			y1 = y + 7;
		}else {
			x1 = x + 16;
			y1 = y + 8;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的右小中心点
	 */
	public LittleCenterPoint getRight() {
		int x1,y1;
		if(direction==Direction.Up) {
			x1 = x + 30;
			y1 = y + 1;
		}else if(direction==Direction.Down){
			x1 = x + 30;
			y1 = y - 1;
		}else if(direction==Direction.Left) {
			x1 = x + 32;
			y1 = y;
		}else{
			x1 = x + 28;
			y1 = y;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	/**
	 * 获取一个小中心点的右上小中心点
	 */
	public LittleCenterPoint getRightUp() {
		int x1,y1;
		if(direction==Direction.Up || direction==Direction.Right) {
			x1 = x + 14;
			y1 = y - 7;
		}else{
			x1 = x + 16;
			y1 = y - 8;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
	}
	
	/**
	 * 获取一个小中心点的上小中心点
	 */
	public LittleCenterPoint getUp() {
		int x1,y1;
		if(direction==Direction.Up) {
			x1 = x;
			y1 = y - 14;
		}else if(direction==Direction.Down){
			x1 = x;
			y1 = y - 16;
		}else if(direction==Direction.Left) {
			x1 = x + 2;
			y1 = y - 15;
		}else {
			x1 = x - 2;
			y1 = y - 15;
		}
		return PointUtil.fetchLittleCenterPoint(x1, y1);
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
		LittleCenterPoint other = (LittleCenterPoint) obj;
		return x == other.x && y == other.y;
	}

	@Override
	public String toString() {
		return  x + ","+  y ;
	}
	
	
	
	
}

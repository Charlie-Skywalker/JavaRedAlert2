package redAlert.utilBean;

import redAlert.MainPanel;
import redAlert.utils.PointUtil;

/**
 * 坐标变量
 * 
 * 最好能让这个对象逃逸,不制造太多垃圾
 */
public class Coordinate {
	
	/**
	 * 坐标类型 视口坐标
	 */
	public final static int TYPE_VIEW_COORD = 1;
	/**
	 * 坐标类型 地图坐标
	 */
	public final static int TYPE_MAP_COORD = 2;
	
	/**
	 * 地图坐标X
	 */
	public int mapX;
	/**
	 * 地图坐标Y
	 */
	public int mapY;
	/**
	 * 视口坐标X
	 * 此值很多时候都是鼠标坐标
	 */
	public int viewX;
	/**
	 * 视口坐标Y
	 * 此值很多时候都是鼠标坐标
	 */
	public int viewY;
	/**
	 * 视口在地图上偏移量
	 * 视野偏移量,用于确认渲染的范围
	 * 由于这个变量变化很快,所以使用时必须复制一份固定值,保证在同一个方法中这个值相同
	 */
	public final int viewportOffX;
	/**
	 * 视口在地图上偏移量
	 * 视野偏移量,用于确认渲染的范围
	 * 由于这个变量变化很快,所以使用时必须复制一份
	 */
	public final int viewportOffY;
	
	public Coordinate(int coordX,int coordY,int coordType) {
		if(coordType==TYPE_VIEW_COORD) {
			this.viewX = coordX;
			this.viewY = coordY;
			this.viewportOffX = MainPanel.viewportOffX;
			this.viewportOffY = MainPanel.viewportOffY;
			this.mapX = viewportOffX+viewX;
			this.mapY = viewportOffY+viewY;
		}else {
			this.mapX = coordX;
			this.mapY = coordY;
			this.viewportOffX = MainPanel.viewportOffX;
			this.viewportOffY = MainPanel.viewportOffY;
			this.viewX = mapX-viewportOffX;
			this.viewY = mapY-viewportOffY;
		}
	}
	
	public Coordinate(int coordX,int coordY,int viewportOffX,int viewportOffY,int coordType) {
		if(coordType==TYPE_VIEW_COORD) {
			this.viewX = coordX;
			this.viewY = coordY;
			this.viewportOffX = viewportOffX;
			this.viewportOffY = viewportOffY;
			this.mapX = viewportOffX+viewX;
			this.mapY = viewportOffY+viewY;
		}else {
			this.mapX = coordX;
			this.mapY = coordY;
			this.viewportOffX = viewportOffX;
			this.viewportOffY = viewportOffY;
			this.viewX = mapX-viewportOffX;
			this.viewY = mapY-viewportOffY;
		}
	}
	
	/**
	 * 转换为中心点
	 */
	public CenterPoint getCenterPoint() {
		return PointUtil.getCenterPoint(mapX, mapY);
	}
	
	

	public int getMapX() {
		return mapX;
	}
	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	public int getMapY() {
		return mapY;
	}
	public void setMapY(int mapY) {
		this.mapY = mapY;
	}

	public int getViewX() {
		return viewX;
	}
	public void setViewX(int viewX) {
		this.viewX = viewX;
	}

	public int getViewY() {
		return viewY;
	}
	public void setViewY(int viewY) {
		this.viewY = viewY;
	}
	
	public int getViewportOffY() {
		return viewportOffY;
	}
	public int getViewportOffX() {
		return viewportOffX;
	}

	
	
	
}

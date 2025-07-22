package redAlert.utils;

import java.awt.event.MouseEvent;

import redAlert.utilBean.Coordinate;

/**
 * 游戏坐标计算工具类
 * 
 * 名词解释
 * 战场坐标：地图实际上很大,在大地图上的坐标
 * 视口坐标：屏幕实际上只能展示战场的一部分,在屏幕上的坐标
 */
public class CoordinateUtil {

	
	/**
	 * 视口坐标切换战场坐标
	 */
	public static int getMapCoordX(int viewX,int viewportOffX) {
		return viewX+viewportOffX;
	}
	/**
	 * 视口坐标切换战场坐标
	 */
	public static int getMapCoordY(int viewY,int viewportOffY) {
		return viewY+viewportOffY;
	}
	/**
	 * 战场坐标切换视口坐标
	 */
	public static int getViewportX(int mapCoordX,int viewportOffX) {
		return mapCoordX-viewportOffX;
	}
	/**
	 * 战场坐标切换视口坐标
	 */
	public static int getViewportY(int mapCoordY,int viewportOffY) {
		return mapCoordY-viewportOffY;
	}
	
	/**
	 * 获取一个坐标
	 */
	public static Coordinate getCoordinate(MouseEvent mouseEvent) {
		int mouseX = mouseEvent.getX();
		int mouseY = mouseEvent.getY();
		return new Coordinate(mouseX,mouseY,Coordinate.TYPE_VIEW_COORD);
	}
	
	/**
	 * 获取一个坐标
	 */
	public static Coordinate getCoordinate(int mouseX,int mouseY) {
		return new Coordinate(mouseX,mouseY,Coordinate.TYPE_VIEW_COORD);
	}
}

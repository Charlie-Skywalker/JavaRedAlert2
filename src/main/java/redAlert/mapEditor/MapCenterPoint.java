package redAlert.mapEditor;

import lombok.Data;

import java.util.Objects;

/**
 * 地编专用的CenterPoint
 */
@Data
public class MapCenterPoint {

	private static final int ox = 30;//菱形横半轴长
	private static final int oy = 15;//菱形竖半轴长
	/**
	 * 中心点横坐标
	 * 该坐标为地图坐标
	 */
	public int x;
	/**
	 * 中心点纵坐标
	 * 该坐标为地图坐标
	 */
	public int y;
	/**
	 * 瓦片
	 */
	public Tile tile;
	
	
	
	/**
	 * 确认点坐标就是中心点的情况下使用
	 * @param x
	 * @param y
	 */
	public MapCenterPoint(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	
	/**
	 * 获取一个中心点的左上中心点
	 */
	public MapCenterPoint getLeftUp() {
		int x1 = x - ox;
		int y1 = y - oy;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的左中心点
	 */
	public MapCenterPoint getLeft() {
		int x1 = x - ox*2;
		int y1 = y;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的左下中心点
	 */
	public MapCenterPoint getLeftDn() {
		int x1 = x - ox;
		int y1 = y + oy;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的下中心点
	 */
	public MapCenterPoint getDn() {
		int x1 = x;
		int y1 = y + oy*2;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右下中心点
	 */
	public MapCenterPoint getRightDn() {
		int x1 = x + ox;
		int y1 = y + oy;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右中心点
	 */
	public MapCenterPoint getRight() {
		int x1 = x + ox*2;
		int y1 = y;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右上中心点
	 */
	public MapCenterPoint getRightUp() {
		int x1 = x + ox;
		int y1 = y - oy;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的上中心点
	 */
	public MapCenterPoint getUp() {
		int x1 = x;
		int y1 = y - oy*2;
		return MapCenterPointUtil.fetchCenterPoint(x1, y1);
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
		MapCenterPoint other = (MapCenterPoint) obj;
		return x == other.x && y == other.y;
	}
	
	
	@Override
	public String toString() {
		return x+"," + y;
	}
}

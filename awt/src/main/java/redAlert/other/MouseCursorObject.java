package redAlert.other;

import redAlert.ShapeUnitFrame;

/**
 * 鼠标指针对象
 */
public class MouseCursorObject {
	
	
	public ShapeUnitFrame mouse;
	/**
	 * 相对系统鼠标位置的偏移
	 */
	public int offX,offY;
	/**
	 * 
	 */
	public ShapeUnitFrame getMouse() {
		return mouse;
	}
	public void setMouse(ShapeUnitFrame mouse) {
		this.mouse = mouse;
	}
	public int getOffX() {
		return offX;
	}
	public void setOffX(int offX) {
		this.offX = offX;
	}
	public int getOffY() {
		return offY;
	}
	public void setOffY(int offY) {
		this.offY = offY;
	}
}

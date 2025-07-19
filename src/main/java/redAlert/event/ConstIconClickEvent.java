package redAlert.event;

import java.awt.event.MouseEvent;

import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 右侧主建筑建造图标点击事件
 */
public class ConstIconClickEvent extends RaEvent{

	/**
	 * 鼠标事件
	 */
	public MouseEvent mouseEvent;
	/**
	 * 被点击的图标
	 */
	public Tab00ConstIcon constIcon;
	
	public ConstIconClickEvent(MouseEvent mouseEvent,Tab00ConstIcon constIcon) {
		this.mouseEvent = mouseEvent;
		this.constIcon = constIcon;
		
	}

	public MouseEvent getMouseEvent() {
		return mouseEvent;
	}

	public void setMouseEvent(MouseEvent mouseEvent) {
		this.mouseEvent = mouseEvent;
	}

	public Tab00ConstIcon getConstIcon() {
		return constIcon;
	}

	public void setConstIcon(Tab00ConstIcon constIcon) {
		this.constIcon = constIcon;
	}
	
	
}

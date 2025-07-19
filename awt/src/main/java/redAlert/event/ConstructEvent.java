package redAlert.event;

import java.awt.event.MouseEvent;

import redAlert.enums.ConstConfig;

/**
 * 军事建筑建造事件
 */
public class ConstructEvent extends RaEvent{

	/**
	 * 鼠标事件
	 */
	public MouseEvent mouseEvent;
	/**
	 * 建造的建筑
	 */
	public ConstConfig constConfig;
	
	
	public ConstructEvent(MouseEvent mouseEvent,ConstConfig constConfig) {
		this.mouseEvent = mouseEvent;
		this.constConfig = constConfig;
		
		
	}
}

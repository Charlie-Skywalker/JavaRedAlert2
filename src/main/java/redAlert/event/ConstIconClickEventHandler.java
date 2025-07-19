package redAlert.event;

import java.awt.event.MouseEvent;
import java.util.ArrayDeque;

import redAlert.Constructor;
import redAlert.MainTest;
import redAlert.MainTest.MouseStatus;
import redAlert.MouseEventDeal;
import redAlert.tabIcon.Tab00ConstIcon;
import redAlert.tabIcon.Tab00Manager;

/**
 * 右侧主建筑建造图标点击事件处理器
 */
public class ConstIconClickEventHandler extends Thread{

	
	public static ArrayDeque<ConstIconClickEventHandler> queue = new ArrayDeque<>();
	
	static {
		for(int i=0;i<3;i++) {
			ConstIconClickEventHandler handler = new ConstIconClickEventHandler();
			queue.add(handler);
		}
	}
	
	public static ConstIconClickEventHandler getInstance(ConstIconClickEvent constIconClickEvent) {
		ConstIconClickEventHandler handler = null;
		if(queue.isEmpty()) {
			handler = new ConstIconClickEventHandler();
		}else {
			handler = queue.poll();
		}
		handler.setConstIconClickEvent(constIconClickEvent);
		return handler;
	}
	
	/**
	 * 放回队列
	 */
	public void pushBack() {
		queue.offer(this);
	}
	
	/**
	 * 建筑图标点击事件
	 */
	public ConstIconClickEvent constIconClickEvent;

	/**
	 * 处理图标点击
	 */
	@Override
	public void run() {
		
		MouseEvent mouseEvent = constIconClickEvent.getMouseEvent();
		Tab00ConstIcon constIcon = constIconClickEvent.getConstIcon();
		
		int status = constIcon.getStatus();
		
		if(mouseEvent.getButton()==MouseEvent.BUTTON1) {//左键
			/*
			 * 正常状态下点击,开始建造,并禁用其他建筑
			 */
			if(status==Tab00ConstIcon.STATUS_IDLE) {
				Tab00Manager.banAllExceptOne(constIcon);
				constIcon.setStatus(Tab00ConstIcon.STATUS_USING);
				Constructor.playOneMusic("ceva052");//Building
				constIcon.setReadyFlag(false);
				constIcon.setOnHoldFlag(false);
				constIcon.setBanFlag(false);
				return;
			}
			/**
			 * 建造ing状态下点击,提示语音
			 */
			if(status==Tab00ConstIcon.STATUS_USING) {
				Constructor.playOneMusic("ceva047");//Unable to comply, building in progress
				return;
			}
			/*
			 * 就绪状态下点击,更新鼠标状态,将能够建造建筑
			 */
			if(status==Tab00ConstIcon.STATUS_READY) {
				MainTest.mouseStatus = MouseStatus.Construct;
				MouseEventDeal.constName = constIcon.getConstInfo();
				return;
			}
			/**
			 * 禁用状态,点击提示语音
			 */
			if(status==Tab00ConstIcon.STATUS_BAN) {
				if(Tab00Manager.isExistBuilding()) {
					Constructor.playOneMusic("ceva047");//Unable to comply, building in progress
				}
				return;
			}
			/**
			 * 暂停状态下点击,继续建造
			 */
			if(status==Tab00ConstIcon.STATUS_ON_HOLD) {
				constIcon.setStatus(Tab00ConstIcon.STATUS_USING);
				Constructor.playOneMusic("ceva052");//Building
				constIcon.setOnHoldFlag(false);
				return;
			}
			
		}
		if(mouseEvent.getButton()==MouseEvent.BUTTON3) {//右键
			/**
			 * 建造ing状态下右键,切为暂停状态
			 */
			if(status==Tab00ConstIcon.STATUS_USING) {
				constIcon.setStatus(Tab00ConstIcon.STATUS_ON_HOLD);
				Constructor.playOneMusic("ceva056");//on hold
				return;
			}
			/**
			 * 暂停状态下右键,取消建造
			 */
			if(status==Tab00ConstIcon.STATUS_ON_HOLD) {
				constIcon.setStatus(Tab00ConstIcon.STATUS_TEMP);
				constIcon.setOnHoldFlag(false);
				Constructor.playOneMusic("ceva051");//cancel
				Tab00Manager.freeAll();
				return;
			}
			/**
			 * 就绪状态下右键,取消建造
			 */
			if(status==Tab00ConstIcon.STATUS_READY) {
				constIcon.setStatus(Tab00ConstIcon.STATUS_TEMP);
				constIcon.setReadyFlag(false);
				Constructor.playOneMusic("ceva051");//cancel
				Tab00Manager.freeAll();
				return;
			}
			
		}
		
		pushBack();
	}

	public ConstIconClickEvent getConstIconClickEvent() {
		return constIconClickEvent;
	}

	public void setConstIconClickEvent(ConstIconClickEvent constIconClickEvent) {
		this.constIconClickEvent = constIconClickEvent;
	}
	
	
	
}

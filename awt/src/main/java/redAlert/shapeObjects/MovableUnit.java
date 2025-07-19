package redAlert.shapeObjects;

import redAlert.Constructor;
import redAlert.other.BloodBar;
import redAlert.other.VehicleBloodBar;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;

/**
 * 对一个可以移动的单位的抽象
 * 
 * 移动的单位都有血条和控制移动时显示的绿色移动线
 */
public abstract class MovableUnit extends ShapeUnit implements Bloodable{

	public MovableUnit() {
		
	}
	
	/**
	 * 血条对象
	 */
	public BloodBar bloodBar;
	/**
	 * 移动时说的话
	 */
	public String [] moveSounds = null;
	/**
	 * 选中时说的话
	 */
	public String [] selectSounds = null;
	/**
	 * 停止令
	 * 当此变量为true时,单位必须在移动到nextTarget后停止移动
	 */
	public volatile boolean stopFlag = false;
	
	
	
	/**
	 * 随机播放移动时语音
	 */
	public void movePlay() {
		if(moveSounds!=null && moveSounds.length>0) {
			Constructor.randomPlayOneMusic(moveSounds);
		}
	}
	/**
	 * 随机播放选中时语音
	 */
	public void selectPlay() {
		if(selectSounds!=null && selectSounds.length>0) {
			Constructor.randomPlayOneMusic(selectSounds);
		}
	}
	
	
	/**
	 * 被用户鼠标选中
	 * 
	 * 显示血条
	 */
	public void beSelected() {
		bloodBar.setVisible(true);
	}
	/**
	 * 失去选中
	 * 
	 * 隐藏血条
	 */
	public void unSelected() {
		bloodBar.setVisible(false);
	}
	
	
	
	
	
	
	
	/**
	 * 移动至目标中心点
	 * 
	 * 只提供这个标准  移动至小中心点的实现由子类自己负责
	 */
	public abstract void moveToTarget(CenterPoint target);
	
	/**
	 * 给步兵用的方法
	 */
	public void moveToTarget(LittleCenterPoint target) {
		
	}
	
	
	
	public BloodBar getBloodBar() {
		return bloodBar;
	}
	public void setBloodBar(VehicleBloodBar bloodBar) {
		this.bloodBar = bloodBar;
	}
	public boolean isStopFlag() {
		return stopFlag;
	}
	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}
	
}

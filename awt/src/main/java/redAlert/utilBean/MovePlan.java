package redAlert.utilBean;

import redAlert.shapeObjects.MovableUnit;

/**
 * 控制群体移动时,为了画移动线,需要这个对象
 */
public class MovePlan {

	/**
	 * 移动单位
	 */
	public MovableUnit unit;
	/**
	 * 目标中心点
	 */
	public CenterPoint targetCp;
	/**
	 * 目标小中心点
	 */
	public LittleCenterPoint targetLCP;
	
	
	public MovableUnit getUnit() {
		return unit;
	}
	public void setUnit(MovableUnit unit) {
		this.unit = unit;
	}
	public CenterPoint getTargetCp() {
		return targetCp;
	}
	public void setTargetCp(CenterPoint targetCp) {
		this.targetCp = targetCp;
	}
	public LittleCenterPoint getTargetLCP() {
		return targetLCP;
	}
	public void setTargetLCP(LittleCenterPoint targetLCP) {
		this.targetLCP = targetLCP;
	}
	
	
}

package redAlert.shapeObjects;

import redAlert.shapeObjects.vehicle.VehicleUtil;
import redAlert.utilBean.CenterPoint;

/**
 * 表示可转动
 * 例如车身 炮塔
 */
public interface Turnable {

	public int max = 15;
	
	/**
	 * 顺时针转动
	 * cw:clockwise
	 */
	default void cwRotate() {
		int curTurn = getCurTurn();
		
		if(curTurn==max) {
			curTurn = 0;
		}else {
			curTurn+=1;
		}
		setCurTurn(curTurn);
	}
	/**
	 * 逆时针转动
	 * acw:anticlockwise
	 */
	default void acwRotate() {
		int curTurn = getCurTurn();
		
		if(curTurn==0) {
			curTurn = max;
		}else {
			curTurn -= 1;
		}
		setCurTurn(curTurn);
	}
	
	/**
	 * 根据当前转向和目标转向  自动进行旋转
	 * 会自动从旋转角度小的方式进行顺/逆时针旋转
	 */
	default void turn() {
		int curTurn = getCurTurn();
		int targetTurn = getTargetTurn();
		int direction = VehicleUtil.calRotateDirection(curTurn,targetTurn);
		if(direction>0) {
			cwRotate();
		}else if(direction<0){
			acwRotate();
		}else {
			//不需要旋转
		}
	}
	
	/**
	 * 计算目标转向并设置
	 */
	default void calAndSetTargetTurn(Vehicle vehicle,CenterPoint nextTarget) {
		int curX = vehicle.getPositionX()+vehicle.getCenterOffX();
		int curY = vehicle.getPositionY()+vehicle.getCenterOffY();
		
//		CenterPoint curCenterPoint = PointUtil.getCenterPoint(curX, curY);
		CenterPoint curCenterPoint = vehicle.getCurCenterPoint();
		int targetTurn = VehicleUtil.getTargetTurn2(curCenterPoint, nextTarget);
		if(targetTurn>=0) {
			setTargetTurn(targetTurn);
		}
	}
	
	
	public int getCurTurn();
	
	public void setCurTurn(int curTurn);
	
	public int getTargetTurn();
	
	public void setTargetTurn(int targetTurn);
	
}

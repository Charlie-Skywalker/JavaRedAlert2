package redAlert.shapeObjects;

import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.vehicle.VehicleUtil;
import redAlert.utils.VxlFileReader;

/**
 * 坦克炮塔
 * 思来想去  炮塔还是应该有一个公共父类
 * 炮塔是坦克的一部分,不参与单独的帧图计算,不应当继承ShapeUnit类
 */
public abstract class TankTurret implements Turnable{

	
	/**
	 * 炮塔的所有帧画面
	 */
	public List<ShapeUnitFrame> frames;
	/**
	 * 阵营颜色
	 */
	public UnitColor unitColor;
	/**
	 * 当前帧
	 */
	public ShapeUnitFrame curFrame;
	/**
	 * 目标转向
	 * 
	 */
	public int targetTurn = 2;
	/**
	 * 当前转向
	 * 炮塔和车身有单独的转向  所以还是应该有单独的变量
	 */
	public int curTurn = 2;
	/**
	 * 车身引用
	 */
	public Vehicle vehicle;
	/**
	 * 开火目标  目前阶段仅设定为建筑
	 */
	public Building attackBuilding = null;
	
	
	/**
	 * 初始化时炮塔转向应该与车身一致
	 */
	public TankTurret(Vehicle vehicle,String vxlPrefix) {
		this.vehicle = vehicle;
		this.unitColor = vehicle.getUnitColor();
		this.frames = VxlFileReader.convertPngFileToBuildingFrames(vxlPrefix,32,1,vehicle.getUnitColor());
		this.curTurn = vehicle.getCurTurn();
		this.targetTurn = vehicle.getTargetTurn();
		this.curFrame = frames.get(curTurn);
	}
	
	/**
	 * 炮塔最后要跟车体的旋转方向一致  所以需要一个单独的turn方法
	 */
	public void turn(int curTurn,int vehicleTurn) {
		//计算旋转方向
		int direction = VehicleUtil.calRotateDirection(curTurn,vehicleTurn);
		
		if(direction>0) {
			cwRotate();
		}else if(direction<0){
			acwRotate();
		}else {
			
		}
	}
	
	/**
	 * 考虑到Vehicle类已经实现了攻击方法,炮塔的攻击方法就独立于Attackable接口了
	 * 由Vehicle类的攻击方法中调用炮塔类的攻击方法
	 */
	public abstract void attack(Building targetBuilding);
	

	@Override
	public int getCurTurn() {
		return curTurn;
	}
	@Override
	public void setCurTurn(int curTurn) {
		this.curTurn = curTurn;
	}
	@Override
	public int getTargetTurn() {
		return targetTurn;
	}
	@Override
	public void setTargetTurn(int targetTurn) {
		this.targetTurn = targetTurn;
	}
	public List<ShapeUnitFrame> getFrames() {
		return frames;
	}
	public void setFrames(List<ShapeUnitFrame> frames) {
		this.frames = frames;
	}
	public UnitColor getUnitColor() {
		return unitColor;
	}
	public void setUnitColor(UnitColor unitColor) {
		this.unitColor = unitColor;
	}
	public ShapeUnitFrame getCurFrame() {
		return curFrame;
	}
	public void setCurFrame(ShapeUnitFrame curFrame) {
		this.curFrame = curFrame;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Building getAttackBuilding() {
		return attackBuilding;
	}

	public void setAttackBuilding(Building attackBuilding) {
		this.attackBuilding = attackBuilding;
	}
	
	
	
	
}

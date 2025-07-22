package redAlert.enums;

/**
 * 坡面类型
 */
public enum RampType {

	
	Flat("平地"),Ramp01("斜坡01");
	
	
	public String desc;
	
	private RampType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 满足建筑摆放的必要条件
	 */
	public boolean buildingCondition() {
		return this==RampType.Flat;
	}
	/**
	 * 满足载具进入的必要条件
	 * 目前都满足,后续需要考虑是否存在特殊斜坡
	 */
	public boolean vehicleCondition() {
		return true;
	}
	/**
	 * 满足步兵进入的必要条件
	 * 目前都满足,后续需要考虑是否存在特殊斜坡
	 */
	public boolean soldierCondition() {
		return true;
	}
}

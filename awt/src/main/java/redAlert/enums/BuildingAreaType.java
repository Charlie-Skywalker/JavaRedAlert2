package redAlert.enums;

/**
 * 建筑区域类型
 * 主要为区分坦克可以压的中心块
 */
public enum BuildingAreaType {

	/**
	 * 表示没有建筑
	 */
	None("无"),
	/**
	 * 普通建筑占用
	 * 不能进坦克
	 * 
	 */
	Normal("普通"),
	/**
	 * 磅秤
	 * 可以进载具
	 */
	WeighBridge("磅秤"),//幽默命名法,磅秤,意思是坦克可以压的中心块   例如维修厂、采矿场、建设工厂的部分中心块
	/**
	 * 磅秤+载具
	 * 可以进坦克
	 */
	Vehicle_MBuilding("磅秤+载具");
	
	public final String desc;
	
	BuildingAreaType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(BuildingAreaType...buildingAreaType) {
		for(BuildingAreaType type:buildingAreaType) {
			if(this==type) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 满足陆地建筑摆放的必要条件
	 */
	public boolean buildingCondition() {
		return this==BuildingAreaType.None;
	}
	/**
	 * 满足载具进入的必要条件
	 * 不考虑两栖载具与船
	 */
	public boolean vehicleCondition() {
		return this.in(None,WeighBridge);
	}
	/**
	 * 满足步兵进入的必要条件
	 */
	public boolean soldierCondition() {
		return this==BuildingAreaType.None;
	}
}

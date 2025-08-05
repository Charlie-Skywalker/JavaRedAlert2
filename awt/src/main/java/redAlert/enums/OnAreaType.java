package redAlert.enums;

/**
 * 表示中心块上的单位类型
 * 游戏中统称为地面单位类型
 * 
 * 地面单位类型可以是
 * 空白、军事建筑、磅秤、磅秤+载具、中立建筑、步兵、载具、船只、树木、木桥、水泥桥、桥梁小屋、矿柱、矿石等都可以是
 * 
 * 注意复合类型，磅秤+载具、桥梁+载具、桥梁+步兵
 * 
 * 地面单位类型与CenterPoint类高度绑定,见CenterPoint.onAreaType
 * 
 * 此类已废弃
 */
@Deprecated
public enum OnAreaType {
	Empty("空白"),
	MBuiding("军事建筑"),
	WeighBridge("磅称"),//幽默命名法,磅秤,意思是坦克可以压的中心块   例如维修厂、采矿场、建设工厂的部分中心块
	Soldier("步兵"),
	Vehicle("载具"),
	Vehicle_MBuilding("磅秤+载具");//表示坦克压在重工、维修厂、采矿场上时，载具所在中心块的地面单位类型
	
	public final String desc;
	
	OnAreaType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(OnAreaType...onAreaTypes) {
		for(OnAreaType type:onAreaTypes) {
			if(this==type) {
				return true;
			}
		}
		return false;
	}
}

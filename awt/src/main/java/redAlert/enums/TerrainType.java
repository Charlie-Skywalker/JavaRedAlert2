package redAlert.enums;

/**
 * 地形类型
 * 
 * 应该明确,
 * 地图初始化时决定了一个地块的地形类型,
 * 地形初始化后,在游戏过程中是不可改变的
 * 
 */
public enum TerrainType {

	
	/**
	 * 野地就是一般粗糙的地面,绿色的草地,白色的雪地都是野地,可以种树
	 * 可以造建筑,可以陆地单位移动,有一定降速
	 */
	Rough("野地"),
	/**
	 * 道路一般有泥路和沥青路两种,载具的行驶速度较快
	 * 可以造建筑,可以陆地单位移动,无降速
	 */
	Road("道路"),
	/**
	 * 整洁的地面
	 * 可以造建筑,可以陆地单位移动,无降速
	 */
	Clear("干净地面"),
	/**
	 * 岩石,单位不能进入,不能造建筑
	 */
	Rock("岩石"),
	/**
	 * 水面
	 * 陆地单位不能进入,不能造建筑(船坞除外)
	 */
	Water("水面"),
	/**
	 * 沙滩,海军、陆地单位不能进入,不能造建筑(船坞也不能造)
	 * 只有两栖的可以进入
	 */
	Beach("沙滩");
	
	public String desc;
	
	private TerrainType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(TerrainType...terrainType) {
		for(TerrainType type:terrainType) {
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
		return this.in(Rough,Road,Clear);
	}
	/**
	 * 满足载具进入的必要条件
	 * 不考虑两栖载具与船
	 */
	public boolean vehicleCondition() {
		return this.in(Rough,Road,Clear);
	}
	/**
	 * 满足步兵进入的必要条件
	 * 不考虑两栖步兵
	 */
	public boolean soldierCondition() {
		return this.in(Rough,Road,Clear);
	}
}

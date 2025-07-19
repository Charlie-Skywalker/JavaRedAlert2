package redAlert.enums;

/**
 * 地面覆盖物类型
 */
public enum OverlayType {
	
	/**
	 * 无覆盖物
	 */
	None("无"),
	/**
	 * 树木 可以打掉
	 */
	Tree("树木"),
	/**
	 * 浮桥  可以打掉
	 */
	LoBridge("浮桥"),
	/**
	 * 高桥  可以打掉
	 */
	Bridge("高桥"),
	/**
	 * 矿石
	 * 泰伯利亚矿是游戏命令与征服中虚构的来自外星的神秘晶体。
	 */
	Tiberium("矿石"),
	/**
	 * 钻井器
	 */
	TibTre("矿柱"),
	/**
	 * 岩石
	 * 一类不能被打掉的物品统称,比如墓碑
	 */
	Rock("岩石"),
	/**
	 * 软物
	 * 一类可以被打掉的物品统称,比如围墙,沙袋,铁丝网
	 */
	Soft("软东西"),
	/**
	 * 工具箱
	 */
	Crate("箱子");
	
	public String desc;
	
	private OverlayType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(OverlayType...onAreaTypes) {
		for(OverlayType type:onAreaTypes) {
			if(this==type) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 满足建筑摆放的必要条件
	 */
	public boolean buildingCondition() {
		return this==OverlayType.None;
	}
	/**
	 * 满足载具进入的必要条件
	 * 不考虑两栖载具与船
	 */
	public boolean vehicleCondition() {
		return this.in(None,LoBridge,Bridge,Tiberium,Crate);
	}
	/**
	 * 满足步兵进入的必要条件
	 * 不考虑两栖步兵
	 * 树木是否能进入,需要实验!!
	 */
	public boolean soldierCondition() {
		return this.in(None,LoBridge,Bridge,Tiberium,Crate);
	}
}

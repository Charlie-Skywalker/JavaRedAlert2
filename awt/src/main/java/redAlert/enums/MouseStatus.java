package redAlert.enums;

/**
 * 鼠标状态枚举
 */
public enum MouseStatus {
	/**
	 * 空闲状态  鼠标指针为默认样式
	 */
	Idle("空闲"),
	/**
	 * 建造状态  鼠标指针也是默认样式
	 * 区别是会显示预建造红绿菱形块   建造状态按右键回到空闲状态
	 */
	Construct("建造"),
	/**
	 * 选中状态  鼠标指针也是默认样式
	 * 当鼠标左键按下并拖动时,鼠标进入选中状态  左键松开后回到空闲状态
	 */
	Select("选中"),
	/**
	 * 预选状态  鼠标指针为singleSelect样式
	 * 当鼠标为空闲状态时,放在单位上进入此状态  
	 * 当鼠标移出单位上时,回到空闲状态
	 */
	PreSingleSelect("预单选单位或建筑"),
	/**
	 * 选中移动单位
	 */
	UnitMove("选中可移动单位"),
	/**
	 * 允许部署
	 */
	UnitExpand("单位部署"),
	/**
	 * 不允许部署
	 */
	UnitNoExpand("禁止部署"),
	/**
	 * 禁止移动
	 */
	UnitNoMove("禁止移动"),
	/**
	 * 卖建筑
	 */
	Sell("卖建筑"),
	/**
	 * 禁止卖建筑
	 */
	NoSell("禁卖建筑");
	
	private final String cnDesc;
	
	MouseStatus(String cnDesc){
		this.cnDesc = cnDesc;
	}
}

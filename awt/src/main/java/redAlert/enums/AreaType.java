package redAlert.enums;

/**
 * 地图中最小菱形格的区域类型，代码中统称位为：中心块
 * 
 * 中心块是在地图加载后就确定的,建筑、移动单位等可以放置在中心块上，但不会改变中心块的值，游戏过程中一般不允许随意修改
 * 
 * 中心块与CenterPoint类高度绑定,见CenterPoint.areaType
 * 
 * 此类已废弃
 */
@Deprecated
public enum AreaType {
	/**
	 * 中心块有几种情况
	 * 地图无移动单位、无军事建筑的情况下地表的类型
	 * 平地、水面、斜坡、岩石、废墟(油田、医院等建筑破坏后的地形，不可建造建筑物)
	 * 公路(移动速度较快)、
	 * 
	 */
	Flat("平地"),Water("水面"),Rock("岩石"),Slope("斜面");
	
	public final String desc;
	
	AreaType(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(AreaType...areaTypes) {
		for(AreaType type:areaTypes) {
			if(this==type) {
				return true;
			}
		}
		return false;
		
	}
}

package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 火箭飞行兵图标
 */
public class AfJjetBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfJjetBtn() {
		super(SoldierEnum.AfJjet);
	}
	
	/**
	 * 火箭飞行兵图标的展示条件
	 * 有盟军兵营 &&  (有空指部或雷达(雷达还没有写))
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfAirc.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

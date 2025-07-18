package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 美国大兵图标
 */
public class AfGiBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfGiBtn() {
		super(SoldierEnum.AfGi);
	}

	/**
	 * 美国大兵图标的展示条件
	 * 有盟军兵营
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void makeOneSoldier() {
		ShapeUnitResourceCenter.addSoldierFromPile(SoldierEnum.AfGi);
	}
}

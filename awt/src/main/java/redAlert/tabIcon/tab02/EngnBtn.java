package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 工程师图标
 */
public class EngnBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public EngnBtn() {
		super(SoldierEnum.Engn);
	}
	
	/**
	 * 美国大兵图标的展示条件
	 * 有兵营(不区分阵营)
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
		ShapeUnitResourceCenter.addSoldierFromPile(SoldierEnum.Engn);
	}
}

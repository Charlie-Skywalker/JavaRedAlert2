package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfTech;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 谭雅图标
 */
public class AfTanyBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfTanyBtn() {
		super(SoldierEnum.AfTany);
	}

	/**
	 * 谭雅图标的展示条件
	 * 有盟军兵营 有盟军作战实验室
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfTech.class)
				) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void makeOneSoldier() {
		ShapeUnitResourceCenter.addSoldierFromPile(SoldierEnum.AfTany);
	}
}

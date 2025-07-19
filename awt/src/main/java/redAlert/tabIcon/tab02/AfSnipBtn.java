package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 狙击手图标
 */
public class AfSnipBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfSnipBtn() {
		super(SoldierEnum.AfSnip);
	}

	/**
	 * 狙击手图标的展示条件
	 * 有盟军兵营 有空指部
	 * 有盟军兵营有雷达的情况下是否可以未验证
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

	@Override
	public void makeOneSoldier() {
		ShapeUnitResourceCenter.addSoldierFromPile(SoldierEnum.AfSnip);
	}
	
	
}

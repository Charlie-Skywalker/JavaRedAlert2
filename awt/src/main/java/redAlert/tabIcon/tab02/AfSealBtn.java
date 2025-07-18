package redAlert.tabIcon.tab02;

import redAlert.enums.SoldierEnum;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfTech;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;

/**
 * 海豹部队图标
 */
public class AfSealBtn extends Tab02ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfSealBtn() {
		super(SoldierEnum.AfSeal);
	}
	
	/**
	 * 海豹部队图标的展示条件
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

}

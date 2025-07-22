package redAlert.tabIcon.tab01;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab01ConstIcon;

/**
 * 盟军围墙图标
 */
public class AfWallConstBtn extends Tab01ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfWallConstBtn() {
		super(ConstConfig.AfWall);
	}
	
	/**
	 * 围墙图标的展示条件
	 * 有盟军基地  有盟军兵营
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

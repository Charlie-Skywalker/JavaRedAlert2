package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPowr;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 盟军兵营图标
 */
public class AfPileConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfPileConstBtn() {
		super(ConstConfig.AfPile);
	}

	/**
	 * 盟军兵营图标的展示条件
	 * 有盟军基地 有发电厂(磁能电厂、核电厂都是电厂,这两个建筑还没写)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPowr.class)) {
			return true;
		}
		return false;
	}
	
	
}

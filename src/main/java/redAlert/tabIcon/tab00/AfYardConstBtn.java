package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPowr;
import redAlert.militaryBuildings.AfRefn;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 盟军造船厂 图标
 */
public class AfYardConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfYardConstBtn() {
		super(ConstConfig.AfYard);
	}
	
	/**
	 * 盟军造船厂图标的展示条件
	 * 有盟军基地  有电厂、矿石精炼厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPowr.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfRefn.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

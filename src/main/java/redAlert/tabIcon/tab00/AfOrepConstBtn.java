package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfTech;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 矿石精炼器图标
 */
public class AfOrepConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfOrepConstBtn() {
		super(ConstConfig.AfOrep);
	}
	
	/**
	 * 矿石精炼器图标的展示条件
	 * 有盟军基地  有盟军作战实验室
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfTech.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

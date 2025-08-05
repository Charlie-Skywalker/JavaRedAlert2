package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 盟军作战实验室图标
 */
public class AfTechConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfTechConstBtn() {
		super(ConstConfig.AfTech);
	}
	
	/**
	 * 盟军作战实验室图标的展示条件
	 * 有盟军基地  有空指部，有盟军战车工厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfAirc.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

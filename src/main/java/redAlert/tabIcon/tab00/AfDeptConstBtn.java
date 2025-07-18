package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 盟军维修厂图标
 */
public class AfDeptConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfDeptConstBtn() {
		super(ConstConfig.AfDept);
	}
	
	/**
	 * 盟军维修厂图标的展示条件
	 * 有盟军基地  有盟军战车工厂 (区分阵营)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

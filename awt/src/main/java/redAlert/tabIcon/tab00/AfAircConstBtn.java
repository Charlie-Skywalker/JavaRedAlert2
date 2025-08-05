package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfRefn;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 空指部图标
 */
public class AfAircConstBtn extends Tab00ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfAircConstBtn() {	
		super(ConstConfig.AfAirc);
	}

	/**
	 * 空指部图标的展示条件
	 * 有盟军基地  有盟军矿石精炼厂(区分阵营)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfRefn.class)
				) {
			return true;
		}else {
			return false;
		}
	}
	
}
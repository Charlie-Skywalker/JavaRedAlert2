package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfYard;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 两栖运输艇图标
 */
public class AfLandIcon extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfLandIcon() {
		super(VehicleEnum.AfLand);
	}
	
	/**
	 * 两栖运输艇图标的展示条件
	 * 有盟军造船厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfYard.class)) {
			return true;
		}else {
			return false;
		}
	}

}

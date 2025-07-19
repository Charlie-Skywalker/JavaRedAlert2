package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfYard;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 驱逐舰图标
 */
public class AfDestBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfDestBtn() {
		super(VehicleEnum.AfDest);
	}

	/**
	 * 驱逐舰图标的展示条件
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

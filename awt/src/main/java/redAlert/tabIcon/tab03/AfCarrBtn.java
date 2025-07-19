package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfYard;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 航空母舰图标
 */
public class AfCarrBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfCarrBtn() {
		super(VehicleEnum.AfCarr);
	}

	/**
	 * 航空母舰图标的展示条件
	 * 有盟军造船厂  &&  (有盟军作战实验室或苏军作战实验室)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfYard.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfTech.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

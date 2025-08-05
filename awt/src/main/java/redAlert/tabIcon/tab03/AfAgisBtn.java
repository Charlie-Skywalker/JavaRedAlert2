package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfYard;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 神盾巡洋舰图标
 */
public class AfAgisBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfAgisBtn() {
		super(VehicleEnum.AfAgis);
	}

	/**
	 * 神盾巡洋舰图标的展示条件
	 * 有盟军造船厂  &&  (有雷达或空指部)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfYard.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfAirc.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfYard;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 海豚图标
 */
public class AfDlphBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfDlphBtn() {
		super(VehicleEnum.AfDlph);
	}

	/**
	 * 超时空矿车图标的展示条件
	 * 有盟军造船厂  &&  有盟军作战实验室
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

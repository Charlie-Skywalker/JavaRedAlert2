package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 多功能步兵车图标
 */
public class AfIfvBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfIfvBtn() {
		super(VehicleEnum.AfIfv);
	}

	/**
	 * 多功能步兵车图标的展示条件
	 * 有盟军建设工厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)) {
			return true;
		}else {
			return false;
		}
	}
	
	@Override
	public void makeOneVehicle() {
		ShapeUnitResourceCenter.addVehicleFromWeap(this.vehicleInfo);
	}
}

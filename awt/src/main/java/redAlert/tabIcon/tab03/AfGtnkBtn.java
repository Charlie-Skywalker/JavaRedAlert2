package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 灰熊坦克图标
 */
public class AfGtnkBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfGtnkBtn() {
		super(VehicleEnum.AfGtnk);
	}

	/**
	 * 灰熊坦克图标的展示条件
	 * 有盟军建设工厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				) {
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

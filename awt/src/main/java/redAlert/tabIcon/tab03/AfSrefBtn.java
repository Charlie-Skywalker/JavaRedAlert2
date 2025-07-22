package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 光棱坦克图标
 */
public class AfSrefBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfSrefBtn() {
		super(VehicleEnum.AfSref);
	}
	
	/**
	 * 光棱坦克图标的展示条件
	 * 有盟军建设工厂 有盟军作战实验室
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfTech.class)
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

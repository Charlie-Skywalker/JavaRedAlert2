package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfDept;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 盟军基地车图标
 */
public class AfMcvBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfMcvBtn() {
		super(VehicleEnum.AfMcv);
	}

	/**
	 * 盟军基地车图标的展示条件
	 * 有盟军建设工厂 有盟军修理厂
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfDept.class)
				) {
			return true;
		}else {
			return false;
		}
	}
	
}

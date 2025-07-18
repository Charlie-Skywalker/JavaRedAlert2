package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfRefn;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 超时空矿车
 */
public class AfAhrvBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfAhrvBtn() {
		super(VehicleEnum.AfAhrv);
	}
	
	/**
	 * 超时空矿车图标的展示条件
	 * 有盟军建设工厂  &&  (有盟军采矿场或苏军采矿场)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfWeap.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfRefn.class)
				) {
			return true;
		}else {
			return false;
		}
	}

}

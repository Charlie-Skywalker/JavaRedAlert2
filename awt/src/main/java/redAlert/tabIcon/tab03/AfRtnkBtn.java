package redAlert.tabIcon.tab03;

import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfWeap;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 幻影坦克图标
 */
public class AfRtnkBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfRtnkBtn() {
		super(VehicleEnum.AfRtnk);
	}

	/**
	 * 幻影图标的展示条件
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
}

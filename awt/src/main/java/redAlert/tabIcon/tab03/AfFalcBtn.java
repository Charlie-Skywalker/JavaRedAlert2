package redAlert.tabIcon.tab03;

import redAlert.GlobalConfig;
import redAlert.enums.Country;
import redAlert.enums.VehicleEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab03ConstIcon;

/**
 * 入侵者战机图标
 */
public class AfFalcBtn extends Tab03ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfFalcBtn() {
		super(VehicleEnum.AfFalc);
	}

	/**
	 * 入侵者战机图标的展示条件
	 * 有空指部 并且玩家不是韩国
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfAirc.class) && GlobalConfig.country!=Country.Korea) {
			return true;
		}else {
			return false;
		}
	}

}

package redAlert.tabIcon.tab01;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfTech;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab01ConstIcon;

/**
 * 裂缝产生器图标
 */
public class AfGapConstBtn extends Tab01ConstIcon{
	private static final long serialVersionUID = 1L;
	
	public AfGapConstBtn() {
		super(ConstConfig.AfGap);
	}
	
	/**
	 * 裂缝产生器图标的展示条件
	 * 有盟军基地  有盟军作战实验室
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfTech.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

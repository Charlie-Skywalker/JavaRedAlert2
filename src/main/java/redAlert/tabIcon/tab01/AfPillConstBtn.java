package redAlert.tabIcon.tab01;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab01ConstIcon;

/**
 * 机枪碉堡创建按钮
 */
public class AfPillConstBtn extends Tab01ConstIcon {

	private static final long serialVersionUID = 1L;
	
	public AfPillConstBtn() {
		super(ConstConfig.AfPill);
	}
	
	/**
	 * 机枪碉堡图标的展示条件
	 * 有盟军基地  有兵营(不区分阵营)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

package redAlert.tabIcon.tab00;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfRefn;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;

/**
 * 盟军战车工厂图标
 */
public class AfWeapConstBtn extends Tab00ConstIcon{

	private static final long serialVersionUID = 1L;
	
	public AfWeapConstBtn() {
		super(ConstConfig.AfWeap);
	}
	
	/**
	 * 盟军战车工厂图标的展示条件
	 * 有盟军基地  有兵营、矿石精炼厂 (不分阵营)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfRefn.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

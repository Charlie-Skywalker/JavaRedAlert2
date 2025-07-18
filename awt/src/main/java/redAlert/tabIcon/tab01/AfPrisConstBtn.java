package redAlert.tabIcon.tab01;

import redAlert.enums.ConstConfig;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfPowr;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab01ConstIcon;

/**
 * 光棱塔图标
 */
public class AfPrisConstBtn extends Tab01ConstIcon{
	
	private static final long serialVersionUID = 1L;
	
	public AfPrisConstBtn() {	
		super(ConstConfig.AfPris);
	}
	
	/**
	 * 光棱塔图标的展示条件
	 * 有盟军基地  有空指部、有发电厂(磁能电厂、核电厂都是电厂,这两个建筑还没写)
	 */
	@Override
	public boolean isDisplay() {
		if(ShapeUnitResourceCenter.containsBuildingClass(AfCnst.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfPowr.class)
				&& ShapeUnitResourceCenter.containsBuildingClass(AfAirc.class)
				) {
			return true;
		}else {
			return false;
		}
	}
}

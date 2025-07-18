package redAlert.shapeObjects.vehicle;

import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.TankTurret;

/**
 * 多功能步兵车炮塔
 */
public class IfvTurret extends TankTurret{

	public IfvTurret(Ifv tank) {
		super(tank, "fvtur");
	}

	@Override
	public void attack(Building building) {
		// TODO Auto-generated method stub
		
	}
	
	
}

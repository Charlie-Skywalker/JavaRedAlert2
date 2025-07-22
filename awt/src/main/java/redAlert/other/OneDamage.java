package redAlert.other;

import redAlert.Constructor;
import redAlert.enums.BuildingAreaType;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.Attackable;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.TankTurret;
import redAlert.shapeObjects.Vehicle;
import redAlert.shapeObjects.animation.TankShell;

/**
 * 一次伤害
 */
public class OneDamage {

	/**
	 * 攻击者   比如某某坦克
	 */
	public Attackable attacker;
	
	/**
	 * 攻击实体   比如炮弹
	 */
	public TankShell shell;
	/**
	 * 被攻击者
	 */
	public ShapeUnit victim;
	/**
	 * 打击类型
	 * 比如 炮弹攻击  枪击  核弹  AOE伤害等等
	 * type0  单次攻击单体伤害
	 * 
	 * 
	 */
	public String type;
	/**
	 * 伤害值
	 */
	public int damageValue;
	
	//伤害结算
	public void settle() {
		if(type.equals("type0")) {//单次单体伤害
			if(victim instanceof Building) {
				Building building = (Building)victim;
				int curHp = building.getBloodBar().curHp;
				curHp-=damageValue;
				building.getBloodBar().setCurHp(curHp);
				Constructor.randomPlayOneMusic("gdamag1a","gdamag1b","gdamag1c","gdamag1d","gdamag1e");
				
				if(curHp<=0) {
					building.setVisible(false);
					building.setEnd(true);
					building.getCurCenterPoint().setBuilding(null);//上边的物品
					ShapeUnitResourceCenter.removeOneBuilding(building);
					building.getCurCenterPoint().setBuilding(null);
					building.getCurCenterPoint().buildingAreaType = BuildingAreaType.None;
					
					Constructor.randomPlayOneMusic("bgendiea","bgendieb","bgendiec","bgendied","bgendiee","bgendief");
					//坦克炮塔的状态标记为非进攻
					
					if(attacker instanceof Vehicle){
						Vehicle v= (Vehicle)attacker;
						TankTurret turret = v.getTurret();
						
						turret.setAttackBuilding(null);
						
					}
					
				}
				
				shell.setDamageSettled(true);//伤害结算完毕
				
			}
		}
	}
	
	
	public OneDamage(Attackable attacker,TankShell shell,ShapeUnit victim,String type,int damageValue) {
		
		this.attacker = attacker;
		this.shell = shell;
		this.victim = victim;
		this.type = type;
		this.damageValue = damageValue;
		
		
	}
	
}

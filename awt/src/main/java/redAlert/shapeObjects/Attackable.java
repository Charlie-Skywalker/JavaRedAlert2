package redAlert.shapeObjects;

/**
 * 可开火的单位
 */
public interface Attackable {

	/**
	 * 是否具有攻击的能力
	 * 普通载具由攻击能力,运兵船、基地车、盟军矿车无攻击能力
	 */
	public boolean isAttackable();
	
	
	//向建筑开火
	public void attack(Building building);
}

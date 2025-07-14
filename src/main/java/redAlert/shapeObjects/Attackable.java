package redAlert.shapeObjects;

/**
 * 可开火的单位
 */
public interface Attackable {

	/**
	 * 是否具有攻击的能力
	 * 普通载具由攻击能力,运兵船、基地车、盟军矿车无攻击能力
	 */
	boolean isAttackable();

	/**
	 * 开火
	 */
	void attack();

	/**
	 * 设置开火目标
	 */
	void setAttackTarget(Bloodable attackTarget);
}

package redAlert.shapeObjects;

/**
 * 表示一些可以展开的单位
 * 
 * 比如基地车、美国大兵、多功能步兵车、辐射工兵
 *
 * 基地车、坦克类等部署后选中再移动可以取消部署
 * 载具类单位部署表示释放成员，成员为空不可以部署，不可以取消部署
 * 可部署兵种部署后选中不可以移动，只能选中后取消部署
 * 周围攻击类单位部署后向周围攻击一次然后恢复
 */
public interface Expandable {

	/**
	 * 展开
	 */
	public void expand();
	/**
	 * 收缩
	 */
	public void unexpand();
	/**
	 * 当前是否能够展开
	 */
	public boolean isExpandable();

	/**
	 * 当前是否能够收缩
	 */
	public boolean isUnExpandable();
	
	
}

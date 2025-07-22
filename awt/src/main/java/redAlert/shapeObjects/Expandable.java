package redAlert.shapeObjects;

/**
 * 表示一些可以展开的单位
 * 
 * 比如基地车、美国大兵、多功能步兵车、辐射工兵
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
	
	
}

package redAlert.other;

/**
 * 可移动单位的血条抽象类
 */
public abstract class MovableUnitBloodBar extends BloodBar {

	/**
	 * 绘制优先级   优先于移动线条  优先于三级标志
	 */
	public MovableUnitBloodBar() {
		super.setPriority(60);
		super.setVisible(false);
	}
	
	public final int borderWidth = 1;//血条的边框厚度
	
	public final int borderHeight = 4;//血条的边框高度
}

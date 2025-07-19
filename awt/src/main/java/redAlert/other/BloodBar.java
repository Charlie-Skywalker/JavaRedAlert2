package redAlert.other;

import java.awt.Color;

import redAlert.shapeObjects.ShapeUnit;

/**
 * 血条
 * 血条  边框1个像素  高度2个像素
 * 中间采用浅绿和深绿的交替像素
 * 坦克的血条一共34个像素  17个明暗交替
 * 半血以上是绿色交替
 * 半血至1/4是黄色交替
 * 1/4以下是红色
 * 血最少的时候只有一个像素红
 */
public abstract class BloodBar extends ShapeUnit{
	
	/**
	 * 最大血块个数
	 */
	public int maxBloodNum;
	/**
	 * 当前血块个数
	 */
	public int curBloodNum;
	/**
	 * 最高血量
	 */
	public int maxHp;
	/**
	 * 当前血量
	 */
	public int curHp;
	
	public final Color gColor = new Color(0,128,0);//健康状态下的暗血条颜色
	public final Color yColor = new Color(240,240,0);//亚健康状态下的暗血条颜色
	public final Color rColor = new Color(128,0,0);//不健康状态下的暗血条颜色

	public int getCurHp() {
		return curHp;
	}
	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}
	public int getMaxBloodNum() {
		return maxBloodNum;
	}
	public void setMaxBloodNum(int maxBloodNum) {
		this.maxBloodNum = maxBloodNum;
	}
	public int getMaxHp() {
		return maxHp;
	}
	public void setMaxHp(int maxHp) {
		this.maxHp = maxHp;
	}
	public int getCurBloodNum() {
		return curBloodNum;
	}
	public void setCurBloodNum(int curBloodNum) {
		this.curBloodNum = curBloodNum;
	}
	
	/**
	 * 重绘血量条
	 */
	public abstract void rePaintBloodBar();
	/**
	 * 根据当前血量和最大血量  计算当前应展示的剩余血块个数
	 */
	public abstract int calCurBloodNum();
	
}

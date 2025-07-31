package redAlert.other;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.MovePlan;

/**
 * 控制单位移动时短暂显示的位移连线
 */
public class MoveLine extends ShapeUnit{

	/**
	 * 线颜色
	 */
	public static Color lineColor = new Color(0,192,0);
	/**
	 * 线条样式
	 */
	public static BasicStroke stroke = new BasicStroke(1);
	/**
	 * 端点方块大小
	 */
	public static int radius = 3;//端点圆的半径
	/**
	 * 线条显示的帧数
	 */
	public int remainLife = 20;
	/**
	 * 移动计划
	 * 包含了可移动单位、终点信息
	 */
	public List<MovePlan> movePlans = null;
	
	/**
	 *
	 */
	public MoveLine(List<MovePlan> movePlans) {
		this.movePlans = movePlans;
		super.curFrame = new ShapeUnitFrame();
		curFrame.setImg(new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB));//放置一个空图片
		super.frameSpeed = 2;
		super.priority = 70;//连接线的优先级很低   比三星标志都低  但比鼠标要高
	}
	
	@Override
	public void calculateNextFrame() {
		
		//只进行短暂的显示
		remainLife--;
		if(remainLife<0) {
			super.end = true;
			super.isVisible = false;
		}
	}

	public List<MovePlan> getMovePlans() {
		return movePlans;
	}
	public void setMovePlans(List<MovePlan> movePlans) {
		this.movePlans = movePlans;
	}

	
	
}

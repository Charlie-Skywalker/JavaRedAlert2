package redAlert.other;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import redAlert.MainPanel;
import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.MovePlan;
import redAlert.utils.CanvasPainter;

/**
 * 控制单位移动时短暂显示的位移连线
 */
public class MoveLine extends ShapeUnit{

	/**
	 * 画线用的画板
	 */
	public static BufferedImage lineImage = new BufferedImage(MainPanel.gameMapWidth,MainPanel.gameMapHeight,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 线颜色
	 */
	public static Color lineColor = new Color(0,192,0);
	/**
	 * 线条样式
	 */
	public static BasicStroke stroke = new BasicStroke(1);
	/**
	 * 起始点
	 */
	public int startx,starty;
	/**
	 * 终点
	 */
	public CenterPoint end;
	/**
	 * 对应的移动单位
	 */
	public MovableUnit unit;
	
	public int radius = 3;//端点圆的半径
	/**
	 * 线条显示的帧数
	 */
	public int remainLife = 20;
	/**
	 * 
	 */
	public List<MovePlan> movePlans = null;
	
	/*
	public MoveLine(int startx,int starty,CenterPoint end,MovableUnit unit) {
		this.startx = startx;
		this.starty = starty;
		this.end = end;
		this.unit = unit;
		int endx = end.getX();
		int endy = end.getY();
		if(startx<endx) {
			super.positionX = startx;
		}else {
			super.positionX = endx;
		}
		if(starty<endy) {
			super.positionY = starty;
		}else {
			super.positionY = endy;
		}
		
		super.curFrame = new ShapeUnitFrame();
		
		BufferedImage image = new BufferedImage( Math.abs(end.getX()-startx) +1+5,  Math.abs(end.getY()-starty)+1+5,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(lineColor);
		g2d.setStroke(stroke);
		g2d.drawLine(startx-positionX+1, starty-positionY+1, endx-positionX+1, endy-positionY+1);//画连接线
		g2d.fillRect(startx-positionX, starty-positionY, radius, radius);//画端点
		g2d.fillRect(endx-positionX, endy-positionY, radius, radius);//画端点
		g2d.dispose();
		curFrame.setImg(image);
		
		super.frameSpeed = unit.getFrameSpeed();
		super.priority = 70;//连接线的优先级很低   比三星标志都低  但比鼠标要高
	}
	*/
	
	/**
	 * 一次画多条线
	 */
	public MoveLine(List<MovePlan> movePlans) {
		this.movePlans = movePlans;
		CanvasPainter.clearImage(lineImage);//防止反复创建图画对象
		super.curFrame = new ShapeUnitFrame();
		for(int i=0;i<movePlans.size();i++) {
			MovePlan plan = movePlans.get(i);
			int startx = plan.getUnit().getCurCenterPoint().getX();
			int starty = plan.getUnit().getCurCenterPoint().getY();
			int endx = plan.getTargetCp().getX();
			int endy = plan.getTargetCp().getY();
			Graphics2D g2d = lineImage.createGraphics();
			g2d.setColor(lineColor);
			g2d.setStroke(stroke);
			g2d.drawLine(startx, starty, endx, endy);//画连接线
			g2d.fillRect(startx, starty, radius, radius);//画端点
			g2d.fillRect(endx, endy, radius, radius);//画端点
		}
		curFrame.setImg(lineImage);
		super.frameSpeed = 2;
		super.priority = 70;//连接线的优先级很低   比三星标志都低  但比鼠标要高
		
	}
	
	
	
	
	@Override
	public void calculateNextFrame() {
//		int startx = unit.getPositionX()+unit.getCenterOffX();
//		int starty = unit.getPositionY()+unit.getCenterOffY();
//		int endx = end.getX();
//		int endy = end.getY();
//		if(startx<endx) {
//			super.positionX = startx-1;
//		}else {
//			super.positionX = endx-1;
//		}
//		if(starty<endy) {
//			super.positionY = starty-1;
//		}else {
//			super.positionY = endy-1;
//		}
//		
//		
//		BufferedImage image = new BufferedImage( Math.abs(end.getX()-startx) +1+5,  Math.abs(end.getY()-starty)+1+5,BufferedImage.TYPE_INT_ARGB);
//		Graphics2D g2d = image.createGraphics();
//		g2d.setColor(lineColor);
//		g2d.setStroke(stroke);
//		g2d.drawLine(startx-positionX+1, starty-positionY+1, endx-positionX+1, endy-positionY+1);
//		g2d.fillRect(startx-positionX, starty-positionY, radius, radius);
//		g2d.fillRect(endx-positionX, endy-positionY, radius, radius);
//		g2d.dispose();
//		curFrame.setImg(image);
		
		CanvasPainter.clearImage(lineImage);//防止反复创建图画对象
		for(int i=0;i<movePlans.size();i++) {
			MovePlan plan = movePlans.get(i);
			int startx = plan.getUnit().getPositionX()+ plan.getUnit().getCenterOffX();
			int starty = plan.getUnit().getPositionY()+ plan.getUnit().getCenterOffY();
			int endx = plan.getTargetCp().getX();
			int endy = plan.getTargetCp().getY();
			Graphics2D g2d = lineImage.createGraphics();
			g2d.setColor(lineColor);
			g2d.setStroke(stroke);
			g2d.drawLine(startx, starty, endx, endy);//画连接线
			g2d.fillRect(startx, starty, radius, radius);//画端点
			g2d.fillRect(endx, endy, radius, radius);//画端点
		}
		curFrame.setImg(lineImage);
		
		
		//只进行短暂的显示
		remainLife--;
		if(remainLife<0) {
			super.end = true;
			curFrame.setImg(null);//取消引用  让GC可以回收？好像不需要？
		}
	}

	
	
}

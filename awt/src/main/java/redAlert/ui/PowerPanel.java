package redAlert.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import redAlert.OptionsPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.utils.CanvasPainter;

/**
 * 电力线的面板
 */
public class PowerPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 电力线面板的宽度
	 * 此值也决定了右侧单位选择面板的宽度
	 */
	public static final int width = 17;
	/**
	 * 整个电力线面板的高度
	 */
	public int height = OptionsPanel.side2Num*50;
	/**
	 * 电力线单个背板
	 * 17像素宽 50像素高
	 */
	public BufferedImage singleBackground = null;
	/**
	 * 电力线背景  不允许修改
	 */
	public BufferedImage background = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 绘制好的
	 */
	public BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
	
	
	public PowerPanel() {
		initResource();
	}
	
	public void initResource() {
		List<ShapeUnitFrame> side2Frame = ShpResourceCenter.loadShpResource("side2", "sidebar", false);
		BufferedImage side2 = side2Frame.get(0).getImg();//双人位背板
		singleBackground = side2.getSubimage(0, 0, width, side2.getHeight());//截取属于电力线背板的部分
		
		Graphics2D g2d =  background.createGraphics();
		for(int i=0;i<12;i++) {
			g2d.drawImage(side2, 0, i*50, null);
		}
		g2d.dispose();
		
		Graphics2D g = image.createGraphics();
		g.drawImage(background, 0, 0, null);
		g.dispose();
		
		
		this.setPreferredSize(new Dimension(width,height));
		
		
		//电力横线
		List<ShapeUnitFrame> topFrame = ShpResourceCenter.loadShpResource("powerp", "sidebar", false);
		BufferedImage black = topFrame.get(0).getImg();
		BufferedImage green = topFrame.get(1).getImg();
		greenU = green.getSubimage(0, 0, 12, 1);
		greenD = green.getSubimage(0, 1, 12, 1);
		BufferedImage yellow = topFrame.get(2).getImg();
		yellowU = yellow.getSubimage(0, 0, 12, 1);
		yellowD = yellow.getSubimage(0, 1, 12, 1);
		BufferedImage red = topFrame.get(3).getImg();
		redU = red.getSubimage(0, 0, 12, 1);
		redD = red.getSubimage(0, 1, 12, 1);
		BufferedImage gray = topFrame.get(4).getImg();
		gray2 = gray.getSubimage(0, 0, 12, 1);
		
		addTimer();
	}
	
	
	
	public int targetRedNum = 10;
	public int targetYellowNum = 3;
	public int targetGreenNum = 20;
	
	public int curRedNum = 0;
	public int curYellowNum = 0;
	public int curGreenNum = 0;
	
	
	public BufferedImage yellowU = null;
	public BufferedImage yellowD = null;
	public BufferedImage redU = null;
	public BufferedImage redD = null;
	public BufferedImage greenU = null;
	public BufferedImage greenD = null;
	public BufferedImage gray2 = null;
	
	public BufferedImage getImage(String a){
		if("rd".equals(a)) {
			return redD;
		}
		if("ru".equals(a)) {
			return redU;
		}
		if("gray".equals(a)) {
			return gray2;
		}
		if("yu".equals(a)) {
			return yellowU;
		}
		if("yd".equals(a)) {
			return yellowD;
		}
		if("gu".equals(a)) {
			return greenU;
		}
		if("gd".equals(a)) {
			return greenD;
		}
		return null;
	}
	
	//从数据中心获取电力数据   抽象成红黄绿线数量
	//实际上target就是当前的电力情况
	public void getTargetNum() {
		
	}
	
	
	/**
	 * 画电力线方法
	 */
	public void drawPower() {
		
		
		//假设红色表示负载
		//黄和绿表示剩余  黄色最少有3根
		ShapeUnitResourceCenter.calculatePowerInfo();
		int powerLoad = ShapeUnitResourceCenter.powerLoad;
		int powerGeneration = ShapeUnitResourceCenter.powerGeneration;
		
		//计算电力线总个数
		int total = powerGeneration/10;
		//12个双人位 最多展示200格电力  2*12/3+48/3*12=200
		if(total>200) {
			total = 200;
		}
		
		//计算红黄绿电力线个数
		targetRedNum = (int)(total*( ((float)powerLoad)/powerGeneration));
		int greenAndYellow = total-targetRedNum;
		if(greenAndYellow<=0) {
			targetYellowNum = 0;
			targetGreenNum = 0;
		}else if(greenAndYellow>0 && greenAndYellow<=3) {
			targetYellowNum = greenAndYellow;
			targetGreenNum = 0;
		}else if(greenAndYellow>3) {
			targetYellowNum = 3;
			targetGreenNum = greenAndYellow - 3;
		}
		
		
		//红黄绿电力线变化效果
		if(curRedNum<targetRedNum) {//负载增加的时候  要考虑剩余电力的减少
			curRedNum++;
			if(curGreenNum>targetGreenNum) {
				curGreenNum--;
			}else {
				if(curYellowNum>targetYellowNum) {
					curYellowNum--;
				}
			}
		}else if(curYellowNum<targetYellowNum) {
			curYellowNum++;
		}else if(curGreenNum<targetGreenNum) {
			curGreenNum++;
		}else if(curRedNum>targetRedNum) {
			curRedNum--;
		}else if(curYellowNum>targetYellowNum) {
			curYellowNum--;
		}else if(curGreenNum>targetGreenNum) {
			curGreenNum--;
		}else {
			return;
		}
		
		
		List<String> ls = new ArrayList<>();
		for(int i=0;i<curRedNum;i++) {
			ls.add("rd");//暗红
			ls.add("ru");//明红
			ls.add("none");//无色
		}
		for(int i=0;i<curYellowNum;i++) {
			ls.add("yd");//暗黄
			ls.add("yu");//明黄
			ls.add("none");//无色
		}
		for(int i=0;i<curGreenNum;i++) {
			ls.add("gd");//暗绿
			ls.add("gu");//明绿
			ls.add("none");//无色
		}
		
		Graphics2D g2d = image.createGraphics();
		CanvasPainter.clearImage(image);
		g2d.drawImage(background, 0, 0, null);
		
		for(int i=0;i<ls.size();i++) {
			String colorName = ls.get(i);
			g2d.drawImage(getImage(colorName), 5, height-1-i, null);
		}
		g2d.dispose();
		
		this.repaint();
		
	}
	
	public void addTimer() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			@Override
			public void run() {
				drawPower();
			}
		};
		timer.schedule(refreshTask, 1L, 100);
	}
	
	
	/**
	 * 重绘方法  将主画板的内容绘制在窗口中
	 */
	@Override
	public void paint(Graphics g) {
		try {
			super.paint(g);
			g.clearRect(0, 0, image.getWidth(), image.getHeight());
			g.drawImage(image, 0, 0, this);//画地形
			g.dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

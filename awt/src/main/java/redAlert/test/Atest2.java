package redAlert.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.lang3.math.NumberUtils;

import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 试试红警的寻路算法
 */
public class Atest2 {

	
	public static final int ox = 32;//菱形的长半径
	public static final int oy = 16;//菱形的短半径
	
	public static final int windowWidth = 1200;
	public static final int windowHeight = 900;
	
	/**
	 * 画板上绘制辅助线格
	 */
	public static void drawGuidelines(BufferedImage guidelinesCanvas) {
		Graphics g = guidelinesCanvas.getGraphics();
		g.setColor(Color.BLACK);
		//x轴向y轴的线
		for(int i=0;i<60;i++) {
			int x1 = ox + i*ox*2;
			int y1 = 0;
			int x2 = 0;
			int y2 = oy + i*oy*2;
			g.drawLine(x1, y1, x2, y2);
		}
		//y轴向右下的线
		for(int i=0;i<100;i++) {
			int x1 = 0;
			int y1 = oy + i*oy*2;
			int max = 2000;//设计一个大于界面宽度的x坐标
			int x2 = max;
			int y2 = max/2+oy+i*oy*2;   //直线簇的公式是  f(x)=x/2+oy+i*32  根据x计算y值
			g.drawLine(x1, y1, x2, y2);
		}
		//x轴向右下的线
		for(int i=0;i<100;i++) {
			int x1 = ox + i*ox*2;
			int y1 = 0;
			int max = 2000;//设计一个大于界面高度的y坐标
			int x2 = max;
			int y2 = max/2-oy-i*oy*2;  //直线簇的公式是  f(x)=x/2-oy-i*32  根据x计算y值
			g.drawLine(x1, y1, x2, y2);
		}
	}
	
	/**
	 * 画一个单位菱形
	 */
	private static void drawRhombus(CenterPoint centerPoint,BufferedImage canvas,Color color) {
		Graphics g = canvas.getGraphics();
		g.setColor(color);
		//假设已经对CenterX CenterY做了修正
		int centerX = centerPoint.getX();
		int centerY = centerPoint.getY();
		int lx1=0,lx2=0,lx3=0,lx4=0;
		int ly1=0,ly2=0,ly3=0,ly4=0;
		lx1 = centerX-ox;
		ly1 = centerY;
		lx2 = centerX;
		ly2 = centerY-oy;
		lx3 = centerX+ox;
		ly3 = centerY;
		lx4 = centerX;
		ly4 = centerY+oy;
		int [] x = {lx1,lx2,lx3,lx4};
		int [] y = {ly1,ly2,ly3,ly4};
		Polygon p = new Polygon(x,y,4);
		g.fillPolygon(p);
	}
	
	/**
	 * 寻路算法测试
	 */
	public static void test5() {
		//1.画好界面
		JFrame jf = new JFrame("测试A*算法");
		jf.setSize(windowWidth,windowHeight);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		BufferedImage img = new BufferedImage(windowWidth,windowHeight,BufferedImage.TYPE_INT_ARGB);
		
		JPanel mp = new JPanel(){
			
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), this);
			}
		};
		mp.setLocation(0, 0);
		mp.setSize(300, 300);
		mp.setLayout(null);
		mp.setMinimumSize(new Dimension(windowWidth,windowHeight));
		mp.setPreferredSize(new Dimension(windowWidth,windowHeight));
		mp.setBackground(Color.gray);
		jf.add(BorderLayout.WEST,mp);
		jf.pack();
		
		drawGuidelines(img);
		
		mp.repaint();
		
		
		//尝试画一个小菱形
//		drawRhombus(PointUtil.getCenterPoint(100, 100), img);
		
		CenterPoint cpStart = PointUtil.getCenterPoint(32, 16);
		CenterPoint cpEnd = PointUtil.getCenterPoint(640, 320);
		
		RaPoint.myPointMap.get("64,32").setCanUse(false);
		RaPoint.myPointMap.get("64,64").setCanUse(false);
		RaPoint.myPointMap.get("96,16").setCanUse(false);
		RaPoint.myPointMap.get("96,48").setCanUse(false);
		
		drawRhombus(PointUtil.getCenterPoint(64, 32), img, Color.red);
		drawRhombus(PointUtil.getCenterPoint(64, 64), img, Color.red);
		drawRhombus(PointUtil.getCenterPoint(96, 16), img, Color.red);
		drawRhombus(PointUtil.getCenterPoint(96, 48), img, Color.red);
		
		
		
		RaPoint start = RaPoint.myPointMap.get(cpStart.x+","+cpStart.y);
		RaPoint end = RaPoint.myPointMap.get(cpEnd.x+","+cpEnd.y);
		start.curPrice = 0;
		
		xunlu(start,end);
		
		if(foundWay) {
			List<RaPoint> luxian = new ArrayList<>();
			
			RaPoint last = end;
			luxian.add(end);
			while(true) {
				last = last.lastPoint;
				if(last.equals(start)) {
					luxian.add(last);
					break;
				}else {
					luxian.add(last);
				}
			}
			
			Collections.reverse(luxian);
			//画菱形
			for(RaPoint po:luxian) {
				
				drawRhombus(PointUtil.getCenterPoint(po.x, po.y), img, Color.green);
				try {
					Thread.sleep(300);
				}catch (Exception e) {
					
				}
				
				mp.repaint();
			}
			
			mp.repaint();
		}else {
			System.out.println("没有路");
		}
		
		
		mp.repaint();
		
		
	}
	
	public static HashSet<RaPoint> haveGetSet = new HashSet<>();
	public static boolean foundWay = false;
	
	public static void xunlu(RaPoint start,RaPoint end) {
		RaPoint myL = start.getLeft();
		RaPoint myR = start.getRight();
		RaPoint myU = start.getUp();
		RaPoint myD = start.getDown();
		
		RaPoint myLU = start.getLeftUp();
		RaPoint myLD = start.getLeftDown();
		RaPoint myRD = start.getRightDown();
		RaPoint myRU = start.getRightUp();
		
		int allPriceL = Integer.MAX_VALUE;//总代价,这个值足够大,计算里边最小值时方便
		boolean lFlag = false;
		int allPriceR = Integer.MAX_VALUE;
		boolean rFlag = false;
		int allPriceU = Integer.MAX_VALUE;
		boolean uFlag = false;
		int allPriceD = Integer.MAX_VALUE;
		boolean dFlag = false;
		
		int allPriceLU = Integer.MAX_VALUE;
		boolean luFlag = false;
		int allPriceLD = Integer.MAX_VALUE;
		boolean ldFlag = false;
		int allPriceRD = Integer.MAX_VALUE;
		boolean rdFlag = false;
		int allPriceRU = Integer.MAX_VALUE;
		boolean ruFlag = false;
		
		if(myRU!=null && !haveGetSet.contains(myRU) && myRU.canUse) {
			int depX = Math.abs(end.x-myRU.x);
			int depY = Math.abs( (end.y-myRU.y)*2  );//垂直距离上应该加倍,因为应该把菱形想象成正方形
			int euDistance = depX*depX+depY*depY;//减少开方计算,所以用距离的平方  使用欧拉距离作为预估代价,应为允许斜着走
			haveGetSet.add(myRU);
			myRU.curPrice = start.curPrice+45^2;//斜着走的距离是32*根号2, 约等于45
			myRU.euDistance = euDistance;
			allPriceRU = euDistance+myRU.curPrice;//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myRU.lastPoint = start;
			ruFlag = true;
			if(myRU.equals(end)) {
				foundWay = true;
			}
		}
		
		if(myRD!=null && !haveGetSet.contains(myRD) && myRD.canUse) {
			int depX = Math.abs(end.x-myRD.x);
			int depY = Math.abs( (end.y-myRD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myRD);
			myRD.curPrice = start.curPrice+45^2;
			myRD.euDistance = euDistance;
			allPriceRD = euDistance+myRD.curPrice;
			myRD.lastPoint = start;
			rdFlag = true;
			if(myRD.equals(end)) {
				foundWay = true;
				
			}
		}
		
		if(myLD!=null && !haveGetSet.contains(myLD) && myLD.canUse) {
			int depX = Math.abs(end.x-myLD.x);
			int depY = Math.abs( (end.y-myLD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLD);
			myLD.curPrice = start.curPrice+45^2;
			myLD.euDistance = euDistance;
			allPriceLD = euDistance+myLD.curPrice;
			myLD.lastPoint = start;
			ldFlag = true;
			if(myLD.equals(end)) {
				foundWay = true;
				
			}
		}
		
		if(myLU!=null && !haveGetSet.contains(myLU) && myLU.canUse) {
			int depX = Math.abs(end.x-myLU.x);
			int depY = Math.abs( (end.y-myLU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLU);
			myLU.curPrice = start.curPrice+45^2;
			myLU.euDistance = euDistance;
			allPriceLU = euDistance+myLU.curPrice;
			myLU.lastPoint = start;
			luFlag = true;
			if(myLU.equals(end)) {
				foundWay = true;
			}
		}
		
		
		if(myL!=null && !haveGetSet.contains(myL) && myL.canUse) {
			int depX = Math.abs(end.x-myL.x);
			int depY = Math.abs( (end.y-myL.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myL);
			myL.curPrice = start.curPrice+64^2;
			myL.euDistance = euDistance;
			allPriceL = euDistance+myL.curPrice;
			myL.lastPoint = start;
			lFlag = true;
			if(myL.equals(end)) {
				foundWay = true;
			}
		}
		
		if(myR!=null && !haveGetSet.contains(myR) && myR.canUse) {
			int depX = Math.abs(end.x-myR.x);
			int depY = Math.abs( (end.y-myR.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myR);
			myR.curPrice = start.curPrice+64^2;
			myR.euDistance = euDistance;
			allPriceR = euDistance+myR.curPrice;
			myR.lastPoint = start;
			rFlag = true;
			if(myR.equals(end)) {
				foundWay = true;
			}
		}
		
		if(myU!=null && !haveGetSet.contains(myU) && myU.canUse) {
			int depX = Math.abs(end.x-myU.x);
			int depY = Math.abs( (end.y-myU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myU);
			myU.curPrice = start.curPrice+64^2;
			myU.euDistance = euDistance;
			allPriceU = euDistance+myU.curPrice;
			myU.lastPoint = start;
			uFlag = true;
			if(myU.equals(end)) {
				foundWay = true;
			}
		}
		
		if(myD!=null && !haveGetSet.contains(myD) && myD.canUse) {
			int depX = Math.abs(end.x-myD.x);
			int depY = Math.abs( (end.y-myD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myD);
			myD.curPrice = start.curPrice+64^2;
			myD.euDistance = euDistance;
			allPriceD = euDistance+myD.curPrice;
			myD.lastPoint = start;
			dFlag = true;
			if(myD.equals(end)) {
				foundWay = true;
			}
		}
		
		List<RaPoint> nextStarts = new ArrayList<>();
		int min = NumberUtils.min(allPriceL,allPriceR,allPriceU,allPriceD,
				allPriceLU,allPriceLD,allPriceRD,allPriceRU);
		if(min==allPriceL && lFlag) {
			nextStarts.add(myL);
		}
		if(min==allPriceR && rFlag) {
			nextStarts.add(myR);
		}
		if(min==allPriceU && uFlag) {
			nextStarts.add(myU);
		}
		if(min==allPriceD && dFlag) {
			nextStarts.add(myD);
		}
		
		if(min==allPriceLU && luFlag) {
			nextStarts.add(myLU);
		}
		if(min==allPriceLD && ldFlag) {
			nextStarts.add(myLD);
		}
		if(min==allPriceRD && rdFlag) {
			nextStarts.add(myRD);
		}
		if(min==allPriceRU && ruFlag) {
			nextStarts.add(myRU);
		}
		
		if(!nextStarts.isEmpty()) {
			for(RaPoint startPoint:nextStarts) {
				if(!foundWay) {
					xunlu(startPoint,end);
				}
			}
		}
		
		
	}
	
	
	public static void main(String[] args) {
		test5();
	}
}

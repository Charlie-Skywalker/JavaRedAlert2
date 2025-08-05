package redAlert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import redAlert.test.MyPoint;

public class Atest {

	public static final int windowWidth = 1200;
	public static final int windowHeight = 900;
	
	public static void test1() {
//		JFrame jf = new JFrame("红色警戒");
//		jf.setSize(windowWidth,windowHeight);
//		jf.setResizable(false);
//		jf.setAlwaysOnTop(true);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jf.setLocationRelativeTo(null);//屏幕居中
//		jf.setVisible(true);
//		
//		JPanel mp = new JPanel();
//		mp.setLocation(0, 0);
//		mp.setVisible(true);
//		mp.setLayout(null);
//		mp.setMinimumSize(new Dimension(windowWidth,windowHeight));
//		mp.setPreferredSize(new Dimension(windowWidth,windowHeight));
//		jf.add(mp);
//		jf.pack();
		
		
		JFrame jf = new JFrame("测试");
		jf.setSize(windowWidth,windowHeight);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
//		jf.setLayout(null);
		
		
		JPanel mp = new JPanel();//与JFrame不同，JPanel默认可见
		mp.setLocation(0, 0);
		mp.setSize(300, 300);
		mp.setLayout(null);
		mp.setMinimumSize(new Dimension(windowWidth,windowHeight));
		mp.setPreferredSize(new Dimension(windowWidth,windowHeight));
		mp.setBackground(Color.blue);
		jf.add(BorderLayout.WEST,mp);
//		jf.pack();//这个方法需要注意,JFrame的布局是BorderLayout  会使固定宽高度的组件宽高失效   使用布局，才能使用这个方法
		

		
		JPanel mp2 = new JPanel();
		mp2.setLocation(300, 0);
		mp2.setSize(300, 300);
//		mp.setVisible(true);
		mp.setMinimumSize(new Dimension(300,300));
		mp.setPreferredSize(new Dimension(300,300));
		JButton jb = new JButton("点击");
		System.out.println(jb.isVisible());
		mp2.add(jb);
		mp2.setBackground(Color.green);
		jf.add(BorderLayout.EAST,mp2);
		
		jf.pack();
	}
	
	public static void test2() {
		int [] x = {0,5,5,0};
		int [] y = {0,0,5,5};
		Polygon p = new Polygon(x,y,4);
		
		System.out.println(p.contains(0f,5f));
	}
	
	/**
	 * 获取中心点坐标集合
	 */
	public static Set<Point> getCenterPointSet() {
		Set<Point> set = new HashSet<>();
		
		for(int m=0;m<500;m++) {//一类
			int y = 32*m;
			for(int n=0;n<50;n++) {
				int x = 64*n;
				set.add(new Point(x,y));
			}
		}
		
		for(int m=0;m<500;m++) {//二类
			int y = 16+32*m;
			for(int n=0;n<50;n++) {
				int x = 32+64*n;
				set.add(new Point(x,y));
			}
		}
		
		return set;
	}
	
	/**
	 * 解决菱形点内坐标问题
	 * 
	 * 输入一个点坐标,找到所在的菱形区域中心点
	 * 多边形法,需要50毫秒,太慢了
	 */
	public static void test3(int x1,int y1) {
		
		Set<Point> set = getCenterPointSet();
		
		System.out.println(System.currentTimeMillis());
		int y_d = y1-y1%16;
		int y_u = y_d+16;
		//双向搜索
		Point pointLeftCenter = null; 
		Point pointRightCenter = null;
		//左向搜索
		for(int i=0;i<32;i++) {
			int x11 = x1-i;
			if(x11%16==0) {
				Point pd = new Point(x11,y_d);
				if(set.contains(pd)) {
					pointLeftCenter = pd;
					break;
				}
				Point pu = new Point(x11,y_u);
				if(set.contains(pu)) {
					pointLeftCenter = pu;
					break;
				}
			}
		}
		
		//右向搜索
		for(int i=1;i<33;i++) {
			int x11 = x1+i;
			if(x11%16==0) {
				Point pd = new Point(x11,y_d);
				if(set.contains(pd)) {
					pointRightCenter = pd;
					break;
				}
				Point pu = new Point(x11,y_u);
				if(set.contains(pu)) {
					pointRightCenter = pu;
					break;
				}
			}
		}
		
		//      矩形
		//		B      D
		//		A      C
		System.out.println(pointLeftCenter);
		System.out.println(pointRightCenter);
		
		//左上 右下模式
		if(pointLeftCenter.getY()<pointRightCenter.getY()) {
			Point pA = new Point(pointLeftCenter.x,pointRightCenter.y);
			Point pD = new Point(pointRightCenter.x,pointLeftCenter.y);
			int [] pxBAD = {pointLeftCenter.x,pA.x,pD.x};
			int [] pyBAD = {pointLeftCenter.y,pA.y,pD.y};
			boolean isLeft = false;
			Polygon poLeft = new Polygon(pxBAD,pyBAD,3);
			if(poLeft.contains(x1, y1)) {
				System.out.println("在左上");//pointLeftCenter是中心点
				isLeft = true;
			}
			int [] pxCAD = {pointRightCenter.x,pA.x,pD.x};
			int [] pyCAD = {pointRightCenter.y,pA.y,pD.y};
			boolean isRight = false;
			Polygon poRight = new Polygon(pxCAD,pyCAD,3);
			if(poRight.contains(x1, y1)) {
				System.out.println("在右下");//pointRightCenter是中心点
				isRight = true;
			}
			if(!isLeft && !isRight) {
				System.out.println("在边上");//确认不了在哪里  但是在边上
			}
			
		}
		
		//左下 右上模式
		if(pointLeftCenter.getY()>pointRightCenter.getY()) {
			Point pB = new Point(pointLeftCenter.x,pointRightCenter.y);
			Point pC = new Point(pointRightCenter.x,pointLeftCenter.y);
			int [] pxABC = {pointLeftCenter.x,pB.x,pC.x};
			int [] pyABC = {pointLeftCenter.y,pB.y,pC.y};
			boolean isLeft = false;
			Polygon poLeft = new Polygon(pxABC,pyABC,3);
			if(poLeft.contains(x1, y1)) {
				System.out.println("在左下");//pointLeftCenter是中心点
				isLeft = true;
			}
			int [] pxDBC = {pointRightCenter.x,pB.x,pC.x};
			int [] pyDBC = {pointRightCenter.y,pB.y,pC.y};
			boolean isRight = false;
			Polygon poRight = new Polygon(pxDBC,pyDBC,3);
			if(poRight.contains(x1, y1)) {
				System.out.println("在右上");//pointRightCenter是中心点
				isRight = true;
			}
			if(!isLeft && !isRight) {
				System.out.println("在边上");//确认不了在哪里  但是在边上
			}
			
		}
		
		System.out.println(System.currentTimeMillis());
	}
	/**
	 * 自己想到的向量法
	 * 速度要快很多,比解决了多边形法速度慢,边上无法判断的问题
	 * 搜索偏向是向下向右的
	 */
	public static void test4(int x1,int y1) {
		
		Set<Point> set = getCenterPointSet();
		
		System.out.println(System.currentTimeMillis());
		int y_d = y1-y1%16;
		int y_u = y_d+16;
		//双向搜索
		Point pointLeftCenter = null; 
		Point pointRightCenter = null;
		//左向搜索
		for(int i=0;i<32;i++) {
			int x11 = x1-i;
			if(x11%16==0) {
				Point pd = new Point(x11,y_d);
				if(set.contains(pd)) {
					pointLeftCenter = pd;
					break;
				}
				Point pu = new Point(x11,y_u);
				if(set.contains(pu)) {
					pointLeftCenter = pu;
					break;
				}
			}
		}
		
		//右向搜索
		for(int i=1;i<33;i++) {
			int x11 = x1+i;
			if(x11%16==0) {
				Point pd = new Point(x11,y_d);
				if(set.contains(pd)) {
					pointRightCenter = pd;
					break;
				}
				Point pu = new Point(x11,y_u);
				if(set.contains(pu)) {
					pointRightCenter = pu;
					break;
				}
			}
		}
		
//      矩形
		//		B      D
		//		A      C
		
		//左上 右下模式
		if(pointLeftCenter.getY()<pointRightCenter.getY()) {
			Point pD = new Point(pointRightCenter.x,pointLeftCenter.y);
			
			//找到向量  pD->p
			//若向量  向量y分量绝对值 除以x分量绝对值 >0.5则在右下   <0.5则在左上
			if(pD.x-x1==0) {
				System.out.println("在右下,在CD边上");
				return;
			}
			if(pD.y-y1==0) {
				System.out.println("在左上,在BD边上");
				return;
			}
			
			double xd = Math.abs(pD.x-x1);
			double yd = Math.abs(pD.y-y1);
			if(yd/xd>0.5) {
				System.out.println("在右下");
			}else if(yd/xd<0.5) {
				System.out.println("在左上");
			}else {
				System.out.println("在AD边上");
			}
			
		}
		
		//左下 右上模式
		if(pointLeftCenter.getY()>pointRightCenter.getY()) {
			Point pB = new Point(pointLeftCenter.x,pointRightCenter.y);
			
			//找到向量  pB->p
			//若向量  向量y分量绝对值 除以x分量绝对值 >0.5则在右下   <0.5则在左上
			if(pB.x==x1) {
				System.out.println("在左下,在BA边上");
				return;
			}
			if(pB.y==y1) {
				System.out.println("在右上,在BD边上");
				return;
			}
			
			double xd = Math.abs(pB.x-x1);
			double yd = Math.abs(pB.y-y1);
			if(yd/xd>0.5) {
				System.out.println("在左下");
			}else if(yd/xd<0.5) {
				System.out.println("在右上");
			}else {
				System.out.println("在BC边上");
			}
			
		}
		
	}
	
	/**
	 * TODO 写一个A*寻路算法,在普通的矩形点阵中
	 * 
	 * 先写一个可以展示的平台
	 * 
	 * 然后写一个矩阵
	 */
	public static void test4() throws Exception{
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
		
		
		Graphics g = img.getGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, 100, 100);
		Thread.sleep(1000);
		mp.repaint();
		
		myPointMap.get(2+","+0).ban();
		myPointMap.get(2+","+1).ban();
		myPointMap.get(2+","+2).ban();
		myPointMap.get(2+","+3).ban();
		
//		myPointMap.get(0+","+3).ban();
//		myPointMap.get(1+","+3).ban();
//		myPointMap.get(2+","+3).ban();
//		myPointMap.get(3+","+3).ban();
		
//		myPointMap.get(3+","+0).ban();
//		myPointMap.get(3+","+1).ban();
//		myPointMap.get(3+","+2).ban();
//		myPointMap.get(3+","+3).ban();
//		myPointMap.get(3+","+4).ban();
//		myPointMap.get(3+","+5).ban();
//		myPointMap.get(3+","+6).ban();
//		myPointMap.get(3+","+7).ban();
//		myPointMap.get(3+","+8).ban();
//		myPointMap.get(3+","+9).ban();
//		myPointMap.get(3+","+10).ban();
//		myPointMap.get(3+","+11).ban();
		
		
		//将这些点画在图上
		int pointWidth = 30;
		int pointNk = 28;//点内宽
		Collection<MyPoint> col = myPointMap.values();
		Iterator<MyPoint> item = col.iterator();
		while(item.hasNext()) {
			MyPoint po = item.next();
			g.setColor(Color.white);
			g.fillRect(po.x*pointWidth, po.y*pointWidth, pointWidth, pointWidth);
			g.setColor(po.color);
			g.fillRect(po.x*pointWidth+ (pointWidth-pointNk)/2 , po.y*pointWidth+ (pointWidth-pointNk)/2, pointNk, pointNk);
		}
		
		mp.repaint();
		
		Thread.sleep(3000);
		
		
		//开始寻路
		
		MyPoint start = new MyPoint(0,0);
		MyPoint end = new MyPoint(29,29);
		start.curPrice = 0;
		
		dead.add(start);
		xunlu(start,end);
		
		if(foundWay) {
			List<MyPoint> luxian = new ArrayList<>();
			
			MyPoint last = end;
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
			//画方块
			for(MyPoint po:luxian) {
//				g.setColor(Color.white);
//				g.fillRect(po.x*pointWidth, po.y*pointWidth, pointWidth, pointWidth);
				g.setColor(Color.RED);
				g.fillRect(po.x*pointWidth+ (pointWidth-pointNk)/2 , po.y*pointWidth+ (pointWidth-pointNk)/2, pointNk, pointNk);
				
				g.setColor(Color.BLACK);
				MyPoint lst = po.lastPoint;
				if(lst!=null) {
					g.drawLine(po.x*pointWidth+pointWidth/2, 
							   po.y*pointWidth+pointWidth/2,
							   lst.x*pointWidth+pointWidth/2,
							   lst.y*pointWidth+pointWidth/2);
				}
			}
			//画连接线
			for(MyPoint po:luxian) {
				MyPoint lst = po.lastPoint;
				if(lst!=null) {
					g.drawLine(po.x*pointWidth+pointWidth/2, 
							   po.y*pointWidth+pointWidth/2,
							   lst.x*pointWidth+pointWidth/2,
							   lst.y*pointWidth+pointWidth/2);
				}
			}
			
			
			mp.repaint();
		}else {
			System.out.println("没有路");
		}
		
	}
	
	public static HashSet<MyPoint> haveGetSet = new HashSet<>();
	public static int curPrice = 0;
	public static boolean isEnd = false;
	public static boolean foundWay = false;
	
	public static HashMap<String,MyPoint> myPointMap = new HashMap<>();
	
	public static List<MyPoint> rest = new ArrayList<>();//边界方块   寻路寻进死以后通过这里复活
	public static Set<MyPoint> dead = new HashSet<>();//存放那些被当作起始点递归的点的集合
	
	static {
		initMyPoint();
	}
	
	public static void initMyPoint() {
		//初始化所有点
		for(int i=0;i<30;i++) {
			for(int j=0;j<30;j++) {
				MyPoint po = new MyPoint(j,i);
				myPointMap.put(j+","+i, po);
			}
		}
	}
	
	public static void xunlu(MyPoint start,MyPoint end) {
		MyPoint myL = start.getLeft();
		MyPoint myR = start.getRight();
		MyPoint myU = start.getUp();
		MyPoint myD = start.getDown();
		
		int allPriceL = 9999;
		boolean lFlag = false;
		int allPriceR = 9999;
		boolean rFlag = false;
		int allPriceU = 9999;
		boolean uFlag = false;
		int allPriceD = 9999;
		boolean dFlag = false;
		if(myL!=null && !haveGetSet.contains(myL) && myL.canUse) {
			//计算曼哈顿距离
			int depX = Math.abs(end.x-myL.x);
			int depY = Math.abs(end.y-myL.y);
			int mhd = depX+depY;
			haveGetSet.add(myL);
			myL.curPrice = start.curPrice+1;
			myL.mhd = mhd;
			allPriceL = mhd+myL.curPrice;
			myL.totalPrice = allPriceL;
			myL.lastPoint = start;
			lFlag = true;
			
			if(myL.equals(end)) {
				end.lastPoint = start;
				foundWay = true;
				return;
			}else {
				rest.add(myL);
			}
		}
		if(myR!=null && !haveGetSet.contains(myR) && myR.canUse) {
			//计算距离 距离
			int depX = Math.abs(end.x-myR.x);
			int depY = Math.abs(end.y-myR.y);
			int mhd = depX+depY;
			haveGetSet.add(myR);
			myR.curPrice = start.curPrice+1;
			myR.mhd = mhd;
			allPriceR = mhd+myR.curPrice;
			myR.totalPrice = allPriceR;
			myR.lastPoint = start;
			rFlag = true;
			if(myR.equals(end)) {
				end.lastPoint = start;
				foundWay = true;
				return;
			}else {
				rest.add(myR);
			}
		}
		if(myU!=null && !haveGetSet.contains(myU) && myU.canUse) {
			//计算距离 距离
			int depX = Math.abs(end.x-myU.x);
			int depY = Math.abs(end.y-myU.y);
			int mhd = depX+depY;
			haveGetSet.add(myU);
			myU.curPrice = start.curPrice+1;
			myU.mhd = mhd;
			allPriceU = mhd+myU.curPrice;
			myU.totalPrice = allPriceU;
			myU.lastPoint = start;
			uFlag = true;
			if(myU.equals(end)) {
				end.lastPoint = start;
				foundWay = true;
				return;
			}else {
				rest.add(myU);
			}
		}
		if(myD!=null && !haveGetSet.contains(myD) && myD.canUse) {
			//计算距离 距离
			int depX = Math.abs(end.x-myD.x);
			int depY = Math.abs(end.y-myD.y);
			int mhd = depX+depY;
			haveGetSet.add(myD);
			myD.curPrice = start.curPrice+1;
			myD.mhd = mhd;
			allPriceD = mhd+myD.curPrice;
			myD.totalPrice = allPriceD;
			myD.lastPoint = start;
			dFlag = true;
			if(myD.equals(end)) {
				end.lastPoint = start;
				foundWay = true;
				return;
			}else {
				rest.add(myD);
			}
		} 
		
		//从rest中找到总代价最低的方块 然后递归
		MyPoint astart = getMinTotalPricePoint(rest);
		if(astart!=null) {
			dead.add(astart);
			xunlu(astart,end);
		}else {
			//没找到路  会自动出栈
		}
		
		
		
		
		
		
		
	}
	
	/**
	 * 获取最小总代价的点
	 */
	public static MyPoint getMinTotalPricePoint(List<MyPoint> rest) {
		int totalPrice = 9999;
		MyPoint minPricePoint = null;
		
		for(int i=0;i<rest.size();i++) {
			MyPoint mp = rest.get(i);
			if(mp.totalPrice<totalPrice) {
				totalPrice = mp.totalPrice;
				minPricePoint = mp;
			}
		}
		if(minPricePoint!=null) {
			rest.remove(minPricePoint);
		}
		
		return minPricePoint;
	}
	
	/**
	 * 直接操作BufferedImage的底层数据
	 */
	public static void test6() throws Exception{
		JFrame jf = new JFrame("测试A*算法");
		jf.setSize(windowWidth,windowHeight);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
		
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
		
		
		Graphics g = img.getGraphics();
		g.setColor(Color.red);
		g.fillRect(0, 0, 100, 100);
		Thread.sleep(1000);
		mp.repaint();
		
		//没研究出什么结果
		DataBufferInt data = (DataBufferInt)img.getRaster().getDataBuffer();
		int [] pixels = data.getData();
		System.out.println(pixels[0]);
		System.out.println(pixels[1]);
		System.out.println(pixels[2]);
		System.out.println(pixels[3]);
		System.out.println(pixels[9999]);
		for(int a:pixels) {
			if(a!=-65536) {
				System.out.println(a);
			}
		}
		
		System.out.println(data.getSize());
		
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception{
//		test2();
//		test3(88,19);
//		test3(45,92);
//		test3(65,65);
//		test3(188,119);
//		test3(00,00);
//		test4(00,00);
//		test4();	
		
		test6();
		
		
//		jf.pack();//这个方法需要注意,JFrame的布局是BorderLayout  会使固定宽高度的组件宽高失效   使用布局，才能使用这个方法
		
	}
}





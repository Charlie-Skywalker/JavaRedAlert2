package redAlert.test;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class JPanelTest {
	
	
	public static void test1() {
		BufferedImage image = new BufferedImage(1200,900,BufferedImage.TYPE_INT_ARGB);
		
		BufferedImage image2 = new BufferedImage(400,300,BufferedImage.TYPE_INT_ARGB);
		
		
		Graphics2D  g2d = image.createGraphics();
		Graphics g = image.getGraphics();
		System.out.println(image.getRGB(0, 0));
//		g.set
//		g2d.setComposite(AlphaComposite.Clear);
		
		Composite ccc = g2d.getComposite();
		System.out.println(ccc.equals(AlphaComposite.Clear));
		System.out.println(ccc.equals(AlphaComposite.Xor));
		System.out.println(ccc.equals(AlphaComposite.Dst));
		System.out.println(ccc.equals(AlphaComposite.DstAtop));
		System.out.println(ccc.equals(AlphaComposite.DstIn));
		System.out.println(ccc.equals(AlphaComposite.DstOut));
		System.out.println(ccc.equals(AlphaComposite.DST_OVER));
		System.out.println(ccc.equals(AlphaComposite.SrcAtop));
		System.out.println(ccc.equals(AlphaComposite.SrcIn));
		System.out.println(ccc.equals(AlphaComposite.SrcOut));
		System.out.println(ccc.equals(AlphaComposite.SrcOver));//默认的
		
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 1, 1);
//		g2d.dispose();
		System.out.println(image.getRGB(0, 0));
		
		g2d.setColor(new Color(0x0000ff00));
		g2d.clearRect(0, 0, 1, 1);
		g2d.dispose();
		System.out.println(image.getRGB(0, 0));
		
		System.out.println(System.currentTimeMillis());
		g2d.fillRect(0, 0, 1200, 900);
		System.out.println(System.currentTimeMillis());
	}
	
	/**
	 * 测试两个JPanel是否能重叠摆放
	 * 
	 * 结论  可以重叠摆放  但是不能设置布局
	 */
	public static void test2() throws Exception{
		 final int windowWidth = 1200;
		 final int windowHeight = 900;
		
		//1.画好界面
		JFrame jf = new JFrame("测试");
		jf.setSize(windowWidth,windowHeight);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		jf.setBackground(Color.black);
		jf.setLayout(null);
		
		BufferedImage img = new BufferedImage(300,300,BufferedImage.TYPE_INT_ARGB);
		JPanel mp = new JPanel(){
			
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), this);
				super.paint(g);
			}
		};
		mp.setLocation(0, 0);
		mp.setSize(300, 300);
		mp.setLayout(null);
		mp.setBackground( new Color(0,0,0,0));
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 300, 300);
		mp.repaint();
		jf.add(mp);
		Thread.sleep(1000);
		
		
		
		BufferedImage image = new BufferedImage(300,300,BufferedImage.TYPE_INT_ARGB);
		JPanel mp2 = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				Graphics2D g2d =(Graphics2D)g;
//				g2d.setComposite(AlphaComposite.Clear);
//				g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
//				g2d.setComposite(AlphaComposite.SrcOver);
				
				g2d.drawImage(image, 0, 0,image.getWidth(), image.getHeight(), this);
				super.paint(g2d);
//				g.drawImage(image, 0, 0,image.getWidth(), image.getHeight(), this);
			}
		};
		mp2.setLayout(null);
		mp2.setLocation(0, 0);
		mp2.setSize(300, 300);
//		mp2.setBackground(new Color(255,255,255,0));
		jf.add(mp2);
		
		//设置堆叠关系
//		jf.getLayeredPane().setLayer(mp, JLayeredPane.DEFAULT_LAYER);
//		jf.getLayeredPane().setLayer(mp2, JLayeredPane.DRAG_LAYER);
		jf.setComponentZOrder(mp, 0);
		jf.setComponentZOrder(mp2, 1);
		
		
		
		Graphics2D g2d2 = image.createGraphics();
		g2d2.setColor(Color.green);
		g2d2.fillRect(0, 0, image.getWidth(), image.getHeight());
		System.out.println(mp2.getBackground());
		mp2.repaint();
		Thread.sleep(1000);
		
		
		
		/*
		
		//问题出在这一步   底下的panel的颜色居然会覆盖上层的panel
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 300, 300);
		mp.repaint();
		Thread.sleep(1000);
		
		
		
		g2d2.setColor(Color.green);
		g2d2.fillRect(0, 0, 100, 100);
		mp2.repaint();
		System.out.println("1");
		Thread.sleep(1000);
		
//		g2d2.setColor(new Color(0,255,0,255));//涂绿
//		g2d2.fillRect(0, 0, 300, 300);
//		mp2.repaint();
//		System.out.println("涂绿了");
//		Thread.sleep(1000);
		*/

		//能否再涂透明?
//		Graphics g = image.getGraphics();
//		g.setColor(new Color(255,255,255,0));
//		g.fillRect(0, 0, 300, 300);
//		g2d2.setComposite(AlphaComposite.Clear);
//		g2d2.setColor(new Color(255,255,255,127));//先涂完全透明
//		g2d2.fillRect(0, 0, 300, 300);
//		ImageIO.write(image, "png", new File("E:/mypngtest.png"));
//		System.out.println("再涂透明");
//		mp2.repaint();
		
//		mp2.setLocation(300, 0);
		
		Thread.sleep(2000);
		
		
		Thread.sleep(2000);
		
		
//		
		
		
		/*
		g2d2.setColor(new Color(255,0,0,127));
//		g2d2.fillRect(0, 0, 300, 300);//这两个函数区别是什么
//		g2d2.drawRect(0, 0, 300, 300);//这个只是画边框
		g2d2.clearRect(0, 0, 300, 300);
		System.out.println(image.getRGB(0, 0));
		System.out.println(new Color(image.getRGB(0, 0)));
		System.out.println(mp2.getBackground());
		
		g2d2.dispose();
//		g2d2.setColor(new Color(0,0,0,0));
		
		mp2.repaint();
		*/
		System.out.println(mp2.getLocation().getX());
		System.out.println(mp2.getLocation().getY());
		System.out.println(mp2.getWidth());
		System.out.println(mp2.getHeight());
		
	}
	
	/**
	 * Japnel上放JCanvas  然后试试事件是否能触发
	 */
	public static void test3() {
		 final int windowWidth = 1200;
		 final int windowHeight = 900;
		
		//1.画好界面
		JFrame jf = new JFrame("测试");
		jf.setSize(windowWidth,windowHeight);
//		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		JPanel parent = new JPanel();//
		parent.setLocation(0, 0);
		parent.setSize(400, 400);
		parent.setLayout(null);
		parent.setVisible(true);
		parent.setMinimumSize(new Dimension(400,400));
		parent.setPreferredSize(new Dimension(400,400));//首选尺寸
		jf.add(BorderLayout.WEST,parent);
		
		
		BufferedImage img = new BufferedImage(300,300,BufferedImage.TYPE_INT_ARGB);
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
		mp.setBackground(new Color(0,0,0,0));
		mp.setVisible(true);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(new Color(255,0,0,255));
		g2d.fillRect(0, 0, 300, 300);
		parent.add(mp);
		mp.repaint();
		mp.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println(e.getX());
				
			}
			
		});
		
		
		JPanel mp2 = new JPanel();
		mp2.setLocation(0, 0);
		mp2.setSize(300, 300);
		mp2.setLayout(null);
		mp2.setBackground(new Color(0,0,0,0));
		mp2.setVisible(true);
		parent.add(mp2);

		mp2.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				System.out.println("mp2收到了");
				
			}
			
		});
		
		
		parent.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent e) {
				
			}

			@Override
			public void mouseMoved(MouseEvent e) {
//				System.out.println(e.getX());
//				mp2.dispatchEvent(e);
			}
			
		});
		
		
		jf.pack();
		
	}
	
	public static void test4() throws Exception{
		JFrame frame = new JFrame();
        frame.setTitle("Demo01"); // 设置窗口标题
        frame.setSize(400, 300); // 设置窗口显示大小
        
        JLayeredPane layeredPane = new JLayeredPane(); // 创建层级面板
        
        
        BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//        JPanel panel = new JPanel(); // 创建一个JPanel
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				System.out.println("1被调用");
				g.clearRect(0, 0, img.getWidth(), img.getHeight());
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), null);
				super.paint(g);
			}
		};
		panel.setOpaque(true); // 设置不透明
		panel.setBackground(new Color(0,0,0,0)); // 设置背景颜色
		panel.setBounds(0, 0, 100, 100); // 设置面板的位置和宽高
		layeredPane.add(panel, 100);
        
        
//        
        
        BufferedImage img2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//        JPanel panel2 = new JPanel(); // 创建一个JPanel
		JPanel panel2 = new JPanel(){
			private static final long serialVersionUID = -1683346022022266879L;

			@Override
			public void paint(Graphics g) {
				System.out.println("2被调用");
				g.drawImage(img2, 0, 0,img2.getWidth(), img2.getHeight(), null);
				super.paint(g);
			}
		};
        
        panel2.setOpaque(true); // 设置不透明
        panel2.setBackground(new Color(0,0,0,0)); // 设置背景颜色
        panel2.setBounds(50, 50, 100, 100); // 设置面板的位置和宽高
        layeredPane.add(panel2, 100);
        
        
        
		
		
		layeredPane.setLayer(panel2, JLayeredPane.DEFAULT_LAYER);
		layeredPane.setLayer(panel, JLayeredPane.DEFAULT_LAYER);//后添加的在后边
		
		
		
		
		
//        layeredPane.setLayer(panel, JLayeredPane.PALETTE_LAYER);
		
        
        frame.add(layeredPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口默认关闭操作
        frame.setLocationRelativeTo(null); // 相对屏幕居中
        frame.setVisible(true); // 设置窗口可见
        
        Thread.sleep(3000);
        
        Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.green);
		g2d.fillRect(0, 0, 100, 100);
		panel.repaint();
		
		
        Graphics2D g2d2 = img2.createGraphics();
		g2d2.setColor(Color.blue);
		g2d2.fillRect(0, 0, 100, 100);
		panel2.repaint();
		
		
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 100, 100);
		panel.repaint();
        
	}
	
	public static void test5() throws Exception{
		JFrame frame = new JFrame();
        frame.setTitle("Demo01"); // 设置窗口标题
        frame.setSize(400, 300); // 设置窗口显示大小
        BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//      JPanel panel = new JPanel(); // 创建一个JPanel
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				System.out.println("1被调用");
				super.paint(g);
				
//				g.clearRect(0, 0, img.getWidth(), img.getHeight());
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), null);
				
			}
		};
		panel.setOpaque(true); // 设置不透明
		panel.setBackground(Color.red);
//		panel.setBackground(new Color(0,0,0,0)); // 设置背景颜色
		panel.setLocation(0, 0);
		panel.setSize(100, 100);
		panel.setLayout(null);
        
		
		BufferedImage img2 = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//      JPanel panel2 = new JPanel(); // 创建一个JPanel
		JPanel panel2 = new JPanel(){
			private static final long serialVersionUID = -1683346022022266879L;

			@Override
			public void paint(Graphics g) {
				System.out.println("2被调用");
				super.paint(g);
				
				g.drawImage(img2, 0, 0,img2.getWidth(), img2.getHeight(), null);
				
			}
		};
      
      panel2.setOpaque(true); // 设置不透明
      panel2.setBackground(Color.green);
//      panel2.setBackground(new Color(0,0,0,0)); // 设置背景颜色
//      panel2.setBounds(50, 50, 100, 100); // 设置面板的位置和宽高
      panel2.setLocation(0, 0);
      panel2.setSize(100, 100);
      
      
      frame.add(panel);
      panel.add(panel2);
      frame.setLayout(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口默认关闭操作
      frame.setLocationRelativeTo(null); // 相对屏幕居中
      frame.setVisible(true); // 设置窗口可见
      
      
      
      Thread.sleep(3000);
      
      Graphics2D g2d = img.createGraphics();
	  g2d.setColor(Color.blue);
	  g2d.fillRect(0, 0, 100, 100);
	  panel.repaint();
		
	}
	
	public static void test6() throws Exception{
		JFrame frame = new JFrame();
        frame.setTitle("Demo01"); // 设置窗口标题
        frame.setSize(400, 300); // 设置窗口显示大小
        BufferedImage img = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//      JPanel panel = new JPanel(); // 创建一个JPanel
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				System.out.println("1被调用");
				super.paint(g);
				
//				g.clearRect(0, 0, img.getWidth(), img.getHeight());
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), null);
				
			}
		};
		panel.setOpaque(true); // 设置不透明
//		panel.setBackground(Color.red);
		panel.setBackground(new Color(0,0,0,0)); // 设置背景颜色
		panel.setLocation(0, 0);
		panel.setSize(100, 100);
		panel.setLayout(null);
		
		frame.add(panel);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口默认关闭操作
		frame.setLocationRelativeTo(null); // 相对屏幕居中
		frame.setVisible(true); // 设置窗口可见
		
		Thread.sleep(3000);
		JLabel jl = new JLabel("什么玩意");
		jl.setLocation(0, 0);
		jl.setSize(50, 50);
		jl.setBackground(Color.blue);
		jl.setVisible(true);
		jl.setName("xxx");
		
		panel.repaint();
		
	}
	
	
	public static void main(String[] args) throws Exception{
		
//		test2();
		
//		test4();
		
//		test5();
		
		test6();

		
	}
}

package redAlert.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class JPanelTest2 {

	
	public static void test1() throws Exception{
		//1.画好界面
		JFrame jf = new JFrame("测试");
		jf.setSize(168,900);
//				jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		
		JPanel parent = new JPanel();//
		parent.setLocation(0, 0);
		parent.setSize(168, 800);
//		parent.setLayout(null);
		parent.setVisible(true);
		parent.setMinimumSize(new Dimension(168,800));
		parent.setPreferredSize(new Dimension(168,800));//首选尺寸
		jf.add(BorderLayout.WEST,parent);
		
//		FlowLayout f=(FlowLayout)parent.getLayout();
//		f.setHgap(0);
//		f.setVgap(0);
		
		jf.pack();
		
		
		JLabel jl = new JLabel();
		BufferedImage image = ImageIO.read(new File("E:/side2b.jpeg"));
		ImageIcon icon = new ImageIcon(image);
		jl.setOpaque(true);
		jl.setIcon(icon);
		jl.setLocation(0, 0);
		jl.setSize(168,50);
		
		JLabel jl3 = new JLabel();
		BufferedImage image3 = ImageIO.read(new File("E:/side2b.jpeg"));
		ImageIcon icon3 = new ImageIcon(image3);
		jl3.setOpaque(true);
		jl3.setIcon(icon3);
		jl3.setLocation(0, 50);
		jl3.setSize(168,50);
		
		JLabel jl2 = new JLabel();
		jl2.setBackground(Color.red);
		jl2.setOpaque(false);
		jl2.setLocation(0, 0);
		jl2.setSize(20,400);
		BufferedImage myPower = new BufferedImage(20,400,BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = myPower.createGraphics();
		g2d.setColor(new Color(12,58,240,50));
		g2d.fillRect(0, 0, 10, 10);
		jl2.setIcon(new ImageIcon(myPower));
		
		parent.add(jl2);
		parent.add(jl);
		parent.add(jl3);

		jl2.addMouseListener(new MouseAdapter() {

				@Override
				public void mousePressed(MouseEvent e) {
					System.out.println("jl2");
				}
			});
		jl.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("jl");
			}
		});
	}
	
	/**
	 * 一个JPanel中有多个JPanel时,调整他们的层级关系
	 */
	public static void test2() {
		//1.画好界面
		JFrame jf = new JFrame("测试");
		jf.setSize(168,900);
//		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		JPanel parent = new JPanel();
		parent.setLocation(0, 0);
		parent.setSize(168, 900);
		parent.setBackground(Color.yellow);
		parent.setLayout(null);
		jf.add(parent);
		
		BufferedImage img = new BufferedImage(168,900,BufferedImage.TYPE_INT_ARGB);
		JPanel mp = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), this);
				
			}
		};
		mp.setLocation(0, 0);
		mp.setSize(168, 900);
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(Color.red);
		g2d.fillRect(0, 0, 168, 900);
		mp.repaint();
		parent.add(mp);
		
		//
		BufferedImage img2 = new BufferedImage(50,900,BufferedImage.TYPE_INT_ARGB);
		JPanel mp2 = new JPanel(){
			private static final long serialVersionUID = -1683346022022266878L;
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				g.drawImage(img2, 0, 0,img2.getWidth(), img2.getHeight(), this);
				
			}
		};
		mp2.setLocation(-40, 0);
		mp2.setSize(50, 900);
		Graphics2D g2d2 = img2.createGraphics();
		g2d2.setColor(Color.blue);
		g2d2.fillRect(0, 0, 50, 900);
		g2d2.dispose();
		parent.add(mp2);
		
		parent.setComponentZOrder(mp2, 0);//ZOrder小的会在上层
		parent.setComponentZOrder(mp, 1);//ZOrder大的会在下层
		mp.repaint();
		mp2.repaint();
		
	}
	
	
	public static void main(String[] args) throws Exception{
//		test1();
		test2();
	}
}

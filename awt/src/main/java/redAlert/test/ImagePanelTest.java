package redAlert.test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 方便写红警的中的特殊图片
 */
public class ImagePanelTest extends JPanel{
	

	//从上往下画  仍不对  继续改
	public static void test3() {
		//1.画好界面
		JFrame jf = new JFrame("图片查看器");
		jf.setSize(800,600);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		
		BufferedImage img = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
		JPanel mp = new JPanel(){
			
			private static final long serialVersionUID = -1683346022022266878L;

			@Override
			public void paint(Graphics g) {
				super.paint(g);
				
				g.drawImage(img, 0, 0,img.getWidth(), img.getHeight(), this);
				
			}
		};
		mp.setLocation(0, 0);
		mp.setSize(300, 300);
		mp.setLayout(null);
		mp.setBackground( new Color(0,0,0,0));
		jf.add(mp);
		
		//画一个血条模型
		Graphics2D g2d = img.createGraphics();
		int x = 200;
		int y = 200;
		
		
		int greenNum = 5;
		for(int i=0;i<greenNum;i++) {
			
			drawOneGreen2(g2d,x,y);
			Color blackGreen = new Color(0,50,0);
			if(i==0) {
				
				g2d.setColor(blackGreen);
				g2d.drawLine(x+5, y+2, x+5, y+4);
				g2d.drawLine(x+4, y+4, x+5, y+4);
				
				Color deepGreen = new Color(0,128,0);
				g2d.setColor(deepGreen);
				g2d.drawLine(x+4, y+3, x+4, y+3);
			}
			if(i==greenNum-1) {
				g2d.setColor(blackGreen);
				g2d.drawLine(x-4, y+3, x-4, y+4);
				g2d.drawLine(x-4, y+4, x-3, y+4);
				g2d.drawLine(x-2, y+5, x-1, y+5);
			}
			
			x-=4;
			y+=2;
		}
		
		int grayNum = 5;
		for(int i=0;i<grayNum;i++) {
			drawOneGray2(g2d,x,y);
			
			if(i==greenNum-1) {
				Color black = Color.black;
				Color white = Color.white;
				g2d.setColor(black);
				
				g2d.drawLine(x-2, y+5, x+3, y+5);
				g2d.drawLine(x, y+6, x+1, y+6);
				
				g2d.setColor(white);
				g2d.drawLine(x+1, y+5, x+1, y+5);
			}
			
			
			x-=4;
			y+=2;
		}
		
		mp.repaint();
	}
	
	//画绿色的
	public static void drawOneGreen2(Graphics2D g2d,int x,int y) {
		Color deepGreen = new Color(0,128,0);
		g2d.setColor(deepGreen);
		g2d.drawLine(x, y, x+1, y);
		
		g2d.drawLine(x-2, y+1, x-1, y+1);
		g2d.setColor(Color.green);
		g2d.drawLine(x, y+1, x+1, y+1);
		g2d.setColor(deepGreen);
		g2d.drawLine(x+2, y+1, x+3, y+1);
		
		g2d.drawLine(x-4, y+2, x-3, y+2);
		g2d.setColor(Color.green);
		g2d.drawLine(x-2, y+2, x+3, y+2);
		g2d.setColor(deepGreen);
		g2d.drawLine(x+4, y+2, x+5, y+2);
		
		g2d.drawLine(x-2, y+3, x-1, y+3);
		g2d.setColor(Color.green);
		g2d.drawLine(x, y+3, x+1, y+3);
		g2d.setColor(deepGreen);
		g2d.drawLine(x+2, y+3, x+3, y+3);
		
		g2d.drawLine(x, y+4, x+1, y+4);
		
		//画黑绿色边
		Color blackGreen = new Color(0,50,0);
		g2d.setColor(blackGreen);
		g2d.drawLine(x-4, y+3, x-4, y+4);
		g2d.drawLine(x-4, y+4, x-3, y+4);
		g2d.setColor(Color.green);
		g2d.drawLine(x-3, y+3, x-3, y+3);
		g2d.drawLine(x-2, y+4, x-1, y+4);
		
		g2d.setColor(blackGreen);
		g2d.drawLine(x, y+5, x+3, y+5);
		g2d.drawLine(x, y+6, x+1, y+6);
		
		g2d.setColor(deepGreen);
		g2d.drawLine(x+2, y+4, x+3, y+4);
		
	}
	
	//画灰色的
	public static void drawOneGray2(Graphics2D g2d,int x,int y) {
		Color gray = new Color(180,180,180);
		g2d.setColor(gray);
		g2d.drawLine(x, y, x+1, y);
		
		g2d.drawLine(x-2, y+1, x-1, y+1);
		g2d.drawLine(x+2, y+1, x+3, y+1);
		
		g2d.drawLine(x-4, y+2, x-3, y+2);
		g2d.drawLine(x+4, y+2, x+5, y+2);
		
		g2d.drawLine(x-2, y+3, x-1, y+3);
		g2d.drawLine(x+2, y+3, x+3, y+3);
		
		g2d.drawLine(x, y+4, x+1, y+4);
		
		//画黑边
		Color black = Color.black;
		g2d.setColor(black);
		g2d.drawLine(x-4, y+3, x-4, y+4);
		g2d.drawLine(x-4, y+4, x-3, y+4);
		
		g2d.drawLine(x, y+5, x+3, y+5);
		g2d.drawLine(x, y+6, x+1, y+6);
		
	}
	
	public static void main(String[] args) throws Exception{
		
		test3();
	}
	
}

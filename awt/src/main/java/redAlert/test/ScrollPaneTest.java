package redAlert.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * 测试一下滚动条方案是否成立
 */
public class ScrollPaneTest {

	
	
	public static void main(String[] args) {
		
		final int windowWidth = 1200;
		final int windowHeight = 900;
		
		//1.画好界面
		JFrame jf = new JFrame("测试");
		jf.setSize(400,300);
		jf.setResizable(false);
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见
		jf.setBackground(Color.black);
//		jf.setLayout(null);
		
		
		
		
		
		
		BufferedImage img = new BufferedImage(300,300,BufferedImage.TYPE_INT_ARGB);
		JPanel mp = new JPanel();
		mp.setLocation(0, 0);
		mp.setSize(800, 800);
		mp.setPreferredSize(new Dimension(1000, 1000));
		mp.setBackground( new Color(125,255,0,125));
		
		JScrollPane jspane = new JScrollPane(mp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//		JScrollPane jspane = new JScrollPane(mp);
//		jspane.getVerticalScrollBar().setValue(jspane.getVerticalScrollBar().getMaximum() / 2);
//		jspane.getViewport().setViewPosition(new Point(0,0));
//		jspane.setViewportView(mp);
//		jspane.setBackground(Color.red);
//		jspane.setOpaque(true);
		
		jf.add(jspane);
//		jf.add(mp);
		
		mp.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("收到");
				System.out.println(e.getY());
				
				jspane.getViewport().setViewPosition(new Point(0,100));
			}
		});
		mp.requestFocus();
		
		mp.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					jspane.getVerticalScrollBar().setValue(jspane.getVerticalScrollBar().getValue()+20);
				}
				if(e.getKeyCode()==KeyEvent.VK_UP){
					jspane.getVerticalScrollBar().setValue(jspane.getVerticalScrollBar().getValue()-20);
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					jspane.getHorizontalScrollBar().setValue(jspane.getHorizontalScrollBar().getValue()-20);
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					jspane.getHorizontalScrollBar().setValue(jspane.getHorizontalScrollBar().getValue()+20);
				}
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
}

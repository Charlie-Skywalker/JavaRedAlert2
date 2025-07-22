package redAlert.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

/**
 * 滚动条组件的测试
 */
public class JScrollPaneTest {

	public static final int frameWidth = 800;
	public static final int frameHeight = 600;
	
	public static void main(String[] args) {
		JFrame jf = new JFrame("红色警戒");
		jf.setSize(frameWidth,frameHeight);
		jf.setMaximumSize(new Dimension(frameWidth, frameHeight));
		jf.setResizable(false);//不可调整大小
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见,设置为可见
		
		JPanel mp = new JPanel();//最好重新paint方法
		mp.setLocation(0, 0);
		mp.setSize(1200, 900);
		mp.setPreferredSize(new Dimension(1200, 900));
		mp.setBackground(new Color(125,125,0,125));
		
//		JScrollPane jScrollPane = new JScrollPane(mp,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);//不显示滚动条
		JScrollPane jScrollPane = new JScrollPane(mp,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);//按需显示滚动条
		jScrollPane.getViewport().setScrollMode(JViewport.BLIT_SCROLL_MODE);//貌似是性能优化中很重要的点  待观察
//		jScrollPane.setBackground(Color.red);
		jf.add(jScrollPane);
		
		
		mp.requestFocus();//必须先获取焦点,键盘事件才能生效
		mp.addKeyListener(new KeyListener() {//添加键盘事件  控制滚动条移动

			@Override
			public void keyTyped(KeyEvent e) {
				
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getValue()+20);
				}
				if(e.getKeyCode()==KeyEvent.VK_UP){
					jScrollPane.getVerticalScrollBar().setValue(jScrollPane.getVerticalScrollBar().getValue()-20);
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					jScrollPane.getHorizontalScrollBar().setValue(jScrollPane.getHorizontalScrollBar().getValue()-20);
				}
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					jScrollPane.getHorizontalScrollBar().setValue(jScrollPane.getHorizontalScrollBar().getValue()+20);
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
		
		JScrollBar verticalScrollBar = jScrollPane.getVerticalScrollBar();//垂直滚动条
		verticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
		    @Override
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		    	System.out.println("??/");
		        // 滚动条位置改变时执行的操作
		    	mp.repaint(); // 重新绘制组件内容
		    }
		});
		
		JScrollBar horizonScrollBar = jScrollPane.getHorizontalScrollBar();//水平高滚动条
		horizonScrollBar.addAdjustmentListener(new AdjustmentListener() {
		    @Override
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		        // 滚动条位置改变时执行的操作
		    	mp.repaint(); // 重新绘制组件内容
		    }
		});
		
	}
}

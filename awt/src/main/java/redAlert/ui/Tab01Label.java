package redAlert.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.Constructor;
import redAlert.OptionsPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.tabIcon.Tab01Manager;

/**
 * 副基地按钮
 */
public class Tab01Label extends JLabel{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 选中时展示icon1   未选中时展示icon2
	 * 选中有就绪时 icon1和icon2轮流展示
	 * 未选中有就绪时  icon4和icon5轮流展示
	 */
	public static Icon icon1;//暗
	public static Icon icon2;//亮
	public static Icon icon3;//空白
	public static Icon icon4;//未选中时的暗
	public static Icon icon5;//未选中时的亮
	
	public Tab01Label mySelf;
	
	public Tab01Label() {
		List<ShapeUnitFrame> tab01Frame = ShpResourceCenter.loadShpResource("tab01", "sidebar", false);
		icon1 = new ImageIcon(tab01Frame.get(0).getImg());
		icon2 = new ImageIcon(tab01Frame.get(1).getImg());
		icon3 = new ImageIcon(tab01Frame.get(2).getImg());
		icon4 = new ImageIcon(tab01Frame.get(3).getImg());
		icon5 = new ImageIcon(tab01Frame.get(4).getImg());
		
		this.setBounds(26+28+1,38,icon1.getIconWidth(), icon1.getIconHeight());
		this.setOpaque(true);
		this.setIcon(icon1);
		
		addEvent();
		mySelf = this;
		addTimer();
	}
	
	public boolean isSelected = false;
	/**
	 * 选中
	 */
	public void setStateSelected() {
		this.setIcon(icon2);
		OptionsPanel.unitPanel01.setVisible(true);
		isSelected = true;
	}
	/**
	 * 不选中
	 */
	public void setStateUnselected() {
		OptionsPanel.unitPanel01.setVisible(false);
		if(this.getIcon()!=icon3) {
			this.setIcon(icon1);
		}
		
		isSelected = false;
	}
	
	public void addEvent() {
		this.addMouseListener(new MouseAdapter() {
			/**
			 * 实测红警扳手和贱卖按钮是左键松开时触发
			 * 松开时,如果鼠标已经不在本按钮上方,也不触发
			 * Component.getMousePosition获取不到鼠标坐标,则说明鼠标不在本按钮上方
			 */
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1 && mySelf.getMousePosition()!=null) {
					if(!mySelf.getIcon().equals(icon3)) {
						Constructor.playOneMusic("utab");
						OptionsPanel.tab00Label.setStateUnselected();
						OptionsPanel.tab01Label.setStateSelected();
						OptionsPanel.tab02Label.setStateUnselected();
						OptionsPanel.tab03Label.setStateUnselected();
					}
				}
				
			}
		});
	}
	
	
	
	public int counter = 0;
	
	public void addTimer() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			@Override
			public void run() {
				
				//显示条件 有主基地 有兵营
				if(!Tab01Manager.isShowTab01()) {
					mySelf.setIcon(icon3);
				}else {
					//判断是否有建造完成的建筑
					if(Tab01Manager.isExistReadyBuilding()) {
						if(mySelf.isSelected){
							if(counter%20>10) {
								if(!mySelf.getIcon().equals(icon2)) {
									mySelf.setIcon(icon2);
									mySelf.repaint();
								}
							}else {
								if(!mySelf.getIcon().equals(icon1)) {
									mySelf.setIcon(icon1);
									mySelf.repaint();
								}
							}
						}else {
							if(counter%20>10) {
								if(!mySelf.getIcon().equals(icon4)) {
									mySelf.setIcon(icon4);
									mySelf.repaint();
								}
							}else {
								if(!mySelf.getIcon().equals(icon5)) {
									mySelf.setIcon(icon5);
									mySelf.repaint();
								}
							}
						}
						counter++;
					}else {
						if(mySelf.isSelected){
							if(!mySelf.getIcon().equals(icon2)) {
								mySelf.setIcon(icon2);
								mySelf.repaint();
							}
						}else {
							if(!mySelf.getIcon().equals(icon1)) {
								mySelf.setIcon(icon1);
								mySelf.repaint();
							}
						}
						counter = 0;
					}
				}
			}
		};
		timer.schedule(refreshTask, 1L, 17);
	}
}

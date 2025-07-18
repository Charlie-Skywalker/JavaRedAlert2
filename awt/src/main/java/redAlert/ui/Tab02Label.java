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
import redAlert.militaryBuildings.AfPile;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 兵按钮
 */
public class Tab02Label extends JLabel{
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
	
	public Tab02Label mySelf;
	
	public Tab02Label() {
		List<ShapeUnitFrame> tab02Frame = ShpResourceCenter.loadShpResource("tab02", "sidebar", false);
		icon1 = new ImageIcon(tab02Frame.get(0).getImg());
		icon2 = new ImageIcon(tab02Frame.get(1).getImg());
		icon3 = new ImageIcon(tab02Frame.get(2).getImg());
		icon4 = new ImageIcon(tab02Frame.get(3).getImg());
		icon5 = new ImageIcon(tab02Frame.get(4).getImg());
		this.setBounds(26+28+1+28+1,38,icon1.getIconWidth(), icon1.getIconWidth());
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
		OptionsPanel.unitPanel02.setVisible(true);
		isSelected = true;
	}
	/**
	 * 不选中
	 */
	public void setStateUnselected() {
		OptionsPanel.unitPanel02.setVisible(false);
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
						OptionsPanel.tab01Label.setStateUnselected();
						OptionsPanel.tab02Label.setStateSelected();
						OptionsPanel.tab03Label.setStateUnselected();
					}
				}
			}
		});
	}
	
	public void addTimer() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			@Override
			public void run() {
				//判断是否有兵营(不分阵营)
				if(!ShapeUnitResourceCenter.containsBuildingClass(AfPile.class)) {
					mySelf.setIcon(icon3);
				}else {
					if(isSelected) {
						mySelf.setIcon(icon2);
					}else {
						mySelf.setIcon(icon1);
					}
				}
			}
		};
		timer.schedule(refreshTask, 1L, 17);
	}
	
	
}

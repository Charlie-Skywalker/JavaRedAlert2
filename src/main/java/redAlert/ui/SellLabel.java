package redAlert.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.Constructor;
import redAlert.OptionsPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 卖建筑按钮
 */
public class SellLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	
	/**
	 * 选中时展示icon1  未选中时展示icon2
	 */
	public Icon icon0;
	public Icon icon1;
	/**
	 * 当前按钮是否被选中
	 */
	public boolean isSelected = false;
	/**
	 * 自身引用
	 */
	public SellLabel mySelf;
	
	
	public SellLabel() {
		List<ShapeUnitFrame> sellFrame = ShpResourceCenter.loadShpResource("sell", "sidebar", false);
		BufferedImage img0 = sellFrame.get(0).getImg();
		BufferedImage img1 = sellFrame.get(1).getImg();
		this.setBounds(20+64,8,img0.getWidth(), img0.getHeight());
		icon0 = new ImageIcon(img0);
		icon1 = new ImageIcon(img1);
		
		this.setOpaque(true);
		this.setIcon(icon0);
		
		addEvent();
		
		mySelf = this;
	}
	
	/**
	 * 添加点击事件
	 */
	public void addEvent() {
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				
				if(!isSelected) {
					setSelected(true);
					Constructor.playOneMusic("umenucl1");
				}else {
					setSelected(false);
					Constructor.playOneMusic("umenucl1");
				}
			}
		});
	}

	public boolean isSelected() {
		return isSelected;
	}
	/**
	 * 设置选中状态
	 */
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if(isSelected) {
			this.setIcon(icon1);
		}else {
			this.setIcon(icon0);
		}
	}
	
	
}

package redAlert.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import redAlert.OptionsPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.tabIcon.Tab02ConstIcon;
import redAlert.tabIcon.Tab02Manager;

/**
 * 步兵图标栏
 * 包括背板、电力线、众多步兵图标点选面板
 */
public class UnitPanel02 extends JPanel{

	private static final long serialVersionUID = 1L;

	public BufferedImage side2 = null;//双人位背板
	
	public UnitPanel02 myself;
	
	public UnitPanel02() {
		myself = this;
		FlowLayout pf = (FlowLayout)getLayout();
		pf.setHgap(0);pf.setVgap(0);
		
		Tab02Manager.init();
		initResource();
		init();
		addTimer();
	}
	
	public List<Tab02ConstIcon> haveAddIcons = new ArrayList<>();
	
	public void initResource() {
		List<ShapeUnitFrame> side2Frame = ShpResourceCenter.loadShpResource("side2", "sidebar", false);
		side2 = side2Frame.get(0).getImg();
		side2 = side2.getSubimage(PowerPanel.width, 0, 168-PowerPanel.width, side2.getHeight());//去除放置电力线的部分
	}
	
	/**
	 * 初始化  只调用一次
	 */
	public void init() {
		List<Tab02ConstIcon> unit02LabelLs = Tab02Manager.getShowTabList();
		List<JLabel> side2List02 = new ArrayList<>();
		int unitPanelHeight2 = 0;
		
		//单位选项卡
		for(int i=0;i<unit02LabelLs.size();i+=2) {
			Tab02ConstIcon unitIconLabel = unit02LabelLs.get(i);
			Tab02ConstIcon unitIconLabel2 = null;
			if(i+1<unit02LabelLs.size()) {
				unitIconLabel2 = unit02LabelLs.get(i+1);
			}
			
			//新建一个双人位
			JLabel side2Label = new JLabel(new ImageIcon(side2));
			side2Label.setBounds(0,0,side2.getWidth(), side2.getHeight());
			side2Label.setOpaque(true);
			this.add(side2Label);
			unitPanelHeight2+=50;
			side2List02.add(side2Label);
			
			//把左边的放进去
			unitIconLabel.setLocation(22-PowerPanel.width, 0);
			side2Label.add(unitIconLabel);
			haveAddIcons.add(unitIconLabel);
			unitIconLabel.initTimer();
			
			//如果有右边的  也放进去
			if(unitIconLabel2!=null) {
				unitIconLabel2.setLocation(85-PowerPanel.width, 0);
				side2Label.add(unitIconLabel2);
				haveAddIcons.add(unitIconLabel2);
				unitIconLabel2.initTimer();
			}
		}
		
		int supEmptyItem2 = 12-unit02LabelLs.size()/2-unit02LabelLs.size()%2;
		//补足多个空白双人位
		for(int i=0;i<supEmptyItem2;i++) {
			//新建一个双人位
			JLabel side2Label = new JLabel();
			side2Label.setBounds(0,0,side2.getWidth(), side2.getHeight());
			Icon side2Icon = new ImageIcon(side2);
			side2Label.setOpaque(true);
			side2Label.setIcon(side2Icon);
			this.add(side2Label);
			unitPanelHeight2+=50;
			side2List02.add(side2Label);
		}
//		OptionsPanel.drawPower(side2List02);//画电力线
		this.setPreferredSize(new Dimension(168-PowerPanel.width,unitPanelHeight2));
		this.setVisible(false);
	}
	
	/**
	 * 重新加载面板  增加了判断条件  防止频繁重绘
	 */
	public void reloadLabels() {
		List<Tab02ConstIcon> unit02LabelLs = Tab02Manager.getShowTabList();
		if(haveAddIcons.containsAll(unit02LabelLs) && unit02LabelLs.containsAll(haveAddIcons)) {
			return;
		}
		this.removeAll();
		haveAddIcons.clear();
		
		List<JLabel> side2List02 = new ArrayList<>();
		int unitPanelHeight2 = 0;
		
		//单位选项卡
		for(int i=0;i<unit02LabelLs.size();i+=2) {
			Tab02ConstIcon unitIconLabel = unit02LabelLs.get(i);
			Tab02ConstIcon unitIconLabel2 = null;
			if(i+1<unit02LabelLs.size()) {
				unitIconLabel2 = unit02LabelLs.get(i+1);
			}
			
			//新建一个双人位
			JLabel side2Label = new JLabel(new ImageIcon(side2));
			side2Label.setBounds(0,0,side2.getWidth(), side2.getHeight());
			side2Label.setOpaque(true);
			this.add(side2Label);
			unitPanelHeight2+=50;
			side2List02.add(side2Label);
			
			//把左边的放进去
			unitIconLabel.setLocation(22-PowerPanel.width, 0);
			side2Label.add(unitIconLabel);
			haveAddIcons.add(unitIconLabel);
			unitIconLabel.initTimer();
			
			//如果有右边的  也放进去
			if(unitIconLabel2!=null) {
				unitIconLabel2.setLocation(85-PowerPanel.width, 0);
				side2Label.add(unitIconLabel2);
				haveAddIcons.add(unitIconLabel2);
				unitIconLabel2.initTimer();
			}
		}
		
		int supEmptyItem2 = 12-unit02LabelLs.size()/2-unit02LabelLs.size()%2;
		//补足多个空白双人位
		for(int i=0;i<supEmptyItem2;i++) {
			//新建一个双人位
			JLabel side2Label = new JLabel();
			side2Label.setBounds(0,0,side2.getWidth(), side2.getHeight());
			Icon side2Icon = new ImageIcon(side2);
			side2Label.setOpaque(true);
			side2Label.setIcon(side2Icon);
			this.add(side2Label);
			unitPanelHeight2+=50;
			side2List02.add(side2Label);
		}
//		OptionsPanel.drawPower(side2List02);//画电力线
		this.setPreferredSize(new Dimension(168-PowerPanel.width,unitPanelHeight2));
		if(OptionsPanel.tab02Label.isSelected) {
			this.setVisible(true);
			this.revalidate();//对组件重新布局并绘制
		}else {
			this.setVisible(false);
		}
//		this.repaint();
	}
	
	public void addTimer() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			@Override
			public void run() {
				reloadLabels();
			}
		};
		timer.schedule(refreshTask, 1L, 17);
	}
}

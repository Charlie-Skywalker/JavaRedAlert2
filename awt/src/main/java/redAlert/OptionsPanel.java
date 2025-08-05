package redAlert;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import redAlert.other.Mouse;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.tabIcon.Tab00ConstIcon;
import redAlert.tabIcon.Tab01ConstIcon;
import redAlert.tabIcon.Tab02ConstIcon;
import redAlert.tabIcon.Tab03ConstIcon;
import redAlert.ui.CreditsLabel;
import redAlert.ui.DiploLabel;
import redAlert.ui.OptLabel;
import redAlert.ui.PowerPanel;
import redAlert.ui.RadarLabel;
import redAlert.ui.RepairLabel;
import redAlert.ui.SellLabel;
import redAlert.ui.Tab00Label;
import redAlert.ui.Tab01Label;
import redAlert.ui.Tab02Label;
import redAlert.ui.Tab03Label;
import redAlert.ui.UnitPanel00;
import redAlert.ui.UnitPanel01;
import redAlert.ui.UnitPanel02;
import redAlert.ui.UnitPanel03;

/**
 * 红警右侧选项卡页面
 */
public class OptionsPanel extends JPanel{

	private static final long serialVersionUID = 810349439955934352L;
	
	/**
	 * 选项卡页面宽高
	 */
	public static final int optionWidth = 168;
	public static final int optionHeight = 900;
	
	public OptionsPanel() {
		GameContext.optionPanel = this;
		super.setPreferredSize(new Dimension(optionWidth,optionHeight));//JFrame是边界布局,只有这个方法可以设置大小, setSize和设置最大最小都没用
		super.setBackground(Color.green);
		initOptions();
		
		
		setCursor(Mouse.getDefaultCursor());//设置一个看不见的鼠标
		
		
		tab00Label.setStateSelected();//默认主基按钮是选中的
	}
	
	public BufferedImage top = null;//顶上的
	public BufferedImage side1 = null;//大背板
	public BufferedImage side2 = null;//双人位
	public BufferedImage side3 = null;//下选板
	public BufferedImage addon = null;//底板   下选板和底板必须合并使用
	public BufferedImage rdn = null;
	public BufferedImage rdn2 = null;
	public BufferedImage rup = null;
	public BufferedImage rup2 = null;
	
	public void loadResource() {
		List<ShapeUnitFrame> topFrame = ShpResourceCenter.loadShpResource("top", "sidebar", false);
		
		top = topFrame.get(0).getImg();
		List<ShapeUnitFrame> side1Frame = ShpResourceCenter.loadShpResource("side1", "sidebar", false);
		side1 = side1Frame.get(0).getImg();
		List<ShapeUnitFrame> side2Frame = ShpResourceCenter.loadShpResource("side2", "sidebar", false);
		side2 = side2Frame.get(0).getImg();
		List<ShapeUnitFrame> side3Frame = ShpResourceCenter.loadShpResource("side3", "sidebar", false);
		side3 = side3Frame.get(0).getImg();
		List<ShapeUnitFrame> addonFrame = ShpResourceCenter.loadShpResource("addon", "sidebar", false);
		addon = addonFrame.get(0).getImg();
		List<ShapeUnitFrame> rdnFrame = ShpResourceCenter.loadShpResource("r-dn", "sidebar", false);
		rdn = rdnFrame.get(0).getImg();
		rdn2 = rdnFrame.get(1).getImg();
		List<ShapeUnitFrame> rupFrame = ShpResourceCenter.loadShpResource("r-up", "sidebar", false);
		rup = rupFrame.get(0).getImg();
		rup2 = rupFrame.get(1).getImg();
		
		try {
//			ImageIO.write(credits, "png", new File("E:/credits.png"));
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public static CreditsLabel creditsLabel;//钱
	public static DiploLabel diploLabel;//顶端左按钮
	public static OptLabel optLabel; //顶端右按钮
	public static RadarLabel radarLabel;//雷达图
	public static RepairLabel repairLabel; //扳手
	public static SellLabel sellLabel;  //卖基地
	public static Tab00Label tab00Label;//主
	public static Tab01Label tab01Label;//防
	public static Tab02Label tab02Label;//兵
	public static Tab03Label tab03Label;//载
	
	public static UnitPanel00 unitPanel00;//主建筑图标栏
	public static UnitPanel01 unitPanel01;//防御建筑图标栏
	public static UnitPanel02 unitPanel02;//兵图标栏
	public static UnitPanel03 unitPanel03;//载具图标栏
	
	public static List<Tab00ConstIcon> unit00LabelLs;//所有主建筑图标
	public static List<Tab01ConstIcon> unit01LabelLs;//所有防御建筑图标
	public static List<Tab02ConstIcon> unit02LabelLs;//所有兵图标
	public static List<Tab03ConstIcon> unit03LabelLs;//所有坦图标
	
	/**
	 * 双人位背板的数量
	 * 本身这个值应该是按照分辨率计算出来的
	 * 但是现在来不及进行计算,先固定写死
	 */
	public static final int side2Num = 12;
	
	/**
	 * 初始化选项卡
	 */
	public void initOptions() {
		//消除间距
		FlowLayout f=(FlowLayout)getLayout();
		f.setHgap(0);
		f.setVgap(0);
		
		loadResource();
		//金钱显示框
		creditsLabel = new CreditsLabel();
		add(creditsLabel);
		//两个系统按钮的背板
		JLabel topLabel = new JLabel();
		topLabel.setBounds(0,16,top.getWidth(), top.getHeight());
		Icon topIcon = new ImageIcon(top);
		topLabel.setOpaque(true);
		topLabel.setIcon(topIcon);
		add(topLabel);
		//左系统按钮
		diploLabel = new DiploLabel();
		topLabel.add(diploLabel);
		//右系统按钮
		optLabel = new OptLabel();
		topLabel.add(optLabel);
		//雷达
		radarLabel = new RadarLabel();
		add(radarLabel);
		//修、卖、主基、副基、兵、坦的背板
		JLabel side1Label = new JLabel();
		side1Label.setBounds(0,0,side1.getWidth(), side1.getHeight());
		Icon side1Icon = new ImageIcon(side1);
		side1Label.setOpaque(true);
		side1Label.setIcon(side1Icon);
		add(side1Label);
		//修理
		repairLabel = new RepairLabel();
		side1Label.add(repairLabel);
		//卖
		sellLabel = new SellLabel();
		side1Label.add(sellLabel);
		//主基
		tab00Label = new Tab00Label();
		side1Label.add(tab00Label);
		//副基
		tab01Label = new Tab01Label();
		side1Label.add(tab01Label);
		//兵
		tab02Label =  new Tab02Label();
		side1Label.add(tab02Label);
		//坦
		tab03Label =  new Tab03Label();
		side1Label.add(tab03Label);
		
		
		
		
		//=====================主基地=======================
		unitPanel00 = new UnitPanel00();
//		unitPanel00.setLocation(-17*2, 0);
//		add(unitPanel00);
		//=====================主基地=======================
		
		//=====================副基地=======================
		unitPanel01 = new UnitPanel01();
//		unitPanel01.setLocation(-17*2, 0);
//		add(unitPanel01);
		//=====================副基地=======================
		
		//=====================兵=======================
		unitPanel02 = new UnitPanel02();
//		unitPanel02.setLocation(-17*2, 0);
//		add(unitPanel02);
		//=====================兵=======================
		
		//=====================坦克=======================
		unitPanel03 = new UnitPanel03();
//		unitPanel03.setLocation(-17*2, 0);
//		add(unitPanel03);
		//=====================坦克=======================
		
		//======================电力线和单位选择Icon===================
		JPanel unitComp = new JPanel();//unitComp 中添加两个JPanel  一个用来放电力线  一个用来放UnitPanel0X
		unitComp.setLayout(new BorderLayout());
		unitComp.setPreferredSize(new Dimension(168,unitPanel00.getPreferredSize().height));
		
		PowerPanel powerPanel = new PowerPanel();//电力线面板
		
		JPanel rightPanel = new JPanel();//单位选择面板
		rightPanel.setSize(168-PowerPanel.width,unitPanel00.getPreferredSize().height);
		rightPanel.setPreferredSize(new Dimension(168-PowerPanel.width,unitPanel00.getPreferredSize().height));
		FlowLayout pf = (FlowLayout)rightPanel.getLayout();
		pf.setHgap(0);pf.setVgap(0);
		
		rightPanel.add(unitPanel00);
		rightPanel.add(unitPanel01);
		rightPanel.add(unitPanel02);
		rightPanel.add(unitPanel03);
		
		unitComp.add(BorderLayout.CENTER,powerPanel);
		
		
		
		unitComp.add(BorderLayout.EAST,rightPanel);
		add(unitComp);
		//======================电力线和单位Tab===================
		
		
		
		//底板+下选板两张图片并成一张
		BufferedImage mergeImage = new BufferedImage(side3.getWidth(),side3.getHeight()+addon.getHeight(),BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d =  mergeImage.createGraphics();
		g2d.drawImage(side3, 0, 0 , null);
		g2d.drawImage(addon, 0, side3.getHeight(), null);
		
		//底板+下选板
		JLabel myLabel = new JLabel();
		myLabel.setBounds(0,0,side3.getWidth(), side3.getHeight()+addon.getHeight());
		Icon myIcon = new ImageIcon(mergeImage);
		myLabel.setOpaque(true);
		myLabel.setIcon(myIcon);
		add(myLabel);
		
		//下翻页
		JLabel rdnLabel = new JLabel();
		rdnLabel.setBounds(38,8,rdn.getWidth(), rdn.getHeight());
		Icon rdnIcon = new ImageIcon(rdn);
		rdnLabel.setOpaque(true);
		rdnLabel.setIcon(rdnIcon);
		myLabel.add(rdnLabel);
		
		//上翻页
		JLabel rupLabel = new JLabel();
		rupLabel.setBounds(38+46,8,rup.getWidth(), rup.getHeight());
		Icon rupIcon = new ImageIcon(rup);
		rupLabel.setOpaque(true);
		rupLabel.setIcon(rupIcon);
		myLabel.add(rupLabel);
		
	}
	
	
	public static BufferedImage yellowU = null;
	public static BufferedImage yellowD = null;
	public static BufferedImage redU = null;
	public static BufferedImage redD = null;
	public static BufferedImage greenU = null;
	public static BufferedImage greenD = null;
	public static BufferedImage gray2 = null;
	/**
	 * 画电力线
	 */
	public static void drawPower(List<JLabel>side2List) {
		List<ShapeUnitFrame> topFrame = ShpResourceCenter.loadShpResource("powerp", "sidebar", false);
		BufferedImage black = topFrame.get(0).getImg();
		BufferedImage green = topFrame.get(1).getImg();
		greenU = green.getSubimage(0, 0, 12, 1);
		greenD = green.getSubimage(0, 1, 12, 1);
		
		BufferedImage yellow = topFrame.get(2).getImg();
		yellowU = yellow.getSubimage(0, 0, 12, 1);
		yellowD = yellow.getSubimage(0, 1, 12, 1);
		
		BufferedImage red = topFrame.get(3).getImg();
		redU = red.getSubimage(0, 0, 12, 1);
		redD = red.getSubimage(0, 1, 12, 1);
		
		BufferedImage gray = topFrame.get(4).getImg();
		gray2 = gray.getSubimage(0, 0, 12, 1);
		
		int power = 800;
		//假设计算结果是这样的分布
		int greenNum = 50;
		int redNum = 10;
		int yellowNum = 10;
		
		List<String> ls = new ArrayList<>();
		for(int i=0;i<redNum;i++) {
			ls.add("rd");//暗红
			ls.add("ru");//明红
			ls.add("none");//无色
		}
		for(int i=0;i<yellowNum;i++) {
			ls.add("yd");//暗黄
			ls.add("yu");//明黄
			ls.add("none");//无色
		}
		for(int i=0;i<greenNum;i++) {
			ls.add("gd");//暗绿
			ls.add("gu");//明绿
			ls.add("none");//无色
		}
		
		
		int k = 0;
		
		for(int i=side2List.size()-1;i>0;i--) {
			JLabel label = side2List.get(i);
			BufferedImage image = new BufferedImage(12,50,BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = image.createGraphics();
			JLabel powerLabel = new JLabel();
			
			for(int j=49;j>=0;j--) {//一个双人位最多画50根线   16*(2彩+1无色)+1*2彩
				if(k<ls.size()) {
					String colorName = ls.get(k);
					if(!"none".equals(colorName)) {
						g2d.drawImage(getImage(colorName), 0, j, label);
					}
					k++;
				}else {
					break;
				}
			}
			
			powerLabel.setLocation(0, 0);
			powerLabel.setSize(12,50);
			powerLabel.setOpaque(false);
			powerLabel.setIcon(new ImageIcon(image));
			label.add(powerLabel);
		}
		
		
		
	}
	public static BufferedImage getImage(String a){
		if("rd".equals(a)) {
			return redD;
		}
		if("ru".equals(a)) {
			return redU;
		}
		if("gray".equals(a)) {
			return gray2;
		}
		if("yu".equals(a)) {
			return yellowU;
		}
		if("yd".equals(a)) {
			return yellowD;
		}
		if("gu".equals(a)) {
			return greenU;
		}
		if("gd".equals(a)) {
			return greenD;
		}
		return null;
	}
	
}

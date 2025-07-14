package redAlert;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import redAlert.enums.UnitColor;
import redAlert.militaryBuildings.AfPill;
import redAlert.militaryBuildings.AfTech;
import redAlert.other.Mouse;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.shapeObjects.soldier.Sniper;
import redAlert.shapeObjects.vehicle.GrizTank;
import redAlert.shapeObjects.vehicle.Mcv;
import redAlert.shapeObjects.vehicle.XiniuTank2;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utils.PointUtil;

/**
 * 程序启动类
 *
 */
public class MainTest {
	/**
	 * 游戏窗口的宽高
	 */
	public static final int frameWidth = MainPanel.viewportWidth + OptionsPanel.optionWidth;
	public static final int frameHeight = MainPanel.viewportHeight+32;//32是微软建议的标题栏高度
	
	/**
	 * 鼠标状态
	 */
	public static MouseStatus mouseStatus = MouseStatus.Idle;
	
	
	/**
	 * 鼠标的状态
	 */
	public enum MouseStatus{
		/**
		 * 空闲状态  鼠标指针为默认样式
		 */
		Idle("空闲"),
		/**
		 * 建造状态  鼠标指针也是默认样式
		 * 区别是会显示预建造红绿菱形块   建造状态按右键回到空闲状态
		 */
		Construct("建造"),
		/**
		 * 选中状态  鼠标指针也是默认样式
		 * 当鼠标左键按下并拖动时,鼠标进入选中状态  左键松开后回到空闲状态
		 */
		Select("选中"),
		/**
		 * 预选状态  鼠标指针为singleSelect样式
		 * 当鼠标为空闲状态时,放在单位上进入此状态  
		 * 当鼠标移出单位上时,回到空闲状态
		 */
		PreSingleSelect("预单选单位或建筑"),
		/**
		 * 预选敌方状态  鼠标指针为PreSingleSelectEnemy样式
		 * 当鼠标为空闲状态时,放在单位上进入此状态
		 * 当鼠标移出单位上时,回到空闲状态
		 */
		PreSingleSelectEnemy("预单选敌方单位或建筑"),
		/**
		 * 选中移动单位
		 */
		UnitMove("选中可移动单位"),
		/**
		 * 允许部署
		 */
		UnitExpand("单位部署"),
		/**
		 * 不允许部署
		 */
		UnitNoExpand("禁止部署"),
		/**
		 * 禁止移动
		 */
		UnitNoMove("禁止移动");
		
		private final String cnDesc;
		
		MouseStatus(String cnDesc){
			this.cnDesc = cnDesc;
		}
	}
	
	
	
	
	public static void main(String[] args) throws Exception{
		//程序窗口
		JFrame jf = new JFrame("红色警戒");
		
		
		//初始化鼠标指针形状图片
		Mouse.initMouseCursor();
		
		//游戏主界面
		MainPanel scenePanel = new MainPanel();
		
		jf.add(BorderLayout.CENTER,scenePanel);//格式布局放中间
		scenePanel.setCursor(Mouse.getNoneCursor());//设置一个看不见的鼠标
		GameContext.setCommanderNo(1);//设置指挥官编号
		GameContext.setUnitColor(UnitColor.Green);//设置指挥官编号

		//选项卡页面
		OptionsPanel optionsPanel = new OptionsPanel();
		jf.add(BorderLayout.EAST,optionsPanel);
		
		jf.setSize(frameWidth,frameHeight);
		jf.setResizable(false);//不可调整大小
		jf.setAlwaysOnTop(false);//总是在最上层
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);//屏幕居中
		jf.setVisible(true);//JFrame默认不可见,设置为可见
		jf.pack();
		
		/**
		 * 鼠标事件的处理
		 */
		MouseEventDeal.init(scenePanel);
		/**
		 * 键盘事件的处理
		 */
		KeyBoardEventDeal.init(scenePanel);
		/**
		 * 伤害计算器
		 */
		ShapeUnitResourceCenter.startDamageCalculate();
		
		
		
		
		
		int time = 2000;
		int enemyCommandNo = 2,enemyCommandNo2=3;//敌方指挥官编号
		UnitColor enemyUnitColor = UnitColor.Red, enemyUnitColor2 = UnitColor.Yellow;//敌方指挥官编号

		//改变建筑颜色的操作
		/*
		AfAirc afAric = new AfAirc(SceneType.TEM,UnitColor.LightBlue,500,550);
		Constructor.putOneBuilding(afAric,scenePanel);//空指部
		afAric.setUnitColor(UnitColor.Purple);
		
		Thread.sleep(3000);
		afAric.setUnitColor(UnitColor.Gray);//改变颜色  实现工程师占领
		Thread.sleep(3000);
		Constructor.putOneBuilding(new AfAirc(SceneType.TEM,UnitColor.Green,600,300),scenePanel);//盟军空指部
		*/
		
		//建造动画
//		AfCnst afCnst = new AfCnst(SceneType.TEM,UnitColor.Orange,400,300);afCnst.setStatus(BuildingStatus.UNDEMAGED);
//		Constructor.putOneBuilding(afCnst,scenePanel);//盟军基地
//		Thread.sleep(time);
//		AfPowr afPowr = new AfPowr(SceneType.TEM,UnitColor.Blue,900,320);afPowr.setStatus(BuildingStatus.DEMAGED);
//		Constructor.putOneBuilding(afPowr,scenePanel);//盟军发电场
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfPile(SceneType.TEM,UnitColor.Green,600,300),scenePanel);//盟军兵营
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfRefn(SceneType.TEM,UnitColor.Gray,700,425),scenePanel);//盟军矿场
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfYard(SceneType.TEM,UnitColor.Yellow,500,120),scenePanel);//船坞
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfDept(SceneType.TEM,UnitColor.Red,420,220),scenePanel);//维修厂
//		Thread.sleep(time);
//		AfTech afte = new AfTech(SceneType.TEM,UnitColor.Red,700,630);
//		Constructor.putOneBuilding(afte,scenePanel);//作战实验室
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfOrep(SceneType.TEM,UnitColor.Green,515,800),scenePanel);//矿石精炼厂
//		Thread.sleep(time);
		CenterPoint cp = PointUtil.getCenterPoint(800, 550);
		AfPill targetPill = new AfPill(cp,SceneType.TEM,GameContext.getUnitColor());
		targetPill.setCommanderNo(GameContext.getCommanderNo());//设置当前指挥官编号
		Constructor.putOneBuilding(targetPill,scenePanel);//机枪碉堡
		
		CenterPoint cp2222 = PointUtil.getCenterPoint(200, 550);
		AfPill targetPill2 = new AfPill(cp2222,SceneType.TEM, enemyUnitColor);
		targetPill2.setCommanderNo(enemyCommandNo);//设置敌方指挥官编号
		Constructor.putOneBuilding(targetPill2,scenePanel);//机枪碉堡
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfSam(SceneType.TEM,UnitColor.LightBlue,550,550),scenePanel);//爱国者飞弹
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfPris(SceneType.TEM,UnitColor.Yellow,400,550),scenePanel);//光棱塔
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfGap(SceneType.TEM,UnitColor.Purple,200,200),scenePanel);//裂缝产生器
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfGcan(SceneType.TEM,UnitColor.Purple,500,600),scenePanel);//巨炮
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfCsph(SceneType.TEM,UnitColor.Red,200,450),scenePanel);//超时空转换器
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfSpst(SceneType.TEM,UnitColor.Blue,300,600),scenePanel);//间谍卫星
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfWeth(SceneType.TEM,UnitColor.Green,300,700),scenePanel);//天气控制器
//		Thread.sleep(time);
//		Constructor.putOneBuilding(new AfComm(SceneType.TEM,UnitColor.Gray,300,150),scenePanel);//通信中心
		
		
		//核弹打小人
		/*
		CenterPoint cp5 = PointUtil.getCenterPoint(500, 500);
		Constructor.putOneShapeUnit(new Sniper(cp5.getX(),cp5.getY(),UnitColor.Red), scenePanel);//狙击手
		
		CenterPoint cpMisl = PointUtil.getCenterPoint(100, 300);
		SfMisl mislSilo = new SfMisl(cpMisl,SceneType.TEM,UnitColor.Blue);
		Constructor.putOneBuilding(mislSilo,scenePanel);//苏军核弹井
		Thread.sleep(2000);
		Constructor.playOneMusic("ceva001");//warning nuclear silo detected
		Thread.sleep(3000);
		mislSilo.nuclearSiloExpand();//展开核弹井
		Thread.sleep(5000);
		mislSilo.nuclearSiloLaunch();//发射核弹
		*/
		
		
		//重工生产坦克和基洛夫
		/*
		AfWeap weap = new AfWeap(SceneType.TEM,UnitColor.Purple,610,310);
		Constructor.putOneBuilding(weap,scenePanel);//建设工厂
		Thread.sleep(time);
		weap.setMakingVehicle(true);//正在生产坦克
		XiniuTank xt = new XiniuTank(weap.getPositionX()+weap.getCenterOffX()-64-32,weap.getPositionY()+weap.getCenterOffY()-64-16,"",UnitColor.Blue);
		Constructor.putOneShapeUnit(xt, scenePanel);//建造坦克
		xt.move = true;//坦克移动 
		xt.moveToTarget( PointUtil.getCenterPoint(xt.positionX+64+32*4, xt.positionY+64+16*4));
		Thread.sleep(5000);
		weap.setMakingVehicle(false);//恢复正常状态
		
		
		Thread.sleep(1000);
		ZepIn zepIn = new ZepIn(weap.getPositionX()+weap.getCenterOffX()-70 , weap.getPositionY()+weap.getCenterOffY()-70,"",UnitColor.Blue);
		Zep zep = new Zep(weap.getPositionX()+weap.getCenterOffX()-70 , weap.getPositionY()+weap.getCenterOffY()-80,"",UnitColor.Blue);
		zep.zepIn = zepIn;
		weap.setMakingFly(true);//正在生产飞行物
		Thread.sleep(100);
		Constructor.putOneShapeUnit(zep, scenePanel);//建造基洛夫
		Constructor.putOneShapeUnit(zepIn, scenePanel);//建造基洛夫影子
		Constructor.playOneMusic("vkirsea");
		Thread.sleep(100);
		zep.move = true;//基洛夫移动
		
		Thread.sleep(2000);
		weap.setMakingFly(false);//恢复正常状态
		*/
		
		
		//坦克寻路移动
		
//		CanvasPainter.drawRhombusDebug(PointUtil.getCenterPoint(1080,600), GameContext.getMainPanel().getGuidelinesCanvas());//辅助Debug的框框
//		CanvasPainter.drawRhombusDebug(PointUtil.getCenterPoint(32, 500), GameContext.getMainPanel().getGuidelinesCanvas());//辅助Debug的框框
//		CanvasPainter.drawRhombusDebug(PointUtil.getCenterPoint(1080, 100), GameContext.getMainPanel().getGuidelinesCanvas());//辅助Debug的框框
//		CanvasPainter.drawGuidelines(GameContext.getMainPanel().getGuidelinesCanvas());
		
		Thread.sleep(500);
		GrizTank gtank = new GrizTank(64*2-64,32*3-64,GameContext.getUnitColor());
		gtank.setCommanderNo(GameContext.getCommanderNo());//设置当前指挥官编号
		Constructor.putOneShapeUnit(gtank, scenePanel);//灰熊坦克
//		
		CenterPoint cc = PointUtil.getCenterPoint(300, 100);
		XiniuTank2 xt2 = new XiniuTank2(cc.getX()-64,cc.getY()-64, enemyUnitColor);
		xt2.setCommanderNo(enemyCommandNo);//设置敌方指挥官编号
		Constructor.putOneShapeUnit(xt2, scenePanel);//犀牛坦克
		
		Thread.sleep(1000);
		
		CenterPoint dd = PointUtil.getCenterPoint(600, 450);
		Mcv mcv = new Mcv(dd.getX()-64,dd.getY()-64,GameContext.getUnitColor());//基地车
		mcv.setCommanderNo(GameContext.getCommanderNo());//设置当前指挥官编号
		Constructor.putOneShapeUnit(mcv, scenePanel);

		CenterPoint dd2 = PointUtil.getCenterPoint(400, 450);
		Mcv mcv2 = new Mcv(dd2.getX()-64,dd2.getY()-64,enemyUnitColor);//基地车
		mcv2.setCommanderNo(enemyCommandNo);//设置当前指挥官编号
		Constructor.putOneShapeUnit(mcv2, scenePanel);
		mcv2.expand();

//		Thread.sleep(3000);
//		mcv.status = Mcv.MCV_STATUS_EXPANDING;
		
		
		
		
		CenterPoint cp1 = PointUtil.getCenterPoint(800, 600);
		CenterPoint cp2 = PointUtil.getCenterPoint(700, 600);
		CenterPoint cp3 = PointUtil.getCenterPoint(650, 650);
		CenterPoint cp4 = PointUtil.getCenterPoint(375, 620);
		
		LittleCenterPoint lcp1Up = cp1.getUpLittleCenterPoint();
		Sniper sniper1Lcp1Up = new Sniper(lcp1Up, GameContext.getUnitColor());
		sniper1Lcp1Up.setUnitName("狙击手0");
		sniper1Lcp1Up.setCommanderNo(GameContext.getCommanderNo());

		LittleCenterPoint lcp1Right = cp1.getRightLittleCenterPoint();
		Sniper sniper1Lcp1Right = new Sniper(lcp1Right, GameContext.getUnitColor());
		sniper1Lcp1Right.setUnitName("狙击手2");
		sniper1Lcp1Right.setCommanderNo(GameContext.getCommanderNo());

		Constructor.putOneShapeUnit(sniper1Lcp1Up, scenePanel);//狙击手
		Constructor.putOneShapeUnit(sniper1Lcp1Right, scenePanel);//狙击手
		
		
		LittleCenterPoint lcpRight = cp2.getRightLittleCenterPoint();
		Sniper sniper3 = new Sniper(lcpRight,enemyUnitColor);
		sniper3.setUnitName("狙击手3");
		sniper3.setCommanderNo(enemyCommandNo);//设置敌方指挥官编号
		Constructor.putOneShapeUnit(sniper3, scenePanel);//狙击手
		
		LittleCenterPoint lcpDown = cp3.getDownLittleCenterPoint();
		Sniper sniper4 = new Sniper(lcpDown,enemyUnitColor2);
		sniper4.setUnitName("狙击手4");
		sniper4.setCommanderNo(enemyCommandNo2);//设置第三方指挥官编号
		Constructor.putOneShapeUnit(sniper4, scenePanel);//狙击手
		
//		LittleCenterPoint lcp1 = cp4.getDownLittleCenterPoint();
//		Tany tany = new Tany(lcp1,UnitColor.Red);
//		Constructor.putOneShapeUnit(tany, scenePanel);//谭雅
//		
//		LittleCenterPoint lcp2 = cp4.getLeftLittleCenterPoint();
//		Tany tany2 = new Tany(lcp2,UnitColor.Blue);
//		Constructor.putOneShapeUnit(tany2, scenePanel);//谭雅
//		
//		LittleCenterPoint lcp3 = cp1.getLeftLittleCenterPoint();
//		Tany tany3 = new Tany(lcp3,UnitColor.Green);
//		Constructor.putOneShapeUnit(tany3, scenePanel);//谭雅
//		
//		LittleCenterPoint lcp4 = cp2.getLeftLittleCenterPoint();
//		Tany tany4 = new Tany(lcp4,UnitColor.Orange);
//		Constructor.putOneShapeUnit(tany4, scenePanel);//谭雅
		
//		sniper2.status = SoldierStatus.UMove;
//		sniper3.status = SoldierStatus.UMove;
//		sniper4.status = SoldierStatus.UMove;
		
//		LittleCenterPoint endTarget = PointUtil.getCenterPoint(492, 485).getLeftLittleCenterPoint();
//		sniper.nextTarget = endTarget;
//		sniper.endTarget = endTarget;
//		sniper.status = SoldierStatus.UMove;
		
//		afCnst.setToFetchCrate(true);
		
		//简单的攻击效果
		while(!targetPill.end) {
			gtank.attack();
		}
		while(!targetPill2.end) {
			gtank.attack();
		}
		
		
		
//		TankShell ts = new TankShell(200,200,500,500);
//		Constructor.putOneShapeUnit(ts, scenePanel);//炮弹
	}
}

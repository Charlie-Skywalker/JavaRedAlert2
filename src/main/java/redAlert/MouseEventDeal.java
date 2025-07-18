package redAlert;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingUtilities;

import redAlert.MainTest.MouseStatus;
import redAlert.enums.ConstConfig;
import redAlert.enums.ConstEnum;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfCsph;
import redAlert.militaryBuildings.AfDept;
import redAlert.militaryBuildings.AfOrep;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfPill;
import redAlert.militaryBuildings.AfPowr;
import redAlert.militaryBuildings.AfPris;
import redAlert.militaryBuildings.AfRefn;
import redAlert.militaryBuildings.AfSam;
import redAlert.militaryBuildings.AfSpst;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfWeap;
import redAlert.militaryBuildings.AfWeth;
import redAlert.militaryBuildings.AfYard;
import redAlert.militaryBuildings.SfMisl;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.Bloodable;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.Building.BuildingStage;
import redAlert.shapeObjects.Expandable;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.shapeObjects.Vehicle;
import redAlert.tabIcon.Tab00Manager;
import redAlert.tabIcon.Tab01Manager;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.LittleCenterPointUtil;
import redAlert.utils.MoveUtil;
import redAlert.utils.PointUtil;

/**
 * 专门用于处理鼠标事件
 */
public class MouseEventDeal {

	public static MainPanel scenePanel;
	
	/**
	 * 建造的建筑
	 */
	public static ConstConfig constName;
	
	/**
	 * 鼠标按下时的坐标
	 */
	public static Point press = new Point();
	
	public static Robot robot = null;
	
	
	/**
	 * 鼠标移动扫过的单位集合  只有一个元素
	 */
	public static Bloodable mouseBloodable = null;
	
	//线程池
	public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	
	public static void init(MainPanel scenePanel) {
		MouseEventDeal.scenePanel = scenePanel;
		press.x = 0;//鼠标按下时的X坐标
		press.y = 0;//鼠标按下时的Y坐标
		try {
			robot = new Robot();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		scenePanel.addMouseListener(new MouseListener() {
			
			/**
			 * 鼠标点击事件的含义是   鼠标按下时和松开时是在同一坐标,才视为是一次点击
			 * 不能将费时的操作直接写在方法中,不然会卡屏
			 * 费时的方法应该使用线程来处理
			 * @param mouseEvent
			 */
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				try {
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					CenterPoint centerPoint = coord.getCenterPoint();
					
					
					if(mouseEvent.getButton()==MouseEvent.BUTTON1) {//左键
						/*
						 * 单击选中一个单位 
						 */
						if(MainTest.mouseStatus == MouseStatus.PreSingleSelect) {
							if(centerPoint.isExistSingleSelectUnit()) {
								ShapeUnit unit = centerPoint.mouseClickGetUnit();
								if(unit instanceof MovableUnit) {
									MovableUnit movable = (MovableUnit)unit;
									//载具 直接选上
									if(movable instanceof Vehicle) {
										ShapeUnitResourceCenter.selectOneUnit(movable);
									}
									
									//如果中心点只有一个步兵,则直接选上,否则先确认小中心点,再选中
									if(movable instanceof Soldier) {
										if(centerPoint.getSoldiers().size()==1) {
											ShapeUnitResourceCenter.selectOneUnit(movable);
										}else {
											//需要确认小中心点
											LittleCenterPoint lcp = LittleCenterPointUtil.getLittleCenterPoint(coord.getMapX(),coord.getMapY());
//											LittleCenterPoint lcp = PointUtil.getMinDisLCP(coord.getMapX(), coord.getMapY(), centerPoint);
											System.out.println("确认的小中心点"+lcp);
											Soldier soldier = lcp.getSoldier();
											ShapeUnitResourceCenter.selectOneUnit(soldier);
										}
									}
									movable.selectPlay();
								}
								
								if(unit instanceof Building) {
									Building selectedBuilding = (Building)unit;
									hideAllBloodBar();
									ShapeUnitResourceCenter.unselectBuilding();
									mouseBloodable = selectedBuilding;
									ShapeUnitResourceCenter.selectOneBuilding(selectedBuilding);
								}
							}
							
							resetMouseStatus(coord);
						}
					}
					if(mouseEvent.getButton()==MouseEvent.BUTTON2) {//中键
					}
					
					if(mouseEvent.getButton()==MouseEvent.BUTTON3) {//右键
						/*
						 * 取消建造
						 */
						if(MainTest.mouseStatus == MouseStatus.Construct) {
							MainTest.mouseStatus = MouseStatus.Idle;
							resetMouseStatus(coord);
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			/**
			 * 鼠标按下
			 */
			@Override
			public void mousePressed(MouseEvent e) {
				if(MainTest.mouseStatus == MouseStatus.PreSingleSelect) {
					hideAllBloodBar();
				}
				
				press.x = e.getX();
				press.y = e.getY();
			}

			/**
			 * 鼠标松开事件(鼠标无论是否正在移动,均触发)
			 */
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				
				try {
					CanvasPainter.removeSelectRect(scenePanel.getCanvasFirst());
					
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					
					if(mouseEvent.getButton()==MouseEvent.BUTTON1) {//左键
						/**
						 * 选中群体单位
						 */
						if(MainTest.mouseStatus==MouseStatus.Select) {
							
							//存在已选中的单位,则变为移动指针
							if(!ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {
								MainTest.mouseStatus = MouseStatus.UnitMove;
							}
							//看是否有选中单位
							int startx = press.x;
							int starty = press.y;
							int mapStartX = CoordinateUtil.getMapCoordX(startx, coord.getViewportOffX());
							int mapStartY = CoordinateUtil.getMapCoordY(starty, coord.getViewportOffY());
							
							List<MovableUnit> selectedUnits = ShapeUnitResourceCenter.getMovableUnitFromWarMap(mapStartX, mapStartY, coord.getMapX(), coord.getMapY());
							
							if(selectedUnits.isEmpty()) {//没选中任何单位
								if(ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {//此前有选中的单位,取消选中
									MainTest.mouseStatus = MouseStatus.Idle;
									return;
								}else {//控制此前选中的单位移动到指定点
									MainTest.mouseStatus = MouseStatus.UnitMove;
									
									CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
									List<MovableUnit> units = ShapeUnitResourceCenter.selectedMovableUnits;
									if(units.size()==1) {
										MovableUnit moveUnit = units.get(0);
										moveUnit.movePlay();
										
										//不适用AWT线程来控制移动   不然可能会卡屏
										//所以在线程池中执行
										Thread thread = new Thread() {
											public void run() {
												MoveUtil.move(moveUnit, targetCp);
											}
										};
										threadPoolExecutor.execute(thread);
										
									}else {
										//命令移动时说话
										int playNum = 0;
										for(int i=0;i<units.size();i++) {
											MovableUnit unit = units.get(i);
											if(playNum<1) {
												unit.movePlay();
												playNum++;
											}
										}
										
										//不适用AWT线程来控制移动   不然可能会卡屏
										//所以在线程池中执行
										Thread thread = new Thread() {
											public void run() {
												MoveUtil.move(units, coord.getMapX(), coord.getMapY());
											}
										};
										threadPoolExecutor.execute(thread);
										
									}
									
									return;
								}
							}else {
								ShapeUnitResourceCenter.cancelSelect();
								ShapeUnitResourceCenter.addAll(selectedUnits);
								MainTest.mouseStatus = MouseStatus.UnitMove;
								
								//选中时说话
								int playNum = 0;
								for(int i=0;i<ShapeUnitResourceCenter.selectedMovableUnits.size();i++) {
									MovableUnit unit = ShapeUnitResourceCenter.selectedMovableUnits.get(i);
									if(playNum<1) {
										unit.selectPlay();
										playNum++;
									}
								}
								
							}
							return;
						}
						
						
						/**
						 * 用户指挥单位进行移动
						 */
						if(MainTest.mouseStatus==MouseStatus.UnitMove) {
							CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
							List<MovableUnit> units = ShapeUnitResourceCenter.selectedMovableUnits;
							if(units.size()==1) {
								MovableUnit moveUnit = units.get(0);
								//不适用AWT线程来控制移动   不然可能会卡屏
								//所以在线程池中执行
								Thread thread = new Thread() {
									public void run() {
										MoveUtil.move(moveUnit, targetCp);
									}
								};
								threadPoolExecutor.execute(thread);
								
								moveUnit.movePlay();
							}else {
								//不适用AWT线程来控制移动   不然可能会卡屏
								//所以在线程池中执行
								Thread thread = new Thread() {
									public void run() {
										MoveUtil.move(units, coord.getMapX(), coord.getMapY());
									}
								};
								threadPoolExecutor.execute(thread);
								
								//命令移动时说话
								int playNum = 0;
								for(int i=0;i<units.size();i++) {
									MovableUnit unit = units.get(i);
									if(playNum<1) {
										unit.movePlay();
										playNum++;
									}
								}
								
							}
							return;
						}
						
						/**
						 * 用户对单位进行展开
						 */
						if(MainTest.mouseStatus==MouseStatus.UnitExpand) {
							CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
							ShapeUnit shapeUnit = targetCp.mouseClickGetUnit();
							if(shapeUnit instanceof Expandable) {
								Expandable exUnit = (Expandable)shapeUnit;
								if(exUnit.isExpandable()) {
									exUnit.expand();
								}
							}
							
							resetMouseStatus(coord);
							return;
						}
						
						/**
						 * 贱卖建筑
						 */
						if(MainTest.mouseStatus==MouseStatus.Sell) {
							CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
							ShapeUnit shapeUnit = targetCp.mouseClickGetUnit();
							if(shapeUnit instanceof Building) {
								Building unit = (Building)shapeUnit;
								if(unit.stage!=BuildingStage.Selling) {
									unit.setStage(BuildingStage.Selling);
									Constructor.playOneMusic("uselbuil");
								}
							}
							
							resetMouseStatus(coord);
							return;
						}
						
						
						
						/**
						 * 军事建筑建造
						 */
						if(MainTest.mouseStatus == MouseStatus.Construct) {
							
							CenterPoint center = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
							
							if(constName==ConstConfig.AfCnst) {
								AfCnst sf = new AfCnst(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//基地车
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfPowr) {
								AfPowr sf = new AfPowr(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//发电厂
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfRefn) {
								AfRefn sf = new AfRefn(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//采矿场
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfPile) {
								AfPile sf = new AfPile(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//盟军兵营
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfWeap) {
								AfWeap sf = new AfWeap(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//建设工厂
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfAirc) {
								AfAirc sf = new AfAirc(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//空指部
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
									if(ShapeUnitResourceCenter.isPowerOn) {
										OptionsPanel.radarLabel.triggleRadarShow();
									}
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfYard) {
								AfYard sf = new AfYard(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//船坞
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfDept) {
								AfDept sf = new AfDept(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//维修厂
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfTech) {
								AfTech sf = new AfTech(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//盟军作战实验室
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfOrep) {
								AfOrep sf = new AfOrep(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//矿石精炼厂
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							
							if(constName==ConstConfig.AfPill) {
								AfPill sf = new AfPill(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//机枪碉堡
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							
							if(constName==ConstConfig.AfPris) {
								AfPris sf = new AfPris(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//光棱塔
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							
							if(constName==ConstConfig.AfSam) {
								AfSam sf = new AfSam(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//爱国者飞弹
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							
							if(constName==ConstConfig.AfCsph) {
								AfCsph sf = new AfCsph(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//超时空转换器
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfWeth) {
								AfWeth sf = new AfWeth(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//天气控制器
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.AfSpst) {
								AfSpst sf = new AfSpst(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//间谍卫星
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab01Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							if(constName==ConstConfig.SfMisl) {
								SfMisl sf = new SfMisl(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
								boolean isSuccess = Constructor.putOneBuilding(sf,scenePanel);//苏军核弹井
								if(isSuccess) {
									MainTest.mouseStatus = MouseStatus.Idle;
									constName = null;
									Tab00Manager.freeAll();
								}else {
									repaintRhombus(mouseEvent);
								}
								return;
							}
							
							return;
						}
					}
					
					
					if(mouseEvent.getButton()==MouseEvent.BUTTON3) {//右键
						/*
						 * 已选中的可移动单位取消选中
						 */
						ShapeUnitResourceCenter.cancelSelect();
						/*
						 * 已选中的建筑取消选中
						 */
						ShapeUnitResourceCenter.unselectBuilding();
						/*
						 * 如果卖建筑按钮是选中状态,则取消选中
						 */
						if(OptionsPanel.sellLabel.isSelected()) {
							OptionsPanel.sellLabel.setSelected(false);
							OptionsPanel.sellLabel.repaint();
						}
						/*
						 * 如果修理按钮是选中状态,则取消选中
						 */
						if(OptionsPanel.repairLabel.isSelected()) {
							OptionsPanel.repairLabel.setSelected(false);
						}
						
						
						resetMouseStatus(coord);
						//移动一下鼠标  触发一下移动事件
						Point mousePoint = MouseInfo.getPointerInfo().getLocation();
						robot.mouseMove(mousePoint.x+1, mousePoint.y);
						robot.mouseMove(mousePoint.x, mousePoint.y);
						return;
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			/**
			 * 鼠标进入
			 */
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			/**
			 * 鼠标退出
			 */
			@Override
			public void mouseExited(MouseEvent e) {
				
			}
			
			/**
			 * 当建造建筑时所使用的地块有单位占用,重新把预建造占位图画上
			 */
			public void repaintRhombus(MouseEvent mouseEvent) {
				if(MainTest.mouseStatus == MouseStatus.Construct) {
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					CanvasPainter.drawRhombus(coord.getMapX(), coord.getMapY(), constName.fxNum, constName.fyNum, scenePanel.getCanvasFirst());
				}
			}
			
		});
		
		
		
		/**
		 * TODO 鼠标拖动和移动事件
		 */
		scenePanel.addMouseMotionListener(new MouseMotionListener() {
			/**
			 * 按下鼠标时拖动鼠标触发
			 * 
			 * 按下鼠标时未拖动则不触发
			 */
			@Override
			public void mouseDragged(MouseEvent mouseEvent) {
				try {
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					int mapX = coord.getMapX();
					int mapY = coord.getMapY();
					
					if(SwingUtilities.isLeftMouseButton(mouseEvent)){//鼠标左键
						if(MainTest.mouseStatus==MouseStatus.Construct) {
							if(mapX==scenePanel.getLastMoveX() && mapY==scenePanel.getLastMoveY()) {
								return;
							}else {
								scenePanel.setLastMoveX(mapX);
								scenePanel.setLastMoveY(mapY);
								
								CenterPoint centerPoint = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
								CenterPoint lastCenterPoint = scenePanel.getLastMoveCenterPoint();
								if(centerPoint.equals(lastCenterPoint)) {
									return;
								}else {
									scenePanel.setLastMoveCenterPoint(centerPoint);
									//这个方法不能调用太频繁   太频繁的绘图会导致程序卡顿
									CanvasPainter.drawRhombus(centerPoint, constName.fxNum, constName.fyNum, scenePanel.getCanvasFirst());
								}
								return;
							}
						}else {
							scenePanel.setLastMoveX(mapX);
							scenePanel.setLastMoveY(mapY);
							MainTest.mouseStatus = MouseStatus.Select;
							CanvasPainter.drawSelectRect(press.x, press.y, coord.getViewX(), coord.getViewY(),scenePanel.getCanvasFirst());
						}
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			
			
			/**
			 * 鼠标移动时(此时没有鼠标按下)
			 */
			@Override
			public void mouseMoved(MouseEvent mouseEvent) {
				
				try {
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					int mapX = coord.getMapX();
					int mapY = coord.getMapY();
					
					/**
					 * 建造状态的判定优先级最高
					 */
					if(MainTest.mouseStatus == MouseStatus.Construct) {
						if(mapX==scenePanel.getLastMoveX() && mapY==scenePanel.getLastMoveY()) {
							return;
						}else {
							scenePanel.setLastMoveX(mapX);
							scenePanel.setLastMoveY(mapY);
							
							CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
							CenterPoint lastCenterPoint = scenePanel.getLastMoveCenterPoint();
							if(centerPoint.equals(lastCenterPoint)) {
								return;
							}else {
								scenePanel.setLastMoveCenterPoint(centerPoint);
								//这个方法不能调用太频繁   太频繁的绘图会导致程序卡顿
								CanvasPainter.drawRhombus(centerPoint, constName.fxNum, constName.fyNum, scenePanel.getCanvasFirst());
							}
							return;
						}
					}
					
					//显示鼠标下的单位的血条
					CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
					if(centerPoint.isExistSingleSelectUnit()) {
						ShapeUnit unit = centerPoint.mouseClickGetUnit();
						
						if(unit instanceof Bloodable) {
							if(unit.equals(mouseBloodable)) {
								mouseBloodable.getBloodBar().setVisible(true);//有可能被右键取消显示,需要加此行代码
							}else {
								Bloodable bloodableUnit = (Bloodable)unit;
								bloodableUnit.getBloodBar().setVisible(true);
								
								if(mouseBloodable!=null) {
									if(mouseBloodable.equals(ShapeUnitResourceCenter.selectedBuilding)) {
										
									}else {
										if(ShapeUnitResourceCenter.selectedMovableUnits.contains(mouseBloodable)) {
											
										}else {
											mouseBloodable.getBloodBar().setVisible(false);
										}
									}
								}else {
									
								}
								
								mouseBloodable = bloodableUnit;
								
							}
						}
						
					}else {
						if(mouseBloodable!=null) {
							if(mouseBloodable.equals(ShapeUnitResourceCenter.selectedBuilding)) {
								
							}else {
								if(ShapeUnitResourceCenter.selectedMovableUnits.contains(mouseBloodable)) {
									
								}else {
									mouseBloodable.getBloodBar().setVisible(false);
								}
							}
							mouseBloodable = null;
						}else {
							
						}
					}
					
					resetMouseStatus(coord);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				
				
			}
			
		});
		
	}
	
	/**
	 * 重画建筑占用红绿菱形
	 * 坦克移动时这个红绿菱形会动态变化  以后需要调用这个方法
	 */
	public static void redrawConstructRhombus() {
		Point p = scenePanel.getMousePosition();
		if(p!=null) {
			int mouseX = p.x;
			int mouseY = p.y;
			int viewportOffX = MainPanel.viewportOffX;
			int viewportOffY = MainPanel.viewportOffY;
			int mapX = CoordinateUtil.getMapCoordX(mouseX, viewportOffX);
			int mapY = CoordinateUtil.getMapCoordX(mouseY, viewportOffY);
			CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
			CanvasPainter.drawRhombus(centerPoint, constName.fxNum, constName.fyNum, scenePanel.getCanvasFirst());
		}
	}
	
	
	
	/**
	 * 根据外部环境,重新设置鼠标状态变量
	 * 
	 * 鼠标的状态应该变为怎样  应该不依赖于当前鼠标状态,而是依赖于外部环境
	 * 外部环境是指:比如点击了修理或卖建筑按钮、当前有选中单位、鼠标下方有单位等等
	 */
	public static void resetMouseStatus(Coordinate coord) {
		
		int mapX = coord.getMapX();
		int mapY = coord.getMapY();
		
		CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
		
		if(OptionsPanel.sellLabel.isSelected()) {//用户点击了卖建筑按钮
			Building building = centerPoint.getBuilding();
			
			if(building!=null && building.stage!=BuildingStage.Selling) {
				if(building.getUnitColor()==GlobalConfig.unitColor) {
					MainTest.mouseStatus = MouseStatus.Sell;
					return;
				}
			}
			MainTest.mouseStatus = MouseStatus.NoSell;
			
			return;
		}
		
		
		if(ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {//没有选中可移动单位时  只可能是空闲或单选
			
			if(!centerPoint.isExistSingleSelectUnit()) {//鼠标处不存在可选中单位->空闲状态
				MainTest.mouseStatus = MouseStatus.Idle;
			}else {
				ShapeUnit unit = centerPoint.mouseClickGetUnit();
				Building selectedBuilding = ShapeUnitResourceCenter.selectedBuilding;
				if(selectedBuilding!=null) {//存在已选中的建筑
					if(selectedBuilding.equals(unit)) {
						MainTest.mouseStatus = MouseStatus.Idle;
					}else {
						MainTest.mouseStatus = MouseStatus.PreSingleSelect;//单选状态
					}
				}else {
					MainTest.mouseStatus = MouseStatus.PreSingleSelect;//单选状态
				}
			}
		}else {
			
			if(!centerPoint.isExistSingleSelectUnit()) {//鼠标是否在单位上
				//单位移动鼠标
				MainTest.mouseStatus = MouseStatus.UnitMove;
			}else {
				ShapeUnit unitUnderMouse = centerPoint.mouseClickGetUnit();
				
				if(unitUnderMouse instanceof Expandable) {//可部署
					if(ShapeUnitResourceCenter.selectedMovableUnits.size()==1) {
						ShapeUnit selectedShapeUnit = ShapeUnitResourceCenter.selectedMovableUnits.get(0);
						if(unitUnderMouse.equals(selectedShapeUnit)) {
							Expandable ex = (Expandable)unitUnderMouse;
							if(ex.isExpandable()) {
								//部署鼠标
								MainTest.mouseStatus = MouseStatus.UnitExpand;
							}else {
								//禁止部署鼠标
								MainTest.mouseStatus = MouseStatus.UnitNoExpand;
							}
						}else {
							//单选鼠标
							MainTest.mouseStatus = MouseStatus.PreSingleSelect;
						}
					}else {
						if(ShapeUnitResourceCenter.selectedMovableUnits.contains(unitUnderMouse)) {
							//禁止移动
							MainTest.mouseStatus = MouseStatus.UnitNoMove;
						}else {
							//单选鼠标
							MainTest.mouseStatus = MouseStatus.PreSingleSelect;
						}
					}
				}else {
					
					//如果鼠标上的单位是选中的单位中的某个,则是禁止移动鼠标,否则是单选鼠标
					if(ShapeUnitResourceCenter.selectedMovableUnits.contains(unitUnderMouse)) {
						//禁止移动
						MainTest.mouseStatus = MouseStatus.UnitNoMove;
					}else {
						//单选鼠标
						MainTest.mouseStatus = MouseStatus.PreSingleSelect;
					}
				}
				
			}
		}
	}
	
	//隐藏所有单位的血条
	public static void hideAllBloodBar() {
		if(mouseBloodable!=null) {
			mouseBloodable.getBloodBar().setVisible(false);
		}
	}
	
	
}

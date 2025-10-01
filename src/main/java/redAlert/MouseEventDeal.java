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

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import redAlert.enums.ConstConfig;
import redAlert.enums.MouseStatus;
import redAlert.event.ConstructEvent;
import redAlert.event.EventHandlerManager;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.*;
import redAlert.shapeObjects.Building.BuildingStage;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.LittleCenterPointUtil;
import redAlert.utils.MoveUtil;
import redAlert.utils.PointUtil;

/**
 * 专门用于处理鼠标事件
 */
public class MouseEventDeal {
	
	/**
	 * 建造的建筑
	 */
	public static ConstConfig constName;
	
	public static Robot robot = null;
	
	
	/**
	 * 鼠标移动扫过的单位集合  只有一个元素
	 */
	public static Bloodable mouseBloodable = null;
	
	//线程池
	public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(3,5,60, TimeUnit.SECONDS,new ArrayBlockingQueue<>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	
	public static void init(JPanel scenePanel) {
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
						if(RuntimeParameter.mouseStatus == MouseStatus.PreSingleSelect) {


							// 清空一下已选择列表
							ShapeUnitResourceCenter.cancelSelect();

							// 清空一下已选择建筑
							ShapeUnitResourceCenter.unselectBuilding();

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
						if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {
							RuntimeParameter.mouseStatus = MouseStatus.Idle;
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
			public void mousePressed(MouseEvent mouseEvent) {
				if(RuntimeParameter.mouseStatus == MouseStatus.PreSingleSelect) {
					hideAllBloodBar();
				}
				RuntimeParameter.pressX = mouseEvent.getX();
				RuntimeParameter.pressY = mouseEvent.getY();
			}

			/**
			 * 鼠标松开事件(鼠标无论是否正在移动,均触发)
			 */
			@Override
			public void mouseReleased(MouseEvent mouseEvent) {
				
				try {
					Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
					
					if(mouseEvent.getButton()==MouseEvent.BUTTON1) {//左键
						/**
						 * 选中群体单位
						 */
						if(RuntimeParameter.mouseStatus==MouseStatus.Select) {
							
							//存在已选中的单位,则变为移动指针
							if(!ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {
								RuntimeParameter.mouseStatus = MouseStatus.UnitMove;
							}
							//看是否有选中单位
							int startx = RuntimeParameter.pressX;
							int starty = RuntimeParameter.pressY;
							int mapStartX = CoordinateUtil.getMapCoordX(startx, coord.getViewportOffX());
							int mapStartY = CoordinateUtil.getMapCoordY(starty, coord.getViewportOffY());
							
							List<MovableUnit> selectedUnits = ShapeUnitResourceCenter.getMovableUnitFromWarMap(mapStartX, mapStartY, coord.getMapX(), coord.getMapY());
							Building selectedBuilding = ShapeUnitResourceCenter.selectedBuilding;
							if(selectedUnits.isEmpty()) {//没选中任何单位

								if(ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {//此前没有选中的单位,取消选中
									RuntimeParameter.mouseStatus = MouseStatus.Idle;
									return;
								}else {//控制此前选中的单位移动到指定点
									RuntimeParameter.mouseStatus = MouseStatus.UnitMove;

									CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
									List<MovableUnit> units = ShapeUnitResourceCenter.selectedMovableUnits;
									if(units.size()==1) {
										MovableUnit moveUnit = units.get(0);
										moveUnit.movePlay();
										
										// 如果此前选中的单位是从部署建筑变化而来
										if(moveUnit instanceof TankExpandable && selectedBuilding instanceof TankExpandable
												&& ((TankExpandable) selectedBuilding).getExpandStatus()==TankExpandable.TANK_STATUS_NORMAL) {
											//建筑卖掉原地显示载具
											TankExpandable exBuilding = (TankExpandable) selectedBuilding;
											exBuilding.unexpandAndTransfer(moveUnit,targetCp);
											ShapeUnitResourceCenter.unselectBuilding();
											return;
										}


										
										
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
								RuntimeParameter.mouseStatus = MouseStatus.UnitMove;
								
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
							// 只要此前有选中的建筑,则取消选中
							ShapeUnitResourceCenter.unselectBuilding();
							return;
						}
						
						
						/**
						 * 用户指挥单位进行移动
						 */
						if(RuntimeParameter.mouseStatus==MouseStatus.UnitMove) {

							CenterPoint targetCp = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
							List<MovableUnit> units = ShapeUnitResourceCenter.selectedMovableUnits;
							Building selectedBuilding = ShapeUnitResourceCenter.selectedBuilding;


							if(units.size()==1) {
								MovableUnit moveUnit = units.get(0);
								//不适用AWT线程来控制移动   不然可能会卡屏
								//所以在线程池中执行
								// 判断一下移动的单位是不是从部署建筑变化而来
								if(moveUnit instanceof TankExpandable && selectedBuilding instanceof TankExpandable
										&& ((TankExpandable) selectedBuilding).getExpandStatus()==TankExpandable.TANK_STATUS_NORMAL) {
									//建筑卖掉原地显示载具

									TankExpandable exBuilding = (TankExpandable) selectedBuilding;
									exBuilding.unexpandAndTransfer(moveUnit,targetCp);
									ShapeUnitResourceCenter.unselectBuilding();
									return;

								}


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
						if(RuntimeParameter.mouseStatus==MouseStatus.UnitExpand) {
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
						 * 用户预选
						 */

						/**
						 * 贱卖建筑
						 */
						if(RuntimeParameter.mouseStatus==MouseStatus.Sell) {
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
						if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {

							//发布一个红警建筑建造事件
							EventHandlerManager.publishOneEvent(new ConstructEvent(mouseEvent, constName));
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
						if(RuntimeParameter.mouseStatus==MouseStatus.Construct) {
							if(mapX==RuntimeParameter.lastMoveX && mapY==RuntimeParameter.lastMoveY) {
								return;
							}else {
								RuntimeParameter.lastMoveX = mapX;
								RuntimeParameter.lastMoveY = mapY;

								CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
								CenterPoint lastCenterPoint = RuntimeParameter.lastMoveCenterPoint;
								if(centerPoint.equals(lastCenterPoint)) {
									return;
								}else {
									RuntimeParameter.lastMoveCenterPoint = centerPoint;
								}
								return;
							}
						}else {
							RuntimeParameter.lastMoveX = mapX;
							RuntimeParameter.lastMoveY = mapY;
							RuntimeParameter.mouseStatus = MouseStatus.Select;
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
					if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {
						if(mapX==RuntimeParameter.lastMoveX && mapY==RuntimeParameter.lastMoveY) {
							return;
						}else {
							RuntimeParameter.lastMoveX = mapX;
							RuntimeParameter.lastMoveY = mapY;

							CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
							CenterPoint lastCenterPoint = RuntimeParameter.lastMoveCenterPoint;
							if(centerPoint.equals(lastCenterPoint)) {
								return;
							}else {
								RuntimeParameter.lastMoveCenterPoint = centerPoint;
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
					RuntimeParameter.mouseStatus = MouseStatus.Sell;
					return;
				}
			}
			RuntimeParameter.mouseStatus = MouseStatus.NoSell;
			
			return;
		}
		
		
		if(ShapeUnitResourceCenter.selectedMovableUnits.isEmpty()) {//没有选中可移动单位时  只可能是空闲或单选
			
			if(!centerPoint.isExistSingleSelectUnit()) {//鼠标处不存在可选中单位->空闲状态
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
			}else {
				ShapeUnit unit = centerPoint.mouseClickGetUnit();
				Building selectedBuilding = ShapeUnitResourceCenter.selectedBuilding;
				if(selectedBuilding!=null) {//存在已选中的建筑
					if(selectedBuilding.equals(unit)) {
						//如果选中的建筑是可部署的,则看是否可以取消部署
						if(selectedBuilding instanceof TankExpandable) {
							TankExpandable ex = (TankExpandable) selectedBuilding;
							if (ex.isUnExpandable()) {
								RuntimeParameter.mouseStatus = MouseStatus.UnitMove;
								//创建一个该建筑关联的取消部署的载具
								MovableUnit movableUnit = ex.getUnexpandUnit();

								ShapeUnitResourceCenter.selectedMovableUnits.add(movableUnit);

							}else{
								RuntimeParameter.mouseStatus = MouseStatus.Idle;
							}
						}else{
							RuntimeParameter.mouseStatus = MouseStatus.Idle;
						}
					}else {
						RuntimeParameter.mouseStatus = MouseStatus.PreSingleSelect;//单选状态
					}
				}else {
					RuntimeParameter.mouseStatus = MouseStatus.PreSingleSelect;//单选状态
				}
			}
		}else {
			
			if(!centerPoint.isExistSingleSelectUnit()) {//鼠标是否在单位上
				//单位移动鼠标
				RuntimeParameter.mouseStatus = MouseStatus.UnitMove;
			}else {
				ShapeUnit unitUnderMouse = centerPoint.mouseClickGetUnit();

				
				if(unitUnderMouse instanceof Expandable) {//可部署
					if(ShapeUnitResourceCenter.selectedMovableUnits.size()==1) {
						ShapeUnit selectedShapeUnit = ShapeUnitResourceCenter.selectedMovableUnits.get(0);
						if(unitUnderMouse.equals(selectedShapeUnit)) {
							Expandable ex = (Expandable)unitUnderMouse;
							if(ex.isExpandable()) {
								//部署鼠标
								RuntimeParameter.mouseStatus = MouseStatus.UnitExpand;
							}else {
								//禁止部署鼠标
								RuntimeParameter.mouseStatus = MouseStatus.UnitNoExpand;
							}
						}else {
							//单选鼠标
							RuntimeParameter.mouseStatus = MouseStatus.PreSingleSelect;
						}
					}else {
						if(ShapeUnitResourceCenter.selectedMovableUnits.contains(unitUnderMouse)) {
							//禁止移动
							RuntimeParameter.mouseStatus = MouseStatus.UnitNoMove;
						}else {
							//单选鼠标
							RuntimeParameter.mouseStatus = MouseStatus.PreSingleSelect;
						}
					}
				}else {
					
					//如果鼠标上的单位是选中的单位中的某个,则是禁止移动鼠标,否则是单选鼠标
					if(ShapeUnitResourceCenter.selectedMovableUnits.contains(unitUnderMouse)) {
						//禁止移动
						RuntimeParameter.mouseStatus = MouseStatus.UnitNoMove;
					}else {
						//单选鼠标
						RuntimeParameter.mouseStatus = MouseStatus.PreSingleSelect;
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

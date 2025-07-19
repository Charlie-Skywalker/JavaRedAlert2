package redAlert.shapeObjects;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import redAlert.Constructor;
import redAlert.GameContext;
import redAlert.ShapeUnitFrame;
import redAlert.enums.BuildingAreaType;
import redAlert.enums.UnitColor;
import redAlert.other.VehicleBloodBar;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.XunLuBeanAdapter;
import redAlert.utils.PointUtil;
import redAlert.utils.VxlFileReader;

/**
 * 载具的超类
 * 
 * 载具:能够在地面上行驶的坦克类玩意，特点是单独占据一个中心点，如：犀牛坦克、磁力棒、蜘蛛等， 但暂时不包括运输艇
 * 
 * 默认可旋转
 * 默认可攻击
 */
public abstract class Vehicle extends MovableUnit implements Turnable,Attackable{
	
	/**
	 * 载具车体的帧画面
	 */
	public List<ShapeUnitFrame> bodyFrames;
	/**
	 * 下一个目标中心点
	 * 移动过程中会不断改变,当移动到此点后,自动计算下一个移动点
	 * 无论是重寻路和强制终止,都必须移动到此点后生效
	 */
	public volatile CenterPoint nextTarget;
	/**
	 * 移动路径  静止时为null
	 * 一般由A*算法计算得到,第一个元素是载具所在地,最后一个元素是移动终点@see endTarget
	 */
	public List<CenterPoint> movePath;
	/**
	 * 移动终点
	 * 由用户鼠标点击确定
	 */
	public CenterPoint endTarget;
	/**
	 * 重定位
	 * 当移动过程中被重新指定移动终点,将赋值true
	 */
	public boolean resetTarget = false;
	/**
	 * 寻路锁,避免AWT线程和规划线程同时进行寻路
	 */
	public ReentrantLock xunluLock = new ReentrantLock(true);
	/**
	 * 坦克的炮塔  拥有炮塔的坦克才会初始化这个东西
	 */
	public TankTurret turret;
	/**
	 * 最大血量  载具固定1700
	 */
	public final static int maxHp = 1700;
	/**
	 * 最大血块个数  载具固定34  但表示34个明暗条纹 知乎上称这是17格血
	 */
	public final static int maxBloodNum = 34;
	/**
	 * 当前血量
	 */
	public int curHp = 1700;
	/**
	 * 车身目标转向
	 */
	public int targetTurn = 2;
	/**
	 * 车身当前转向
	 */
	public int curTurn = 2;
	/**
	 * 是否可以攻击
	 * 载具默认是可以攻击的
	 */
	public boolean attackable = true;
	
	
	
	
	/**
	 * 载具图标都是128*128大小  所以偏移量是64
	 * 为保证载具的移动速度 设置帧速是2  偏快
	 */
	public Vehicle() {
		super.centerOffX = 64;
		super.centerOffY = 64;
		super.priority = 50;
		super.frameSpeed = 2;
	}
	
	/**
	 * 发动机状态
	 * 只要认为发动基状态是启动的
	 */
	public enum EngineStatus{
		Started("已启动"),
		Stopped("已停止");//只有刚生产出来、到达了终点、寻不到路被迫停止的载具状态才是停车
		
		public String desc;
		
		private EngineStatus(String desc) {
			this.desc = desc;
		}
		
	}
	
	public EngineStatus engineStatus = EngineStatus.Stopped;
	
	public int speed = 0;//静止是0  运动时>0
	
	
	/**
	 * 初始化载具公有变量
	 */
	public void initVehicleParam(CenterPoint bornCp, UnitColor unitColor, String vxlPrefix){
		super.positionX = bornCp.getX()-super.centerOffX;
		super.positionY = bornCp.getY()-super.centerOffY;
		super.unitColor = unitColor;
		this.bodyFrames = VxlFileReader.convertPngFileToBuildingFrames(vxlPrefix,16,1,unitColor);
		super.curFrame = bodyFrames.get(curTurn).copy();
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		curCenterPoint = bornCp;
		curCenterPoint.addVehicle(this);
		if(curCenterPoint.isInShadow()) {
			this.isHided();
		}
		//血条
		super.bloodBar = new VehicleBloodBar(this);
		Constructor.putOneShapeUnit(super.bloodBar, GameContext.scenePanel);
	}
	
	/**
	 * 初始化载具公有变量
	 */
	public void initVehicleParam(int positionX, int positionY, UnitColor unitColor, String vxlPrefix){
		CenterPoint bornCp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		initVehicleParam(bornCp,unitColor,vxlPrefix);
	}
	
	/**
	 * 命令载具移动的方法
	 * 
	 * 当用户通过鼠标命令单位移动时调用
	 */
	@Override
	public void moveToTarget(CenterPoint moveTarget) {
		//校验这个位置是否符合条件
		if(moveTarget==null) {
			return;
		}
		//不可达地点
		if(!moveTarget.isVehicleCanOn()) {
			movePath=null;
			endTarget = null;
			return;
		}
		
		//从静止状态开始移动的
		if(!haveNextTarget()) {
			
			
			xunluLock.lock();
			try {
				XunLuBeanAdapter xlb = XunLuBeanAdapter.getInstance();
				List<CenterPoint> planMovePath = xlb.xunlu(curCenterPoint, moveTarget);
				if(planMovePath!=null && planMovePath.size()>1) {
					this.nextTarget = planMovePath.get(0);
					this.endTarget = planMovePath.get(planMovePath.size()-1);
					this.movePath = planMovePath;
//					nextTarget.addBook(this);
					
					//确定炮塔的旋转方向
					if(turret!=null) {
						turret.calAndSetTargetTurn(this, moveTarget);
					}
				}else {
					/*
					 * 红警2的奇怪的逻辑：
					 * 当位置不可达时,会寻找一个与目标单位X坐标相同或Y坐标相同的位置进行移动
					 * 
					 */
					System.out.println("指定位置不可达");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				xunluLock.unlock();
			}
			
		}
		//目标正在移动,然后指定新的目标位置
		else {
			/*
			stopFlag = true;
			
			while(true) {
				if(curCenterPoint.equals(nextTarget)) {
					break;
				}else {
					try {
						Thread.sleep(0);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			//直接从当前位置寻路就可以了
			xunluLock.lock();
			try {
				XunLuBeanAdapter xlb = XunLuBeanAdapter.getInstance();
				List<CenterPoint> path = xlb.xunlu(curCenterPoint, moveTarget);
				if(path!=null) {
					this.nextTarget = path.get(0);
					this.movePath = path;
					this.endTarget = path.get(path.size()-1);
					resetTarget = true;
					nextTarget.addBook();
					
					//确定炮塔的旋转方向
					if(turret!=null) {
						turret.calAndSetTargetTurn(this, moveTarget);
					}
				}else {
					System.out.println("重指定位置不可达");
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				xunluLock.unlock();
			}
			
			stopFlag = false;
			*/
			
		}
	}
	
	/**
	 * 是否有下一个移动目标
	 * 这个方法计划为以后移动遮挡和移动避让做准备
	 */
	public boolean haveNextTarget() {
		if(nextTarget==null) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * 是否已经到达制定目标中心点
	 */
	public boolean isArrivedNextTarget() {
		int nextPositionX = nextTarget.getX()-centerOffX;
		int nextPositionY = nextTarget.getY()-centerOffY;
		if(nextPositionX==positionX && nextPositionY==positionY) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 是否已经到达终点
	 */
	public boolean isArrivedEndTarget() {
		int nextPositionX = endTarget.getX()-centerOffX;
		int nextPositionY = endTarget.getY()-centerOffY;
		if(nextPositionX==positionX && nextPositionY==positionY) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 移动一次
	 */
	public void moveOneTime() {
		/**
		 * 车体方向需旋转到位才能移动
		 */
		if(targetTurn!=curTurn) {
			turn();
			return;
		}
		
		/*
		 * 需要注意：当前位置可能不是中心点  所以需要比较目的地的Position坐标
		 */
		int nextTargetX = nextTarget.getX();
		int nextTargetY = nextTarget.getY();
		int nextPositionX = nextTargetX-centerOffX;
		int nextPositionY = nextTargetY-centerOffY;
		
		if(positionX!=nextPositionX) {
			if(Math.abs(positionX-nextPositionX)<10) {//修正补齐
				positionX = nextPositionX;
			}else if(positionX<nextPositionX) {
				positionX+=10;
			}else if(positionX>nextPositionX) {
				positionX-=10;
			}
		}
		
		if(positionY!=nextPositionY) {
			if(Math.abs(positionY-nextPositionY)<5) {//修正补齐
				positionY = nextPositionY;
			}else if(positionY<nextPositionY) {
				positionY+=5;
			}else if(positionY>nextPositionY) {
				positionY-=5;
			}
		}
		
		//更新格子状态
		CenterPoint newCenterPoint = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		//移动过程中的中间点如果发现所在点是已占用点   不更新当前格子状态
		
		/*
		 * 当载具来到下一个中心点且完全对齐后,才释放占用预约
		 * 若是到达终点,还要熄火
		 */
		if(nextTargetX==positionX+centerOffX && nextTargetY==positionY+centerOffY && movePath.contains(newCenterPoint)) {
			newCenterPoint.addVehicle(this);
			newCenterPoint.exitBook(this);
			if(newCenterPoint.equals(endTarget)) {
				setEngineStatus(EngineStatus.Stopped);
			}
		}
		
		//中心点上的坦克  还是要即时更新的  不然会有建筑建造Bug
		/*
		 * 当载具移动到新地界,立即释放原地界中心点的载具引用
		 * 并把当前所在中心点更新为新中心点
		 */
		if(!newCenterPoint.equals(curCenterPoint) && movePath.contains(newCenterPoint)) {
			
			curCenterPoint.removeUnit(this);
			newCenterPoint.addVehicle(this);//这一行代码的合理性存疑
			speed = 1;

			
			if(newCenterPoint.isInShadow()) {
				this.setHided(true);
			}else {
				this.setHided(false);
			}
			curCenterPoint = newCenterPoint;
		}
		
		
		
	}
	
	/**
	 * 载具帧计算
	 */
	@Override
	public void calculateNextFrame() {
		//没有下一个位置
		if(nextTarget==null) {
			return;
		}
		//没有终点
		if(endTarget==null ) {
			return;
		}
		//没有路线
		if(movePath==null || movePath.size()<1) {
			return;
		}
		
		//判定是否已经到达下一个地点
		if(isArrivedNextTarget()) {
			if(isArrivedEndTarget()) {//判断是否已经到达终点
				movePath = null;
				nextTarget=null;
				endTarget=null;
				setEngineStatus(EngineStatus.Stopped);
				curCenterPoint.exitBook(this);
			}else {
				
				//临时停止标志,此时停止移动,等待寻路完成,重新设置目的地
				if(stopFlag) {
					nextTarget = null;
					return;
				}
				
				
				//若发生重定位,说明movePath变量发生了变化,获取nextTarget的方式要改变
				if(resetTarget) {
					nextTarget = movePath.get(0);
					resetTarget = false;
					calAndSetTargetTurn(this, nextTarget);
					speed = 1;
					moveOneTime();
				}else {//根据当前位置  确认下一个位置
					int curIndex = movePath.indexOf(curCenterPoint);
					nextTarget = movePath.get(curIndex+1);
					
					
					calAndSetTargetTurn(this, nextTarget);//释放预约占用前 计算好下一步的车身转向,需要转动则把自己设置为转向中，这样后边的坦克不会撞上来
					/**
					 * 车体方向需旋转到位才能移动
					 */
					if(targetTurn!=curTurn) {
						turn();
					}
					
					//退出预占领
					curCenterPoint.exitBook(this);//实际上在此以前已经释放了占用预约
					
					//先收集一下nextTarget的信息  并放入栈中  后续不再查询nextTarget的信息  避免逻辑错误
					boolean isVehicleCanOn = nextTarget.isVehicleCanOn();
					boolean isExistBuilding = nextTarget.buildingAreaType==BuildingAreaType.Normal;
					boolean isExistSolider = !nextTarget.getSoldiers().isEmpty();
					boolean isBookedByOther = nextTarget.isBooked();
					Vehicle nVehicle = nextTarget.getVehicle();
					boolean isExistVehicle = nVehicle!=null;
					boolean isVehicleStopped = true;//需要先判定nVehicle是否为空才能使用
					if(nVehicle!=null) {
						if(nVehicle.getEngineStatus()==EngineStatus.Started) {
							isVehicleStopped = false;
						}
					}
					
					if(isExistVehicle) {
						if(isVehicleStopped) {
							//重寻路
							//实现重新规划线路
							xunluLock.lock();
							try {
								if(!nextTarget.equals(endTarget)) {
									XunLuBeanAdapter xlb = XunLuBeanAdapter.getInstance();
									List<CenterPoint> path = xlb.xunlu(curCenterPoint, endTarget);
									if(path!=null) {
										this.movePath = path;
										nextTarget = movePath.get(0);
									}else {
										nextTarget = null;
										endTarget = null;
										movePath = null;
										setEngineStatus(EngineStatus.Stopped);
										curCenterPoint.exitBook(this);
										speed = 0;
									}
								}else {
									nextTarget = null;
									endTarget = null;
									movePath = null;
									setEngineStatus(EngineStatus.Stopped);
									curCenterPoint.exitBook(this);
									speed = 0;
									System.out.println("下个点就是终点,而终点不可达2");
								}
							}catch (Exception e) {
								System.out.println("程序自动寻路异常");
								e.printStackTrace();
							}finally {
								xunluLock.unlock();
							}
							
						}else {
							//等待
							nextTarget = curCenterPoint;
						}
					}else {
						if(isExistBuilding || isExistSolider) {
							//重寻路
							//实现重新规划线路
							xunluLock.lock();
							try {
								if(!nextTarget.equals(endTarget)) {
									XunLuBeanAdapter xlb = XunLuBeanAdapter.getInstance();
									List<CenterPoint> path = xlb.xunlu(curCenterPoint, endTarget);
									if(path!=null) {
										this.movePath = path;
										nextTarget = movePath.get(0);
									}else {
										nextTarget = null;
										endTarget = null;
										movePath = null;
										setEngineStatus(EngineStatus.Stopped);
										curCenterPoint.exitBook(this);
										speed = 0;
									}
								}else {
									nextTarget = null;
									endTarget = null;
									movePath = null;
									setEngineStatus(EngineStatus.Stopped);
									curCenterPoint.exitBook(this);
									speed = 0;
									System.out.println("下个点就是终点,而终点不可达2");
								}
							}catch (Exception e) {
								System.out.println("程序自动寻路异常");
								e.printStackTrace();
							}finally {
								xunluLock.unlock();
							}
							
						}else {
							if(isBookedByOther) {
								//等待
								nextTarget = curCenterPoint;
							}else {
								boolean bookedFlag = nextTarget.addBook(this);
								if(bookedFlag) {
									//移动
									speed = 1;
									moveOneTime();//是否应该移动   代码不一定会走到这里
								}else {
									//等待
									nextTarget = curCenterPoint;
								}
							}
						}
					}
					
				}
			}
		}else {
			moveOneTime();
		}
		
	}
	
	/**
	 * 载具的开火方法默认调用炮塔的开火方法
	 * 
	 * 有特殊攻击方法的载具，由载具的实现类重写此方法
	 */
	@Override
	public void attack(Building building) {
		if(isAttackable()) {
			if(turret!=null) {
				turret.attack(building);
			}
			
		}
	}
	
	

	public int getMaxHp() {
		return maxHp;
	}
	public int getCurHp() {
		return curHp;
	}
	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}
	public int getTargetTurn() {
		return targetTurn;
	}
	public void setTargetTurn(int targetTurn) {
		this.targetTurn = targetTurn;
	}
	public int getCurTurn() {
		return curTurn;
	}
	public void setCurTurn(int curTurn) {
		this.curTurn = curTurn;
	}
	public EngineStatus getEngineStatus() {
		return engineStatus;
	}
	public void setEngineStatus(EngineStatus engineStatus) {
		this.engineStatus = engineStatus;
	}
	public int getSpeed() {
		return speed;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public List<ShapeUnitFrame> getBodyFrames() {
		return bodyFrames;
	}
	public void setBodyFrames(List<ShapeUnitFrame> bodyFrames) {
		this.bodyFrames = bodyFrames;
	}
	public CenterPoint getNextTarget() {
		return nextTarget;
	}
	public void setNextTarget(CenterPoint nextTarget) {
		this.nextTarget = nextTarget;
	}
	public List<CenterPoint> getMovePath() {
		return movePath;
	}
	public void setMovePath(List<CenterPoint> movePath) {
		this.movePath = movePath;
	}
	public CenterPoint getEndTarget() {
		return endTarget;
	}
	public void setEndTarget(CenterPoint endTarget) {
		this.endTarget = endTarget;
	}
	public boolean isResetTarget() {
		return resetTarget;
	}
	public void setResetTarget(boolean resetTarget) {
		this.resetTarget = resetTarget;
	}
	public boolean isStopFlag() {
		return stopFlag;
	}
	public void setStopFlag(boolean stopFlag) {
		this.stopFlag = stopFlag;
	}
	public ReentrantLock getXunluLock() {
		return xunluLock;
	}
	public void setXunluLock(ReentrantLock xunluLock) {
		this.xunluLock = xunluLock;
	}
	public TankTurret getTurret() {
		return turret;
	}
	public void setTurret(TankTurret turret) {
		this.turret = turret;
	}
	public static int getMaxhp() {
		return maxHp;
	}
	public static int getMaxbloodnum() {
		return maxBloodNum;
	}
	public void setAttackable(boolean attackable) {
		this.attackable = attackable;
	}
	@Override
	public boolean isAttackable() {
		return attackable;
	}
	

	
	
	
}

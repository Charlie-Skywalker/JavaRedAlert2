package redAlert.shapeObjects.soldier;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.enums.Direction;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Soldier;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utils.LittleCenterPointUtil;
import redAlert.utils.PointUtil;

/**
 * 谭雅
 */
public class Tany extends Soldier{
	
	public Tany(LittleCenterPoint lcp,UnitColor color) {
		
		super(lcp,"tany",color);
		super.centerOffX = 62;
		super.centerOffY = 57;
		super.positionX = lcp.x - centerOffX;
		super.positionY =  lcp.y - centerOffY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "tany";
		
	}

	
	public List<LittleCenterPoint> haveGetPoint = new ArrayList<>();//已经走过的路
	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	@Override
	public void calculateNextFrame() {
		
		if(status==SoldierStatus.UMove) {
			if(nextTarget==null) {
				status = SoldierStatus.Standing;
				return;
			}
			
			
			//判定是否已经到达下一个地点
			if(isArrivedNextTarget()) {
				if(isArrivedEndTarget()) {//判断是否已经到达终点
					movePath = null;
					nextTarget=null;
					endTarget=null;
					status = SoldierStatus.Standing;
					curLittleCenterPoint.preBooked.compareAndSet(true, false);
					haveGetPoint.clear();
					//终点不能和别的步兵站一起  需要找一个周围可以站立的点
					
				}else {
					//是否有临时停止标志  有的话  需要暂停计算  等待寻路完成  帧停顿
					if(stopFlag) {
						return;
					}
					
					//若发生重定位,说明movePath变量发生了变化,获取nextTarget的方式要改变
					if(resetTarget) {
//						nextTarget = movePath.get(1);
						resetTarget = false;
						moveOneStep();
					}else {
						if(curLittleCenterPoint.equals(movePath.get(0))) {//到达了路线中的下一个,出队
							movePath.remove(0);
						}
						LittleCenterPoint pathNextTarget = movePath.get(0);//查看当前点与下一个是否相邻
						
						if(LittleCenterPointUtil.isNeibor(curLittleCenterPoint, pathNextTarget)) {
							if(pathNextTarget.equals(endTarget)) {//下一个是不是终点
								if(pathNextTarget.isSoldierCanOn() && pathNextTarget.getSoldier()==null) {
									nextTarget = pathNextTarget;
									moveOneStep();
								}else {
									movePath = null;
									nextTarget=null;
									endTarget=null;
									status = SoldierStatus.Standing;
									curLittleCenterPoint.preBooked.compareAndSet(true, false);
									haveGetPoint.clear();
								}
							}else {
								//判断这个点是否可达
								if(pathNextTarget.isSoldierCanOn()) {
									nextTarget = pathNextTarget;
									moveOneStep();
								}else {
									movePath.remove(0);//从路线上移除这个点  然后再自然寻路
								}
							}
						}else {
							if(!pathNextTarget.isSoldierCanOn()) {
								movePath.remove(0);
								return;
							}
							
							Map<String,Object> result = findNextTarget(pathNextTarget,haveGetPoint);//根据当前位置  找到周围离终点最近的下一个位置
							String desc = (String)result.get("desc");
							if("ok".equals(desc)) {
								nextTarget= (LittleCenterPoint)result.get("cp");
								haveGetPoint.add(nextTarget);
								moveOneStep();
							}else if("occ".equals(desc)) {
								//其他兵临时占用 需要等待  什么也不做
							}else if("unacc".equals(desc)) {
								//其他兵把这个点当作终点使用了
								haveGetPoint.add((LittleCenterPoint)result.get("cp"));
								//下次继续找
							}else if("noway".equals(desc)) {
								//兵走进死胡同了
								System.out.println("走死胡同了");
							}
						}
						
						
//						int curIndex = movePath.indexOf(nextTarget);
//						nextTarget = movePath.get(curIndex+1);
//						
//						//确认这个位置是否可达
//						if(nextTarget.isSoldierCanOn()) {
//							moveOneStep();
//						}else {
//							//实现重新规划线路
//							xunluLock.lock();
//							try {
//								SoldierXunLuBean xlb = new SoldierXunLuBean();
//								List<LittleCenterPoint> path = xlb.xunlu(curLittleCenterPoint, endTarget);
//								if(path!=null) {
//									this.movePath = path;
//									nextTarget = movePath.get(1);
//									moveOneStep();
//								}else {
//									nextTarget = null;
//									endTarget = null;
//									movePath = null;
//								}
//							}catch (Exception e) {
//								System.out.println("程序自动寻路异常");
//								e.printStackTrace();
//							}finally {
//								xunluLock.unlock();
//							}
//						}
						
					}
					
				}
			}else {
				moveOneStep();
			}
			
		}
		
		
		if(status==SoldierStatus.Ease1) {
			easeIndex++;
			int slowIndex = (easeIndex/10)%easeList1.size();
			
			
			ShapeUnitFrame frame = easeList1.get(slowIndex);
			transToColorful(frame);//上阵营色
			
			easeIndex++;
			
			if(easeIndex== (easeList1.size()-1)*10 ) {
				status = SoldierStatus.Standing;
				curDirection=Direction.LeftDown;//细节  动作一做完了 站立朝左下
				easeIndex = 0;
			}
			return;
		}
		
		if(status==SoldierStatus.Ease2) {
			easeIndex++;
			int slowIndex = (easeIndex/10)%easeList2.size();
			ShapeUnitFrame frame = easeList2.get(slowIndex);
			transToColorful(frame);//上阵营色
			
			easeIndex++;
			if(easeIndex== (easeList2.size()-1)*10 ) {
				status = SoldierStatus.Standing;
				curDirection=Direction.RightDown;//细节  动作二做完了 站立朝右下
				easeIndex = 0;
			}
			return;
		}
		
		if(status==SoldierStatus.Standing) {
			
			ShapeUnitFrame frame = null;
			if(curDirection==Direction.Up) {
				frame = standingFrames.get(0);
			}
			if(curDirection==Direction.LeftUp) {
				frame = standingFrames.get(1);
			}
			if(curDirection==Direction.Left) {
				frame = standingFrames.get(2);
			}
			if(curDirection==Direction.LeftDown) {
				frame = standingFrames.get(3);
			}
			if(curDirection==Direction.Down) {
				frame = standingFrames.get(4);
			}
			if(curDirection==Direction.RightDown) {
				frame = standingFrames.get(5);
			}
			if(curDirection==Direction.Right) {
				frame = standingFrames.get(6);
			}
			if(curDirection==Direction.RightUp) {
				frame = standingFrames.get(7);
			}
			
			standingTime++;
			if(standingTime>200) {
				Random r = new Random();
				int n = r.nextInt(3);
				if(n==0) {
					status=SoldierStatus.Ease1;
					frame = easeList1.get(1);
				}else if(n==1){
					status=SoldierStatus.Ease2;
					frame = easeList1.get(1);
				}else {
					int direction = r.nextInt(8);
					frame = standingFrames.get(direction);
					switch(direction){
						case 0:curDirection = Direction.Up;break;
						case 1:curDirection = Direction.LeftUp;break;
						case 2:curDirection = Direction.Left;break;
						case 3:curDirection = Direction.LeftDown;break;
						case 4:curDirection = Direction.Down;break;
						case 5:curDirection = Direction.RightDown;break;
						case 6:curDirection = Direction.Right;break;
						case 7:curDirection = Direction.RightUp;break;
					}
					
				}
				
				standingTime = 0;
				easeIndex = 0;
			}else {
				
			}
			
			transToColorful(frame);//上阵营色
			
		}
		
	}
	
	/**
	 * 群体移动方法移动方法
	 * @param moveTarget
	 */
	@Override
	public void moveToTarget(LittleCenterPoint moveTarget,LinkedList<LittleCenterPoint> path) {
		
		//从静止状态开始移动的
		if(!isMoving()) {
			
			this.nextTarget = path.get(0);
			path.remove(0);//出队
			this.movePath = path;
			this.endTarget = moveTarget;
			status=SoldierStatus.UMove;
			if(nextTarget!=null) {
				moveOneStep();
			}else {
				System.out.println("需要有别的措施继续寻路,比如让路算法");
				this.endTarget = null;
			}
			
			
//			SoldierXunLuBean xlb = new SoldierXunLuBean();
//			List<LittleCenterPoint> planMovePath = xlb.xunlu(curLittleCenterPoint, moveTarget);
//			if(planMovePath!=null && planMovePath.size()>1) {
//				this.nextTarget = planMovePath.get(0);
//				this.endTarget = planMovePath.get(planMovePath.size()-1);
//				this.movePath = planMovePath;
//				status=SoldierStatus.UMove;
//				
//			}else {
//				
//				System.out.println("指定位置不可达");
//			}
			
			
		}
		//目标正在移动,然后指定新的目标位置
		else {
			stopFlag = true;
			while(true) {
				if(curLittleCenterPoint.equals(nextTarget)) {
					break;
				}else {
					try {
						Thread.sleep(0);
					}catch (Exception e) {
						e.printStackTrace();
					}
					
				}
			}
			
			curLittleCenterPoint.preBooked.set(false);
			Map<String,Object> result = findNextTarget(moveTarget,haveGetPoint);
			this.nextTarget = (LittleCenterPoint)result.get("cp");
			resetTarget = true;
			this.endTarget = moveTarget;
			
			
			//直接从当前位置寻路就可以了
//			SoldierXunLuBean xlb = new SoldierXunLuBean();
//			List<LittleCenterPoint> path = xlb.xunlu(curLittleCenterPoint, moveTarget);
//			if(path!=null) {
//				this.nextTarget = path.get(0);
//				this.movePath = path;
//				this.endTarget = path.get(path.size()-1);
//				resetTarget = true;
//				
//				
//			}else {
//				System.out.println("重指定位置不可达");
//			}
			
			stopFlag = false;
			
		}
	
	}
	
	/**
	 * 走一步的方法
	 * 
	 * 对于会游泳的步兵  需要重写这个方法  以设置正确的帧图
	 */
	@Override
	public void moveOneStep() {
		
		setTargetDirection(nextTarget);
		if(curDirection!=targetDirection) {
			curDirection=targetDirection;
			umoveIndex = 0;
		}
		
		/**
		 * 确定使用的帧图
		 * 部分步兵会游泳  这里需要重写moveOneStep方法 以获取合适的帧图
		 */
		List<ShapeUnitFrame> moveFrame = directionMap.get(curDirection);
		umoveIndex+=1;
		ShapeUnitFrame frame = moveFrame.get( (umoveIndex/3)%moveFrame.size());
		transToColorful(frame);//上阵营色
		
		/*
		 * 修改位移
		 * 需要注意：当前位置可能不是中心点  所以需要比较目的地的Position坐标
		 */
		int nextTargetX = nextTarget.getX();
		int nextTargetY = nextTarget.getY();
		int nextPositionX = nextTargetX-centerOffX;
		int nextPositionY = nextTargetY-centerOffY;
		
		if(positionX!=nextPositionX) {
			if(Math.abs(positionX-nextPositionX)<2) {//修正补齐
				positionX = nextPositionX;
			}else if(positionX<nextPositionX) {
				positionX+=2;
			}else if(positionX>nextPositionX) {
				positionX-=2;
			}
		}
		
		if(positionY!=nextPositionY) {
			if(Math.abs(positionY-nextPositionY)<1) {//修正补齐
				positionY = nextPositionY;
			}else if(positionY<nextPositionY) {
				positionY+=1;
			}else if(positionY>nextPositionY) {
				positionY-=1;
			}
		}
		
		//更新格子状态
		CenterPoint newCenterPoint = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		LittleCenterPoint newLCP = PointUtil.getMinDisLCP(positionX+centerOffX, positionY+centerOffY, newCenterPoint);
		
		if(newLCP.equals(curLittleCenterPoint)) {
			
		}else if(isArrivedNextTarget()){
			curCenterPoint.removeUnit(this);
			curLittleCenterPoint.preBooked.compareAndSet(true, false);
			lastLittleCenterPoint = curLittleCenterPoint;
			
			curCenterPoint = newCenterPoint;
			curLittleCenterPoint = newLCP;
			curCenterPoint.addSoldier(this);
			curLittleCenterPoint.soldier = this;//待优化
			if(curCenterPoint.isInShadow()) {
				this.setHided(true);
			}else {
				this.setHided(false);
			}
		}
	}
	
	
	public LittleCenterPoint lastLittleCenterPoint = null;
	
	/**
	 * 寻找下一个运动点
	 */
	public Map<String,Object> findNextTarget(LittleCenterPoint moveTarget ,List<LittleCenterPoint>haveGetPoint) {
		return LittleCenterPointUtil.getMinDisOfLcpNeighbor(curLittleCenterPoint, moveTarget, lastLittleCenterPoint,haveGetPoint);
	}
	
}

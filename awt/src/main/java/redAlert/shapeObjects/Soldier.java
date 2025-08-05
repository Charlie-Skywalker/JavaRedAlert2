package redAlert.shapeObjects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.enums.Direction;
import redAlert.enums.UnitColor;
import redAlert.other.SoldierBloodBar;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.LittleCenterPoint;
import redAlert.utilBean.SoldierXunLuAdapter;
import redAlert.utilBean.SoldierXunLuBean;
import redAlert.utils.CanvasPainter;
import redAlert.utils.PointUtil;

/**
 * 士兵的超类
 */
public abstract class Soldier extends MovableUnit{
	
	/**
	 * 人物状态
	 */
	public enum SoldierStatus{
		Standing("立正"),Ease1("稍息动作1"),Ease2("稍息动作2"),
		LMove("向左移动"),UMove("向上移动");
		
		private final String cnDesc;
		
		SoldierStatus(String cnDesc){
			this.cnDesc = cnDesc;
		}
	}
	
	/**
	 * 最大血量  步兵固定1000
	 */
	public final static int maxHp = 1000;
	/**
	 * 最大血块个数  士兵固定16  但表示16个明暗条纹 知乎上称这是8格血
	 */
	public final static int maxBloodNum = 16;
	/**
	 * 当前血量
	 */
	public int curHp = 1000;
	/**
	 * 目标朝向
	 */
	public Direction targetDirection;
	/**
	 * 当前朝向
	 */
	public Direction curDirection;
	/**
	 * 所有帧
	 */
	public List<ShapeUnitFrame> allFrames = null;
	/**
	 * 站立动作 8个方向
	 */
	public List<ShapeUnitFrame> standingFrames = null;
	/**
	 * 向上移动的帧
	 */
	public List<ShapeUnitFrame> upMoveFrames = null;
	/**
	 * 向左上移动的帧
	 */
	public List<ShapeUnitFrame> leftUpMoveFrames = null;
	/**
	 * 向左移动的帧
	 */
	public List<ShapeUnitFrame> leftMoveFrames = null;
	/**
	 * 向左下移动的帧
	 */
	public List<ShapeUnitFrame> leftDownMoveFrames = null;
	/**
	 * 向下移动的帧
	 */
	public List<ShapeUnitFrame> downMoveFrames = null;
	/**
	 * 向右下移动的帧
	 */
	public List<ShapeUnitFrame> rightDownMoveFrames = null;
	/**
	 * 向右移动的帧
	 */
	public List<ShapeUnitFrame> rightMoveFrames = null;
	/**
	 * 向右上移动的帧
	 */
	public List<ShapeUnitFrame> rightUpMoveFrames = null;
	/**
	 * 兵种有两套稍息动作
	 */
	public List<ShapeUnitFrame> easeList1 = null;
	public List<ShapeUnitFrame> easeList2 = null;
	
	
	/**
	 * 移动帧图下标
	 */
	public int umoveIndex = 0;
	/**
	 * 下一个目标位
	 */
	public LittleCenterPoint nextTarget;
	/**
	 * 移动终点
	 */
	public LittleCenterPoint endTarget;
	/**
	 * 当前所在小中心点
	 * 当前所在中心点必须是移动路径上的点
	 */
	public LittleCenterPoint curLittleCenterPoint;
	/**
	 * 移动路径  静止时为null
	 */
	public List<LittleCenterPoint> movePath;
	/**
	 * 重定位
	 */
	public boolean resetTarget = false;
	/**
	 * 寻路锁,避免AWT线程和规划线程同时进行寻路
	 */
	public ReentrantLock xunluLock = new ReentrantLock(true);
	/**
	 * 方向和帧的对应关系
	 */
	public Map<Direction,List<ShapeUnitFrame>> directionMap = new HashMap<>();
	/**
	 * 步兵状态默认是站立
	 */
	public SoldierStatus status = SoldierStatus.Standing;
	/**
	 * 稍息下标
	 */
	public int easeIndex = 0;
	/**
	 * 站立时间
	 */
	public int standingTime = 0;//保持站立需要一定时间后才能稍息
	
	/**
	 * 构造方法
	 * @param shpPrefix
	 * @param color
	 */
	public Soldier(LittleCenterPoint lcp,String shpPrefix,UnitColor color) {
		super.unitColor = color;
		allFrames = ShpResourceCenter.loadShpResource(shpPrefix, SceneType.TEM.getPalPrefix());
		standingFrames = allFrames.subList(0, 8);
		upMoveFrames = allFrames.subList(8, 14);//向上运动
		leftUpMoveFrames = allFrames.subList(14, 20);//左上运动
		leftMoveFrames = allFrames.subList(20, 26);//左运动
		leftDownMoveFrames = allFrames.subList(26, 32);//左下运动
		downMoveFrames = allFrames.subList(32, 38);//下运动
		rightDownMoveFrames = allFrames.subList(38, 44);//右下运动
		rightMoveFrames = allFrames.subList(44, 50);//右运动
		rightUpMoveFrames = allFrames.subList(50, 56);//右上运动
		easeList1 = allFrames.subList(56, 72);//稍息动作一
		easeList2 = allFrames.subList(72, 87);//稍息动作二
		directionMap.put(Direction.Up, upMoveFrames);
		directionMap.put(Direction.LeftUp, leftUpMoveFrames);
		directionMap.put(Direction.Left, leftMoveFrames);
		directionMap.put(Direction.LeftDown, leftDownMoveFrames);
		directionMap.put(Direction.Down, downMoveFrames);
		directionMap.put(Direction.RightDown, rightDownMoveFrames);
		directionMap.put(Direction.Right, rightMoveFrames);
		directionMap.put(Direction.RightUp, rightUpMoveFrames);
		curDirection = Direction.Down;//默认步兵朝向下方
		curFrame = calculateFirstFrame();
		curLittleCenterPoint = lcp;
		curCenterPoint = PointUtil.getCenterPoint(lcp.getX(), lcp.getY());
		curCenterPoint.addSoldier(this);
		curLittleCenterPoint.soldier = this;
		if(curCenterPoint.isInShadow()) {
			this.isHided();
		}
		//步兵的血条
		SoldierBloodBar bar = new SoldierBloodBar(this);
		super.bloodBar = bar;
		Constructor.putOneShapeUnit(bar);
	}
	
	/**
	 * 由于新建建筑是直接扔进缓存队列的,所以需要计算好第一帧的颜色
	 * 计算第一帧
	 */
	public ShapeUnitFrame calculateFirstFrame() {
		ShapeUnitFrame curFrame = allFrames.get(0);
		
		ShapeUnitFrame newFrame = curFrame.copy();
		BufferedImage image = newFrame.getImg();
		giveFrameUnitColor(image,newFrame);//上阵营色
		return newFrame;
	}
	
	
	/**
	 * 指令控制步兵移动方法
	 */
	@Override
	public void moveToTarget(CenterPoint moveTarget) {
		//校验这个位置是否符合条件
		if(moveTarget==null) {return;}
		//不可达地点
		if(!moveTarget.isSoldierCanOn()) {
			
			movePath=null;
			endTarget = null;
			return;
		}
		LittleCenterPoint targetLCP = PointUtil.getMinDisLCP(curLittleCenterPoint.getX(), curLittleCenterPoint.getY(), moveTarget);
		
		moveToTarget(targetLCP);
	}
	
	
	/**
	 * 移动方法
	 * @param moveTarget
	 */
	public abstract void moveToTarget(LittleCenterPoint moveTarget,LinkedList<LittleCenterPoint> path);
	/**
	 * 移动方法
	 * @param moveTarget
	 */
	public void moveToTarget(LittleCenterPoint moveTarget) {
		
		//从静止状态开始移动的
		if(!isMoving()) {
			SoldierXunLuAdapter xlb = SoldierXunLuAdapter.getInstance();
			List<LittleCenterPoint> planMovePath = xlb.xunlu(curLittleCenterPoint, moveTarget);
			
			if(planMovePath!=null && planMovePath.size()>1) {
				this.nextTarget = planMovePath.get(0);
				this.endTarget = planMovePath.get(planMovePath.size()-1);
				this.movePath = planMovePath;
				status=SoldierStatus.UMove;
				
			}else {
				
				System.out.println("指定位置不可达");
			}
			
			
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
			
			//直接从当前位置寻路就可以了
			SoldierXunLuAdapter xlb = SoldierXunLuAdapter.getInstance();
			List<LittleCenterPoint> path = xlb.xunlu(curLittleCenterPoint, moveTarget);
			if(path!=null) {
				this.nextTarget = path.get(0);
				this.movePath = path;
				this.endTarget = path.get(path.size()-1);
				resetTarget = true;
				
				
			}else {
				System.out.println("重指定位置不可达");
			}
			
			stopFlag = false;
			
		}
	
		
	}
	
	/**
	 * 是否正在移动
	 * 这个方法计划为以后移动遮挡和移动避让做准备
	 */
	public boolean isMoving() {
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
	 * 设置目标方向
	 */
	public void setTargetDirection(LittleCenterPoint nextTarget) {
		int startX = positionX+centerOffX;
		int startY = positionY+centerOffY;
		int targetX = nextTarget.getX();
		int targetY = nextTarget.getY();
		
		//确定使用车身角度
		if( startX-targetX<0 && startY-targetY==0 ) {//右运动
			targetDirection = Direction.Right;
		}
		else if( startX-targetX<0 && startY-targetY<0 ) {//右下运动
			targetDirection = Direction.RightDown;
		}
		else if( startX-targetX==0 && startY-targetY<0 ) {//下运动
			targetDirection = Direction.Down;
		}
		else if( startX-targetX>0 && startY-targetY<0 ) {//左下运动
			targetDirection = Direction.LeftDown;
		}
		else if( startX-targetX>0 && startY-targetY==0 ) {//左运动
			targetDirection = Direction.Left;
		}
		else if( startX-targetX>0 && startY-targetY>0 ) {//左上运动
			targetDirection = Direction.LeftUp;
		}
		else if( startX-targetX==0 && startY-targetY>0 ) {//上运动
			targetDirection = Direction.Up;
		}
		else if( startX-targetX<0 &&  startY-targetY>0 ) {//右上运动
			targetDirection = Direction.RightUp;
		}else {
			targetDirection = curDirection;
		}
		
	}
	
	/**
	 * 走一步的方法
	 *
	 * 对于会游泳的步兵  需要重写这个方法  以设置正确的帧图
	 */
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
		ShapeUnitFrame frame = moveFrame.get( (umoveIndex/3)   %moveFrame.size());
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
		
		}else {
			if(!newCenterPoint.isSoldierCanOn()) {
			
			}if(!movePath.contains(newLCP)) {
			
			}else {
				curCenterPoint.removeUnit(this);
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
	}
	

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
				}else {
					//是否有临时停止标志  有的话  需要暂停计算  等待寻路完成  帧停顿
					if(stopFlag) {
						return;
					}
					
					//若发生重定位,说明movePath变量发生了变化,获取nextTarget的方式要改变
					if(resetTarget) {
						nextTarget = movePath.get(1);
						resetTarget = false;
						moveOneStep();
					}else {//根据当前位置  确认下一个位置
						int curIndex = movePath.indexOf(nextTarget);
						nextTarget = movePath.get(curIndex+1);
						
						//确认这个位置是否可达
						if(nextTarget.isSoldierCanOn()) {
							moveOneStep();
						}else {
							//实现重新规划线路
							xunluLock.lock();
							try {
								SoldierXunLuBean xlb = new SoldierXunLuBean();
								List<LittleCenterPoint> path = xlb.xunlu(curLittleCenterPoint, endTarget);
								if(path!=null) {
									this.movePath = path;
									nextTarget = movePath.get(1);
									moveOneStep();
								}else {
									nextTarget = null;
									endTarget = null;
									movePath = null;
								}
							}catch (Exception e) {
								System.out.println("程序自动寻路异常");
								e.printStackTrace();
							}finally {
								xunluLock.unlock();
							}
						}
						
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
					frame = easeList2.get(1);
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

	public int getMaxHp() {
		return maxHp;
	}

	public int getCurHp() {
		return curHp;
	}

	public void setCurHp(int curHp) {
		this.curHp = curHp;
	}

	public Direction getTargetDirection() {
		return targetDirection;
	}

	public void setTargetDirection(Direction targetDirection) {
		this.targetDirection = targetDirection;
	}

	public Direction getCurDirection() {
		return curDirection;
	}

	public void setCurDirection(Direction curDirection) {
		this.curDirection = curDirection;
	}

	public LittleCenterPoint getCurLittleCenterPoint() {
		return curLittleCenterPoint;
	}

	public void setCurLittleCenterPoint(LittleCenterPoint curLittleCenterPoint) {
		this.curLittleCenterPoint = curLittleCenterPoint;
	}
	
	/**
	 * 原始帧图换成有颜色的帧图
	 */
	public void transToColorful(ShapeUnitFrame frame) {
		BufferedImage newImg = curFrame.getImg();
		CanvasPainter.clearImage(newImg);
		Graphics g2d = newImg.createGraphics();
		BufferedImage oriImage = frame.getImg();
		g2d.drawImage(oriImage, 0, 0, null);
		giveFrameUnitColor(newImg,frame);//上阵营色
	}
}

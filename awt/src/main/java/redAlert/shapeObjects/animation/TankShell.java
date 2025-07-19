package redAlert.shapeObjects.animation;

import java.util.ArrayList;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.other.OneDamage;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;

/**
 * 灰熊坦克和犀牛坦克的炮弹
 *
 * 炮弹动画分为两部分
 * 第1部分：炮弹的飞行动画
 * 第2部分：炮弹的爆炸动画  爆炸后的第一帧进行伤害结算
 *
 */
public class TankShell extends ShapeUnit{

	/**
	 * 炮弹的目的地
	 */
	public int targetPositionX,targetPositionY;
	/**
	 * 线性插值获取的移动坐标集合
	 */
	public List<int []> flyPath = null;
	/**
	 * 炮弹飞行下标
	 */
	public int flyIndex = 0;
	/**
	 * 炮弹炮炸下标
	 */
	public int expIndex = 0;
	/**
	 * 爆炸帧动画
	 */
	public List <ShapeUnitFrame> expShps = null;
	/**
	 * 炮弹帧动画
	 */
	public ShapeUnitFrame shell = null;
	
	/**
	 * 炮弹中心偏移
	 */
	public int shellCenterOffX = 11,shellCenterOffY = 11;
	/**
	 * 爆炸中心偏移
	 */
	public int expCenterOffX = 109,expCenterOffY = 108;
	/**
	 * 爆炸中心坐标
	 */
	public int expCenterX,expCenterY;
	/**
	 * 炮弹飞行速度
	 * 指单位帧飞行的距离
	 */
	public int shellSpeed = 20;
	/**
	 * 发射炮弹的载具
	 */
	public Vehicle vehicle;
	/**
	 * 炮弹的伤害
	 */
	public int damageValue = 100;
	/**
	 * 目标建筑
	 */
	public Building targetBuilding;
	
	
	/**
	 * 炮弹状态枚举
	 */
	public enum ShellStatus{
		
		Flying("飞行中"),Exploding("爆炸中"),End("炸完了");
		
		public String desc;
		
		private ShellStatus(String desc) {
			this.desc = desc;
		}
	}
	/**
	 * 炮弹状态
	 */
	public ShellStatus status = ShellStatus.Flying;
	
	/**
	 * 伤害是否已结算
	 */
	public boolean isDamageSettled = false;//伤害是否已结算
	/**
	 * 初始化炮弹时应该使用炮弹的中心坐标，这样炮弹最后的中心坐标就是爆炸坐标
	 * 炮弹的飞行目的地也是炮弹的中心坐标的着点
	 */
	public TankShell(int shellCenterX,int shellCenterY,Vehicle vehicle,Building targetBuilding) {
		this.vehicle = vehicle;
		this.targetBuilding = targetBuilding;
		
		CenterPoint targetCp = targetBuilding.getCurCenterPoint();
		int targetCenterX = targetCp.getX();
		int targetCenterY = targetCp.getY();
		
		super.positionX = shellCenterX-shellCenterOffX;
		super.positionY = shellCenterY-shellCenterOffY;
		this.targetPositionX = targetCenterX-shellCenterOffX;
		this.targetPositionY = targetCenterY-shellCenterOffY;
		this.expCenterX = targetCenterX;
		this.expCenterY = targetCenterY;
		
		super.setPriority(70);
		super.setFrameSpeed(4);
		
		this.shell = ShpResourceCenter.loadShpResource("120mm", SceneType.TEM.getPalPrefix(), false).get(0);
		expShps = ShpResourceCenter.loadShpResource("brrlexp1", SceneType.ANIM.getPalPrefix(), false);
		
		super.curFrame = shell;
		
		
		flyPath = test1(positionX,positionY,targetPositionX,targetPositionY);
		
		super.positionX = flyPath.get(flyIndex)[0];
		super.positionY = flyPath.get(flyIndex)[1];
	}
	
	/**
	 * 帧动画计算
	 */
	@Override
	public void calculateNextFrame() {
		
		//根据状态来进行帧图计算
		if(status==ShellStatus.End) {
			return;
		}
		
		if(status==ShellStatus.Flying) {
			flyIndex++;
			if(flyIndex>flyPath.size()-1) {
				status = ShellStatus.Exploding;
				super.positionX = expCenterX-expCenterOffX;
				super.positionY = expCenterY-expCenterOffY;
				super.curFrame = expShps.get(expIndex);
				
				/*
				 * 可以考虑在这个位置进行伤害结算
				 */
				OneDamage damage = new OneDamage(vehicle,this,targetBuilding,"type0",damageValue);
				
				ShapeUnitResourceCenter.damageBlockingQueue.offer(damage);
				
			}else {
				super.positionX = flyPath.get(flyIndex)[0];
				super.positionY = flyPath.get(flyIndex)[1];
			}
			
			return;
			
		}
		
		if(status==ShellStatus.Exploding) {
			expIndex++;
			
			if(expIndex>expShps.size()-1) {
				expIndex = 0;
				flyIndex = 0;
				this.setVisible(false);
				this.setEnd(true);//炮弹轻易被回收  恐怕不太好
				
				
			}else {
				super.curFrame = expShps.get(expIndex);
			}
		}
		
		
		

		
		
	
		
		
		
		
		
	}
	
	
	/**
	 * 将出发地和目的地之间做做一个线性插值
	 */
	public List<int []> test1(int positionX,int positionY,int targetX,int targetY) {
	
//		kx*kx+ky*ky = 20*20;
//
//		ky/kx = k;
//		ky = k*kx;
//
//		kx*kx+k*k*kx*kx = 20*20;
//		kx*kx = 20*20/(1+k*k);
//		kx = Math.sqrt(20*20/(1+k*k));
//		ky = k*kx;

//		int speed = 20;
		int speed = shellSpeed;
		
		int deltaX = targetX-positionX;
		int deltaY = targetY-positionY;
		
		
		int kx = 0;//X方向的速度
		int ky = 0;//Y方向的速度
		
		//获取一组插值
		List<int []> list = new ArrayList<>();
		int [] a0 = new int [] {positionX,positionY};
		list.add(a0);
		
		if(deltaX==0) {
			kx = 0;
			ky = speed;
			
			double dky = speed;
			if(deltaY<0) {
				dky = -dky;
			}
			
			double nextX = positionX;
			double nextY = positionY;
			
			while(true) {
				double s = Math.abs(targetY-nextY);
				
				if(s<=speed) {
					int [] a = new int [] {targetX,targetY};
					list.add(a);
					break;
				}else {
					nextY = nextY+dky;
					int [] a = new int [] {positionX,(int)nextY};
					list.add(a);
				}
			}
			
		}else if(deltaY==0) {
			kx = speed;
			ky = 0;
			
			double dkx = speed;
			if(deltaX<0) {
				dkx = -dkx;
			}
			
			double nextX = positionX;
			double nextY = positionY;
			
			while(true) {
				double s = Math.abs(targetX-nextX);
				
				if(s<=speed) {
					int [] a = new int [] {targetX,targetY};
					list.add(a);
					break;
				}else {
					nextX = nextX+dkx;
					int [] a = new int [] {(int)nextX ,positionY};
					list.add(a);
				}
			}
			
		}else {
		
			double k = (double)deltaY/(double)deltaX;
			
			double dkx = Math.sqrt(speed*speed/(1+k*k));//X方向的速度的绝对值
			double dky = Math.abs(k*dkx);//Y方向的速度的绝对值
			
			//确定移动方向
			if(deltaX<0) {
				dkx = -dkx;
			}
			if(deltaY<0) {
				dky = -dky;
			}
			
			double nextX = positionX;
			double nextY = positionY;
			
			while(true) {
				
				double s = Math.sqrt(  (targetX-nextX)*(targetX-nextX) + (targetY-nextY)*(targetY-nextY) );
				
				if(s<=speed) {
					int [] a = new int [] {targetX,targetY};
					list.add(a);
					break;
				}else {
					nextX = nextX+dkx;
					nextY = nextY+dky;
					int [] a = new int [] {(int)nextX,(int)nextY};
					list.add(a);
				}
			}
		}
		
		return list;
	}

	public boolean isDamageSettled() {
		return isDamageSettled;
	}

	public void setDamageSettled(boolean isDamageSettled) {
		this.isDamageSettled = isDamageSettled;
	}
	
//	public static void main(String[] args) {
//		List<int []> list = test1(200,200,500,500);
//
//		for(int [] a:list) {
//			System.out.println(a[0]+","+a[1]);
//		}
//
//	}


}

package redAlert.shapeObjects.vehicle;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.CanvasPainter;

/**
 * 灰熊坦克
 * 
 * 图片资源文件是128*128像素  中心点为64,64
 */
public class GrizTank extends Vehicle {
	
	public GrizTank() {
		
	}
	
	/**
	 * 坦克的curFrame对象不直接用帧图
	 * 而是利用车身、炮塔结合绘制出来的图片,因为车身和炮塔应该有一样的绘制优先级
	 * 
	 */
	public GrizTank(int positionX,int positionY,UnitColor unitColor) {
		super.initVehicleParam(positionX,positionY, unitColor, "gtnk");
		//定义名称
		super.unitName = "灰熊坦克";
		//移动时单位语音
		super.moveSounds = new String[] {"vgramoa","vgramob","vgramoc","vgramod","vgramoe","vgramof"};
		//选中时单位语音
		super.selectSounds = new String []{"vgrasea","vgraseb","vgrasec","vgrased","vgrasee"};
		
		
		//灰熊坦克炮塔
		turret = new GrizTankTurret(this);
		
		//把车身和炮塔画一块
		BufferedImage image = curFrame.getImg();
		CanvasPainter.clearImage(image);
		Graphics2D g2d = image.createGraphics();
		g2d.drawImage(bodyFrames.get(curTurn).getImg(), 0, 0, null);
		g2d.drawImage(turret.getFrames().get(turret.getCurTurn()).getImg(),0,0,null);
		g2d.dispose();
	}

	/**
	 * 先调用父类的方法实现车身的移动和绘制
	 * 然后需要绘制炮塔
	 */
	@Override
	public void calculateNextFrame() {
		super.calculateNextFrame();//只画了车身,需要画炮塔
		
		/**
		 * 炮塔朝向逻辑:
		 * 开火状态时,炮塔朝向目标
		 * 非开火状态且在移动时,炮塔朝向终点  
		 * 非开火状态且静止时,炮塔与车身朝向一致
		 * 
		 */
		if(turret.getAttackBuilding()!=null) {//开火状态
			Building building = turret.getAttackBuilding();
			if(building!=null) {
				CenterPoint attackTarget = building.getCurCenterPoint();
				turret.calAndSetTargetTurn(this, attackTarget);
				turret.turn();
			}
		}else if(endTarget==null) {//静止状态
			turret.setTargetTurn(curTurn);
			turret.turn();
		}else if(endTarget!=null) {//运动状态
			turret.calAndSetTargetTurn(this, endTarget);
			turret.turn();
		}
		
		BufferedImage image = curFrame.getImg();
		Graphics2D g2d = image.createGraphics();
		CanvasPainter.clearImage(image);
		g2d.drawImage(bodyFrames.get(curTurn).getImg(), 0, 0, null);
		g2d.drawImage(turret.getFrames().get(turret.getCurTurn()).getImg(),0,0,null);
		g2d.dispose();
		super.positionMinX = bodyFrames.get(curTurn).getMinX()+positionX;
		super.positionMinY = bodyFrames.get(curTurn).getMinY()+positionY;
	}
	
}

package redAlert.shapeObjects.soldier;

import java.util.LinkedList;
import java.util.Random;

import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Soldier;
import redAlert.utilBean.LittleCenterPoint;

public class Adog extends Soldier{
	
	public Adog(LittleCenterPoint lcp,UnitColor color) {
		
		super(lcp,"adog",color);
		super.centerOffX = 36;
		super.centerOffY = 35;
		super.positionX = lcp.x - centerOffX;
		super.positionY =  lcp.y - centerOffY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "警犬";
		
		//移动时说的话
		super.moveSounds = new String[] {"idogmova"};
		//选中时说的话
		super.selectSounds = new String []{"idogsela"};
	}

	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	public void calculateNextFrame() {
		
		super.calculateNextFrame();
		
	}

	@Override
	public void moveToTarget(LittleCenterPoint moveTarget, LinkedList<LittleCenterPoint> path) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

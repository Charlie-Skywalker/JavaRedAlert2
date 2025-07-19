package redAlert.shapeObjects.soldier;

import java.util.LinkedList;

import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Soldier;
import redAlert.utilBean.LittleCenterPoint;

/**
 * 表示狙击手
 */
public class Sniper extends Soldier{
	
	public Sniper(LittleCenterPoint lcp,UnitColor color) {
		
		super(lcp,"snipe",color);
		super.centerOffX = 56;
		super.centerOffY = 40;
		super.positionX = lcp.x - centerOffX;
		super.positionY =  lcp.y - centerOffY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		super.unitName = "狙击手";
		//移动时说的话
		super.moveSounds = new String[] {"isnimoa","isnimob","isnimoc","isnimod","isnimoe"};
		//选中时说的话
		super.selectSounds = new String []{"isnisea","isniseb","isnisec","isnised"};
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

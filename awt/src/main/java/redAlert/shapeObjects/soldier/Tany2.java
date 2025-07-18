package redAlert.shapeObjects.soldier;

import java.util.LinkedList;

import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Soldier;
import redAlert.utilBean.LittleCenterPoint;

public class Tany2 extends Soldier{
	
	public Tany2(LittleCenterPoint lcp,UnitColor color) {
		
		super(lcp,"tany",color);
		super.centerOffX = 62;
		super.centerOffY = 57;
		super.positionX = lcp.x - centerOffX;
		super.positionY =  lcp.y - centerOffY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		super.unitName = "谭雅";
		//移动时说的话
		super.moveSounds = new String[] {"itanmoa","itanmob","itanmoc","itanmod","itanmoe","itanmof"};
		//选中时说的话
		super.selectSounds = new String []{"itansea","itanseb","itansec","itansed","itansee"};
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

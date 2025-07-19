package redAlert.shapeObjects.soldier;

import java.util.LinkedList;

import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Soldier;
import redAlert.utilBean.LittleCenterPoint;

/**
 * 美国大兵
 */
public class Gi extends Soldier{
	
	public Gi(LittleCenterPoint lcp,UnitColor color) {
		
		super(lcp,"gi",color);
		super.centerOffX = 37;
		super.centerOffY = 35;
		super.positionX = lcp.x - centerOffX;
		super.positionY =  lcp.y - centerOffY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义名称
		super.unitName = "美国大兵";
		
		//移动时说的话
		super.moveSounds = new String[] {"igimoa","igimob","igimoc","igimod","igimoe","igimof"};
		//选中时说的话
		super.selectSounds = new String []{"igisea","igiseb","igisec","igised","igisee","igisef"};
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

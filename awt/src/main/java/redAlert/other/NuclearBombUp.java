package redAlert.other;

import java.util.List;
import java.util.Random;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.militaryBuildings.SfMisl;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.shapeObjects.ShapeUnit;

/**
 * 向上飞的核弹体
 */
public class NuclearBombUp extends ShapeUnit{
	
	int flag = 0;
	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	@Override
	public void calculateNextFrame() {
		this.positionY-=5;
		this.positionMinY = positionY+4;
		
		//需要在屁股后边用火
		if(positionY>-800 && flag%5==0) {
			Nukepuff nuclearBomb = new Nukepuff(positionX+1,positionY+130);
			Constructor.putOneShapeUnit(nuclearBomb);
		}
		if(positionY<-800) {
			end = true;
		}
		
		flag++;
		
	}
	
	/**
	 * 根据个核弹井的位置来构造
	 * 安放弹体、需要传入核弹井对象,以此来找到安放位置
	 * 偏移核弹井左上角向右86个像素
	 */
	public NuclearBombUp(SfMisl sfMisl) {
		super.priority = 51;//要比普通的建筑优先级低
		super.unitColor = sfMisl.getUnitColor();
		super.curFrame = initResource();
		this.positionX = sfMisl.getPositionX()+86;
		this.positionY = sfMisl.getPositionY();
		this.positionMinX = positionX+5;
		this.positionMinY = positionY+4;
		this.unitColor = sfMisl.getUnitColor();
		
		giveFrameUnitColor(curFrame.getImg(),curFrame);//上阵营色
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "nkmslup";
		
		super.frameSpeed = 4;
	}
	
	/**
	 * 加载弹体图片
	 */
	public ShapeUnitFrame initResource() {
		try {
			if(curFrame==null) {
//				List<ShapeUnitFrame> list1 = ShpFileReader.convertShpFileToBuildingFrames(ShpResourceCenter.getShpByName("nkmslup"), "nkmslup",null,true,unitColor);
				List<ShapeUnitFrame> list1 = ShpResourceCenter.loadShpResource("nkmslup", SceneType.TEM.getPalPrefix());
				
				return list1.get(0);
			}else {
				return curFrame;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

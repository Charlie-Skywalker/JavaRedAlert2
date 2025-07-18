package redAlert.other;

import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.shapeObjects.ShapeUnit;

/**
 * 向下落的核弹体
 */
public class NuclearBombDown extends ShapeUnit {
	
	/**
	 * 掉落目标区域X坐标
	 */
	public int targetX = 0;
	/**
	 * 掉落目标区域Y坐标
	 */
	public int targetY = 0;
	
	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	@Override
	public void calculateNextFrame() {
//		this.positionY+=5;
//		this.positionMinY = positionY+4;
	}
	
	
	/**
	 * 根据个核弹井的位置来构造
	 * 安放弹体、需要传入核弹井对象,以此来找到安放位置
	 * 偏移核弹井左上角向右86个像素
	 */
	public NuclearBombDown(int targetX,int targetY,UnitColor color) {
		super.unitColor = color;
		super.priority = 51;//要比普通的建筑优先级低
		super.curFrame = initResource();
		this.positionX = targetX-34;
		this.positionY = -300;
		this.positionMinX = positionX+5;
		this.positionMinY = positionY+4;
		this.targetX = targetX;
		this.targetY = targetY;
		this.unitColor = color;
		giveFrameUnitColor(curFrame.getImg(),curFrame);//上阵营色
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "nkmsldn";
		
		super.frameSpeed = 4;
	}
	
	/**
	 * 加载弹体图片
	 */
	public ShapeUnitFrame initResource() {
		try {
			if(curFrame==null) {
//				List<ShapeUnitFrame> list1 = ShpFileReader.convertShpFileToBuildingFrames(ShpResourceCenter.getShpByName("nkmsldn"), "nkmsldn",null,true,unitColor);
				List<ShapeUnitFrame> list1 = ShpResourceCenter.loadShpResource("nkmsldn",SceneType.TEM.getPalPrefix());
				return list1.get(0);
			}else {
				return curFrame;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	public int getTargetX() {
		return targetX;
	}


	public void setTargetX(int targetX) {
		this.targetX = targetX;
	}


	public int getTargetY() {
		return targetY;
	}


	public void setTargetY(int targetY) {
		this.targetY = targetY;
	}
	
	
}

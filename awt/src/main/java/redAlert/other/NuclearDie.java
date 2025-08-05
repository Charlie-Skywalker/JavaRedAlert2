package redAlert.other;

import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Building.SceneType;

/**
 * 小人因为核辐射融化的动画
 */
public class NuclearDie extends ShapeUnit{

	
	/**
	 * 动画目标区域X坐标
	 */
	public int targetX = 0;
	/**
	 * 动画目标区域Y坐标
	 */
	public int targetY = 0;
	
	/**
	 * 建筑建造动画帧集合
	 *   因为建造是一个单向连续的过程，不包括多个组成部分，所以是一个简单数组
	 */
	protected List<ShapeUnitFrame> aniFrames;
	
	protected int index;
	
	/**
	 * 当一帧绘完  building会被扔入BuildingDrawer的队列中调用此方法算下一帧画面
	 * 计算下一帧画面
	 */
	@Override
	public void calculateNextFrame() {
		index++;
		if(index>aniFrames.size()-1) {
			end = true;
		}else {
			curFrame = aniFrames.get(index);
		}
	}
	
	/**
	 * 根据个核弹井的位置来构造
	 * 安放弹体、需要传入核弹井对象,以此来找到安放位置
	 * 偏移核弹井左上角向右86个像素
	 */
	public NuclearDie(int targetX,int targetY) {
		super.priority = 51;//要比普通的建筑优先级低
		this.aniFrames = initResource();
		super.positionX = targetX;
		super.positionY = targetY;
		super.positionMinX = positionX+1;
		super.positionMinY = positionY+1;
		this.targetX = targetX;
		this.targetY = targetY;
		curFrame = aniFrames.get(index);
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "nukedie";
		
		super.frameSpeed = 4;
	}
	
	/**
	 * 加载辐射致死图片
	 */
	public List<ShapeUnitFrame> initResource() {
		try {
			if(aniFrames==null) {
//				return ShpFileReader.convertShpFileToBuildingFrames(ShpResourceCenter.getShpByName("nukedie"), "nukedie","anim",false,null);
				return ShpResourceCenter.loadShpResource("nukedie", SceneType.ANIM.getPalPrefix(), false);
				
			}else {
				return aniFrames;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

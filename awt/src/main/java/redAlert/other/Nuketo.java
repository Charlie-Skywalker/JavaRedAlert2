package redAlert.other;

import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Building.SceneType;

/**
 * 发射瞬间喷射火焰
 */
public class Nuketo extends ShapeUnit{

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
	public Nuketo(int positionX,int positionY) {
		super.priority = 51;//要比普通的建筑优先级低
		this.aniFrames = initResource();
		super.positionX = positionX;
		super.positionY = positionY;
		super.positionMinX = positionX+5;
		super.positionMinY = positionY+5;
		curFrame = aniFrames.get(index);
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "nuketo";
		
		super.frameSpeed = 4;
	}
	
	/**
	 * 加载喷射火焰图片
	 */
	public List<ShapeUnitFrame> initResource() {
		try {
			if(aniFrames==null) {
//				return ShpFileReader.convertShpFileToBuildingFrames(ShpResourceCenter.getShpByName("nuketo"), "nuketo","anim",false,null);
				return ShpResourceCenter.loadShpResource("nuketo", SceneType.ANIM.getPalPrefix(), false);
			}else {
				return aniFrames;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}

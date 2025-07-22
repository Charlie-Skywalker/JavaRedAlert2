package redAlert.shapeObjects.vehicle;

import java.util.List;
import java.util.Random;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.VxlFileReader;

/**
 * 基洛夫的影子
 */
public class ZepIn extends Vehicle{

	protected List<ShapeUnitFrame> allFrames;
	int index = 1;
	
	public boolean move = false;
	public int moveNum = 0;
	
	int step = 1;
	
	
	public ZepIn() {
		
	}
	
	public ZepIn(int positionX,int positionY,String scene,UnitColor color) {
		super.unitColor = color;
		super.priority = 48;
		try {
			this.allFrames = VxlFileReader.convertPngFileToBuildingFrames("E:/zep_in.png",8,1,color);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		super.curFrame = allFrames.get(index);
		super.positionX = positionX;
		super.positionY = positionY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "基洛夫空艇影子";
		
	}
	@Override
	public void calculateNextFrame() {
		
	}

	@Override
	public void moveToTarget(CenterPoint target) {
		// TODO Auto-generated method stub
		
	}
	
	
}

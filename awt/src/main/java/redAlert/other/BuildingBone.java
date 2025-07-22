package redAlert.other;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.apache.commons.lang3.math.NumberUtils;

import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.ShapeUnit;

/**
 * 点击建筑后显示的立体结构图（骨架）
 */
public class BuildingBone extends ShapeUnit{

	
	public BuildingBone(Building building) {
		super.setPriority(55);//骨架的优先级高于血条   高于三星标志  高于飞行兵   
		super.setVisible(false);
		
		
		super.curFrame = new ShapeUnitFrame();
		BufferedImage image = curFrame.getImg();
		if(image==null) {
			//计算这个图片的宽高
			int leftX = building.getLeftFirst().getX()-30;
			int leftY = building.getLeftFirst().getY();
			int rightX = building.getRightFirst().getX()+29;
			int rightY = building.getRightFirst().getY();
			int topY = building.getTopFirst().getY()-14;
			int topX = building.getTopFirst().getX();
			
			int y = NumberUtils.max(leftY,rightY);
			
			int height = y-topY+1+building.getHeight();
			int width = rightX - leftX +1;
			
			//左点
			int x1 = 0;
			int y1 = leftY-topY+2;
			
			//中间点
			int x2 = topX-leftX+1;
			int y2 = 0;
			
			//右点
			int x3 = rightX-leftX-1;
			int y3 = rightY-topY;
			
			
			
			image = new BufferedImage(width,  height, BufferedImage.TYPE_INT_ARGB);
			int fxNum = building.getConstConfig().fxNum;
			int fyNum = building.getConstConfig().fyNum;
			int fxLength = (fxNum-1)*10;
			int fyLength = (fyNum-1)*10;
			if(fxNum<3 && fxNum>=2) {
				fxLength = 16;
			}
			if(fxNum==1) {
				fxLength = 8;
			}
			if(fyNum<3 && fyNum>=2) {
				fyLength = 16;
			}
			if(fyNum==1) {
				fyLength = 8;
			}
			int vertHeight = building.getHeight()/4;
			
			Graphics2D g2d = image.createGraphics();
			g2d.setColor(Color.white);
			g2d.drawLine(x1, y1, x1, y1+vertHeight);
			g2d.drawLine(x1, y1, x1+fyLength, y1+fyLength/2);
			g2d.drawLine(x1, y1, x1+fxLength, y1-fxLength/2);
			
			g2d.drawLine(x2, y2, x2, y2+vertHeight);
			g2d.drawLine(x2, y2, x2-fxLength, y2+fxLength/2);
			g2d.drawLine(x2, y2, x2+fyLength, y2+fyLength/2);
			
			g2d.drawLine(x3, y3, x3, y3+vertHeight);
			g2d.drawLine(x3, y3, x3-fxLength, y3+fxLength/2);
			g2d.drawLine(x3, y3, x3-fyLength, y3-fyLength/2);
			
			g2d.dispose();
			
			curFrame.setImg(image);
			
			super.setPositionX(leftX-1);
			super.setPositionY(topY-building.getHeight()+12);
			
		}
		
	}
	
	@Override
	public void calculateNextFrame() {
		
		
	}
	
	
	public void repaintBone() {
		
		
		
	}

}

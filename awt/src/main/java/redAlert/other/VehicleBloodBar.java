package redAlert.other;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.Vehicle;
import redAlert.utils.CanvasPainter;

/**
 * 装甲单位的血量条
 * 
 * 血量1700
 * 血块个数34(17个明暗条纹)
 */
public class VehicleBloodBar extends MovableUnitBloodBar{

	public Vehicle vehicle;
	
	public VehicleBloodBar(Vehicle vehicle) {
		this.vehicle = vehicle;
		setMaxBloodNum(Vehicle.maxBloodNum);
		setMaxHp(Vehicle.maxHp);
		setCurHp(vehicle.getCurHp());
		super.curFrame = new ShapeUnitFrame();
		
		rePaintBloodBar();
	}
	
	
	/**
	 * 重绘血量条
	 */
	@Override
	public void rePaintBloodBar() {
		BufferedImage image = curFrame.getImg();
		if(image==null) {
			image = new BufferedImage(maxBloodNum + 2*borderWidth,  borderHeight, BufferedImage.TYPE_INT_ARGB);
		}
		CanvasPainter.clearImage(image);
		Graphics2D g2d = image.createGraphics();
		g2d.setColor(Color.white);
		g2d.drawRect(0, 0, maxBloodNum+2*borderWidth-1, borderHeight-1);//画边框
		
		int curBloodNum = calCurBloodNum()*2;
		int useColorMing = 0;
		int useColorAn = 0;
		if(curHp>850) {
			useColorMing = gColor.getRGB();
			useColorAn = Color.green.getRGB();
		}else if(curHp>425 && curHp<=850) {
			useColorMing = yColor.getRGB();
			useColorAn = Color.yellow.getRGB();
		}else if(curHp>=200 && curHp<=425) {
			useColorMing = rColor.getRGB();
			useColorAn = Color.red.getRGB();
		}else {
			useColorMing = rColor.getRGB();
			useColorAn = Color.red.getRGB();
			curBloodNum = 1;//只画一格红血
		}
		
		for(int i=0;i<curBloodNum;i++) {
			if(i%2!=0) {
				image.setRGB(i+1, 1, useColorMing);
				image.setRGB(i+1, 2, useColorMing);
			}else {
				image.setRGB(i+1, 1, useColorAn);
				image.setRGB(i+1, 2, useColorAn);
			}
		}
		curFrame.setImg(image);
	}

	/**
	 * 计算剩余血块个数
	 */
	@Override
	public int calCurBloodNum() {
		if(curHp>850) {
			curBloodNum = curHp/100;
		}else if(curHp>425 && curHp<=850) {
			curBloodNum = curHp/100;
		}else if(curHp>=200 && curHp<=425) {
			curBloodNum = curHp/100;
		}else {
			curBloodNum = curHp/100;
		}
		return curBloodNum;
	}
	
	/**
	 * 设定血条的长度和位置
	 */
	@Override
	public void calculateNextFrame() {
		super.setPositionX(vehicle.getPositionX()+46);
		super.setPositionY(vehicle.getPositionY()+32);
		super.curHp = vehicle.getCurHp();
		rePaintBloodBar();
	}

	
	
}

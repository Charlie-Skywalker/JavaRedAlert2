package redAlert.other;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.Soldier;
import redAlert.utils.CanvasPainter;

/**
 * 士兵单位的血量条
 * 
 * 血量1000
 * 血块个数16(8个明暗条纹)
 */
public class SoldierBloodBar extends MovableUnitBloodBar{

	public Soldier soldier;
	
	public SoldierBloodBar(Soldier soldier) {
		this.soldier = soldier;
		setMaxBloodNum(Soldier.maxBloodNum);
		setMaxHp(Soldier.maxHp);
		setCurHp(soldier.getCurHp());
		
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
		g2d.drawRect(0, 0, maxBloodNum+2*borderWidth-1, borderHeight-1);//画边框
		
		int curBloodNum = calCurBloodNum();
		if(curHp>500) {
			for(int i=0;i<curBloodNum*2;i++) {//绿血
				if(i%2!=0) {
					image.setRGB(i+1, 1, gColor.getRGB());
					image.setRGB(i+1, 2, gColor.getRGB());
				}else {
					image.setRGB(i+1, 1, Color.green.getRGB());
					image.setRGB(i+1, 2, Color.green.getRGB());
				}
			}
			curFrame.setImg(image);
		}else if(curHp>250 && curHp<=500) {
			for(int i=0;i<curBloodNum*2;i++) {//黄血
				if(i%2!=0) {
					image.setRGB(i+1, 1, yColor.getRGB());//这个黄色的味道和RA2对不上  先放下
					image.setRGB(i+1, 2, yColor.getRGB());
				}else {
					image.setRGB(i+1, 1, Color.yellow.getRGB());
					image.setRGB(i+1, 2, Color.yellow.getRGB());
				}
			}
			curFrame.setImg(image);
		}else if(curHp==250) {
			for(int i=0;i<curBloodNum*2;i++) {//红血
				if(i%2!=0) {
					image.setRGB(i+1, 1, rColor.getRGB());
					image.setRGB(i+1, 2, rColor.getRGB());
				}else {
					image.setRGB(i+1, 1, Color.red.getRGB());
					image.setRGB(i+1, 2, Color.red.getRGB());
				}
			}
			curFrame.setImg(image);
		}else {
			for(int i=0;i<curBloodNum;i++) {//半格红血
				if(i%2!=0) {
					image.setRGB(i+1, 1, rColor.getRGB());
					image.setRGB(i+1, 2, rColor.getRGB());
				}else {
					image.setRGB(i+1, 1, Color.red.getRGB());
					image.setRGB(i+1, 2, Color.red.getRGB());
				}
			}
			curFrame.setImg(image);
		}
		
	}
	
	
	/**
	 * 计算剩余血块个数
	 */
	@Override
	public int calCurBloodNum() {
		if(curHp>500) {
			if(curHp==1000) {
				curBloodNum = 8;
			}else if(curHp>=875 && curHp<1000) {
				curBloodNum = 7;
			}else if(curHp>=750 && curHp<875) {
				curBloodNum = 6;
			}else if(curHp>=625 && curHp<750) {
				curBloodNum = 5;
			}else if(curHp>500 && curHp<625) {
				curBloodNum = 4;
			}
		}else if(curHp>250 && curHp<=500) {
			if(curHp==500) {
				curBloodNum = 4;
			}else if(curHp>=375 && curHp<500) {
				curBloodNum = 3;
			}else if(curHp>250 && curHp<375) {
				curBloodNum = 2;
			}
		}else if(curHp<=250) {
			if(curHp==250) {
				curBloodNum = 2;
			}
			if(curHp<250) {
				curBloodNum = 1;
			}
		}
		return curBloodNum;
	}
	
	
	@Override
	public void calculateNextFrame() {
		int firstMinX = soldier.allFrames.get(0).getMinX();
		int firstMinY = soldier.allFrames.get(0).getMinY();
		super.setPositionX(soldier.getPositionX()+firstMinX);
		super.setPositionY(soldier.getPositionY()+firstMinY);
		
		super.curHp = soldier.getCurHp();
		rePaintBloodBar();
		
	}

}

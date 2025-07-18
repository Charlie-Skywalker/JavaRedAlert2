package redAlert.other;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.CanvasPainter;

/**
 * 建筑血条
 */
public class BuildingBloodBar extends BloodBar{

	/**
	 * 绘制优先级   优先于移动线条  优先于三级标志
	 * 
	 * 由于不同建筑物  血条数量都不一样
	 */
	public BuildingBloodBar(Building building) {
		ConstConfig config = building.getConstConfig();
		super.maxHp = config.maxHp;
		setMaxHp(maxHp);
		setMaxBloodNum(maxHp/100);
		setCurHp(maxHp);
		super.setPriority(60);
		super.setVisible(false);
		
		super.curFrame = new ShapeUnitFrame();
		BufferedImage image = curFrame.getImg();
		if(image==null) {
			//计算这个图片的宽高
			int height = 7+(maxBloodNum-1)*2;
			int width = 10+(maxBloodNum-1)*4;
			image = new BufferedImage(width,  height, BufferedImage.TYPE_INT_ARGB);
		}
		CanvasPainter.clearImage(image);
		
		
		//经观察  血条的位置  位于建筑的最左一块和最上一块的上方
		CenterPoint cp = building.getTopFirst();
		int height = 7+(maxBloodNum-1)*2;
		int width = 10+(maxBloodNum-1)*4;
		
		super.setPositionX(cp.getX()-width+7);
		super.setPositionY(cp.getY()-building.getHeight());
	}

	@Override
	public void rePaintBloodBar() {
		
		
		
		
		BufferedImage image = curFrame.getImg();
		if(image==null) {
			//计算这个图片的宽高
			int height = 7+(maxBloodNum-1)*2;
			int width = 10+(maxBloodNum-1)*4;
			image = new BufferedImage(width,  height, BufferedImage.TYPE_INT_ARGB);
		}
		CanvasPainter.clearImage(image);
		Graphics2D g2d = image.createGraphics();
		
		//在图片上的起始坐标
		int x = image.getWidth()-1-5;
		int y = 0;
		
		int curBloodNum = calCurBloodNum();
		Color blackColor = null;//最深的颜色
		Color deepColor = null;//深色
		Color innerColor = null;//内部颜色
		if(curBloodNum>=maxBloodNum/2) {
			blackColor = new Color(0,50,0);
			deepColor = new Color(0,128,0);
			innerColor = Color.green;
		}else if(curBloodNum<maxBloodNum/2 && curBloodNum>=maxBloodNum/4) {
			blackColor = new Color(50,50,0);
			deepColor = new Color(128,128,0);
			innerColor = Color.yellow;
		}else {
			blackColor = new Color(134,17,34);
			deepColor = new Color(204,34,34);
			innerColor = new Color(238,68,51);
		}
		
		//画剩余血量
		for(int i=0;i<curBloodNum;i++) {
			drawOneGreen(g2d,x,y,blackColor,deepColor,innerColor);
			if(i==0) {
				g2d.setColor(blackColor);
				g2d.drawLine(x+5, y+2, x+5, y+4);
				g2d.drawLine(x+4, y+4, x+5, y+4);
				
				g2d.setColor(deepColor);
				g2d.drawLine(x+4, y+3, x+4, y+3);
			}
			if(i==curBloodNum-1) {
				g2d.setColor(blackColor);
				g2d.drawLine(x-4, y+3, x-4, y+4);
				g2d.drawLine(x-4, y+4, x-3, y+4);
				g2d.drawLine(x-2, y+5, x-1, y+5);
			}
			
			x-=4;
			y+=2;
		}
		//画已丢失血量(空白血条框)
		int grayNum = maxBloodNum-curBloodNum;
		for(int i=0;i<grayNum;i++) {
			drawOneGray(g2d,x,y);
			
			if(i==grayNum-1) {
				Color black = Color.black;
				Color white = Color.white;
				g2d.setColor(black);
				
				g2d.drawLine(x-2, y+5, x+3, y+5);
				g2d.drawLine(x, y+6, x+1, y+6);
				
				g2d.setColor(white);
				g2d.drawLine(x+1, y+5, x+1, y+5);
			}
			
			x-=4;
			y+=2;
		}
		g2d.dispose();
		
		curFrame.setImg(image);
		
	}

	/**
	 * 计算剩余血块个数
	 */
	@Override
	public int calCurBloodNum() {
		curBloodNum = curHp/100;
		if(curBloodNum==0) {
			curBloodNum = 1;
		}
		return curBloodNum;
	}

	@Override
	public void calculateNextFrame() {
		rePaintBloodBar();
		
	}
	
	//画剩余血块
	public void drawOneGreen(Graphics2D g2d,int x,int y,Color blackColor,Color deepColor,Color innerColor) {
		g2d.setColor(deepColor);
		g2d.drawLine(x, y, x+1, y);
		
		g2d.drawLine(x-2, y+1, x-1, y+1);
		g2d.setColor(innerColor);
		g2d.drawLine(x, y+1, x+1, y+1);
		g2d.setColor(deepColor);
		g2d.drawLine(x+2, y+1, x+3, y+1);
		
		g2d.drawLine(x-4, y+2, x-3, y+2);
		g2d.setColor(innerColor);
		g2d.drawLine(x-2, y+2, x+3, y+2);
		g2d.setColor(deepColor);
		g2d.drawLine(x+4, y+2, x+5, y+2);
		
		g2d.drawLine(x-2, y+3, x-1, y+3);
		g2d.setColor(innerColor);
		g2d.drawLine(x, y+3, x+1, y+3);
		g2d.setColor(deepColor);
		g2d.drawLine(x+2, y+3, x+3, y+3);
		
		g2d.drawLine(x, y+4, x+1, y+4);
		
		//画黑绿色边
		g2d.setColor(blackColor);
		g2d.drawLine(x-4, y+3, x-4, y+4);
		g2d.drawLine(x-4, y+4, x-3, y+4);
		g2d.setColor(innerColor);
		g2d.drawLine(x-3, y+3, x-3, y+3);
		g2d.drawLine(x-2, y+4, x-1, y+4);
		
		g2d.setColor(blackColor);
		g2d.drawLine(x, y+5, x+3, y+5);
		g2d.drawLine(x, y+6, x+1, y+6);
		
		g2d.setColor(deepColor);
		g2d.drawLine(x+2, y+4, x+3, y+4);
		
	}
	//画灰色的
	public void drawOneGray(Graphics2D g2d,int x,int y) {
		Color gray = new Color(180,180,180);
		g2d.setColor(gray);
		g2d.drawLine(x, y, x+1, y);
		
		g2d.drawLine(x-2, y+1, x-1, y+1);
		g2d.drawLine(x+2, y+1, x+3, y+1);
		
		g2d.drawLine(x-4, y+2, x-3, y+2);
		g2d.drawLine(x+4, y+2, x+5, y+2);
		
		g2d.drawLine(x-2, y+3, x-1, y+3);
		g2d.drawLine(x+2, y+3, x+3, y+3);
		
		g2d.drawLine(x, y+4, x+1, y+4);
		
		//画黑边
		Color black = Color.black;
		g2d.setColor(black);
		g2d.drawLine(x-4, y+3, x-4, y+4);
		g2d.drawLine(x-4, y+4, x-3, y+4);
		
		g2d.drawLine(x, y+5, x+3, y+5);
		g2d.drawLine(x, y+6, x+1, y+6);
		
	}
	
	 
	
}

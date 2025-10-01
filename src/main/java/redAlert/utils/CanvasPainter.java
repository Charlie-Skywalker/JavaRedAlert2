package redAlert.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.jogamp.opengl.GLAutoDrawable;
import redAlert.RuntimeParameter;
import redAlert.ShapeUnitFrame;
import redAlert.SysConfig;
import redAlert.enums.UnitColor;
import redAlert.other.Place;
import redAlert.utilBean.CenterPoint;

/**
 * 负责在画板上画东西
 */
public class CanvasPainter {

	public static final int ox = 32;//菱形的长半径
	public static final int oy = 16;//菱形的短半径
	
	/**
	 * 清空画板
	 */
	public static void clearImage(BufferedImage bufferedImage) {
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.setComposite(AlphaComposite.Clear);//设置为清理模式
		g2d.fillRect(0, 0, width, height);
		g2d.dispose();
	}
	
	/**
	 * 向画板中增加一个选择框
	 */
	public static void drawSelectRect(int startMouseX,int startMouseY,int endMouseX,int endMouseY,BufferedImage canvas) {
		Graphics2D g2d = canvas.createGraphics();
		int rectWidth = 0;
		if(endMouseX>startMouseX) {
			rectWidth = endMouseX-startMouseX+1;
		}else {
			rectWidth = startMouseX-endMouseX+1;
			startMouseX = endMouseX;
		}
		int rectHeight = 0;
		if(endMouseY>startMouseY) {
			rectHeight = endMouseY-startMouseY+1;
		}else {
			rectHeight = startMouseY-endMouseY+1;
			startMouseY = endMouseY;
		}
		g2d.setColor(Color.white);
		g2d.drawRect(startMouseX, startMouseY, rectWidth, rectHeight);
		g2d.dispose();
	}
	
	/**
	 * 画板上绘制辅助线网格
	 * 
	 * 
	 */
	public static void drawGuidelines(BufferedImage canvas,int viewportOffX,int viewportOffY) {
		
		Graphics2D g2d = canvas.createGraphics();
		g2d.setColor(Color.gray);
		g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		g2d.setColor(Color.black);
		
		
		int viewportWidth = SysConfig.viewportWidth;
		int viewportHeight = SysConfig.viewportHeight;
		//一类中心点
		for(int m=0;m<50;m++) {
			int y = 15+30*m;
			for(int n=0;n<50;n++) {
				int x = 30+60*n;
				CenterPoint centerPoint = PointUtil.fetchCenterPoint(x, y);
				int centerX = centerPoint.getX();
				int centerY = centerPoint.getY();
				
				if(centerX>= viewportOffX-100 && centerX<=viewportOffX+viewportWidth+100 && centerY>=viewportOffY-100 && centerY< viewportOffY+viewportHeight+100) {
					centerX = CoordinateUtil.getViewportX(centerX, viewportOffX);
					centerY = CoordinateUtil.getViewportX(centerY, viewportOffY);
					
					g2d.drawLine(centerX+1, centerY-14, centerX-2, centerY-14);
					g2d.drawLine(centerX-2, centerY-14, centerX-30, centerY);
					g2d.drawLine(centerX-30, centerY, centerX-2, centerY+14);
					g2d.drawLine(centerX-2, centerY+14, centerX+1, centerY+14);
					g2d.drawLine(centerX+1, centerY+14, centerX+29, centerY);
					g2d.drawLine(centerX+29, centerY,centerX+1, centerY-14);
				}
				
			}
		}
		
		//二类中心点
		for(int m=0;m<50;m++) {
			int y = 30*m;
			for(int n=0;n<50;n++) {
				int x = 60*n;
				CenterPoint centerPoint = PointUtil.fetchCenterPoint(x, y);
				int centerX = centerPoint.getX();
				int centerY = centerPoint.getY();
				
				if(centerX>= viewportOffX-100 && centerX<=viewportOffX+viewportWidth+100 && centerY>=viewportOffY-100 && centerY< viewportOffY+viewportHeight+100) {
					centerX = CoordinateUtil.getViewportX(centerX, viewportOffX);
					centerY = CoordinateUtil.getViewportX(centerY, viewportOffY);
					
					g2d.drawLine(centerX+1, centerY-14, centerX-2, centerY-14);
					g2d.drawLine(centerX-2, centerY-14, centerX-30, centerY);
					g2d.drawLine(centerX-30, centerY, centerX-2, centerY+14);
					g2d.drawLine(centerX-2, centerY+14, centerX+1, centerY+14);
					g2d.drawLine(centerX+1, centerY+14, centerX+29, centerY);
					g2d.drawLine(centerX+29, centerY,centerX+1, centerY-14);
				}
				
			}
		}
	}
	
	/**
	 * 画建筑建造预占地方块
	 * mouseX 鼠标的地图X坐标
	 * mouseY 鼠标的地图Y坐标
	 * int fxNum 从西南向东北数占几个菱形（此方向即纺射X轴）
	 * int fyNum 从东南向西北数占几个菱形（此方向即仿射Y轴）
	 */
	public static void drawRhombus(int mapX,int mapY,int fxNum,int fyNum,BufferedImage canvas) {
		CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);//获取中心点
		drawRhombus(centerPoint,fxNum,fyNum,canvas);
	}
	
	/**
	 * 画建筑建造预占地方块
	 * centerPoint 菱形中心点
	 * int fxNum 从西南向东北数占几个菱形（此方向即纺射X轴）
	 * int fyNum 从东南向西北数占几个菱形（此方向即仿射Y轴）
	 */
	public static void drawRhombus(CenterPoint centerPoint,int fxNum,int fyNum,BufferedImage canvas) {
		
		//没有中间画板,不需要清除
//		clearImage(canvas);
		
		if(fxNum==1 && fyNum==1) {
			drawRhombus(centerPoint,canvas);
		}
		if(fxNum==2 && fyNum==2) {//发电厂 间谍卫星
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
		}
		if(fxNum==3 && fyNum==3) {//核弹井 维修厂 天气控制 矿石精炼
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeft(),canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
			drawRhombus(centerPoint.getRight(),canvas);
			drawRhombus(centerPoint.getRightUp(),canvas);
			drawRhombus(centerPoint.getUp(),canvas);
			drawRhombus(centerPoint.getLeftUp(),canvas);
		}
		if(fxNum==2 && fyNum==3) {//兵营 空指部 实验室
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeftUp(),canvas);
			drawRhombus(centerPoint.getLeft(),canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
		}
		if(fxNum==3 && fyNum==4) {//矿场 超时空
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeft(),canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
			drawRhombus(centerPoint.getRight(),canvas);
			drawRhombus(centerPoint.getRightUp(),canvas);
			drawRhombus(centerPoint.getUp(),canvas);
			drawRhombus(centerPoint.getLeftUp(),canvas);
			
			drawRhombus(centerPoint.getRight().getRightDn(),canvas);
			drawRhombus(centerPoint.getRightDn().getRightDn(),canvas);
			drawRhombus(centerPoint.getDn().getRightDn(),canvas);
		}
		if(fxNum==4 && fyNum==4) {//基地  船坞
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeft(),canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
			drawRhombus(centerPoint.getRight(),canvas);
			drawRhombus(centerPoint.getRightUp(),canvas);
			drawRhombus(centerPoint.getUp(),canvas);
			drawRhombus(centerPoint.getLeftUp(),canvas);
			
			drawRhombus(centerPoint.getLeft().getLeftDn(),canvas);
			drawRhombus(centerPoint.getLeftDn().getLeftDn(),canvas);
			drawRhombus(centerPoint.getLeftDn().getDn(),canvas);
			drawRhombus(centerPoint.getRight().getRightDn(),canvas);
			drawRhombus(centerPoint.getRightDn().getRightDn(),canvas);
			drawRhombus(centerPoint.getDn().getRightDn(),canvas);
			drawRhombus(centerPoint.getDn().getDn(),canvas);
		}
		if(fxNum==3 && fyNum==5) {//建设工厂
			drawRhombus(centerPoint,canvas);
			drawRhombus(centerPoint.getLeft(),canvas);
			drawRhombus(centerPoint.getLeftDn(),canvas);
			drawRhombus(centerPoint.getDn(),canvas);
			drawRhombus(centerPoint.getRightDn(),canvas);
			drawRhombus(centerPoint.getRight(),canvas);
			drawRhombus(centerPoint.getRightUp(),canvas);
			drawRhombus(centerPoint.getUp(),canvas);
			drawRhombus(centerPoint.getLeftUp(),canvas);
			
			drawRhombus(centerPoint.getLeft().getLeftUp(),canvas);
			drawRhombus(centerPoint.getLeftUp().getLeftUp(),canvas);
			drawRhombus(centerPoint.getUp().getLeftUp(),canvas);
			drawRhombus(centerPoint.getRight().getRightDn(),canvas);
			drawRhombus(centerPoint.getRightDn().getRightDn(),canvas);
			drawRhombus(centerPoint.getDn().getRightDn(),canvas);
		}
		
		
	}
	
	/**
	 * 画一个单位菱形
	 * 新方法
	 */
	private static void drawRhombus(CenterPoint centerPoint,BufferedImage canvas) {
		Graphics g = canvas.getGraphics();
		if(!centerPoint.isBuildingCanPutOn()) {
			g.setColor(Color.red);
		}else {
			g.setColor(Color.green);
		}
		int centerX = centerPoint.getX();
		int centerY = centerPoint.getY();
		int viewportOffX = RuntimeParameter.viewportOffX;
		int viewportOffY = RuntimeParameter.viewportOffY;
		int viewX = CoordinateUtil.getViewportX(centerX, viewportOffX);
		int viewY = CoordinateUtil.getViewportX(centerY, viewportOffY);
		
		
		for(int i=0;i<15;i++) {
			g.drawLine(viewX-2-2*i, viewY-14+i, viewX+1+2*i, viewY-14+i);
		}
		for(int i=0;i<14;i++) {
			g.drawLine(viewX-1-29+2+2*i, viewY+1+i, viewX+29-2-2*i, viewY+1+i);
		}
	}

	/**
	 * 画建筑建造预占地方块
	 * centerPoint 菱形中心点
	 * int fxNum 从西南向东北数占几个菱形（此方向即纺射X轴）
	 * int fyNum 从东南向西北数占几个菱形（此方向即仿射Y轴）
	 */
	public static void drawRhombus(GLAutoDrawable drawable, CenterPoint centerPoint, int fxNum, int fyNum, int viewportOffX, int viewportOffY) {
		if(fxNum==1 && fyNum==1) {
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
		}
		if(fxNum==2 && fyNum==2) {//发电厂 间谍卫星
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==3) {//核弹井 维修厂 天气控制 矿石精炼
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
		}
		if(fxNum==2 && fyNum==3) {//兵营 空指部 实验室
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==4) {//矿场 超时空
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);

			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==4 && fyNum==4) {//基地  船坞
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);

			drawRhombus(drawable,centerPoint.getLeft().getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn().getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn().getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getDn(),viewportOffX,viewportOffY);
		}
		if(fxNum==3 && fyNum==5) {//建设工厂
			drawRhombus(drawable,centerPoint,viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeft(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp(),viewportOffX,viewportOffY);

			drawRhombus(drawable,centerPoint.getLeft().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getLeftUp().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getUp().getLeftUp(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRight().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getRightDn().getRightDn(),viewportOffX,viewportOffY);
			drawRhombus(drawable,centerPoint.getDn().getRightDn(),viewportOffX,viewportOffY);
		}
	}

	/**
	 * 画最小单位菱形块
	 */
	private static void drawRhombus(GLAutoDrawable drawable,CenterPoint centerPoint,int viewportOffX,int viewportOffY) {
		ShapeUnitFrame suf = null;
		if(!centerPoint.isBuildingCanPutOn()) {
			suf = Place.getRedRect();
		}else {
			suf = Place.getGreenRect();
		}

		int positionX = centerPoint.getX()-30;
		int positionY = centerPoint.getY()-14;

		DrawableUtil.drawOneSufAtPosition(drawable, suf, positionX, positionY, viewportOffX, viewportOffY);
	}


	/**
	 * 将一个颜色转成指定颜色
	 */
	public static int transColor(int oriColor,UnitColor targetColor) {
		int da = delAlpha(oriColor);
		int mc = 0;
		if(targetColor==null || targetColor==UnitColor.Red) {
			//本来就是红色的
			mc = da;
		}
		if(targetColor==UnitColor.Blue){
			mc = turnBlue(da);
		}
		if(targetColor==UnitColor.Green) {
			mc = turnGreen(da);
		}
		if(targetColor==UnitColor.Yellow) {
			mc = turnYellow(da);
		}
		if(targetColor==UnitColor.Purple) {
			mc = turnPurple(da);
		}
		if(targetColor==UnitColor.LightBlue) {
			mc = turnLightBlue(da);
		}
		if(targetColor==UnitColor.Orange) {
			mc = turnOrange(da);
		}
		if(targetColor==UnitColor.Gray) {
			mc = turnGray(da);
		}
		return addAlpha(mc);//之所以颜色要加alpha,是因为ARBG图片,如果alpha位为0,则颜色完全透明,表现为黑色
	}
	
	private static int turnBlue(int oriColor)   {return oriColor >> 16;}                 						//盟军蓝   最深色#0000EC
	private static int turnGreen(int oriColor)  {return oriColor >>  8;}                 						//标准绿   最深色#00EC00
	private static int turnYellow(int oriColor) {return oriColor | turnGreen(oriColor);}      					//标准黄   最深色#ECEC00  (红+绿)
	private static int turnPurple(int oriColor) {return oriColor | turnBlue(oriColor);}					    	//紫色     最深色#EC00EC  (红+蓝)
	private static int turnLightBlue(int oriColor) {return turnGreen(oriColor) | turnBlue(oriColor);}        	//淡蓝     最深色#00ECEC  (绿+蓝)
	private static int turnGray(int oriColor) {return oriColor | turnGreen(oriColor) | turnBlue(oriColor);}		//中立灰   最深色#ECECEC  (红+绿+蓝)
	private static int turnOrange(int oriColor) {//橙色
		int a = oriColor>>16;
		double bl = a/255.0;
		double b = 124*bl;
		int c = (int)b;
		int d = c<<8;
		int result = oriColor | d;
		return result;
	}									
	
	private static int delAlpha(int oriColor) {return oriColor & 0x00FFFFFF;}
	private static int addAlpha(int oriColor) {return oriColor | 0xFF000000;}
	
	/**
	 * 复制一个BufferedImage对象
	 */
	public static BufferedImage copyImage(BufferedImage oriImage) {
		int width = oriImage.getWidth();
		int height = oriImage.getHeight();
		BufferedImage image = new BufferedImage(oriImage.getWidth(),oriImage.getHeight(),oriImage.getType());
		image.createGraphics().drawImage(oriImage, 0, 0, width, height, null);
		return image;
	}
	
}

package redAlert;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;

import redAlert.enums.MouseStatus;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.PointUtil;

/**
 * 专门用于处理键盘事件
 */
public class KeyBoardEventDeal {

	public static JPanel scenePanel;
	
	public static final int minMovePixel = 3;//移动像素个数
	
	
	public static void init(JPanel scenePanel) {
		
		KeyBoardEventDeal.scenePanel = scenePanel;
		
		
		scenePanel.requestFocus();//请求焦点  键盘事件才能生效
		
		
		
		long thisFrame = RuntimeParameter.frameCount;
		
		scenePanel.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			
			}
			
			@Override
			public void keyReleased(KeyEvent e) {

			
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				e.isControlDown();//Ctrl按键是否按下
				
				if(e.getKeyCode()==KeyEvent.VK_UP){
					if(thisFrame!=RuntimeParameter.frameCount) {
						if(RuntimeParameter.viewportOffY>0) {
							RuntimeParameter.viewportOffY-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(thisFrame!=RuntimeParameter.frameCount) {
						if(RuntimeParameter.viewportOffY<SysConfig.gameMapHeight-SysConfig.viewportHeight) {
							RuntimeParameter.viewportOffY+=minMovePixel;
						}
					}
				}
				
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					if(thisFrame!=RuntimeParameter.frameCount) {
						if(RuntimeParameter.viewportOffX>0) {
							RuntimeParameter.viewportOffX-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					if(thisFrame!=RuntimeParameter.frameCount) {
						if(RuntimeParameter.viewportOffX<SysConfig.gameMapWidth-SysConfig.viewportWidth) {
							RuntimeParameter.viewportOffX+=minMovePixel;
						}
					}
				}
				
				if(thisFrame!=RuntimeParameter.frameCount) {
					Point mousePoint = scenePanel.getMousePosition();
					if(mousePoint!=null) {
						
						int mouseX = mousePoint.x;
						int mouseY = mousePoint.y;
						Coordinate coord = CoordinateUtil.getCoordinate(mouseX, mouseY);
						int mapX = coord.getMapX();
						int mapY = coord.getMapY();
						
						/**
						 * 建造状态的判定优先级最高
						 */
						if(RuntimeParameter.mouseStatus == MouseStatus.Construct) {
							if(mapX==RuntimeParameter.lastMoveX && mapY==RuntimeParameter.lastMoveY) {
								return;
							}else {
								RuntimeParameter.lastMoveX = mapX;
								RuntimeParameter.lastMoveY = mapY;
								
								CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
								CenterPoint lastCenterPoint = RuntimeParameter.lastMoveCenterPoint;
								if(centerPoint.equals(lastCenterPoint)) {
									return;
								}else {
									RuntimeParameter.lastMoveCenterPoint = centerPoint;
									//这个方法不能调用太频繁   太频繁的绘图会导致程序卡顿
//									CanvasPainter.drawRhombus(centerPoint, MouseEventDeal.constName.fxNum, MouseEventDeal.constName.fyNum, scenePanel.getCanvasFirst());
								}
								return;
							}
						}else {
							MouseEventDeal.resetMouseStatus(coord);
						}
					}
					
					
					
				}
				
			}
		});
		
	}
}

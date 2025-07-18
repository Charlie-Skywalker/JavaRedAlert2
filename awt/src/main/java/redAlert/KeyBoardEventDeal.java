package redAlert;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import redAlert.MainTest.MouseStatus;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utils.CanvasPainter;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.PointUtil;

/**
 * 专门用于处理键盘事件
 */
public class KeyBoardEventDeal {

	public static MainPanel scenePanel;
	
	public static final int minMovePixel = 3;//移动像素个数
	
	
	public static void init(MainPanel scenePanel) {
		
		KeyBoardEventDeal.scenePanel = scenePanel;
		
		
		scenePanel.requestFocus();//请求焦点  键盘事件才能生效
		
		
		
		long thisFrame = MainPanel.frameCount;
		
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
					if(thisFrame!=MainPanel.frameCount) {
						if(MainPanel.viewportOffY>0) {
							MainPanel.viewportOffY-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(thisFrame!=MainPanel.frameCount) {
						if(MainPanel.viewportOffY<MainPanel.gameMapHeight-MainPanel.viewportHeight) {
							MainPanel.viewportOffY+=minMovePixel;
						}
					}
				}
				
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					if(thisFrame!=MainPanel.frameCount) {
						if(MainPanel.viewportOffX>0) {
							MainPanel.viewportOffX-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					if(thisFrame!=MainPanel.frameCount) {
						if(MainPanel.viewportOffX<MainPanel.gameMapWidth-MainPanel.viewportWidth) {
							MainPanel.viewportOffX+=minMovePixel;
						}
					}
				}
				
				if(thisFrame!=MainPanel.frameCount) {
					Point mousePoint = GameContext.scenePanel.getMousePosition();
					if(mousePoint!=null) {
						
						int mouseX = mousePoint.x;
						int mouseY = mousePoint.y;
						Coordinate coord = CoordinateUtil.getCoordinate(mouseX, mouseY);
						int mapX = coord.getMapX();
						int mapY = coord.getMapY();
						
						/**
						 * 建造状态的判定优先级最高
						 */
						if(MainTest.mouseStatus == MouseStatus.Construct) {
							if(mapX==scenePanel.getLastMoveX() && mapY==scenePanel.getLastMoveY()) {
								return;
							}else {
								scenePanel.setLastMoveX(mapX);
								scenePanel.setLastMoveY(mapY);
								
								CenterPoint centerPoint = PointUtil.getCenterPoint(mapX, mapY);
								CenterPoint lastCenterPoint = scenePanel.getLastMoveCenterPoint();
								if(centerPoint.equals(lastCenterPoint)) {
									return;
								}else {
									scenePanel.setLastMoveCenterPoint(centerPoint);
									//这个方法不能调用太频繁   太频繁的绘图会导致程序卡顿
									CanvasPainter.drawRhombus(centerPoint, MouseEventDeal.constName.fxNum, MouseEventDeal.constName.fyNum, scenePanel.getCanvasFirst());
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

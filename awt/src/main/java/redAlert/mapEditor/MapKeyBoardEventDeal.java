package redAlert.mapEditor;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MapKeyBoardEventDeal {
	
	public static MapEditorPanel scenePanel;
	
	public static final int minMovePixel = 3;//移动像素个数
	
	
	public static void init(MapEditorPanel scenePanel) {
		
		MapKeyBoardEventDeal.scenePanel = scenePanel;
		
		
		scenePanel.requestFocus();//请求焦点  键盘事件才能生效
		
		
		
		long thisFrame = MapEditorPanel.frameCount;
		
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
					if(thisFrame!=MapEditorPanel.frameCount) {
						if(MapEditorPanel.viewportOffY>0) {
							MapEditorPanel.viewportOffY-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_DOWN){
					if(thisFrame!=MapEditorPanel.frameCount) {
						if(MapEditorPanel.viewportOffY<MapEditorPanel.gameMapHeight-MapEditorPanel.viewportHeight) {
							MapEditorPanel.viewportOffY+=minMovePixel;
						}
					}
				}
				
				if(e.getKeyCode()==KeyEvent.VK_LEFT){
					if(thisFrame!=MapEditorPanel.frameCount) {
						if(MapEditorPanel.viewportOffX>0) {
							MapEditorPanel.viewportOffX-=minMovePixel;
						}
					}
				}
				if(e.getKeyCode()==KeyEvent.VK_RIGHT){
					if(thisFrame!=MapEditorPanel.frameCount) {
						if(MapEditorPanel.viewportOffX<MapEditorPanel.gameMapWidth-MapEditorPanel.viewportWidth) {
							MapEditorPanel.viewportOffX+=minMovePixel;
						}
					}
				}
				
				
			}
		});
		
	}
}

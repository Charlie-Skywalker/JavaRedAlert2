package redAlert.mapEditor;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import redAlert.utilBean.Coordinate;
import redAlert.utils.CoordinateUtil;

/**
 * 处理地图编辑器的点击事件
 */
public class MapMouseEventDeal {

	/**
	 * 界面引用
	 */
	public static MapEditorPanel editorPanel = null;
	/**
	 * 被选中的点
	 */
	public static MapCenterPoint selectedCenterPoint; 
	
	public static void init(MapEditorPanel editorPanel ) {
		MapMouseEventDeal.editorPanel = editorPanel;
		
		editorPanel.addMouseListener(new MouseAdapter() {

			/**
			 * 选中地形
			 */
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
				selectedCenterPoint = MapCenterPointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
			}
			
		});
		
	}
	
}

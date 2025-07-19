package redAlert.mapEditor;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * 右侧瓦片
 */
public class TerrainJLabel extends JLabel{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	
	public Tile tile;
	
	public TerrainJLabel(Tile tile) {
		this.tile = tile;
		
		setOpaque(true);
		Icon topIcon = new ImageIcon(tile.getImage());
		setIcon(topIcon);
		setSize(topIcon.getIconWidth(), topIcon.getIconHeight());
		
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				
				MapCenterPoint cp = MapMouseEventDeal.selectedCenterPoint;
				if(cp!=null) {
					Graphics g2d = MapMouseEventDeal.editorPanel.guidelinesCanvas.createGraphics();
					g2d.drawImage(tile.getImage(), cp.getX()-30- MapEditorPanel.viewportOffX, cp.getY()-15-MapEditorPanel.viewportOffY, null);
					g2d.dispose();
					
					cp.tile = tile;
				}
				
			}
			
		});
	}
	
	
	
}

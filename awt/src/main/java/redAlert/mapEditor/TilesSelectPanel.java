package redAlert.mapEditor;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;

/**
 * 右侧瓦片选择框
 */
public class TilesSelectPanel extends JPanel{
	
	private static final long serialVersionUID = 810349439955934352L;
	
	/**
	 * 选项卡页面宽高
	 */
	public static final int optionWidth = 168;
	public static final int optionHeight = 900;
	
	
	public TilesSelectPanel() {
		super.setLocation(MapEditorPanel.viewportWidth, 0);
		super.setMinimumSize(new Dimension(optionWidth,optionHeight));
		super.setPreferredSize(new Dimension(optionWidth,optionHeight));
		super.setBackground(Color.green);
		
		
		List<Tile> terrainImageList = TilesSourceCenter.terrainImageList;
		
		for(int i=0;i<terrainImageList.size();i++) {
			Tile tile = terrainImageList.get(i);
			TerrainJLabel terr = new TerrainJLabel(tile);
			add(terr);
		}
	}
}

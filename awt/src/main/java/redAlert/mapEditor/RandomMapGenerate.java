package redAlert.mapEditor;

import java.awt.Graphics;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import redAlert.GlobalConfig;

/**
 * 随机地图生成的方法放在这里
 */
public class RandomMapGenerate {
	
	public static void randomGenerate() {
		
		//从一个指定点  遍历全图
		ArrayDeque<MapCenterPoint> rest = new ArrayDeque<>();
		MapCenterPoint mcp = MapCenterPointUtil.getCenterPoint(600,450);//起始点
		rest.add(mcp);
		Set<MapCenterPoint> haveGet = new HashSet<>();
		
		while(!rest.isEmpty()) {
			MapCenterPoint start = rest.poll();
			if(!haveGet.contains(start)) {
				dealOneMcp(start);
				haveGet.add(start);
			}
			
			List<MapCenterPoint> ls = MapCenterPointUtil.getNeighbors(start);
			for(MapCenterPoint theMcp:ls) {
				if(!haveGet.contains(theMcp) && !rest.contains(theMcp)) {
					rest.add(theMcp);
				}
			}
			
		}
		
		
		//将生成的信息保存进文件  供游戏进程读取
		StringBuilder text = new StringBuilder(50000);
		for(int i=0;i<allMcps.size();i++  ) {
			MapCenterPoint point = allMcps.get(i);
			if(i<allMcps.size()-1) {
				text.append(point.getX())
			    .append(",")
			    .append(point.getY())
			    .append(",")
			    .append(point.getTile().getName())
			    .append("$");
			}else {
				text.append(point.getX())
			    .append(",")
			    .append(point.getY())
			    .append(",")
			    .append(point.getTile().getName());
			}
		}
		
		try {
			FileUtils.writeStringToFile(new File(GlobalConfig.mapFilePath), text.toString(), "UTF-8");
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}
	
	public static List<MapCenterPoint> allMcps = new ArrayList<>();
	
	public static void dealOneMcp(MapCenterPoint start ) {
		String targetType = "";
		
		
		if(start.getRightUp()!=null && start.getRightUp().getTile()!=null) {
			Tile tile = start.getRightUp().getTile();
			targetType += tile.getLeftDownType();
		}else {
			targetType += "2";
		}
		if(start.getRightDn()!=null && start.getRightDn().getTile()!=null) {
			Tile tile = start.getRightDn().getTile();
			targetType += tile.getLeftUpType();
		}else {
			targetType += "2";
		}
		if(start.getLeftDn()!=null && start.getLeftDn().getTile()!=null) {
			Tile tile = start.getLeftDn().getTile();
			targetType += tile.getRightUpType();
		}else {
			targetType += "2";
		}
		if(start.getLeftUp()!=null && start.getLeftUp().getTile()!=null) {
			Tile tile = start.getLeftUp().getTile();
			targetType += tile.getRightDownType();
		}else {
			targetType += "2";
		}
		
		
		Tile tile = TilesSourceCenter.searchOneTargetTile(targetType);
		Graphics g2d = MapMouseEventDeal.editorPanel.guidelinesCanvas.createGraphics();
		g2d.drawImage(tile.getImage(), start.getX()-30- MapEditorPanel.viewportOffX, start.getY()-15-MapEditorPanel.viewportOffY, null);
		g2d.dispose();
		
		start.tile = tile;
		allMcps.add(start);
		
		try {
//			Thread.sleep(5);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}

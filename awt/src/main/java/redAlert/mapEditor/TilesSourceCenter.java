package redAlert.mapEditor;

import java.util.ArrayList;
import java.util.List;

import redAlert.utils.RandomUtil;

/**
 * 瓦片的资源中心
 */
public class TilesSourceCenter {

	
	/**
	 * 地形菱形块列表
	 */
	public static List<Tile> terrainImageList = new ArrayList<>();
	
	
	public static void loadResource() {
		//在这里试着画一些地形的内容
		try {
			terrainImageList.add(new Tile("clat01.sno","0000"));
			terrainImageList.add(new Tile("clat02.sno","1000"));
			terrainImageList.add(new Tile("clat03.sno","0100"));
			terrainImageList.add(new Tile("clat04.sno","1100"));
			terrainImageList.add(new Tile("clat05.sno","0010"));
			terrainImageList.add(new Tile("clat06.sno","1010"));
			terrainImageList.add(new Tile("clat07.sno","0110"));
			terrainImageList.add(new Tile("clat08.sno","1110"));
			terrainImageList.add(new Tile("clat09.sno","0001"));
			terrainImageList.add(new Tile("clat10.sno","1001"));
			terrainImageList.add(new Tile("clat11.sno","0101"));
			terrainImageList.add(new Tile("clat12.sno","1101"));
			terrainImageList.add(new Tile("clat13.sno","0011"));
			terrainImageList.add(new Tile("clat14.sno","1011"));
			terrainImageList.add(new Tile("clat15.sno","0111"));
			terrainImageList.add(new Tile("clat16.sno","1111"));
			
			terrainImageList.add(new Tile("clat01a.sno","0000"));
			terrainImageList.add(new Tile("clat02a.sno","1000"));
			terrainImageList.add(new Tile("clat03a.sno","0100"));
			terrainImageList.add(new Tile("clat04a.sno","1100"));
			terrainImageList.add(new Tile("clat05a.sno","0010"));
			terrainImageList.add(new Tile("clat06a.sno","1010"));
			terrainImageList.add(new Tile("clat07a.sno","0110"));
			terrainImageList.add(new Tile("clat08a.sno","1110"));
			terrainImageList.add(new Tile("clat09a.sno","0001"));
			terrainImageList.add(new Tile("clat10a.sno","1001"));
			terrainImageList.add(new Tile("clat11a.sno","0101"));
			terrainImageList.add(new Tile("clat12a.sno","1101"));
			terrainImageList.add(new Tile("clat13a.sno","0011"));
			terrainImageList.add(new Tile("clat14a.sno","1011"));
			terrainImageList.add(new Tile("clat15a.sno","0111"));
			terrainImageList.add(new Tile("clat16a.sno","1111"));
			
//			terrainImageList.add(new Tile("clat01.tem","0000"));
//			terrainImageList.add(new Tile("clat02.tem","1000"));
//			terrainImageList.add(new Tile("clat03.tem","0100"));
//			terrainImageList.add(new Tile("clat04.tem","1100"));
//			terrainImageList.add(new Tile("clat05.tem","0010"));
//			terrainImageList.add(new Tile("clat06.tem","1010"));
//			terrainImageList.add(new Tile("clat07.tem","0110"));
//			terrainImageList.add(new Tile("clat08.tem","1110"));
//			terrainImageList.add(new Tile("clat09.tem","0001"));
//			terrainImageList.add(new Tile("clat10.tem","1001"));
//			terrainImageList.add(new Tile("clat11.tem","0101"));
//			terrainImageList.add(new Tile("clat12.tem","1101"));
//			terrainImageList.add(new Tile("clat13.tem","0011"));
//			terrainImageList.add(new Tile("clat14.tem","1011"));
//			terrainImageList.add(new Tile("clat15.tem","0111"));
//			terrainImageList.add(new Tile("clat16.tem","1111"));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取一个指定类型的块
	 */
	public static Tile searchOneTargetTile(String targetType) {
		if("2222".equals(targetType)) {
			int index = RandomUtil.randomInt(0, terrainImageList.size()-1);
			return terrainImageList.get(index);
		}
		
		String a = targetType.substring(0,1);
		String b = targetType.substring(1,2);
		String c = targetType.substring(2,3);
		String d = targetType.substring(3,4);
		
		List<Tile> result = new ArrayList<>();
		
		for(Tile tile:terrainImageList) {
			String type = tile.getType();
			if(isOk1(a,type) && isOk2(b,type) && isOk3(c,type) && isOk4(d,type)) {
				result.add(tile);
			}
		}
		
		if(!result.isEmpty()) {
			return result.get(RandomUtil.randomInt(0, result.size()-1));
		}else {
			return null;
		}
	}
	
	public static boolean isOk1(String a,String type) {
		if(a.equals("2")) {
			return true;
		}else {
			if(a.equals(type.substring(0, 1))) {
				return true;
			}else {
				return false;
			}
		}
	}
	public static boolean isOk2(String b,String type) {
		if(b.equals("2")) {
			return true;
		}else {
			if(b.equals(type.substring(1, 2))) {
				return true;
			}else {
				return false;
			}
		}
	}
	public static boolean isOk3(String c,String type) {
		if(c.equals("2")) {
			return true;
		}else {
			if(c.equals(type.substring(2, 3))) {
				return true;
			}else {
				return false;
			}
		}
	}
	public static boolean isOk4(String d,String type) {
		if(d.equals("2")) {
			return true;
		}else {
			if(d.equals(type.substring(3, 4))) {
				return true;
			}else {
				return false;
			}
		}
	}
}

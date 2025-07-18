package redAlert.mapEditor;

import java.awt.image.BufferedImage;

import redAlert.utils.TmpFileReader;

/**
 * 瓦片对象
 */
public class Tile {

	
	
	public BufferedImage image;
	
	/**
	 * 表示四条边的明暗情况
	 * 亮边用1表示
	 * 暗边用0表示
	 */
	public String type = "0000";
	/**
	 * 文件名
	 */
	public String name = "";
	
	
	public Tile(String name,String type) {
		try {
			this.name = name;
			this.image = TmpFileReader.test(name);
			this.type = type;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Tile(BufferedImage image,String type) {
		this.image = image;
		this.type = type;
	}

	public String getRightUpType() {
		return type.substring(0, 1);
	}
	public String getRightDownType() {
		return type.substring(1, 2);
	}
	public String getLeftDownType() {
		return type.substring(2, 3);
	}
	public String getLeftUpType() {
		return type.substring(3, 4);
	}
	
	
	public BufferedImage getImage() {
		return image;
	}


	public void setImage(BufferedImage image) {
		this.image = image;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	
}

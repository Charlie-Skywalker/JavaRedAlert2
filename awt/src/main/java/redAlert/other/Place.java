package redAlert.other;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * 建造块形状的SHP
 */
public class Place {

	/**
	 * place.shp文件中的所有鼠标指针图标集
	 */
	public static List<ShapeUnitFrame> placeShapeFrames;
	
	/**
	 * 初始化鼠标指针图标集和红警游戏默认指针
	 *
	 */
	public static void initPlaceRect() {
		placeShapeFrames = ShpResourceCenter.loadShpResource("place", "../shp/palette");
		
		/*
		 * 右侧红警对战默认指针
		 * 使用Cursor设置指针,图片应为60*29大小
		 * Java的一个坑！！：BufferedImage.getSubImage方法  获取的子图片与原图片共享数据区  当使用Raster获取DataBufferInt时  获取的是原图片的数组
		 */
		BufferedImage oriGreenImage = placeShapeFrames.get(0).getImg();
		BufferedImage oriRedImage = placeShapeFrames.get(1).getImg();
		
		BufferedImage greenRectImage = new BufferedImage(60,29,BufferedImage.TYPE_INT_ARGB);
		BufferedImage redRectImage = new BufferedImage(60,29,BufferedImage.TYPE_INT_ARGB);
		for(int i=30;i<59;i++) {
			for(int j=0;j<60;j++) {
				greenRectImage.setRGB(j, i-30, oriGreenImage.getRGB(j, i));
				redRectImage.setRGB(j, i-30, oriRedImage.getRGB(j, i));
			}
		}
		placeShapeFrames.get(0).setImg(greenRectImage);
		placeShapeFrames.get(1).setImg(redRectImage);
		
	}
	/**
	 * 绿色菱形块
	 */
	public static ShapeUnitFrame getGreenRect() {
		ShapeUnitFrame suf = placeShapeFrames.get(0);
		suf.setShouldBeLoadedToGpu(true);
		return suf;
	}
	/**
	 * 红色菱形块
	 */
	public static ShapeUnitFrame getRedRect() {
		ShapeUnitFrame suf = placeShapeFrames.get(1);
		suf.setShouldBeLoadedToGpu(true);
		return suf;
	}
	
}

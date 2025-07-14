package redAlert.other;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.MainPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 关于鼠标指针形状的SHP
 */
public class Mouse {

	public static List<BufferedImage> mouseCursorLs = new ArrayList<>();
	
	
	public static BufferedImage defaultCursorImage;
	
	public static List<ShapeUnitFrame> mouseShapeFrames;
	
	/**
	 * 红警对战中的默认鼠标
	 */
	public static Cursor defaultCursor;
	/**
	 * 预单选指针集
	 */
	public static List<Cursor> singleSelectCursors;
	/**
	 * 单位移动指针集
	 */
	public static List<Cursor> unitMoveCursors;
	
	/**
	 * 初始化鼠标指针图片
	 * 
	 * 当鼠标指针图片宽高小于32*32时,将原图片放在32*32图片中间
	 * 
	 * 当鼠标指针图片宽高大于32*32时，已宽高中较大值，构建正方形图片，再将内容放在中间
	 * 
	 * 废弃方法
	 */
	public static void initMouseCursor() {
		mouseShapeFrames = ShpResourceCenter.loadShpResource("mouse", "mousepal", false);	
		
		//默认指针可以用
		defaultCursorImage = mouseShapeFrames.get(0).getImg().getSubimage(0, 0, 32, 32);
		
	}
	
	/**
	 * 获得默认鼠标
	 * 一个白色指针,可以在右侧控制栏使用
	 */
	public static Cursor getDefaultCursor() {
		if(defaultCursor==null) {
			Point hotSpot = new Point(0,0);
			return defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(defaultCursorImage, hotSpot, "defaultCursor");
		}else {
			return defaultCursor;
		}
	}
	
	/**
	 * 一个不显示的鼠标
	 * 提供给启动类用来隐藏系统默认鼠标
	 */
	public static Cursor getNoneCursor() {
		BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		Point hotSpot = new Point(0,0);
		return Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "NoneCursor");
	}
	
	/**
	 * 获取默认鼠标指针图片
	 */
	public static BufferedImage getDefaultCursorImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(0);
		return suf.getImg();
	}
	
	/**
	 * 获取预单选鼠标指针图片
	 */
	public static BufferedImage getPreSingleSelectCursorImage() {
		int index = 18+(int)MainPanel.frameCount/4%13;//除以4目的在于控制帧率
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		return suf.getImg();
	}

	/**
	 * 获取预单选敌方鼠标指针图片
	 */
	public static BufferedImage getPreSingleSelectEnemyCursorImage() {
		int index = 53 + (int) MainPanel.frameCount / 4 % 5;//除以4目的在于控制帧率,5表示动画图像有5帧画面
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		return suf.getImg();
	}
	
	/**
	 * 获取单位移动指针图片
	 */
	public static BufferedImage getUnitMoveCursorImage() {
		int index = 31+(int)MainPanel.frameCount/4%10;
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		return suf.getImg();
	}
	
	/**
	 * 获取单位部署指针图片
	 */
	public static BufferedImage getUnitExpandImage() {
		int index = 110+(int)MainPanel.frameCount/4%9;
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		return suf.getImg();
	}
	
	/**
	 * 获取单位禁止部署指针图片
	 */
	public static BufferedImage getUnitNoExpandImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(119);
		return suf.getImg();
	}
	/**
	 * 获取单位禁止移动指针图片
	 */
	public static BufferedImage getUnitNoMoveImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(41);
		return suf.getImg();
	}
	
}

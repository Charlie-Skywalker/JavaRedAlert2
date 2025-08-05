package redAlert.other;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.List;

import redAlert.RuntimeParameter;
import redAlert.ShapeUnitFrame;
import redAlert.enums.MouseStatus;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 关于鼠标指针形状的SHP
 */
public class Mouse {
	
	/**
	 * mouse.shp文件中的所有鼠标指针图标集
	 */
	public static List<ShapeUnitFrame> mouseShapeFrames;
	
	/**
	 * 红警对战中的默认鼠标
	 * 用于右侧OptionPanel
	 * (右侧只有一种鼠标样式,所以使用AWT系统鼠标指针)
	 */
	public static Cursor defaultCursor;
	
	/**
	 * 初始化鼠标指针图标集和红警游戏默认指针
	 *
	 */
	public static void initMouseCursor() {
		mouseShapeFrames = ShpResourceCenter.loadShpResource("mouse", "mousepal", false);
		
		/*
		 * 右侧红警对战默认指针
		 * 使用Cursor设置指针,图片应为32*32大小
		 */
		BufferedImage defaultCursorImage = mouseShapeFrames.get(0).getImg().getSubimage(0, 0, 32, 32);
		Point hotSpot = new Point(0,0);
		defaultCursor = Toolkit.getDefaultToolkit().createCustomCursor(defaultCursorImage, hotSpot, "defaultCursor");
	}
	
	/**
	 * 获得默认鼠标
	 * 一个白色箭头指针,在右侧控制栏(OptionPanel)使用,右侧直接修改JPanel上的Cursor对象进行鼠标指针切换
	 * 这与左侧MainPanel上有所不同
	 */
	public static Cursor getDefaultCursor() {
		return defaultCursor;
	}
	
	
	
	/**
	 * 一个不显示的鼠标
	 * 提供给启动类用来隐藏系统默认鼠标指针
	 */
	public static Cursor getNoneCursor() {
		BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		Point hotSpot = new Point(0,0);
		return Toolkit.getDefaultToolkit().createCustomCursor(image, hotSpot, "NoneCursor");
	}
	
	
	public static MouseCursorObject mouseObj = new MouseCursorObject();
	
	/**
	 * 获取鼠标的方法  由此类承包
	 */
	public static MouseCursorObject getMouseCursor(MouseStatus mouseStatus) {
		if(mouseStatus==MouseStatus.Idle) {
			return getDefaultCursorImage();
		}else if(mouseStatus==MouseStatus.PreSingleSelect) {
			return getPreSingleSelectCursorImage();
		}else if(mouseStatus==MouseStatus.Construct) {
			return getDefaultCursorImage();
		}else if(mouseStatus==MouseStatus.Select) {
			return getDefaultCursorImage();
		}else if(mouseStatus==MouseStatus.UnitMove) {
			return getUnitMoveCursorImage();
		}else if(mouseStatus==MouseStatus.UnitExpand) {
			return getUnitExpandImage();
		}else if(mouseStatus==MouseStatus.UnitNoExpand) {
			return getUnitNoExpandImage();
		}else if(mouseStatus==MouseStatus.UnitNoMove) {
			return getUnitNoMoveImage();
		}else if(mouseStatus==MouseStatus.Sell) {
			return getSellCursorImage();
		}else if(mouseStatus==MouseStatus.NoSell) {
			return getNoSellCursorImage();
		}
		return null;
	}
	
	/**
	 * 获取默认鼠标指针图片
	 */
	public static MouseCursorObject getDefaultCursorImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(0);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(0);
		mouseObj.setOffY(0);
		return mouseObj;
	}
	
	/**
	 * 获取预单选鼠标指针图片
	 */
	public static MouseCursorObject getPreSingleSelectCursorImage() {
		int index = 18+(int)RuntimeParameter.frameCount/4%13;//除以4目的在于控制帧率
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	
	/**
	 * 获取单位移动指针图片
	 */
	public static MouseCursorObject getUnitMoveCursorImage() {
		int index = 31+(int)RuntimeParameter.frameCount/4%10;
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	
	/**
	 * 获取单位部署指针图片
	 */
	public static MouseCursorObject getUnitExpandImage() {
		int index = 110+(int)RuntimeParameter.frameCount/4%9;
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	
	/**
	 * 获取单位禁止部署指针图片
	 */
	public static MouseCursorObject getUnitNoExpandImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(119);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	/**
	 * 获取单位禁止移动指针图片
	 */
	public static MouseCursorObject getUnitNoMoveImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(41);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	/**
	 * 获取卖建筑指针图片
	 */
	public static MouseCursorObject getSellCursorImage() {
		int index = 129+(int)RuntimeParameter.frameCount/4%10;
		ShapeUnitFrame suf = mouseShapeFrames.get(index);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	/**
	 * 获取禁卖建筑指针图片
	 */
	public static MouseCursorObject getNoSellCursorImage() {
		ShapeUnitFrame suf = mouseShapeFrames.get(149);
		suf.setShouldBeLoadedToGpu(true);
		mouseObj.setMouse(suf);
		mouseObj.setOffX(27);
		mouseObj.setOffY(21);
		return mouseObj;
	}
	
}

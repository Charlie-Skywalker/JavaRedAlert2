package redAlert;

import java.awt.image.BufferedImage;
import java.util.List;

import lombok.Data;
import redAlert.utilBean.ColorPoint;
import redAlert.utils.CanvasPainter;

/**
 * 表示一帧shp画面
 * 仅仅表示一帧
 * 这个类是用来绘制画面的
 * 
 * 需要优化此类,图片中阵营颜色应该可以改变,增加一个表示颜色的集合,渲染时使用此集合来设定颜色
 * 这样在一个SHP加载完成后,再次建造此单位,不需要重复从硬盘中加载此资源
 */
@Data
public class ShapeUnitFrame {

	private BufferedImage img;//图片
	private int minY;//表示建筑图形中非透明像素点在图形中的Y坐标最小值
	private int minX;//表示建筑图形中非透明像素点在图形中的X坐标最小值
	private int maxY;//表示建筑图形中非透明像素点在图形中的Y坐标最大值
	private int maxX;//表示建筑图形中非透明像素点在图形中的X坐标最大值
	private int realPartWidth;//有效宽度   动态图是几个shp文件拼起来的,鉴于这个变量目前没什么用  就先不计算它了  挺麻烦的
	private int realPartHeight;//有效高度
	
	private List<ColorPoint> colorPointList;//含有队伍颜色信息的像素坐标
	
	public ShapeUnitFrame() {
		
	}
	
	public ShapeUnitFrame(BufferedImage img,int positionX,int positionY,int minX,int minY,int realPartWidth,int realPartHeight){
		
	}
	
	/**
	 * 拷贝一份
	 * 这个方法用在建造动画的复制
	 */
	public ShapeUnitFrame copy() {
		ShapeUnitFrame frame = new ShapeUnitFrame();
		frame.setImg(CanvasPainter.copyImage(this.img));//图片需要深拷贝
		frame.setMinX(minX);
		frame.setMinY(minY);
		frame.setMaxX(maxX);
		frame.setMaxY(maxY);
		frame.setRealPartHeight(realPartHeight);
		frame.setRealPartHeight(realPartHeight);
		frame.setColorPointList(colorPointList);//这个对象不会变,所以浅拷贝也可以
		return frame;
	}
	
	public ShapeUnitFrame(int minX,int minY) {
		this.minX = minX;
		this.minY = minY;
	}

}

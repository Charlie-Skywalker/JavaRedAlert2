package redAlert.shapeObjects;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.builder.CompareToBuilder;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.ColorPoint;
import redAlert.utils.CanvasPainter;
import redAlert.utils.RandomUtil;

/**
 * 表示一个绘制方块
 * 
 * 绘制方块可以是建筑、人物、静态物、载具等任何可以在游戏主界面中绘制的东西
 */
public abstract class ShapeUnit implements Comparable<ShapeUnit>{

	public ShapeUnit() {
		this.unitNo = RandomUtil.newUnitNo();
	}
	
	/**
	 * 绘制优先级
	 *   该值越小越优先，且优先使用此变量判定优先级
	 */
	public int priority = 50;
	/**
	 * 图形矩形左上顶点在主画板上的X坐标和Y坐标
	 *   该值用来确认方块的绘制位置
	 */
	public int positionX,positionY;
	/**
	 * 地理放置中心偏离图片左上角positionX,positionY的距离
	 * positionX+centerOffX是建筑的菱形中心点横坐标
	 * positionY+centerOffY是建筑的菱形中心点纵坐标
	 */
	public int centerOffX,centerOffY;
	/**
	 * 方块图片中非透明像素点在主画板中的X坐标、Y坐标最小值
	 *   该值的主要作用是确认建筑摆放的优先级,positionMinY值越小越优先,然后是positionMinX越小越优先
	 *   该值在调用calculateNextFrame方法时会动态计算,根据positionX、positionY和ShapeUnitFrame对象的minX、maxY相加而得
	 */
	public int positionMinX,positionMinY;
	/**
	 * 当前帧，实现动画效果的核心变量
	 *    变量中含有一个图片，界面渲染时绘制的就是这个图片
	 */
	public ShapeUnitFrame curFrame;//当前帧   画板拿这个来绘制内容
	/**
	 * 移除标志,当移除标志为true,规划线程将不处理这个单位,这个单位会退出规划-绘制循环，从而丢失被GC处理
	 */
	public volatile boolean end = false;
	/**
	 * 单位名称
	 * 当鼠标停在单位上后,将以tooltip的形式展示名称
	 */
	public String unitName;
	/**
	 * 单位唯一编号
	 * 将此编号唯一,可以提高判断的速度
	 * 应该在构造函数中初始化
	 */
	public int unitNo;
	/**
	 * 阵营颜色
	 * null表示不含有阵营颜色信息
	 * 为什么ShapeUnit有颜色属性?为什么不安排在Building中设置此参数
	 * 比如核弹发射井的核弹图片,不是建筑,但是有不同的颜色
	 */
	public UnitColor unitColor;
	/**
	 * 放慢倍数
	 * 原始帧率是60帧/秒   则建筑动画的帧率是15帧/秒,则设置frameSpeed=4
	 */
	public int frameSpeed = 1;
	/**
	 * 渲染帧计数
	 * 也就是调用calculateNextFrame方法次数
	 */
	public int frameNum = 1;
	/**
	 * 是否被其他物体遮挡
	 * 
	 * 涉及到显示先后顺序的重要变量
	 */
	public boolean isHided = false;
	/**
	 * 渲染时是否显示
	 */
	public boolean isVisible = true;
	/**
	 * 当前所在中心块坐标
	 * 对军事建筑而言: 表示建造时预占用中心块的中心点
	 * 对载具而言：表示载具中心点所在中心块的中心点
	 * 对步兵而言：表示步兵中心点所在中心块的中心点
	 * 注意：这个点对于移动单位来说，只是它所在的中心点的坐标，不是它实时的脚下坐标
	 * 实时坐标应该使用 positionX+centerOffX  positionY+centerOffY 计算
	 */
	public CenterPoint curCenterPoint;
	
	
	/**
	 * 计算下一帧画面
	 * 当方块的一帧画面展示完毕后,帧计算线程会调用此方法更改curFrame变量,计算出下一帧应展示的画面
	 */
	public abstract void calculateNextFrame();
	
	/**
	 * 上阵营色
	 * 注意,此处的image可能是frame的成员变量，也可能不是
	 */
	public void giveFrameUnitColor(BufferedImage image,ShapeUnitFrame frame){
		//赋予阵营颜色
		List <ColorPoint> colorPointLs = frame.getColorPointList();
		if(colorPointLs!=null && !colorPointLs.isEmpty()) {
			for(ColorPoint cp:colorPointLs) {
				int oriColor = image.getRGB(cp.getX(), cp.getY());
				image.setRGB(cp.getX(), cp.getY(), CanvasPainter.transColor(oriColor,this.unitColor));
			}
		}
	}
	
	
	
	
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getPositionX() {
		return positionX;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	public int getPositionMinX() {
		return positionMinX;
	}
	public void setPositionMinX(int positionMinX) {
		this.positionMinX = positionMinX;
	}
	public int getPositionMinY() {
		return positionMinY;
	}
	public void setPositionMinY(int positionMinY) {
		this.positionMinY = positionMinY;
	}
	public ShapeUnitFrame getCurFrame() {
		return curFrame;
	}
	public void setCurFrame(ShapeUnitFrame curFrame) {
		this.curFrame = curFrame;
	}
	public boolean isEnd() {
		return end;
	}
	public void setEnd(boolean end) {
		this.end = end;
	}
	public UnitColor getUnitColor() {
		return unitColor;
	}
	public void setUnitColor(UnitColor unitColor) {
		this.unitColor = unitColor;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public int getUnitNo() {
		return unitNo;
	}
	public void setUnitNo(int unitNo) {
		this.unitNo = unitNo;
	}
	public int getCenterOffX() {
		return centerOffX;
	}
	public void setCenterOffX(int centerOffX) {
		this.centerOffX = centerOffX;
	}
	public int getCenterOffY() {
		return centerOffY;
	}
	public void setCenterOffY(int centerOffY) {
		this.centerOffY = centerOffY;
	}
	public int getFrameSpeed() {
		return frameSpeed;
	}
	public void setFrameSpeed(int frameSpeed) {
		this.frameSpeed = frameSpeed;
	}
	public int getFrameNum() {
		return frameNum;
	}
	public void setFrameNum(int frameNum) {
		this.frameNum = frameNum;
	}
	public boolean isHided() {
		return isHided;
	}
	public void setHided(boolean isHided) {
		this.isHided = isHided;
	}
	public boolean isVisible() {
		return isVisible;
	}
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	public CenterPoint getCurCenterPoint() {
		return curCenterPoint;
	}
	public void setCurCenterPoint(CenterPoint curCenterPoint) {
		this.curCenterPoint = curCenterPoint;
	}

	/**
	 * 由于优先级比较
	 * 先比较优先级,值越小的优先级越高，值越大的优先级越高
	 * 优先级相同则minY越小优先级越高  
	 * minY相同则minX越小优先级越高
	 */
	@Override
	public int compareTo(ShapeUnit o) {
		if(o.priority!=this.priority) {
			if(o.priority<this.priority) {
				return 1;//大于0表示this优先级低,渲染顺序靠后
			}else {
				return -1;
			}
		}else {
			if(o.isHided && !this.isHided ) {
				return 1;
			}else if(!o.isHided && this.isHided) {
				return -1;
			}else {
				int thisCpX = positionX+centerOffX;
				int thisCpY = positionY+centerOffY;
				int oCpX = o.positionX+o.centerOffX;
				int oCpY = o.positionY+o.centerOffY;
				
				if(thisCpY>oCpY) {
					return 1;
				}else if(thisCpY==oCpY) {
					if(thisCpX>oCpX) {
						return 1;
					}else if(thisCpX==oCpX) {
//						return 0;
						
						//主要解决载具在维修厂上的显示顺序问题
						if(o.positionMinY<this.positionMinY) {
							return 1;//大于0表示this优先级低
						}else if(o.positionMinY==this.positionMinY){
							if(o.positionMinX<this.positionMinX) {
								return 1;
							}else if(o.positionMinX==this.positionMinX) {
								return 0;
							}else {
								return -1;
							}
						}else {
							return -1;
						}
						
						
					}else {
						return -1;
					}
				}else {
					return -1;
				}
				
				
			}
		}
	}



	@Override
	public int hashCode() {
		return Objects.hash(curFrame, positionMinX, positionMinY, positionX, positionY, priority);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ShapeUnit other = (ShapeUnit) obj;
		return Objects.equals(curFrame, other.curFrame) && positionMinX == other.positionMinX
				&& positionMinY == other.positionMinY && positionX == other.positionX && positionY == other.positionY
				&& priority == other.priority;
	}

	@Override
	public String toString() {
		return "ShapeUnit [unitName=" + unitName + ", unitNo=" + unitNo + "]";
	}
	
	
}

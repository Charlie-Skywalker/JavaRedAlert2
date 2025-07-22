package redAlert.tabIcon.tab01;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.tabIcon.Tab01ConstIcon;
import redAlert.utils.CanvasPainter;

/**
 * 铁幕
 */
public class SfIrcrBtn extends Tab01ConstIcon {
	private static final long serialVersionUID = 1L;
	
	public SfIrcrBtn() {
		List<ShapeUnitFrame> list = ShpResourceCenter.loadShpResource("ircricon", "cameo", false);
		iconImage = list.get(0).getImg();
		
		/*
		 * 四个角涂黑，伪造圆角矩形
		 */
		iconImage.setRGB(0, 0, Color.black.getRGB());
		iconImage.setRGB(1, 0, Color.black.getRGB());
		iconImage.setRGB(0, 1, Color.black.getRGB());
		
		iconImage.setRGB(59, 0, Color.black.getRGB());
		iconImage.setRGB(58, 0, Color.black.getRGB());
		iconImage.setRGB(59, 1, Color.black.getRGB());
		
		iconImage.setRGB(0, 47, Color.black.getRGB());
		iconImage.setRGB(0, 46, Color.black.getRGB());
		iconImage.setRGB(1, 47, Color.black.getRGB());
		
		iconImage.setRGB(59, 47, Color.black.getRGB());
		iconImage.setRGB(59, 46, Color.black.getRGB());
		iconImage.setRGB(58, 47, Color.black.getRGB());
		
		this.setOpaque(true);//JLabel默认是透明的
		this.setSize(60, 48);
		this.setIcon( new ImageIcon(iconImage));
		this.setToolTipText("<html>"
				+ "<b>"
				+ "<div>"
				+ "<span>铁幕</span>"
				+ "</div>"
				+ "</b></html>");
		
		
		iconCanvas = new BufferedImage(iconImage.getWidth(),iconImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		myself = this;
		
		//伞兵默认生产中
		super.status = STATUS_USING;
		
		/**
		 * 添加点击事件
		 */
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1) {//左键
					/*
					 * 正常状态下点击
					 */
					if(status==STATUS_IDLE) {
						
					}
					/**
					 * 建造ing状态下点击
					 */
					if(status==STATUS_USING) {
						
					}
					/*
					 * 就绪状态下点击
					 */
					if(status==STATUS_READY) {
						
					}
					/**
					 * 保持或禁用状态
					 */
					if(status==STATUS_KEEP || status==STATUS_BAN) {
						
					}
					/**
					 * 暂停状态下点击
					 */
					if(status==STATUS_ON_HOLD) {
						
					}
					
				}
				if(e.getButton()==MouseEvent.BUTTON2) {//中键
				}
				if(e.getButton()==MouseEvent.BUTTON3) {//右键
					/**
					 * 建造ing状态下右键,切为暂停状态
					 */
					if(status==STATUS_USING) {
						
					}
					/**
					 * 暂停状态下右键,取消建造
					 */
					if(status==STATUS_ON_HOLD) {
						
					}
					/**
					 * 就绪状态下右键,取消建造
					 */
					if(status==STATUS_READY) {
						
					}
					
				}
			}
			
		});
		
	}
	
	/**
	 * 计时器
	 */
	public int clock = 0;
	/**
	 * 灰色蒙版颜色
	 */
	public Color grayMaskColor = new Color(0x1c,0xbc,0xe8,80);
	/**
	 * 
	 */
	public Color color0 = new Color(0,0,0,0);
	/**
	 * 每多少帧,建造进度进一格
	 * 这个值越大,建造越慢
	 */
	public int flushFrameNum = 5;
	/**
	 * 蒙板Image
	 */
	public BufferedImage grayMaskImage = new BufferedImage(60,48,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 表示渲染是否已经做了
	 * 避免重复刷界面,减少资源消耗
	 */
	public boolean isReadyFlag = false;
	public boolean isOnHoldFlag = false;
	public boolean isBanFlag = false;
	
	/**
	 * 初始化计时器
	 */
	public void initTimer() {
		Timer timer = new Timer();
		TimerTask refreshTask = new TimerTask() {
			@Override
			public void run() {
				
				//建造ing  倒计时
				if(status==STATUS_USING) {
					//清空画板
					CanvasPainter.clearImage(iconCanvas);
					//画上原图
					Graphics2D g2d = iconCanvas.createGraphics();
					g2d.drawImage(iconImage, 0, 0 , null);
					
					//准备一个半透明灰色蒙板
					CanvasPainter.clearImage(grayMaskImage);
					Graphics2D g2d2 = grayMaskImage.createGraphics();
					g2d2.setColor(grayMaskColor);
					g2d2.fillRect(0, 0, 60, 48);
					
					int index = clock/flushFrameNum;
					//根据时间   计算第三点坐标
					if(index==0) {//第一帧让灰色蒙版覆盖整个Icon
						g2d.drawImage(grayMaskImage, 0, 0 , null);
					}else {
						g2d2.setColor(color0);
						g2d2.setComposite(AlphaComposite.Clear);
						if(index>=0 && index<=5) {
							int [] x = {30,30,xarry[index]};
							int [] y = {24,0,yarry[index]};
							Polygon p = new Polygon(x, y, 3);
							g2d2.fillPolygon(p);
							g2d.drawImage(grayMaskImage, 0, 0 , null);
						}
						
						if(index>=6 && index<=17) {
							int [] x = {30,30,59,xarry[index]};
							int [] y = {24,0,0,yarry[index]};
							Polygon p = new Polygon(x, y, 4);
							g2d2.fillPolygon(p);
							g2d.drawImage(grayMaskImage, 0, 0 , null);
						}
						
						if(index>=18 && index<=29) {
							int [] x = {30,30,59,59,xarry[index]};
							int [] y = {24,0,0,47,yarry[index]};
							Polygon p = new Polygon(x, y, 5);
							g2d2.fillPolygon(p);
							g2d.drawImage(grayMaskImage, 0, 0 , null);
						}
						
						if(index>=30 && index<=41) {
							int [] x = {30,30,59,59,0,xarry[index]};
							int [] y = {24,0,0,47,47,yarry[index]};
							Polygon p = new Polygon(x, y, 6);
							g2d2.fillPolygon(p);
							g2d.drawImage(grayMaskImage, 0, 0 , null);
						}
						
						if(index>=42 && index<=46) {
							int [] x = {30,30,59,59,0,0,xarry[index]};
							int [] y = {24,0,0,47,47,0,yarry[index]};
							Polygon p = new Polygon(x, y, 7);
							g2d2.fillPolygon(p);
							g2d.drawImage(grayMaskImage, 0, 0 , null);
						}
					}
					
					ImageIcon ii = (ImageIcon)myself.getIcon();
					ii.setImage(iconCanvas);
					myself.setIcon(ii);
					myself.repaint();
					
					clock++;
					
					if(index>47) {//倒计时图像一共48帧,结束后切换就绪状态
						status = STATUS_READY;
						isReadyFlag = false;
						Constructor.playOneMusic("ceva048");//Construction complete
						clock = 0;
					}
					
					return;
				}
				
				if(status==STATUS_ON_HOLD) {
					if(!isOnHoldFlag) {
						//清空画板
						CanvasPainter.clearImage(iconCanvas);
						//画上原图
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						
						//准备一个半透明灰色蒙版
						BufferedImage myClock = new BufferedImage(60,48,BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2d2 = myClock.createGraphics();
						g2d2.setColor(new Color(0x1c,0xbc,0xe8,80));
						g2d2.fillRect(0, 0, 60, 48);
						
						int index = clock/3;//每3帧刷新一次
						//根据时间
						if(index==0) {//第一帧全灰
							g2d.drawImage(myClock, 0, 0 , null);
						}else {
							g2d2.setColor(new Color(0,0,0,0));
							g2d2.setComposite(AlphaComposite.Clear);
							if(index>=0 && index<=5) {
								int [] x = {30,30,xarry[index]};
								int [] y = {24,0,yarry[index]};
								Polygon p = new Polygon(x, y, 3);
								g2d2.fillPolygon(p);
								g2d.drawImage(myClock, 0, 0 , null);
							}
							
							if(index>=6 && index<=17) {
								int [] x = {30,30,59,xarry[index]};
								int [] y = {24,0,0,yarry[index]};
								Polygon p = new Polygon(x, y, 4);
								g2d2.fillPolygon(p);
								g2d.drawImage(myClock, 0, 0 , null);
							}
							
							if(index>=18 && index<=29) {
								int [] x = {30,30,59,59,xarry[index]};
								int [] y = {24,0,0,47,yarry[index]};
								Polygon p = new Polygon(x, y, 5);
								g2d2.fillPolygon(p);
								g2d.drawImage(myClock, 0, 0 , null);
							}
							
							if(index>=30 && index<=41) {
								int [] x = {30,30,59,59,0,xarry[index]};
								int [] y = {24,0,0,47,47,yarry[index]};
								Polygon p = new Polygon(x, y, 6);
								g2d2.fillPolygon(p);
								g2d.drawImage(myClock, 0, 0 , null);
							}
							
							if(index>=42 && index<=46) {
								int [] x = {30,30,59,59,0,0,xarry[index]};
								int [] y = {24,0,0,47,47,0,yarry[index]};
								Polygon p = new Polygon(x, y, 7);
								g2d2.fillPolygon(p);
								g2d.drawImage(myClock, 0, 0 , null);
							}
						}
						
						BufferedImage dengdai = new BufferedImage(40,20,BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2d3 = dengdai.createGraphics();
						g2d3.setColor(new Color(20,20,20,240));
						g2d3.fillRect(0, 0, 36, 20);
						g2d3.setColor(new Color(165,211,255));
						g2d3.setFont(new Font("宋体", Font.PLAIN, 18)); // 设置字体样式
						g2d3.drawString("等待", 0, 15);
						g2d.drawImage(dengdai, 12, 0 , null);
						
						Tab01ConstIcon btn = myself;
						ImageIcon ii = (ImageIcon)btn.getIcon();
						ii.setImage(iconCanvas);
						btn.setIcon(ii);
						btn.repaint();
						isOnHoldFlag = true;
						return;
					}
					
				}
				
				
				//就绪
				if(status==STATUS_READY) {
					if(!isReadyFlag) {
						CanvasPainter.clearImage(iconCanvas);
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						
						BufferedImage jiuxu = new BufferedImage(40,20,BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2d2 = jiuxu.createGraphics();
						g2d2.setColor(new Color(20,20,20,240));
						g2d2.fillRect(0, 0, 36, 20);
						g2d2.setColor(new Color(165,211,255));
						g2d2.setFont(new Font("宋体", Font.PLAIN, 18)); // 设置字体样式
						g2d2.drawString("就绪", 0, 15);
						
						g2d.drawImage(jiuxu, 12, 0 , null);
						
						Tab01ConstIcon btn = myself;
						ImageIcon ii = (ImageIcon)btn.getIcon();
						ii.setImage(iconCanvas);
						btn.setIcon(ii);
						btn.repaint();
						isReadyFlag = true;
						return;
					}
					
				}
				
				
				//禁用状态
				if(status==Tab01ConstIcon.STATUS_BAN) {
					if(!isBanFlag) {
						CanvasPainter.clearImage(iconCanvas);
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						
						BufferedImage myClock = new BufferedImage(60,48,BufferedImage.TYPE_INT_ARGB);
						Graphics2D g2d2 = myClock.createGraphics();
						g2d2.setColor(new Color(20,20,20,120));
						g2d2.fillRect(0, 0, 60, 48);
						
						g2d.drawImage(myClock, 0, 0 , null);
						
						Tab01ConstIcon btn = myself;
						ImageIcon ii = (ImageIcon)btn.getIcon();
						ii.setImage(iconCanvas);
						btn.setIcon(ii);
						btn.repaint();
						
						status=STATUS_KEEP;
						isBanFlag = true;
						return;
					}
				}
				
				//返回最初状态
				if(status==Tab01ConstIcon.STATUS_TEMP) {
					CanvasPainter.clearImage(iconCanvas);
					Graphics2D g2d = iconCanvas.createGraphics();
					g2d.drawImage(iconImage, 0, 0 , null);
					Tab01ConstIcon btn = myself;
					ImageIcon ii = (ImageIcon)btn.getIcon();
					ii.setImage(iconCanvas);
					btn.setIcon(ii);
					btn.repaint();
					status=STATUS_IDLE;
					clock=0;
					isReadyFlag = false;
					isOnHoldFlag = false;
					isBanFlag = false;
				}
				
				
				
			}
		};
		timer.schedule(refreshTask, 1L, 17);
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
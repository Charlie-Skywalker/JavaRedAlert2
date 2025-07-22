package redAlert.tabIcon;

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
import javax.swing.JLabel;

import redAlert.Constructor;
import redAlert.ShapeUnitFrame;
import redAlert.enums.VehicleEnum;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.utils.CanvasPainter;

/**
 * 载具图标父类
 */
public class Tab03ConstIcon extends JLabel{
	
	private static final long serialVersionUID = 1L;
	public VehicleEnum vehicleInfo = null;
	
	public final static int STATUS_IDLE = 0;//正常
	public final static int STATUS_TRAINING = 1;//生产载具中  并且在倒计时
	public final static int STATUS_TRAINING_WAIT = 2;//生产等待 右上方会有一个数字
	public final static int STATUS_TEMP = 3;//中间临时状态,一般建造完成后置为此状态
	public final static int STATUS_BAN = 4;//禁用状态  点击无效  黑色框
	public final static int STATUS_KEEP = 5;//保持状态   不做任何渲染
	public final static int STATUS_ON_HOLD = 6;//暂停   不做任何渲染
	/**
	 * 图标状态
	 */
	public int status = STATUS_IDLE;
	
	//建造中转动坐标集合,一共47个
	public static int [] xarry = {35,40,45,50,55,59,  59,59,59,59,59,59,  59,59,59,59,59,59  ,59,55,50,45,40,35, 30,25,20,15,10,5,  0,0,0,0,0,0, 0,0,0,0,0,0, 5,10,15,20,25};
	public static int [] yarry = {0,0,0,0,0,0,  3,7,11,15,19,23,  27,31,35,39,43,47,  47,47,47,47,47,47,  47,47,47,47,47,47,  43,39,35,31,27,23,  19,15,11,7,3,0,  0,0,0,0,0};
	
	/**
	 * 原始图标图片
	 */
	public BufferedImage iconImage;
	/**
	 * 加工画板,用于展示后期效果
	 */
	public BufferedImage iconCanvas;
	/**
	 * 自己的引用,给任务内部类使用
	 */
	public Tab03ConstIcon myself;
	
	/**
	 * 构造方法
	 */
	public Tab03ConstIcon(VehicleEnum vehicleInfo) {
		this.vehicleInfo = vehicleInfo;
		List<ShapeUnitFrame> list = ShpResourceCenter.loadShpResource(vehicleInfo.shpIconPrefix, "cameo", false);
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
				+ "<span>"+vehicleInfo.vehicleName+"</span><br/>"
				+ "<span>$"+vehicleInfo.price+"</span>"
				+ "</div>"
				+ "</b></html>");
		
		
		iconCanvas = new BufferedImage(iconImage.getWidth(),iconImage.getHeight(),BufferedImage.TYPE_INT_ARGB);
		
		myself = this;
		
		/**
		 * 添加点击事件
		 */
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1) {//左键
					/*
					 * 载具生产的队列、图标的状态  统一由管理器管理
					 */
					if(status==STATUS_IDLE) {
						if(Tab03Manager.trainList.isEmpty()) {
							Constructor.playOneMusic("ceva052");//Building
						}
						Tab03Manager.addTrainPlan(vehicleInfo);
						return;
					}
					
					if(status==STATUS_TRAINING) {
						Tab03Manager.addTrainPlan(vehicleInfo);
						return;
					}
					
					if(status==STATUS_TRAINING_WAIT) {
						Tab03Manager.addTrainPlan(vehicleInfo);
						return;
					}
					if(status==STATUS_ON_HOLD) {//暂停后恢复生产
						status = STATUS_TRAINING;
						Constructor.playOneMusic("ceva052");//Building
						isOnHoldFlag = false;
						return;
					}
				}
				
				if(e.getButton()==MouseEvent.BUTTON3) {//右键
					if(status==STATUS_TRAINING) {//生产转暂停
						Constructor.playOneMusic("ceva056");//on hold
						status = STATUS_ON_HOLD;
						return;
					}
					
					if(status==STATUS_ON_HOLD) {//暂停转取消
						Tab03Manager.reduceTrainPlan(vehicleInfo);
						Constructor.playOneMusic("ceva051");//cancel
						isOnHoldFlag = false;
						return;
					}
					
					if(status==STATUS_TRAINING_WAIT) {//排队转取消
						Tab03Manager.reduceWaitingTrainPlan(vehicleInfo);
						return;
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
	public int flushFrameNum = 1;
	/**
	 * 蒙板Image
	 */
	public BufferedImage grayMaskImage = new BufferedImage(60,48,BufferedImage.TYPE_INT_ARGB);
	/**
	 * 数字
	 */
	public BufferedImage numberImage = new BufferedImage(16,20,BufferedImage.TYPE_INT_ARGB);
	
	/**
	 * 表示渲染是否已经做了
	 * 避免重复刷界面,减少资源消耗
	 */
	public boolean isReadyFlag = false;
	public boolean isOnHoldFlag = false;
	public boolean isBanFlag = false;
	
	public Timer timer = null;
	public boolean isTimerStart = false;
	/**
	 * 结束计时器
	 */
	public void destroyTimer() {
		if(timer!=null) {
			timer.cancel();
			timer = null;
			isTimerStart = false;
		}
		clock = 0;
		isReadyFlag = false;
		isOnHoldFlag = false;
		isBanFlag = false;
		status = STATUS_IDLE;
	}
	
	/**
	 * 初始化计时器
	 */
	public void initTimer() {
		if(!isTimerStart) {
			timer = new Timer();
			isTimerStart = true;
			TimerTask refreshTask = new TimerTask() {
				@Override
				public void run() {
					
					//生产ing  倒计时
					if(status==STATUS_TRAINING) {
						//清空画板
						CanvasPainter.clearImage(iconCanvas);
						//画上原图
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						
						//判断是否需要画数字
						int num = Tab03Manager.countNum(vehicleInfo);
						if(num>1) {
							if(num<10) {
								Graphics2D numG = numberImage.createGraphics();
								numG.setColor(new Color(20,20,20,240));
								numG.fillRect(8, 0, 8, 20);
								numG.setColor(new Color(165,211,255));
								numG.setFont(new Font("黑体", Font.BOLD, 12)); // 设置字体样式
								numG.drawString(num+"", 8, 15);
								g2d.drawImage(numberImage, 44, 0 , null);
							}else {
								Graphics2D numG = numberImage.createGraphics();
								numG.setColor(new Color(20,20,20,240));
								numG.fillRect(0, 0, 16, 20);
								numG.setColor(new Color(165,211,255));
								numG.setFont(new Font("黑体", Font.BOLD, 12)); // 设置字体样式
								numG.drawString(num+"", 0, 15);
								g2d.drawImage(numberImage, 44, 0 , null);
							}
						}else {
							
						}
						
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
							clock = 0;
							Tab03Manager.next();
							makeOneVehicle();
							Constructor.playOneMusic("ceva062");// unit ready
						}
						
						return;
					}
					
					//就绪
					if(status==STATUS_TRAINING_WAIT) {
						//清空画板
						CanvasPainter.clearImage(iconCanvas);
						//画上原图
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						
						//判断是否需要画数字
						int num = Tab03Manager.countNum(vehicleInfo);
						if(num>=1) {
							if(num<10) {
								Graphics2D numG = numberImage.createGraphics();
								numG.setColor(new Color(20,20,20,240));
								numG.fillRect(8, 0, 8, 20);
								numG.setColor(new Color(165,211,255));
								numG.setFont(new Font("黑体", Font.BOLD, 12)); // 设置字体样式
								numG.drawString(num+"", 8, 15);
								g2d.drawImage(numberImage, 44, 0 , null);
							}else {
								Graphics2D numG = numberImage.createGraphics();
								numG.setColor(new Color(20,20,20,240));
								numG.fillRect(0, 0, 16, 20);
								numG.setColor(new Color(165,211,255));
								numG.setFont(new Font("黑体", Font.BOLD, 12)); // 设置字体样式
								numG.drawString(num+"", 0, 15);
								g2d.drawImage(numberImage, 44, 0 , null);
							}
						}else {
							
						}
							
						ImageIcon ii = (ImageIcon)myself.getIcon();
						ii.setImage(iconCanvas);
						myself.setIcon(ii);
						myself.repaint();
						return;
					}
					
					//暂停等待
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
							
							ImageIcon ii = (ImageIcon)myself.getIcon();
							ii.setImage(iconCanvas);
							myself.setIcon(ii);
							myself.repaint();
							isOnHoldFlag = true;
							return;
						}
						
					}
					
					
					
					
					
					//禁用状态
					if(status==Tab00ConstIcon.STATUS_BAN) {
						if(!isBanFlag) {
							CanvasPainter.clearImage(iconCanvas);
							Graphics2D g2d = iconCanvas.createGraphics();
							g2d.drawImage(iconImage, 0, 0 , null);
							
							BufferedImage myClock = new BufferedImage(60,48,BufferedImage.TYPE_INT_ARGB);
							Graphics2D g2d2 = myClock.createGraphics();
							g2d2.setColor(new Color(20,20,20,120));
							g2d2.fillRect(0, 0, 60, 48);
							
							g2d.drawImage(myClock, 0, 0 , null);
							
							ImageIcon ii = (ImageIcon)myself.getIcon();
							ii.setImage(iconCanvas);
							myself.setIcon(ii);
							myself.repaint();
							
							status=STATUS_KEEP;
							isBanFlag = true;
							return;
						}
					}
					
					//返回最初状态
					if(status==Tab00ConstIcon.STATUS_TEMP) {
						CanvasPainter.clearImage(iconCanvas);
						Graphics2D g2d = iconCanvas.createGraphics();
						g2d.drawImage(iconImage, 0, 0 , null);
						ImageIcon ii = (ImageIcon)myself.getIcon();
						ii.setImage(iconCanvas);
						myself.setIcon(ii);
						myself.repaint();
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
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public boolean isDisplay() {
		return false;
	}
	public void makeOneVehicle() {
		
	}
}

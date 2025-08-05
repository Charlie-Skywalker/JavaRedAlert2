package redAlert.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.Constructor;
import redAlert.OptionsPanel;
import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 雷达图
 */
public class RadarLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	
	
	
	public List<BufferedImage> radars = new ArrayList<>();
	public BufferedImage radar = null;
	
	public boolean isPlayed = false;
	
	public RadarLabel() {
		List<ShapeUnitFrame> radarFrame = ShpResourceCenter.loadShpResource("radar", "sidebar", false);
		for(ShapeUnitFrame suf : radarFrame) {
			radars.add(suf.getImg());
		}
		radar = radarFrame.get(0).getImg();
		
		this.setBounds(0,0,radar.getWidth(), radar.getHeight());
		Icon radarIcon = new ImageIcon(radar);
		this.setOpaque(true);
		this.setIcon(radarIcon);
	}
	
	/**
	 * 触发雷达图显示事件
	 */
	public void triggleRadarShow() {
		
		if(!isPlayed) {
			Thread t = new Thread() {
				public void run() {
					try {
						List<BufferedImage> radars = OptionsPanel.radarLabel.radars;
						for(int i=0;i<radars.size()+1;i++) {
							if(i==radars.size()){
//								BufferedImage img2 = radars.get(i-1);
//								Graphics2D g2d = img2.createGraphics();
//								BufferedImage yijian = ImageIO.read(new File("E:/一箭三蓝.png"));
//								g2d.drawImage(yijian, 14, 0, null);
//								Icon radarIcon = new ImageIcon(img2);
//								OptionsPanel.radarLabel.setIcon(radarIcon);
//								OptionsPanel.radarLabel.repaint();
							}else {
								BufferedImage img = radars.get(i);
								Icon radarIcon = new ImageIcon(img);
								OptionsPanel.radarLabel.setIcon(radarIcon);
								OptionsPanel.radarLabel.repaint();
								Thread.sleep(50);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
			Constructor.playOneMusic("uradaron");
			isPlayed = true;
		}
	}
	
	/**
	 * 关闭雷达的显示屏
	 */
	public void turnOffRadarShow() {
		if(isPlayed) {
			Thread t = new Thread() {
				public void run() {
					try {
						List<BufferedImage> radars = OptionsPanel.radarLabel.radars;
						for(int i=radars.size()-1;i>0;i--) {
							if(i==radars.size()){
//								BufferedImage img2 = radars.get(i-1);
//								Graphics2D g2d = img2.createGraphics();
//								BufferedImage yijian = ImageIO.read(new File("E:/一箭三蓝.png"));
//								g2d.drawImage(yijian, 14, 0, null);
//								Icon radarIcon = new ImageIcon(img2);
//								OptionsPanel.radarLabel.setIcon(radarIcon);
//								OptionsPanel.radarLabel.repaint();
							}else {
								BufferedImage img = radars.get(i);
								Icon radarIcon = new ImageIcon(img);
								OptionsPanel.radarLabel.setIcon(radarIcon);
								OptionsPanel.radarLabel.repaint();
								Thread.sleep(50);
							}
						}
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			t.start();
			Constructor.playOneMusic("uradarof");
			isPlayed = false;
		}
	}
}

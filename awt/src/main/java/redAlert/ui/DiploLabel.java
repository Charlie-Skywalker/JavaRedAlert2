package redAlert.ui;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 顶端左按钮
 */
public class DiploLabel extends JLabel{
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage img;
	private BufferedImage img2;
	
	public DiploLabel() {
		List<ShapeUnitFrame> topFrame = ShpResourceCenter.loadShpResource("diplobtn", "sidebar", false);
		img = topFrame.get(0).getImg();
		img2 = topFrame.get(1).getImg();
		this.setBounds(12,5,img.getWidth(), img.getHeight());
		Icon diplobIcon = new ImageIcon(img);
		this.setOpaque(true);
		this.setIcon(diplobIcon);
	}
	
}

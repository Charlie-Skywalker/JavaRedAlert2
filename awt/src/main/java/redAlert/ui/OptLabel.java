package redAlert.ui;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 顶端右按钮
 */
public class OptLabel extends JLabel{
	
	private static final long serialVersionUID = 1L;
	
	private BufferedImage img;
	private BufferedImage img2;
	
	public OptLabel() {
		List<ShapeUnitFrame> optFrame = ShpResourceCenter.loadShpResource("optbtn", "sidebar", false);
		img = optFrame.get(0).getImg();
		img2 = optFrame.get(1).getImg();
		this.setBounds(12+72,5,img.getWidth(), img.getHeight());
		Icon optIcon = new ImageIcon(img);
		this.setOpaque(true);
		this.setIcon(optIcon);
	}
}

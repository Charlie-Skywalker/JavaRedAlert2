package redAlert.test;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PanelB extends JPanel{

	public volatile BufferedImage bufferedImage = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB); 
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		super.paint(g);
		g.drawImage(bufferedImage, 0, 0,bufferedImage.getWidth(), bufferedImage.getHeight(), this);
//		super.paintComponent(g);
	}
}

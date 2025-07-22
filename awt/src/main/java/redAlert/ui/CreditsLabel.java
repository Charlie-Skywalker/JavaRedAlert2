package redAlert.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 金钱显示框
 */
public class CreditsLabel extends JLabel{

	private static final long serialVersionUID = 1L;
	
	private BufferedImage img;
	
	public CreditsLabel() {
		List<ShapeUnitFrame> creditsFrame = ShpResourceCenter.loadShpResource("credits", "sidebar", false);
		img = creditsFrame.get(0).getImg();
		Icon creditIcon = new ImageIcon(img);
		this.setOpaque(true);
		this.setIcon(creditIcon);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setHorizontalTextPosition(JLabel.CENTER);
		this.setForeground(new Color(186,230,233));
		this.setText("114514");
		this.setFont(new Font("宋体", Font.BOLD, 12));
	}
}

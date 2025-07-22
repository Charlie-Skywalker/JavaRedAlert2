package redAlert;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JToolTip;

public class CustomToolTip extends JToolTip{
	 @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // 自定义绘制逻辑，例如改变背景色和字体
	        g.setColor(Color.black); // 设置背景色为蓝色
	        g.fillRect(0, 0, 60, 48); // 填充背景色
	        g.setColor(new Color(186,230,233));
	        g.drawRect(0, 0, 60, 48);
	        g.drawRect(1, 1, 57, 45);
	        
	        g.setColor(new Color(186,230,233)); // 设置字体颜色为白色
	        g.setFont(new Font("宋体", Font.PLAIN, 15)); // 设置字体样式
//	        g.drawString(getTipText(), 4, 13); // 绘制文本
	        
	        String a = getTipText();
	        
	        String b [] = a.split("@");
	        g.drawString(b[0], 4, 13); // 绘制文本
	        g.setFont(new Font("宋体", Font.PLAIN, 12)); // 设置字体样式
	        g.drawString(b[1], 4, 26); // 绘制文本
	        
	        
	        
	        
	    }
	 
	 
	 @Override
     public void setTipText(String tipText) {
         super.setForeground(new Color(157,135,233,127));
         super.setTipText(tipText);
         super.setSize(300, 100);
     }
	 
	 @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(60, 48); // 设置你想要的宽度和高度
	    }
}

package redAlert;

import java.awt.*;

public class VxlTest {
    
    public static void main(String[] args) throws Exception {
        
        // 对比法  更改载具阵营颜色
        //		List<ShapeUnitFrame> ls = convertPngFileToBuildingFrames("gtnk",16,1);
        //		System.out.println(ls.size());
        //		System.out.println(ls.get(0).getColorPointList().size());
        //
        //		int i=0;
        //		for(ShapeUnitFrame suf:ls) {
        //			giveFrameUnitColor(suf.getImg(),suf,UnitColor.Red);
        //			ImageIO.write(suf.getImg(), "png", new File("E:/z_gtanks/redGtnk"+i+".png"));
        //			i++;
        //		}
        
        // 研究如何使用HSL色彩
        //		BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
        //		Color hsbColor = Color.getHSBColor(0/240.0f, 1.0f, 2.0f);//黄色
        //		Graphics g = image.getGraphics();
        //		g.setColor(hsbColor);
        //		g.fillRect(0, 0, 100, 100);
        //		ImageIO.write(image, "png", new File("E:/z_gtanks/test.png"));
        
        //		Color.getHSBColor(imageLength, imageLength, imageLength);
        
        Color oriColor = new Color(24, 14, 255);
        //		testTransColor(oriColor,UnitColor.Gray);
        
    }
}

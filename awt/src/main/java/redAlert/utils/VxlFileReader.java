package redAlert.utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.utilBean.ColorPoint;

/**
 * 从网页工具中读取到的载具各个角度画面信息
 */
public class VxlFileReader {

	/**
	 * 规定导出的每个角度图片都是128*128大小
	 */
	public final static int imageLength = 128;
	
	/**
	 * SHP文件前缀和路径名称缓存
	 */
	private static HashMap<String,String> pngPrefixToPathMap = new HashMap<String,String>();
	/**
	 * 加载ClassPath目录下的所有SHP文件名和文件目录
	 */
	static {
		String classPath = VxlFileReader.class.getClassLoader().getResource(".").getPath();//当前目录  也就是与classes文件夹所在目录  变量以"/"或"\"结尾
		
		try {
			classPath= URLDecoder.decode(classPath, "UTF-8");
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		File file = new File(classPath+ "png");
		
		File [] files = file.listFiles();
		for(int i=0;i<files.length;i++) {
			String name = files[i].getName();
			if(name.endsWith("png")) {
				String shpPrefix = name.substring(0, name.indexOf("."));
				String path = files[i].getAbsolutePath();
				pngPrefixToPathMap.put(shpPrefix, path);
			}
		}
	}
	
	/**
	 * 转换png图片到ShapeUnitFrame列表
	 * x 每行有多少个贴图
	 * y 每列有多少个贴图
	 */
	public static List<ShapeUnitFrame> convertPngFileToBuildingFrames(String tankPrefix,int x,int y,UnitColor unitColor) {
		
		List<ShapeUnitFrame> result = null;
		try {
			result = new ArrayList<>();
			String vxlFilePath = pngPrefixToPathMap.get(tankPrefix);
			File vxlFile = new File(vxlFilePath);
			BufferedImage img = ImageIO.read(vxlFile);
			
			//读取标记队伍颜色信息坐标的文件
			String posFilePath = StringUtils.replace(vxlFilePath, ".png", ".pos");
			File posFile = new File(posFilePath);
			String posStr = FileUtils.readFileToString(posFile,"utf-8");
			String [] posStrArr = StringUtils.split(posStr, "$");
			Set<String> set = new HashSet<>();
			for(String str: posStrArr) {
				set.add(str);
			}
			
			for(int i=0;i<y;i++) {
				for(int j=0;j<x;j++) {
					BufferedImage targetImage = new BufferedImage(imageLength,imageLength,BufferedImage.TYPE_INT_ARGB);
					
					int startx = j*imageLength;
					int starty = i*imageLength;
					
					int minX = 99999;
					int minY = 99999;
					
					ShapeUnitFrame shapeUnitFrame = new ShapeUnitFrame();
					List<ColorPoint> colorPointList = new ArrayList<ColorPoint>(100);
					shapeUnitFrame.setColorPointList(colorPointList);
					for(int m=0;m<imageLength;m++) {//行 y
						for(int n=0;n<imageLength;n++) {//列 x
							int rgb = img.getRGB(startx+n, starty+m);
							
							if(rgb!=0) {
								if(n<=minX) {
									minX = n;
								}
								if(m<=minY) {
									minY = m;
								}
							}
							targetImage.setRGB(n, m, rgb);
							
							//如果这个点坐标在阵营点集合中,标记
							if(set.contains((startx+n)+","+(starty+m))) {
								ColorPoint ccp = new ColorPoint(n,m);
								colorPointList.add(ccp);
							}
							
						}
					}
					giveFrameUnitColor(targetImage,shapeUnitFrame,unitColor);
					
					shapeUnitFrame.setImg(targetImage);
					shapeUnitFrame.setMinX(minX);
					shapeUnitFrame.setMinY(minY);
					result.add(shapeUnitFrame);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 上阵营色
	 * 注意,此处的image可能是frame的成员变量，也可能不是
	 */
	public static void giveFrameUnitColor(BufferedImage image,ShapeUnitFrame frame,UnitColor unitColor){
		//赋予阵营颜色
		List <ColorPoint> colorPointLs = frame.getColorPointList();
		if(colorPointLs!=null && !colorPointLs.isEmpty()) {
			for(ColorPoint cp:colorPointLs) {
				int oriColor = image.getRGB(cp.getX(), cp.getY());
				image.setRGB(cp.getX(), cp.getY(), transColor(oriColor,unitColor));
			}
		}
	}
	
	/**
	 * 将一个颜色更改为同饱和度、同亮度的颜色
	 */
	public static int transColor(int oriColor,UnitColor unitColor) {
		
		int r0 = (oriColor >> 16) &0xff;
		int g0 = (oriColor >> 8) &0xff;
		int b0 = oriColor & 0xff;
		
		float [] hsb = Color.RGBtoHSB(r0, g0, b0, null);
		float hue = hsb[0];
		float s = hsb[1];
		float b = hsb[2];
		
		if(unitColor==UnitColor.Red) {
			hue = 0;
		}
		if(unitColor==UnitColor.Orange) {
			hue = 0.1f;
		}
		if(unitColor==UnitColor.Yellow) {
			hue = 1/6.0f;
		}
		if(unitColor==UnitColor.Green) {
			hue = 1/3.0f;
		}
		if(unitColor==UnitColor.LightBlue) {
			hue = 0.5f;
		}
		if(unitColor==UnitColor.Blue) {
			hue = 144/240.0f;
		}
		if(unitColor==UnitColor.Purple) {
			hue = 185/240.0f;
		}
		if(unitColor==UnitColor.Pink) {
			hue = 219/240.0f;
		}
		if(unitColor==UnitColor.Gray) {
			//色调不变
			//饱和度变低
			s = 0.1f;
		}
		return Color.HSBtoRGB(hue, s, b);
		
	}
	
	
	
	
	
	public static void main(String[] args) throws Exception{
		

		
		//对比法  更改载具阵营颜色
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
		
		
		//研究如何使用HSL色彩
//		BufferedImage image = new BufferedImage(100,100,BufferedImage.TYPE_INT_ARGB);
//		Color hsbColor = Color.getHSBColor(0/240.0f, 1.0f, 2.0f);//黄色
//		Graphics g = image.getGraphics();
//		g.setColor(hsbColor);
//		g.fillRect(0, 0, 100, 100);
//		ImageIO.write(image, "png", new File("E:/z_gtanks/test.png"));

//		Color.getHSBColor(imageLength, imageLength, imageLength);
		
		Color oriColor = new Color(24,14,255);
//		testTransColor(oriColor,UnitColor.Gray);
	
	
	}
}

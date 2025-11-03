package redAlert.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

import redAlert.ShapeUnitFrame;
import redAlert.utilBean.ColorPoint;

public class PosFileGenerator {

	//尝试生成一份像素位置信息的文本文件  标记了阵营像素的位置信息
		public static void test1(File file1,File file2,String fileName) throws Exception{
			BufferedImage img1 = ImageIO.read(file1);
			BufferedImage img2 = ImageIO.read(file2);
			
			int y = 1;
			int x = 32;
			int imageLength = 128;
			
			File text = new File("E:/ZTestRADir/"+fileName+".pos");
			StringBuilder sb = new StringBuilder();
			
			
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
							int rgb = img1.getRGB(startx+n, starty+m);
							
							int rgb_r = img2.getRGB(startx+n, starty+m);
							if(rgb!=0) {
								if(n<=minX) {
									minX = n;
								}
								if(m<=minY) {
									minY = m;
								}
							}
							targetImage.setRGB(n, m, rgb);
							
							
							
							if(rgb!=rgb_r) {
								ColorPoint ccp = new ColorPoint(n,m);
								colorPointList.add(ccp);
								
								if(sb.length()<1) {
									sb.append( (startx+n)+","+(starty+m));
								}else {
									sb.append("$"+(startx+n)+","+(starty+m));
								}
							}
							
						}
					}
					
					
					shapeUnitFrame.setImg(targetImage);
					shapeUnitFrame.setMinX(minX);
					shapeUnitFrame.setMinY(minY);
					
				}
			}
			
			
			FileUtils.writeStringToFile(text, sb.toString(), "UTF-8");
			
			
			
			
			
		}
		
		public static void main(String[] args) throws Exception{
			
//			test1(new File("E:/ZTestRADir/png/gtnk.png"),new File("E:/ZTestRADir/png/gtnk_r.png"),"gtnk");
//			test1(new File("E:/ZTestRADir/png/gtnktur.png"),new File("E:/ZTestRADir/png/gtnktur_r.png"),"gtnktur");
			test1(new File("E:/ZTestRADir/tank组合图/htnk.png"),new File("E:/ZTestRADir/tank组合图/htnk_r.png"),"htnk");
//			test1(new File("E:/ZTestRADir/tur组合图/htnktur.png"),new File("E:/ZTestRADir/tur组合图/htnktur_r.png"),"htnktur");
//			test1(new File("E:/ZTestRADir/tank组合图/mcv.png"),new File("E:/ZTestRADir/tank组合图/mcv_r.png"),"mcv");
//			test1(new File("E:/ZTestRADir/tank组合图/fv.png"),new File("E:/ZTestRADir/tank组合图/fv_r.png"),"fv");
//			test1(new File("E:/ZTestRADir/tur组合图/fvtur.png"),new File("E:/ZTestRADir/tur组合图/fvtur_r.png"),"fvtur");
//			test1(new File("E:/ZTestRADir/tank组合图/sref.png"),new File("E:/ZTestRADir/tank组合图/sref_r.png"),"sref");
//			test1(new File("E:/ZTestRADir/tur组合图/sreftur.png"),new File("E:/ZTestRADir/tur组合图/sreftur_r.png"),"sreftur");
//			test1(new File("E:/ZTestRADir/tank组合图/gtnk.png"),new File("E:/ZTestRADir/tank组合图/gtnk_r.png"),"gtnk");
//			test1(new File("E:/ZTestRADir/tur组合图/gtnktur.png"),new File("E:/ZTestRADir/tur组合图/gtnktur_r.png"),"gtnktur");
		}
}

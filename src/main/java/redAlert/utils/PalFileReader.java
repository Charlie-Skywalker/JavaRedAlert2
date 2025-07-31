package redAlert.utils;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;

import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShpResourceCenter;

/**
 * 读取调色盘文件
 *
 */
public class PalFileReader {
	
	
	public static String palPath = "";
	
	static {
		palPath = ResourceDir.getResourceDir("pal");
	}
	
	/**
	 * 通过pal文件前缀获取颜色数组
	 */
	public static int [] getColorArrayByPrefix(String palPrefix) {
		try {
			return PalFileReader.getColorArray(palPath+"/"+palPrefix+".pal");
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * pal文件全都是768字节
	 */
	private static int[] getColorArray(String palFilePath) throws Exception{
		File shpFile = new File(palFilePath);
		RandomAccessFile raf = new RandomAccessFile(shpFile,"rw");
		byte [] pal = new byte [768];
		raf.read(pal);
		
		raf.close();
		
		//返回一个int数组大小 768/3=256
		int [] colorArray = new int [256];
		for(int i=0,k=0;i<pal.length;i+=3,k+=1) {
			int r = (pal[i] & 0xFF)*4;//文件中颜色值最大是85,是正常值的1/4,所以乘以4
			int g = (pal[i+1] & 0xFF)*4;
			int b = (pal[i+2] & 0xFF)*4;
			colorArray[k] = (255<<24) | (r<<16) | (g<<8) | b;
			//alpha如果都是0  在argb图片里  图片背景会变成黑色!!
		}
		
		//更改index  17~31 的颜色数值  可以更改建筑的所属队伍
		/*
		for(int i=0;i<colorArray.length;i++) {
			if(i>15 && i<32) {
				int da = delAlpha(colorArray[i]);
				int mc = 0;
				if(color==null || color==UnitColor.Red) {
					//本来就是红色的
					mc = da;
				}
				
				if(color==UnitColor.Blue){
					mc = turnBlue(da);
				}
				if(color==UnitColor.Green) {
					mc = turnGreen(da);
				}
				if(color==UnitColor.Yellow) {
					mc = turnYellow(da);
				}
				if(color==UnitColor.Purple) {
					mc = turnPurple(da);
				}
				if(color==UnitColor.LightBlue) {
					mc = turnLightBlue(da);
				}
				if(color==UnitColor.Orange) {
					mc = turnOrange(da);
				}
				if(color==UnitColor.Gray) {
					mc = turnGray(da);
				}
				
				int aa = addAlpha(mc);//之所以颜色要加alpha,是因为ARBG图片,如果alpha位为0,则颜色完全透明,表现为黑色
				
				colorArray[i] = aa;
			}
		}
		*/
		
		
		//更改第一个颜色的数值  改成透明色
		colorArray[0] = 0;
		
		return colorArray;
		
	}
	
	
	/**
	 * pal文件全都是768字节
	 * 获取一个指定阵营颜色的调色板  J3D中仍需要这个方法
	 */
	public static int[] getColorArray(String filepath,UnitColor color) throws Exception{
		File shpFile = new File(filepath);
		RandomAccessFile raf = new RandomAccessFile(shpFile,"rw");
		byte [] pal = new byte [768];
		raf.read(pal);
		
		raf.close();
		
		//返回一个int数组大小 768/3=256
		int [] colorArray = new int [256];
		for(int i=0,k=0;i<pal.length;i+=3,k+=1) {
			int r = (pal[i] & 0xFF)*4;//文件中颜色值最大是85,是正常值的1/4,所以乘以4
			int g = (pal[i+1] & 0xFF)*4;
			int b = (pal[i+2] & 0xFF)*4;
			colorArray[k] = (255<<24) | (r<<16) | (g<<8) | b;
			//alpha如果都是0  在argb图片里  图片背景会变成黑色!!
		}
		
		//更改index  17~31 的颜色数值  可以更改建筑的所属队伍
		
		for(int i=0;i<colorArray.length;i++) {
			if(i>15 && i<32) {
				int da = delAlpha(colorArray[i]);
				int mc = 0;
				if(color==null || color==UnitColor.Red) {
					//本来就是红色的
					mc = da;
				}
				
				if(color==UnitColor.Blue){
					mc = turnBlue(da);
				}
				if(color==UnitColor.Green) {
					mc = turnGreen(da);
				}
				if(color==UnitColor.Yellow) {
					mc = turnYellow(da);
				}
				if(color==UnitColor.Purple) {
					mc = turnPurple(da);
				}
				if(color==UnitColor.LightBlue) {
					mc = turnLightBlue(da);
				}
				if(color==UnitColor.Orange) {
					mc = turnOrange(da);
				}
				if(color==UnitColor.Gray) {
					mc = turnGray(da);
				}
				
				int aa = addAlpha(mc);//之所以颜色要加alpha,是因为ARBG图片,如果alpha位为0,则颜色完全透明,表现为黑色
				
				colorArray[i] = aa;
			}
		}
		
		
		
		//更改第一个颜色的数值  改成透明色
		colorArray[0] = 0;
		
		return colorArray;
		
	}
	
	
	public static int turnBlue(int oriColor)   {return oriColor >> 16;}                 						//盟军蓝   最深色#0000EC
	public static int turnGreen(int oriColor)  {return oriColor >>  8;}                 						//标准绿   最深色#00EC00
	public static int turnYellow(int oriColor) {return oriColor | turnGreen(oriColor);}      					//标准黄   最深色#ECEC00  (红+绿)
	public static int turnPurple(int oriColor) {return oriColor | turnBlue(oriColor);}					    	//紫色     最深色#EC00EC  (红+蓝)
	public static int turnLightBlue(int oriColor) {return turnGreen(oriColor) | turnBlue(oriColor);}        	//淡蓝     最深色#00ECEC  (绿+蓝)
	public static int turnGray(int oriColor) {return oriColor | turnGreen(oriColor) | turnBlue(oriColor);}		//中立灰   最深色#ECECEC  (红+绿+蓝)
	public static int turnOrange(int oriColor) {//橙色
		int a = oriColor>>16;
		double bl = a/255.0;
		double b = 124*bl;
		int c = (int)b;
		int d = c<<8;
		int result = oriColor | d;
		return result;
	}									
	
	public static int delAlpha(int oriColor) {return oriColor & 0x00FFFFFF;}
	public static int addAlpha(int oriColor) {return oriColor | 0xFF000000;}
	
	public static void main(String[] args) throws Exception{
		int [] b = getColorArray("C:\\Users\\R&D1\\Desktop\\RA\\uniturb.pal");
		
		System.out.println(b[0]);
	}
}

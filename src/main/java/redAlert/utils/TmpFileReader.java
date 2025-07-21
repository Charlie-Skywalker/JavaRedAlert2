package redAlert.utils;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.net.URLDecoder;

import redAlert.utils.PalFileReader;

/**
 * 红警的地图文件读取
 */
public class TmpFileReader {

	public static String tmpPath = "";
	
	static {
		tmpPath = ResourceDir.getResourceDir("tmp");
	}
	
	/**
	 * 通过tmp文件名称获取文件的完整路径
	 * 
	 * tmp文件的前缀有重复,需要文件带上后缀,入参比如  clat01.sno
	 */
	public static String getFilePathByFileName(String tmpFileName) {
		try {
			String suffix = tmpFileName.substring(tmpFileName.indexOf(".")+1).toLowerCase();//后缀
			return tmpPath+"/"+suffix+"/"+tmpFileName;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取一个1*1的地形文件  
	 * 测试阶段   
	 * @param tmpFilePath
	 * @return
	 * @throws Exception
	 */
	public static BufferedImage test(String tmpFileName) throws Exception{
		
		String tmpFilePath = getFilePathByFileName(tmpFileName);
		String suffix = tmpFileName.substring(tmpFileName.indexOf(".")+1).toLowerCase();//后缀
		int [] colorArray = PalFileReader.getColorArrayByPrefix("iso"+suffix);
		
		File shpFile = new File(tmpFilePath);
		RandomAccessFile raf = new RandomAccessFile(shpFile,"r");
		
		int tilesOfX = read4Byte(raf);//横向瓦片数量
		int tilesOfY = read4Byte(raf);//纵向瓦片数量
		
		int tileWidth = read4Byte(raf);//单个瓦片宽度
		int tileHeight = read4Byte(raf);//单个瓦片高度
		
		int numbersOfTiles = tilesOfX*tilesOfY;//瓦片数量
		
		int [] offsetsArray = new int [numbersOfTiles];//图片位置偏移
		
		
		for(int i=0;i<numbersOfTiles;i++) {
			offsetsArray[i] = read4Byte(raf);
		}
		
		for(int i=0;i<offsetsArray.length;i++) {
			int offset = offsetsArray[i];
			raf.seek(offset);
			
			int x = read4Byte(raf);//瓦片位置的X坐标  30的整数倍
			int y = read4Byte(raf);//瓦片位置的Y坐标  15的整数倍
			
			raf.skipBytes(4);//不明
			raf.skipBytes(4);//不明
			
			int dataBlockSize = read4Byte(raf);//CDCDCDCD
			int extraX = read4Byte(raf);//CDCDCDCD
			int extraY = read4Byte(raf);//CDCDCDCD
			int extraWidth = read4Byte(raf);//CDCDCDCD
			int extraHeight = read4Byte(raf);//CDCDCDCD
			
			int flags = read4Byte(raf);//CA CD CD CD 
			
			int height = raf.readUnsignedByte();// Number of cells high for this specific tile piece
			int terrainType =  raf.readUnsignedByte();//地形类型
			int rampType = raf.readUnsignedByte();//斜坡类型 
			
			byte[] radarLeft = new byte [3];
			raf.read(radarLeft);
			byte[] radarRight = new byte [3];
			raf.read(radarRight);
			
			raf.skipBytes(3);
			
			int mainTileDataByteLength = (tileWidth * tileHeight) / 2;
			
			byte [] colorBytes = new byte [mainTileDataByteLength];
			
			raf.read(colorBytes);
			
			BufferedImage image = new BufferedImage(60,30,BufferedImage.TYPE_INT_ARGB);
			
			
			for(int j=0;j<colorBytes.length;j++) {
				//将j与图片的横纵坐标对应起来
				Point p = calXY(j);
				int colorIndex = colorBytes[j] & 0xFF;
				image.setRGB(p.x, p.y,colorArray[colorIndex]);
			}
//			System.out.println(System.currentTimeMillis());
			
//			ImageIO.write(image, "png", new File("E:/myMap.png"));
			
//			System.out.println(System.currentTimeMillis());
			
			raf.close();
			return image;
		}
		
		return null;
		
	}
	
	/**
	 * 给出一个下标  计算对应BufferedImage上的Y坐标
	 */
	public static int calY(int index) {
		int a = 3;
		int s = 0;
		int col = 4;
		boolean flag = false;
		while(true) {
			int r = index-a;
			if(r<=0) {
				return s;
			}else {
				if(flag) {
					col = col-4;
					a = a + col;
				}else {
					col = col+4;
					a = a + col;
					if(col==60) {
						flag = true;
					}
				}
				s+=1;
			}
		}
	}
	
	/**
	 * 给出一个下标  计算对应BufferedImage上的X坐标
	 */
	public static int calX(int index) {
		int col = 4;
		int row = 0;
		while(true) {
			if(index>=col) {
				if(row<14) {
					index = index-col;
					col+=4;
					row+=1;
				}else {
					index = index-col;
					col-=4;
					row+=1;
				}
			}else {
				if(row<15) {
					return 28-row*2+index%col;
				}else {
					return (row-14)*2+index%col;
				}
				
			}
		}
	}
	
	/**
	 * 一次性把XY都计算出来
	 */
	public static Point calXY(int index) {
		int col = 4;
		int row = 0;
		while(true) {
			if(index>=col) {
				if(row<14) {
					index = index-col;
					col+=4;
					row+=1;
				}else {
					index = index-col;
					col-=4;
					row+=1;
				}
			}else {
				if(row<15) {
					int x = 28-row*2+index%col;
					int y = row;
					return new Point(x,y);
				}else {
					int x = (row-14)*2+index%col;
					int y = row;
					return new Point(x,y);
				}
				
			}
		}
	}
	
	
	/**
	 * 读四个字节  返回一个int整数
	 */
	private static int read4Byte(RandomAccessFile raf) throws Exception{
		byte [] bytes = new byte [4];
		raf.read(bytes);
		int result = 0;
		int result0 = bytes[0] & 0xFF;
		int result1 = bytes[1] & 0xFF;
		int result2 = bytes[2] & 0xFF;
		int result3 = bytes[3] & 0xFF;
		result = (result0 | result);
		result = (result1 <<8) | result;
		result = (result2 <<16) | result;
		result = (result3 <<24) | result;
		return result;
	}
	
//	public static void main(String[] args) throws Exception{
//		test("dlat01.sno");
//		
//		System.out.println(calXY(0));
//		System.out.println(calXY(1));
//		System.out.println(calXY(2));
//		System.out.println(calXY(3));
//		System.out.println(calXY(4));
//		System.out.println(calXY(5));
//		System.out.println(calX(11));
//		System.out.println(calXY(449));
//		System.out.println(calXY(899));
//		
//	}
}

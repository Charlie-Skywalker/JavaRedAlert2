package redAlert.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import redAlert.ShapeUnitFrame;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building.SceneType;
import redAlert.utilBean.ColorPoint;

/**
 * 读取Shp文件
 *
 */

/*
名词解释
颜色数据:每个数据1个字节,表示在调色盘pal数组的下标(由此转换成真正的RGBA或者RGB颜色)
空色数据:表示数值为0的颜色数据
有色数据:表示数值不为0的颜色数据
有效区域:有色数据区的最小矩形区域,其宽度为W,高度为H
SHP文件数据格式总结
//==========文件头=======
00~01字节  (2字节)无意义
02~03字节  (2字节)图片宽度
04~05字节  (2字节)图片高度
06~07字节  (2字节)图片帧数  设帧数为P
//==========文件头=======

//============帧头========
第1帧头
08~09字节  (2字节)此帧有效像素最小X坐标
0A~0B字节  (2字节)此帧有效像素最小Y坐标
0C~0D字节  (2字节)此帧有效像素区域最大宽度 设为W
0E~0F字节  (2字节)此帧有效像素区域最大高度 设为H
10~13字节  (4字节)分隔符03000000或01000000
14~17字节  (4字节)意义不明,猜想可能是校验位
18~1B字节  (4字节)目前已发现的全是00000000,猜想无意义的分隔符
1C~1F字节  (4字节)此帧在数据行的第一个字节下标

第2帧头
20~21字节  (2字节)此帧有效像素最小X坐标
22~23字节  (2字节)此帧有效像素最小Y坐标
24~25字节  (2字节)此帧有效像素区域最大宽度  设为W
26~27字节  (2字节)此帧有效像素区域最大高度  设为H
28~2B字节  (4字节)分隔符03000000或01000000
2C~2F字节  (4字节)意义不明,猜想可能是校验位
30~33字节  (4字节)目前已发现的全是00000000,猜想无意义的分隔符
34~37字节  (4字节)此帧在数据行的第一个字节下标
....
....
第P帧头
//============帧头========


=====数据行======

----帧头分隔符为01000000----
此行一共H个字节   全部表示颜色数据
----帧头分隔符为01000000----

----帧头分隔符为03000000----
1.最正常情况(2+2+N+2模式)
01~02字节:  
表示此行数据的字节数(包含01~02字节在内),假设数值为S>6  S=2+2+N+2
03~04字节:
表示第一个有色数据左边有几个空色数据
N个字节:  
每个字节顺序读  数值大于0,表示有色数据;数值等于0,且后边一个字节数值X大于0,表示后边接X个空色数据
最后2字节:
假设数值为C  则表示最后一个字节右边再数(C-1)下,是此帧图像的有色数据的X坐标最大值

2.特殊情况1  (2+N+2模式)  (此行第一个有色数据在有效区域左边缘)
01~02字节:
表示此行数据的字节数(包含01~02字节在内),假设数值为S>6  S=2+N+2
N个字节:  
每个字节顺序读  数值大于0,表示有色数据;数值等于0,且后边一个字节数值X大于0,表示后边接X个空色数据
最后2字节:
假设数值为C  则表示最后一个字节右边再数(C-1)下,是此帧图像的有色数据的X坐标最大值

3.特殊情况2  (2+2+N模式)  (此行最后一个有色数据在有效区域右边缘)
01~02字节:  
表示此行数据的字节数(包含01~02字节在内),假设数值为S>6  S=2+2+N
03~04字节:
表示第一个有色数据左边有几个空色数据
N个字节:  
每个字节顺序读  数值大于0,表示有色数据;数值等于0,且后边一个字节数值X大于0,表示后边接X个空色数据

4.特殊情况3  (2+N模式)   (此行第一个有色数据在有效区域左边缘,且此行最后一个有色数据在有效区域右边缘)
01~02字节:  
表示此行数据的字节数(包含01~02字节在内),假设数值为S>6  S=2+N
N个字节:  
每个字节顺序读  数值大于0,表示有色数据;数值等于0,且后边一个字节数值X大于0,表示后边接X个空色数据

5.换行情况  (表示此行)
01~02字节: 此时该数据为04 00
03~04字节: 有数值,含义不明
----帧头分隔符为03000000----

每帧数据的末尾,重要!!
补若干个00  使得此帧最后一个数据的字节数是8的整数

=====数据行======

 */
public class ShpFileReader {
	
	
	/**
	 * 转换shp到ShapeUnitFrame数组
	 * 
	 */
//	public static List<ShapeUnitFrame> convertShpFileToBuildingFrames(String shpFilePath,String buildingName) throws Exception{
//		return convertShpFileToBuildingFrames(shpFilePath,buildingName,true);
//	}
	
	/**
	 * 转换shp到ShapeUnitFrame数组
	 * 
	 */
	public static List<ShapeUnitFrame> convertShpFileToBuildingFrames(String shpFilePath,String palPrefix,boolean half) throws Exception{
		//读取调色盘信息
		int [] colorArray = PalFileReader.getColorArrayByPrefix(palPrefix);
		
		File shpFile = new File(shpFilePath);
		RandomAccessFile raf = new RandomAccessFile(shpFile,"r");
		//1.文件头信息 8个字节
		//文件标识   00-01字节 无意义
		byte [] fileFlagBytes = new byte [2];
		raf.read(fileFlagBytes);
		//图片宽度   02-03字节
		int width = read2Byte(raf);
		//图片高度   04~05字节
		int height = read2Byte(raf);
		//图片帧数   06~07字节
		int frames = read2Byte(raf);
		
		//2.帧头信息
		List<FrameHeadInfo> list = new ArrayList<FrameHeadInfo>(frames);
		for(int i=0;i<frames;i++) {
			//5.最小X坐标  
			int minIndexX = read2Byte(raf);//2字节
			//6.最小Y坐标  
			int minIndexY = read2Byte(raf);//2字节
			//7.有效区域宽度
			int realPartWidth = read2Byte(raf);//2字节
			//8.有效区域高度     
			int realPartHeight = read2Byte(raf);//2字节
			//9.表示此帧类型的字节   有03和01两种  目前看来
			int frameType = read4Byte(raf);//4字节
			//10.意义不明,可能是校验位         
			int unknown = read4Byte(raf);//4字节 
			//11.分隔符字节  无意义
			int c00000000 = read4Byte(raf);
			//12.第一帧画面的起始位置下标
			int firstByteIndex = read4Byte(raf);//4字节
			
			FrameHeadInfo fhi = new FrameHeadInfo();
			fhi.minIndexX = minIndexX;
			fhi.minIndexY = minIndexY;
			fhi.realPartWidth = realPartWidth;
			fhi.realPartHeight = realPartHeight;
			fhi.frameType = frameType;
			fhi.firstByteIndex = firstByteIndex;
			list.add(fhi);
		}
		
		//3.数据帧信息
		boolean isUseShadow = false;//是否使用阴影效果  目前还用不了  部分shp文件的后半部分不是阴影  会导致错误
		int readSize = 0;
		if(isUseShadow) {
			readSize = list.size();
		}else {
			if(list.size()==1) {
				readSize = 1;
			}else {
				if(half) {
					readSize = list.size()/2;//建造动画只取前一半
				}else {
					readSize = list.size();//建造动画取全部
				}
				
			}
		}
		List<ShapeUnitFrame> resultObjectBuildingList = new ArrayList<ShapeUnitFrame>(readSize);
		for(int i=0;i<readSize;i++) {
			//一帧一图
			BufferedImage targetImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
			
			FrameHeadInfo head = list.get(i);
			/**
			 * 可以在此计算一下帧图中的重要数据信息
			 */
			int minX = head.minIndexX;//最小X坐标
			int minY = head.minIndexY;//最小Y坐标
			int maxX = minX + head.realPartWidth-1;//最大X坐标
			int maxY = minY + head.realPartHeight-1;//最大Y坐标
			int realPartWidth = head.realPartWidth;//有效区域宽度
			int realPartHeight = head.realPartHeight;//有效区域高度
			int frameType = head.frameType;//帧类型
			
			ShapeUnitFrame shapeUnitFrame = new ShapeUnitFrame();
			shapeUnitFrame.setImg(targetImage);
			shapeUnitFrame.setMinX(minX);
			shapeUnitFrame.setMinY(minY);
			shapeUnitFrame.setMaxX(maxX);
			shapeUnitFrame.setMaxY(maxY);
			shapeUnitFrame.setRealPartWidth(realPartWidth);
			shapeUnitFrame.setRealPartHeight(realPartHeight);
			List<ColorPoint> colorPointList = null;
			
			boolean isUseUnitColor = false;
			if(SceneType.SNOW.getPalPrefix().equals(palPrefix) || SceneType.TEM.getPalPrefix().equals(palPrefix) || SceneType.URBAN.getPalPrefix().equals(palPrefix)) {
				isUseUnitColor = true;
				colorPointList = new ArrayList<>();
				shapeUnitFrame.setColorPointList(colorPointList);
			}
			
			
			//curX和curY指引targetImage的绘图位置,像个指针
			int curX = 0;//图中像素点X坐标
			int curY = list.get(i).minIndexY;//图中像素点Y坐标
			
			if(realPartWidth==0 && realPartHeight==0) {//这一画面里没有任何有用的内容
				
			}else {
				if(frameType==01 || frameType==0){
					for(int k=0;k<realPartHeight;k++) {
						curX = list.get(i).minIndexX;
						byte [] rowBytes = new byte [realPartWidth];//按有效宽度读数据
						raf.read(rowBytes);
						for(int b=0;b<rowBytes.length;b++) {
							int colorIndex = rowBytes[b] & 0xFF;
							targetImage.setRGB(curX, curY, colorArray[colorIndex]);
							if(isUseUnitColor) {
								if(colorIndex>15 && colorIndex<32) {
									ColorPoint colorPoint = new ColorPoint(curX,curY);
									colorPointList.add(colorPoint);
								}
							}
							curX++;
						}
						curY++;
					}
				}else {
					for(int k=0;k<realPartHeight;k++) {
						//2字节  有效数据长度 如果遇上4那表示要换行的
						int effectLenth = read2Byte(raf);//如果遇上4那表示要换行的 要特殊处理 正常最小是7
						if(effectLenth==4) {
							raf.skipBytes(2);
							curY++;
						}else {
							//直接把剩余字节全读掉,然后再分析
							byte [] restBytes = new byte [effectLenth-2];
							raf.read(restBytes);
							//分析内容
							byte [] pixelBytes = null;
							if(restBytes[0]==0 && restBytes[restBytes.length-2]==0) {//2+2+N+2模式
								int nullBytesLeft = restBytes[1] & 0xFF;
								curX = list.get(i).minIndexX+nullBytesLeft;
								pixelBytes = Arrays.copyOfRange(restBytes, 2, restBytes.length-2);
								////存在一种特殊情况导致2+2+N+2模式退化到2+2+N模式  需要确认最后一个像素坐标在X的极大值上  先不考虑这种情况  应该比较少见 且少一个像素无关紧要
							}
							
							if(restBytes[0]!=0 && restBytes[restBytes.length-2]==0) {//2+N+2模式
								curX = list.get(i).minIndexX;
								pixelBytes = Arrays.copyOfRange(restBytes, 0, restBytes.length-2);
								//存在一种特殊情况导致2+N+2模式退化到2+N模式   需要确认最后一个像素坐标在X的极大值上  先不考虑这种情况  应该比较少见 且少一个像素无关紧要
							}
							
							if(restBytes[0]==0 && restBytes[restBytes.length-2]!=0) {//2+2+N模式  (这个情况真的存在吗,存疑)
								int nullBytesLeft = restBytes[1] & 0xFF;
								curX = list.get(i).minIndexX+nullBytesLeft;
								pixelBytes = Arrays.copyOfRange(restBytes, 2, restBytes.length);
							}
							
							if(restBytes[0]!=0 && restBytes[restBytes.length-2]!=0) {//2+N模式
								curX = list.get(i).minIndexX;
								pixelBytes = Arrays.copyOfRange(restBytes, 0, restBytes.length);
							}
							//绘图
							for(int b=0;b<pixelBytes.length;b++) {
								if(pixelBytes[b]==0 && (b+1)<pixelBytes.length && (pixelBytes[b+1] & 0xFF)>0) {
									int ntime0 = pixelBytes[b+1] & 0xFF;
									for(int b1=0;b1<ntime0;b1++) {
										targetImage.setRGB(curX, curY, colorArray[0]);
										curX++;
									}
									b++;
								}else {
									int colorIndex = pixelBytes[b] & 0xFF;
									
									targetImage.setRGB(curX, curY, colorArray[colorIndex]);
									
									if(isUseUnitColor) {
										if(colorIndex>15 && colorIndex<32) {
											ColorPoint colorPoint = new ColorPoint(curX,curY);
											colorPointList.add(colorPoint);
										}
									}
									
									curX++;
								}
							}
							
							curY++;//应该放在这里才是正确的
							
						}
					}
				}
			}
			
			
			//补0  补的个数是看现在已经读个数  满足补0之后  当前读的字节数取余8==0
			long a = raf.getFilePointer();
			int e = 8-(int)a%8;
			raf.skipBytes(e==8?0:e);
			
			resultObjectBuildingList.add(shapeUnitFrame);
		}
		
		
		
		raf.close();
		return resultObjectBuildingList;
	}
	
	private static class FrameHeadInfo {
		public int minIndexX;
		public int minIndexY;
		public int realPartWidth;
		public int realPartHeight;
		public int frameType;
		public int firstByteIndex;
	}
	
	/**
	 * 读两个字节  返回一个int整数
	 */
	private static int read2Byte(RandomAccessFile raf) throws Exception{
		byte [] bytes = new byte [2];
		raf.read(bytes);
		int result = 0;
		int result0 = bytes[0] & 0xFF;
		int result1 = bytes[1] & 0xFF;
		result = (result0 | result);
		result = (result1 <<8) | result;
		return result;
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
	
	
	/**
	 * 测试方法  把SHP逐帧保存成图片
	 * @param shpPrefix shp文件名前缀  例如gayardmk
	 * @param savePath  保存的文件夹路径
	 * @param palPrefix  调色板文件名前缀 见SceneType
	 * @throws Exception
	 */
	private static void test1(String shpPrefix,String savePath,String palPrefix) throws Exception{
		File file = new File(savePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		List<ShapeUnitFrame> resultBuildingList = ShpResourceCenter.loadShpResource(shpPrefix, palPrefix, false);
		
		System.out.println(resultBuildingList.size()+"帧画面");
		for(int i=0;i<resultBuildingList.size();i++) {
			ShapeUnitFrame ob = resultBuildingList.get(i);
			BufferedImage targetImage = ob.getImg();
			System.out.println(targetImage);
			ImageIO.write(targetImage, "png", new File(savePath+"/"+shpPrefix+i+".png"));
			Thread.sleep(10);
		}
		
	}
	
	public static void main(String[] args) throws Exception{
		//long t1 = System.currentTimeMillis();
		
//		test1("nukedie","E:/xxx",SceneType.ANIM.getPalPrefix());
		
		//test1("D:\\redAlertFile\\shp\\ntcnstmk.shp","D:/redAlertFile/my/苏联/A0基地展开","ntcnstmk");
		//test1("D:\\redAlertFile\\shp\\ntpowrmk.shp","D:/redAlertFile/my/苏联/A01发电厂","ntpowrmk");
		//test1("D:\\redAlertFile\\shp\\ntrefnmk.shp","D:/redAlertFile/my/苏联/A02采矿场","ntrefnmk");
		//test1("D:\\redAlertFile\\shp\\nthandmk.shp","D:/redAlertFile/my/苏联/A03兵营","nthandmk");
		//test1("D:\\redAlertFile\\shp\\ntweapmk.shp","D:/redAlertFile/my/苏联/A04建设工厂","ntweapmk");
		//test1("D:\\redAlertFile\\shp\\ntradrmk.shp","D:/redAlertFile/my/苏联/A05雷达","ntradrmk");
		//test1("D:\\redAlertFile\\shp\\ntyardmk.shp","D:/redAlertFile/my/苏联/A06船坞","ntyardmk");
		//test1("D:\\redAlertFile\\shp\\ntdeptmk.shp","D:/redAlertFile/my/苏联/A07维修场","ntdeptmk");
		//test1("D:\\redAlertFile\\shp\\nttechmk.shp","D:/redAlertFile/my/苏联/A08实验室","nttechmk");
		//test1("D:\\redAlertFile\\shp\\ntnrctmk.shp","D:/redAlertFile/my/苏联/A09核子反应堆","ntnrctmk");
		//test1("D:\\redAlertFile\\shp\\ntclonmk.shp","D:/redAlertFile/my/苏联/A10复制中心","ntclonmk");
		
		//test1("D:\\redAlertFile\\shp\\ntlasrmk.shp","D:/redAlertFile/my/苏联哨戒炮建造","ntlasrmk");
		//test1("D:\\redAlertFile\\shp\\ntflakmk.shp","D:/redAlertFile/my/苏联防空炮建造","ntflakmk");
		//test1("D:\\redAlertFile\\shp\\nttslamk.shp","D:/redAlertFile/my/苏联磁暴线圈建造","nttslamk");
		//test1("D:\\redAlertFile\\shp\\ntpsismk.shp","D:/redAlertFile/my/苏联心灵探测器建造","ntpsismk");
		//test1("D:\\redAlertFile\\shp\\ntironmk.shp","D:/redAlertFile/my/苏联铁幕建造","ntironmk");
		//test1("D:\\redAlertFile\\shp\\ntmislmk.shp","D:/redAlertFile/my/苏联核弹建造","ntmislmk");
		//test1("D:\\redAlertFile\\shp\\ntpsybmk.shp","D:/redAlertFile/my/苏联小信标建造","ntpsybmk");
		
		//test1("D:\\redAlertFile\\shp\\gtcnstmk.shp","D:/redAlertFile/my/盟军基地展开","gtcnstmk");
		//test1("D:\\redAlertFile\\shp\\gtpowrmk.shp","D:/redAlertFile/my/盟军发电厂建造","gtpowrmk");
		//test1("D:\\redAlertFile\\shp\\gtrefnmk.shp","D:/redAlertFile/my/盟军采矿场建造","gtrefnmk");
		//test1("D:\\redAlertFile\\shp\\gtpilemk.shp","D:/redAlertFile/my/盟军兵营建造","gtpilemk");
//		test1("D:\\redAlertFile\\shp\\gtweapmk.shp","D:/redAlertFile/my/盟军工厂建造","gtweapmk");
		//test1("D:\\redAlertFile\\shp\\gaweap_b.shp","D:/redAlertFile/my/盟军工厂建造11","gtweapmk");
		//test1("D:\\redAlertFile\\shp\\gtaircmk.shp","D:/redAlertFile/my/盟军空指部建造","gtaircmk");
		//test1("D:\\redAlertFile\\shp\\gtyardmk.shp","D:/redAlertFile/my/盟军船坞建造","gtyardmk");
		//test1("D:\\redAlertFile\\shp\\gtdeptmk.shp","D:/redAlertFile/my/盟军维修场建造","gtdeptmk");
		//test1("D:\\redAlertFile\\shp\\gttechmk.shp","D:/redAlertFile/my/盟军实验室建造","gttechmk");
		//test1("D:\\redAlertFile\\shp\\gtcommmk.shp","D:/redAlertFile/my/盟军不明建筑","gtcommmk");
		//test1("D:\\redAlertFile\\shp\\gtorepmk.shp","D:/redAlertFile/my/盟军矿石精炼器建造","gtorepmk");
		
		//test1("D:\\redAlertFile\\shp\\gtpillmk.shp","D:/redAlertFile/my/盟军碉堡建造","gtpillmk");
		//test1("D:\\redAlertFile\\shp\\ntsammk.shp","D:/redAlertFile/my/盟军爱国者飞弹建造","ntsammk");
		//test1("D:\\redAlertFile\\shp\\gtprismk.shp","D:/redAlertFile/my/盟军光棱塔建造","gtprismk");
		//test1("D:\\redAlertFile\\shp\\gtgapmk.shp","D:/redAlertFile/my/盟军裂缝产生器建造","gtgapmk");
		//test1("D:\\redAlertFile\\shp\\gtspstmk.shp","D:/redAlertFile/my/盟军间谍卫星建造","gtspstmk");
		//test1("D:\\redAlertFile\\shp\\gtcsphmk.shp","D:/redAlertFile/my/盟军超时空建造","gtcsphmk");
		//test1("D:\\redAlertFile\\shp\\gtwethmk.shp","D:/redAlertFile/my/盟军天气控制器建造","gtwethmk");
		//test1("D:\\redAlertFile\\shp\\gtgcanmk.shp","D:/redAlertFile/my/盟军巨炮建造","gtgcanmk");
		
		
		//test1("D:\\redAlertFile\\shp\\ntpsya_a.shp","D:/redAlertFile/my/苏联大信标a","ntpsya_a");
		//test1("D:\\redAlertFile\\shp\\ntpsya.shp","D:/redAlertFile/my/苏联大信标底座","ntpsya");
		//test1("D:\\redAlertFile\\shp\\ctoild_a.shp","D:/redAlertFile/my/中立油田a","ctoild_a");
		//test1("D:\\redAlertFile\\shp\\ctoutp_a.shp","D:/redAlertFile/my/中立维修a","ctoutp_a");
		//test1("D:\\redAlertFile\\shp\\ctoutp_b.shp","D:/redAlertFile/my/中立维修b","ctoutp_b");
		//test1("D:\\redAlertFile\\shp\\ctoutp_c.shp","D:/redAlertFile/my/中立维修c","ctoutp_c");
		
		//test1("D:\\redAlertFile\\shp\\ctchig05.shp","D:/redAlertFile/my/中立芝加哥威利斯大厦","ctchig05");
		//test1("D:\\redAlertFile\\shp\\ctnwy05.shp","D:/redAlertFile/my/中立世贸大厦","ctnwy05");
		//test1("D:\\redAlertFile\\shp\\ctnewy01.shp","D:/redAlertFile/my/中立建筑物01","ctnewy01");
		//test1("D:\\redAlertFile\\shp\\ctnewy06.shp","D:/redAlertFile/my/中立建筑物06","ctnewy06");
		//test1("D:\\redAlertFile\\shp\\ctpars08.shp","D:/redAlertFile/my/中立建筑物08","ctpars08");
		//test1("D:\\redAlertFile\\shp\\ctpars09.shp","D:/redAlertFile/my/中立建筑物09","ctpars09");
		//test1("D:\\redAlertFile\\shp\\ctnwy26.shp","D:/redAlertFile/my/中立建筑物26","ctnwy26");
		//test1("D:\\redAlertFile\\shp\\cttech01.shp","D:/redAlertFile/my/中立实验室","cttech01");
		//test1("D:\\redAlertFile\\shp\\ctlab.shp","D:/redAlertFile/my/中立实验室lab","ctlab");
		//test1("D:\\redAlertFile\\shp\\ctmex03.shp","D:/redAlertFile/my/中立mex03","ctmex03");
		//test1("D:\\redAlertFile\\shp\\ammo01.shp","D:/redAlertFile/my/ammo01","ammo01");
		//test1("D:\\redAlertFile\\shp\\wake1.shp","D:/redAlertFile/my/wake1","wake1");
		
		//System.out.println(System.currentTimeMillis()-t1);
	}
}

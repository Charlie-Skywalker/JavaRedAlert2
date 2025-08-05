package redAlert;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Random;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import redAlert.enums.BuildingAreaType;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;
import redAlert.utils.WavFileReader;

/**
 * 负责建造建筑的专职类
 *
 */
public class Constructor {
	
	
	
	public static int time = 0;
	
	/**
	 * @param movableUnit  可移动单位
	 * @param scenePanel  游戏主面板
	 */
	public static void putOneShapeUnit(MovableUnit movableUnit ) {
		RuntimeParameter.addBuildingToQueue(movableUnit);
		ShapeUnitResourceCenter.addMovableUnit(movableUnit);
	}
	
	/**
	 * 添加一个单位
	 */
	public static void putOneShapeUnit(ShapeUnit shapeUnit) {
		RuntimeParameter.addBuildingToQueue(shapeUnit);
		ShapeUnitResourceCenter.addUnit(shapeUnit);
		
	}
	
	
	/**
	 * @param positionX  表示图片的左上角在Panel中的X坐标
	 * @param positionY  表示图片的左上角在Panel中的Y坐标
	 * @param buildingName 建筑的名称
	 * @param scene 表示场景
	 */
	public static boolean putOneBuilding(Building building) {
		
		//占
		List<CenterPoint> mbuildingAreas = building.getNoConstCpList();//建筑使用中心块列表(建筑占地)
		List<CenterPoint> noVehicleLs = building.getNoVehicleCpList();//车辆禁止入内用地
		if(mbuildingAreas==null) {
			RuntimeParameter.addBuildingToQueue(building);
			playOneMusic("uplace");
			return true;
		}else {
			boolean isCanMake = true;
			for(CenterPoint cp:mbuildingAreas) {
				if(!cp.isBuildingCanPutOn()) {
					isCanMake = false;
				}
			}
			
			if(isCanMake) {
				for(CenterPoint cp:mbuildingAreas) {
					if(noVehicleLs.contains(cp)) {
						cp.addBuilding(building,BuildingAreaType.Normal);//军事建筑占用
					}else {
						cp.addBuilding(building,BuildingAreaType.WeighBridge);//磅秤占用
					}
				}
				
				//将建筑阴影中的建筑设置为被遮挡
				List<CenterPoint> shadowAreas = building.getShadownCpList();
				for(CenterPoint shadowCp:shadowAreas) {//声明阴影区域
					shadowCp.setInShadow(true);
					Building inShadowBuilding = shadowCp.getBuilding();
					Vehicle inShadowVehicle = shadowCp.getVehicle();
					List<Soldier> soldiers = shadowCp.getSoldiers();
					
					if(inShadowBuilding!=null) {
						inShadowBuilding.setHided(true);
					}
					if(inShadowVehicle!=null) {
						inShadowVehicle.setHided(true);
					}
					if(!soldiers.isEmpty()) {
						for(Soldier soldier:soldiers) {
							soldier.setHided(true);//阴影中的单位被挡了
						}
					}
				}
				
				//如果自己也在阴影中,设置自己为被遮挡
				if(PointUtil.isBuidingInShadow(building)) {
					building.setHided(true);
				}
				
				ShapeUnitResourceCenter.addBuilding(building);
				RuntimeParameter.addBuildingToQueue(building);
				playOneMusic("uplace");
				return true;
			}else {
				playOneMusic("ceva063");//Can not deploy here
				System.out.println("不能建造");
				return false;
			}
		}
	}
	
	public static void randomPlayOneMusic(String ... wavPrefix) {
		Random r = new Random();
		int index = r.nextInt(wavPrefix.length);
		playOneMusic(wavPrefix[index]);
	}
	
	public static void playOneMusic(String wavPrefix) {
		Thread audioTask = new Thread() {
			public void run() {
				try {
					synchronized (Constructor.class) {
						ByteArrayInputStream musicInputStream = WavFileReader.getMusicInputStream(wavPrefix);
						AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicInputStream);//打开音频文件
						AudioFormat format = audioStream.getFormat();//获取音频格式
						DataLine.Info info = new DataLine.Info(Clip.class, format);
						Clip clip = (Clip)AudioSystem.getLine(info);//创建音频剪辑流
						//打开音频剪辑流
						clip.open(audioStream);
						//播放
						clip.start();
						clip.addLineListener(new LineListener() {
							@Override
							public void update(LineEvent event) {
								if(event.getType()==LineEvent.Type.STOP) {
									try {
										clip.close();
										audioStream.close();
									}catch (Exception e) {
										e.printStackTrace();
									}
								}
							}
						});
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		audioTask.start();
	}
	
}

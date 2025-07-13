package redAlert.shapeObjects.vehicle;

import java.util.List;
import java.util.Random;

import javax.sound.sampled.Clip;

import redAlert.MusicPlayer;
import redAlert.ShapeUnitFrame;
import redAlert.enums.UnitColor;
import redAlert.shapeObjects.Vehicle;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.VxlFileReader;

/**
 * 基洛夫空艇
 */
public class Zep extends Vehicle{

	protected List<ShapeUnitFrame> allFrames;
	int index = 1;
	
	public boolean move = false;
	public int moveNum = 0;
	
	int step = 1;
	
	public ZepIn zepIn;//基洛夫的影子
	
	public Clip clip;//音乐播放器
	public Clip stopClip;//音乐播放器
	
	public Zep() {
		
	}
	
	public Zep(int positionX,int positionY,String scene,UnitColor color) {
		super.unitColor = color;
		
		try {
			this.allFrames = VxlFileReader.convertPngFileToBuildingFrames("E:/zep.png",8,1,color);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		super.curFrame = allFrames.get(index);
		super.positionX = positionX;
		super.positionY = positionY;
		super.positionMinX = curFrame.getMinX()+positionX;
		super.positionMinY = curFrame.getMinY()+positionY;
		//定义唯一编号
		Random random = new Random();
		super.unitNo = random.nextInt();
		super.unitName = "基洛夫空艇";
		
		clip = MusicPlayer.getClip("vkirlo2a",-1);
		stopClip = MusicPlayer.getClip("vkirlo3",1);
	}
	@Override
	public void calculateNextFrame() {
		if(move) {
			if(step==1) {
				if(moveNum<=10) {
					moveNum++;
					return;
				}
				if(moveNum<36) {
					clip.start();
					super.positionY -= 4;
					moveNum++;
					if(moveNum>11) {
						zepIn.setPriority(51);
					}
					
				}else {
					step=2;
					moveNum=0;
				}
			}
			
			if(step==2) {
				if(moveNum<46) {
					super.positionX += 4;
					super.positionY += 2;
					zepIn.setPositionX(zepIn.getPositionX()+4);
					zepIn.setPositionY(zepIn.getPositionY()+2);
					moveNum++;
				}else {
					step=3;
					moveNum=0;
				}
			}
			if(step==3) {
				if(moveNum<10) {
					super.positionY += 4;
					moveNum++;
				}else {
					move=false;
					zepIn.move=false;
					clip.stop();
					stopClip.start();
				}
			}
			
		}
	}

	@Override
	public void moveToTarget(CenterPoint target) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}

package redAlert.other;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import redAlert.Constructor;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Soldier;

/**
 * 核弹爆炸任务
 */
public class YiShu extends TimerTask{

	
	public YiShu(NuclearBombDown bomb) {
		this.bomb = bomb;
	}
	
	public NuclearBombDown bomb;
	
	Timer timer = new Timer();
	long delay = 1;//延迟毫秒时间
	long period = 100;//执行周期
	
	int time = 0;
	boolean isNukeballPutIn = false;
	boolean isBombRemove = false;
	boolean isNukeAnimPutIn = false;
	Nukeball ball = null;
	Nukeanim anim = null;
	
	@Override
	public void run() {
		//前20秒什么也不错
		if(time<100) {
		
		}else if(time==100) {
			Constructor.putOneShapeUnit(bomb);
			System.out.println("下落核弹放入画面");
		}else if(time>100) {
			if(bomb.getPositionY()<bomb.getTargetY()-157) {
				bomb.setPositionY(bomb.getPositionY()+10);
				bomb.setPositionMinY(bomb.getPositionY()+4);
				System.out.println("在下落了");
			}else {
				if(!bomb.end) {
					bomb.end = true;
					System.out.println("移除下落核弹");
					return;
				}
				
				if(!isNukeballPutIn) {
					ball = new Nukeball(bomb.getTargetX(),bomb.getTargetY());
					Constructor.putOneShapeUnit(ball);
					isNukeballPutIn = true;
					Constructor.playOneMusic("snukintr");
					System.out.println("核爆球加入");
					return;
				}
				
				if(ball.end && !isNukeAnimPutIn) {
					anim = new Nukeanim(bomb.getTargetX(),bomb.getTargetY());
					Constructor.putOneShapeUnit(anim);
					isNukeAnimPutIn = true;
					Constructor.playOneMusic("snukexpl");
					System.out.println("蘑菇云加入");
					
					//辐射附近的士兵
					List<MovableUnit> list = ShapeUnitResourceCenter.movableUnitQueryList;
					
					List<NuclearDie> dieSoldierList = new ArrayList<NuclearDie>();
					
					synchronized (list) {
						Iterator<MovableUnit> ee =  list.iterator();
						while(ee.hasNext()) {
							ShapeUnit su = ee.next();
							if(su instanceof Soldier) {
								//为了遍历集合时不在集合新增元素,先放入临时List中
								NuclearDie die = new NuclearDie(su.getPositionX(),su.getPositionY());
								dieSoldierList.add(die);
								su.setEnd(true);
								
							}
						}
						
						for(NuclearDie die:dieSoldierList) {
							Constructor.putOneShapeUnit(die);
							Random r = new Random();
							int m0 = r.nextInt(3);
							if(m0==0) {
								Constructor.playOneMusic("isnidia");
							}
							if(m0==1) {
								Constructor.playOneMusic("isnidib");
							}
							if(m0==2) {
								Constructor.playOneMusic("isnidic");
							}
							int m = r.nextInt(3);
							if(m==0) {
								Constructor.playOneMusic("igenmela");
							}
							if(m==1) {
								Constructor.playOneMusic("igenmelb");
							}
							if(m==2) {
								Constructor.playOneMusic("igenmelc");
							}
							
						}
						
					}
					
					
					return;
				}
				if(isNukeAnimPutIn && anim.end) {
					timer.cancel();
					System.out.println("结束了");
				}
			}
		}
		
		
		
		//把核弹放入画面,不断修改核弹的坐标
		
		//检测到达目标位置后
		
		//把核弹移除  把核爆画面放入
		
		//核爆画面播放完毕后  把核爆画面移除
		
		time++;
	}

	public void start() {
		timer.schedule(this, 1, 67);
	}
	
}

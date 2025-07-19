package redAlert.tabIcon;

/**
 * 坦管理器
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import redAlert.enums.VehicleEnum;
import redAlert.tabIcon.tab03.AfAgisBtn;
import redAlert.tabIcon.tab03.AfAhrvBtn;
import redAlert.tabIcon.tab03.AfBeagBtn;
import redAlert.tabIcon.tab03.AfCarrBtn;
import redAlert.tabIcon.tab03.AfDestBtn;
import redAlert.tabIcon.tab03.AfDlphBtn;
import redAlert.tabIcon.tab03.AfFalcBtn;
import redAlert.tabIcon.tab03.AfGtnkBtn;
import redAlert.tabIcon.tab03.AfIfvBtn;
import redAlert.tabIcon.tab03.AfLandIcon;
import redAlert.tabIcon.tab03.AfMcvBtn;
import redAlert.tabIcon.tab03.AfRtnkBtn;
import redAlert.tabIcon.tab03.AfShadBtn;
import redAlert.tabIcon.tab03.AfSrefBtn;
import redAlert.tabIcon.tab03.AfTnkdBtn;

public class Tab03Manager {
	public static AfAhrvBtn ahrvBtn;//超时空矿车
	public static AfGtnkBtn gtnkBtn;//灰熊坦克
	public static AfIfvBtn fvBtn;//多功能步兵车
	public static AfTnkdBtn tnkdBtn;//坦克杀手
	public static AfBeagBtn beagBtn;//黑鹰
	public static AfSrefBtn srefBtn;//光棱坦克
	public static AfRtnkBtn rtnkBtn;//幻影坦克
	public static AfMcvBtn mcvBtn;//基地车
	public static AfFalcBtn falcBtn;//入侵者战机
	public static AfShadBtn shadBtn;//夜莺直升机
	public static AfLandIcon landIcon;//两栖运输艇
	public static AfDestBtn destBtn;//驱逐舰
	public static AfDlphBtn dlphBtn;//海豚
	public static AfAgisBtn agisBtn;//神盾
	public static AfCarrBtn carrBtn;//航母
	
	/**
	 * 全量图标
	 */
	public static List<Tab03ConstIcon> tab03List = new ArrayList<>();
	/**
	 * 应显示图标
	 */
	public static List<Tab03ConstIcon> displayIcons = new ArrayList<>();
	/**
	 * 对应关系
	 */
	public static Map<VehicleEnum,Tab03ConstIcon> rel = new HashMap<>();
	
	public static void init() {
		ahrvBtn = new AfAhrvBtn();//超时空矿车
		gtnkBtn = new AfGtnkBtn();//灰熊坦克
		fvBtn = new AfIfvBtn();//多功能步兵车
		tnkdBtn = new AfTnkdBtn();//坦克杀手
		beagBtn = new AfBeagBtn();//黑鹰
		
		srefBtn = new AfSrefBtn();//光棱坦克
		rtnkBtn = new AfRtnkBtn();//幻影坦克
		mcvBtn = new AfMcvBtn();//盟军基地车
		falcBtn = new AfFalcBtn();//入侵者战机
		shadBtn = new AfShadBtn();//夜莺直升机
		
		landIcon = new AfLandIcon();//两栖运输艇
		destBtn = new AfDestBtn();//驱逐舰
		dlphBtn = new AfDlphBtn();//海豚
		agisBtn = new AfAgisBtn();//神盾
		carrBtn = new AfCarrBtn();//航母
		
		tab03List.add(ahrvBtn);
		tab03List.add(gtnkBtn);
		tab03List.add(fvBtn);
		tab03List.add(tnkdBtn);
		tab03List.add(beagBtn);
		
		tab03List.add(srefBtn);
		tab03List.add(rtnkBtn);
		tab03List.add(mcvBtn);
		tab03List.add(falcBtn);
		tab03List.add(shadBtn);
		
		tab03List.add(landIcon);
		tab03List.add(destBtn);
		tab03List.add(dlphBtn);
		tab03List.add(agisBtn);
		tab03List.add(carrBtn);
		
		rel.put(VehicleEnum.AfAhrv,ahrvBtn);
		rel.put(VehicleEnum.AfGtnk,gtnkBtn);
		rel.put(VehicleEnum.AfIfv,fvBtn);
		rel.put(VehicleEnum.AfTnkd,tnkdBtn);
		rel.put(VehicleEnum.AfBeag,beagBtn);
		
		rel.put(VehicleEnum.AfSref,srefBtn);
		rel.put(VehicleEnum.AfRtnk,rtnkBtn);
		rel.put(VehicleEnum.AfMcv,mcvBtn);
		rel.put(VehicleEnum.AfFalc,falcBtn);
		rel.put(VehicleEnum.AfShad,shadBtn);
		
		rel.put(VehicleEnum.AfLand,landIcon);
		rel.put(VehicleEnum.AfDest,destBtn);
		rel.put(VehicleEnum.AfDlph,dlphBtn);
		rel.put(VehicleEnum.AfAgis,agisBtn);
		rel.put(VehicleEnum.AfCarr,carrBtn);
	}
	
	
	
	/**
	 * 获取应该显示的图标列表
	 */
	public static List<Tab03ConstIcon> getShowTabList() {
		displayIcons.clear();
		for(Tab03ConstIcon icon:tab03List) {
			if(icon.isDisplay()) {
				displayIcons.add(icon);
			}else {
				icon.destroyTimer();
			}
		}
		return displayIcons;
	}
	
	/**
	 * 载具生产队列
	 */
	public static LinkedList<VehicleEnum> trainList = new LinkedList<>();
	
	/**
	 * 队列中添加生产人物
	 * 添加生产小兵的计划
	 */
	public static void addTrainPlan(VehicleEnum vehicle) {
		if(trainList.isEmpty()) {
			trainList.addLast(vehicle);
			Tab03ConstIcon tabIcon = rel.get(vehicle);
			tabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING);
		}else {
			if(countNum(vehicle)>=30) {
				return;
			}
			VehicleEnum theFirstVehicle = trainList.peekFirst();
			if(vehicle==theFirstVehicle) {
				trainList.addLast(vehicle);
			}else {
				Tab03ConstIcon tabIcon = rel.get(vehicle);
				if(tabIcon.getStatus()==Tab03ConstIcon.STATUS_IDLE) {
					tabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING_WAIT);
					trainList.addLast(vehicle);
				}else if(tabIcon.getStatus()==Tab03ConstIcon.STATUS_TRAINING_WAIT) {
					trainList.addLast(vehicle);
				}
			}
			
		}
	}
	
	/**
	 * 把正在生产的载具移除
	 */
	public static void reduceTrainPlan(VehicleEnum vehicle) {
		if(trainList.isEmpty()) {
			return;
		}
		
		VehicleEnum firstVehicle = trainList.pollFirst();
		if(trainList.isEmpty()) {
			Tab03ConstIcon tabIcon = rel.get(firstVehicle);
			tabIcon.setStatus(Tab03ConstIcon.STATUS_TEMP);
		}else {
			VehicleEnum nextVehicle = trainList.peekFirst();
			if(firstVehicle==nextVehicle) {//继续生产
				
			}else {
				Tab03ConstIcon tabIcon = rel.get(firstVehicle);
				if(countNum(vehicle)>0) {
					tabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING_WAIT);
				}else {
					tabIcon.setStatus(Tab03ConstIcon.STATUS_TEMP);
				}
				Tab03ConstIcon nextTabIcon = rel.get(nextVehicle);
				nextTabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING);
			}
		}
	}
	
	/**
	 * 把正在等待生产的载具移除
	 */
	public static void reduceWaitingTrainPlan(VehicleEnum vehicle) {
		Tab03ConstIcon tabIcon = rel.get(vehicle);
		trainList.removeLastOccurrence(vehicle);
		if(trainList.contains(vehicle)) {
			
		}else {
			tabIcon.setStatus(Tab03ConstIcon.STATUS_TEMP);
		}
	}
	
	/**
	 * 队列中有几个这样的单位
	 */
	public static int countNum(VehicleEnum vehicle) {
		int num = 0;
		for(VehicleEnum vehicleInfo:trainList) {
			if(vehicleInfo==vehicle) {
				num++;
			}
		}
		return num;
	}
	
	/**
	 * 当一个载具生产完毕后  TabIcon调用此方法
	 */
	public static void next() {
		VehicleEnum vehicleName = trainList.poll();
		Tab03ConstIcon tabIcon = rel.get(vehicleName);
		
		if(trainList.isEmpty()) {
			tabIcon.setStatus(Tab03ConstIcon.STATUS_TEMP);
		}else {
			VehicleEnum nextVehicle = trainList.peekFirst();
			if(nextVehicle==vehicleName) {
				//不需要做任何事
				return;
			}else {
				Tab03ConstIcon nextTabIcon = rel.get(nextVehicle);
				
				if(countNum(vehicleName)>0) {
					tabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING_WAIT);
				}else {
					tabIcon.setStatus(Tab03ConstIcon.STATUS_TEMP);
				}
				nextTabIcon.setStatus(Tab03ConstIcon.STATUS_TRAINING);
			}
		}
	}
	
	/**
	 * 把所有的都回归正常状态
	 */
	public static void freeAll() {
		for(Tab03ConstIcon cb: tab03List) {
			cb.setStatus(Tab03ConstIcon.STATUS_TEMP);
		}
	}
	
}

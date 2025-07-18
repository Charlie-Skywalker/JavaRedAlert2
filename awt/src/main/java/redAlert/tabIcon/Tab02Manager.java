package redAlert.tabIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import redAlert.enums.SoldierEnum;
import redAlert.tabIcon.tab02.AfAdogBtn;
import redAlert.tabIcon.tab02.AfClegBtn;
import redAlert.tabIcon.tab02.AfGiBtn;
import redAlert.tabIcon.tab02.AfJjetBtn;
import redAlert.tabIcon.tab02.AfSealBtn;
import redAlert.tabIcon.tab02.AfSnipBtn;
import redAlert.tabIcon.tab02.AfSpyBtn;
import redAlert.tabIcon.tab02.AfTanyBtn;
import redAlert.tabIcon.tab02.EngnBtn;

/**
 * 兵管理器
 */
public class Tab02Manager {
	
	public static AfGiBtn giBtn;//美国大兵
	public static EngnBtn engnBtn;//工程师
	public static AfAdogBtn adogBtn;//狗
	public static AfJjetBtn jjetBtn;//飞行兵
	public static AfSnipBtn snipBtn;//狙击手
	public static AfSpyBtn spyBtn;//间谍
	public static AfSealBtn sealBtn;//海豹部队
	public static AfTanyBtn tanyBtn;//谭雅
	public static AfClegBtn clegBtn;//时空军团兵
	/**
	 * 全量图标
	 */
	public static List<Tab02ConstIcon> tab02List = new ArrayList<>();
	/**
	 * 应显示图标
	 */
	public static List<Tab02ConstIcon> displayIcons = new ArrayList<>();
	/**
	 * 对应关系
	 */
	public static Map<SoldierEnum,Tab02ConstIcon> rel = new HashMap<>();
	
	
	
	/**
	 * 初始化
	 */
	public static void init() {
		giBtn = new AfGiBtn();
		engnBtn = new EngnBtn();
		adogBtn = new AfAdogBtn();
		jjetBtn = new AfJjetBtn();
		snipBtn = new AfSnipBtn();
		spyBtn = new AfSpyBtn();
		sealBtn = new AfSealBtn();
		tanyBtn = new AfTanyBtn();
		clegBtn = new AfClegBtn();
		Tab02Manager.tab02List.add(giBtn);
		Tab02Manager.tab02List.add(engnBtn);
		Tab02Manager.tab02List.add(adogBtn);
		Tab02Manager.tab02List.add(jjetBtn);
		Tab02Manager.tab02List.add(snipBtn);
		Tab02Manager.tab02List.add(spyBtn);
		Tab02Manager.tab02List.add(sealBtn);
		Tab02Manager.tab02List.add(tanyBtn);
		Tab02Manager.tab02List.add(clegBtn);
		
		rel.put(SoldierEnum.AfGi,giBtn);
		rel.put(SoldierEnum.Engn,engnBtn);
		rel.put(SoldierEnum.AfAdog,adogBtn);
		rel.put(SoldierEnum.AfJjet,jjetBtn);
		rel.put(SoldierEnum.AfSnip,snipBtn);
		rel.put(SoldierEnum.AfSpy,spyBtn);
		rel.put(SoldierEnum.AfSeal,sealBtn);
		rel.put(SoldierEnum.AfTany,tanyBtn);
		rel.put(SoldierEnum.AfCleg,clegBtn);
		
		
	}
	
	/**
	 * 获取应该显示的图标列表
	 */
	public static List<Tab02ConstIcon> getShowTabList() {
		displayIcons.clear();
		for(Tab02ConstIcon icon:tab02List) {
			if(icon.isDisplay()) {
				displayIcons.add(icon);
			}else {
				icon.destroyTimer();
			}
		}
		return displayIcons;
	}
	
	/**
	 * 步兵生产队列
	 */
	public static LinkedList<SoldierEnum> trainList = new LinkedList<>();
	
	
	/**
	 * 队列中添加生产人物
	 * 添加生产小兵的计划
	 */
	public static void addTrainPlan(SoldierEnum soldier) {
		if(trainList.isEmpty()) {
			trainList.addLast(soldier);
			Tab02ConstIcon tabIcon = rel.get(soldier);
			tabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING);
		}else {
			if(countNum(soldier)>=30) {
				return;
			}
			SoldierEnum theFirstSoldier = trainList.peekFirst();
			if(soldier==theFirstSoldier) {
				trainList.addLast(soldier);
			}else {
				Tab02ConstIcon tabIcon = rel.get(soldier);
				if(tabIcon.getStatus()==Tab02ConstIcon.STATUS_IDLE) {
					tabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING_WAIT);
					trainList.addLast(soldier);
				}else if(tabIcon.getStatus()==Tab02ConstIcon.STATUS_TRAINING_WAIT) {
					trainList.addLast(soldier);
				}
			}
			
		}
	}
	
	/**
	 * 把正在生产的小兵移除
	 */
	public static void reduceTrainPlan(SoldierEnum soldier) {
		if(trainList.isEmpty()) {
			return;
		}
		
		SoldierEnum firstSoldier = trainList.pollFirst();
		if(trainList.isEmpty()) {
			Tab02ConstIcon tabIcon = rel.get(firstSoldier);
			tabIcon.setStatus(Tab02ConstIcon.STATUS_TEMP);
		}else {
			SoldierEnum nextSoldier = trainList.peekFirst();
			if(firstSoldier==nextSoldier) {//继续生产
				
			}else {
				Tab02ConstIcon tabIcon = rel.get(firstSoldier);
				if(countNum(soldier)>0) {
					tabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING_WAIT);
				}else {
					tabIcon.setStatus(Tab02ConstIcon.STATUS_TEMP);
				}
				Tab02ConstIcon nextTabIcon = rel.get(nextSoldier);
				nextTabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING);
			}
		}
	}
	
	/**
	 * 把正在等待生产的小兵移除
	 */
	public static void reduceWaitingTrainPlan(SoldierEnum soldier) {
		Tab02ConstIcon tabIcon = rel.get(soldier);
		trainList.removeLastOccurrence(soldier);
		if(trainList.contains(soldier)) {
			
		}else {
			tabIcon.setStatus(Tab02ConstIcon.STATUS_TEMP);
		}
	}
	
	
	
	/**
	 * 队列中有几个这样的单位
	 */
	public static int countNum(SoldierEnum soldier) {
		int num = 0;
		for(SoldierEnum soldierInfo:trainList) {
			if(soldierInfo==soldier) {
				num++;
			}
		}
		return num;
	}
	/**
	 * 当一个小兵生产完毕后  TabIcon调用此方法
	 */
	public static void next() {
		SoldierEnum soldierName = trainList.poll();
		Tab02ConstIcon tabIcon = rel.get(soldierName);
		
		if(trainList.isEmpty()) {
			tabIcon.setStatus(Tab02ConstIcon.STATUS_TEMP);
		}else {
			SoldierEnum nextSoldier = trainList.peekFirst();
			if(nextSoldier==soldierName) {
				//不需要做任何事
				return;
			}else {
				Tab02ConstIcon nextTabIcon = rel.get(nextSoldier);
				
				if(countNum(soldierName)>0) {
					tabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING_WAIT);
				}else {
					tabIcon.setStatus(Tab02ConstIcon.STATUS_TEMP);
				}
				nextTabIcon.setStatus(Tab02ConstIcon.STATUS_TRAINING);
			}
		}
	}
	
	/**
	 * 把所有的都回归正常状态
	 */
	public static void freeAll() {
		for(Tab02ConstIcon cb: tab02List) {
			cb.setStatus(Tab02ConstIcon.STATUS_TEMP);
		}
	}
	
}

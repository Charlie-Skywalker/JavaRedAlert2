package redAlert.tabIcon;

import java.util.ArrayList;
import java.util.List;

import redAlert.tabIcon.tab00.AfAircConstBtn;
import redAlert.tabIcon.tab00.AfDeptConstBtn;
import redAlert.tabIcon.tab00.AfOrepConstBtn;
import redAlert.tabIcon.tab00.AfPileConstBtn;
import redAlert.tabIcon.tab00.AfPowrConstBtn;
import redAlert.tabIcon.tab00.AfRefnConstBtn;
import redAlert.tabIcon.tab00.AfTechConstBtn;
import redAlert.tabIcon.tab00.AfWeapConstBtn;
import redAlert.tabIcon.tab00.AfYardConstBtn;

/**
 * 主建筑的管理器
 * 
 * 主建筑图标的一些功能都由此类提供方法统一管理
 */
public class Tab00Manager {

	
	public static AfPowrConstBtn powrBtn;//发电厂
	public static AfRefnConstBtn refnBtn;//盟军矿石精炼厂
	public static AfPileConstBtn pileBtn;//盟军兵营
	public static AfWeapConstBtn weapBtn;//盟军战车工厂
	public static AfAircConstBtn aircBtn;//空指部
	public static AfYardConstBtn yardBtn;//盟军造船厂
	public static AfDeptConstBtn deptBtn;//盟军维修厂
	public static AfTechConstBtn techBtn;//盟军作战实验室
	public static AfOrepConstBtn orepBtn;//矿石精炼器
	
	/**
	 * 图标大全
	 */
	public static List<Tab00ConstIcon> tab00List = new ArrayList<>();
	
	public static void init() {
		powrBtn = new AfPowrConstBtn();//发电厂
		refnBtn = new AfRefnConstBtn();//盟军矿石精炼厂
		pileBtn = new AfPileConstBtn();//盟军兵营
		weapBtn = new AfWeapConstBtn();//盟军战车工厂
		aircBtn = new AfAircConstBtn();//空指部
		yardBtn = new AfYardConstBtn();//盟军造船厂
		deptBtn = new AfDeptConstBtn();//盟军维修厂
		techBtn = new AfTechConstBtn();//盟军作战实验室
		orepBtn = new AfOrepConstBtn();//矿石精炼器 
		Tab00Manager.tab00List.add(powrBtn);
		Tab00Manager.tab00List.add(refnBtn);
		Tab00Manager.tab00List.add(pileBtn);
		Tab00Manager.tab00List.add(weapBtn);
		Tab00Manager.tab00List.add(aircBtn);
		Tab00Manager.tab00List.add(yardBtn);
		Tab00Manager.tab00List.add(deptBtn);
		Tab00Manager.tab00List.add(techBtn);
		Tab00Manager.tab00List.add(orepBtn);
	}
	
	/**
	 * 应显示图标
	 */
	public static List<Tab00ConstIcon> displayIcons = new ArrayList<>();
	
	/**
	 * 获取应该显示的图标列表
	 */
	public static List<Tab00ConstIcon> getShowTabList() {
		displayIcons.clear();
		for(Tab00ConstIcon icon:tab00List) {
			if(icon.isDisplay()) {
				displayIcons.add(icon);
			}else {
				icon.destroyTimer();
			}
		}
		return displayIcons;
	}
	
	/**
	 * 除去入参的  其他图标都禁用
	 */
	public static void banAllExceptOne(Tab00ConstIcon btn) {
		for(Tab00ConstIcon cb: tab00List) {
			if(!cb.equals(btn)) {
				cb.setStatus(Tab00ConstIcon.STATUS_BAN);
			}
		}
	}
	
	/**
	 * 把所有的都回归正常状态
	 */
	public static void freeAll() {
		for(Tab00ConstIcon cb: tab00List) {
			cb.setStatus(Tab00ConstIcon.STATUS_TEMP);
		}
	}
	
	/**
	 * 是否有某个存在就绪或建造中状态
	 */
	public static boolean isExistBuilding() {
		for(Tab00ConstIcon cb: tab00List) {
			int status = cb.getStatus();
			if(status==Tab00ConstIcon.STATUS_USING || status==Tab00ConstIcon.STATUS_READY) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否存在就绪状态的建筑
	 */
	public static boolean isExistReadyBuilding() {
		for(Tab00ConstIcon cb: tab00List) {
			int status = cb.getStatus();
			if(status==Tab00ConstIcon.STATUS_READY) {
				return true;
			}
		}
		return false;
	}
}

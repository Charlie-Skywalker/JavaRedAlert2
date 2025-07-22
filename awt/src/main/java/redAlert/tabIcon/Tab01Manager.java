package redAlert.tabIcon;

import java.util.ArrayList;
import java.util.List;

import redAlert.tabIcon.tab01.AfAparBtn;
import redAlert.tabIcon.tab01.AfChroBtn;
import redAlert.tabIcon.tab01.AfCsphConstBtn;
import redAlert.tabIcon.tab01.AfGapConstBtn;
import redAlert.tabIcon.tab01.AfGcanConstBtn;
import redAlert.tabIcon.tab01.AfLightntingStormBtn;
import redAlert.tabIcon.tab01.AfPillConstBtn;
import redAlert.tabIcon.tab01.AfPrisConstBtn;
import redAlert.tabIcon.tab01.AfSamConstBtn;
import redAlert.tabIcon.tab01.AfSpstConstBtn;
import redAlert.tabIcon.tab01.AfWallConstBtn;
import redAlert.tabIcon.tab01.AfWethConstBtn;
import redAlert.tabIcon.tab01.ParaBtn;

/**
 * 防御建筑的管理器
 * 
 * 防御建筑图标的一些功能都由此类提供方法统一管理
 */
public class Tab01Manager {
	
	public static AfWallConstBtn wallBtn;//盟军围墙
	public static AfPillConstBtn pillBtn;//机枪碉堡
	public static AfSamConstBtn samBtn;//爱国者导弹
	public static AfPrisConstBtn prisBtn;//光棱塔
	public static AfGapConstBtn gapBtn;//裂缝产生器
	public static AfGcanConstBtn gcanBtn;//巨炮
	public static AfSpstConstBtn spstBtn;//间谍卫星
	public static AfCsphConstBtn csphBtn;//超时空传送仪
	public static AfWethConstBtn wethBtn;//天气控制器图标
	public static AfAparBtn afApar;//空降部队
	public static ParaBtn para;//空降部队
	public static AfChroBtn chroBtn;//超时空转换
	public static AfLightntingStormBtn lightningStormBtn;//闪电风暴
	
	/**
	 * 图标大全
	 */
	public static List<Tab01ConstIcon> tab01List = new ArrayList<>();
	
	public static void init() {
		para = new ParaBtn();//伞兵
		afApar = new AfAparBtn();//空降部队
		chroBtn = new AfChroBtn();//超时空转换
		lightningStormBtn = new AfLightntingStormBtn();//闪电风暴
		wallBtn = new AfWallConstBtn();//盟军围墙
		pillBtn = new AfPillConstBtn();//机枪碉堡
		samBtn = new AfSamConstBtn();//爱国者导弹
		prisBtn = new AfPrisConstBtn();//光棱塔
		gapBtn = new AfGapConstBtn();//裂缝产生器
		gcanBtn = new AfGcanConstBtn();//巨炮
		spstBtn = new AfSpstConstBtn();//间谍卫星
		csphBtn = new AfCsphConstBtn();//超时空传送仪
		wethBtn = new AfWethConstBtn();//天气控制器图标
		Tab01Manager.tab01List.add(para);
		Tab01Manager.tab01List.add(afApar);
		Tab01Manager.tab01List.add(chroBtn);
		Tab01Manager.tab01List.add(lightningStormBtn);
		Tab01Manager.tab01List.add(wallBtn);
		Tab01Manager.tab01List.add(pillBtn);
		Tab01Manager.tab01List.add(samBtn);
		Tab01Manager.tab01List.add(prisBtn);
		Tab01Manager.tab01List.add(gapBtn);
		Tab01Manager.tab01List.add(gcanBtn);
		Tab01Manager.tab01List.add(spstBtn);
		Tab01Manager.tab01List.add(csphBtn);
		Tab01Manager.tab01List.add(wethBtn);
		
	}
	
	/**
	 * 应显示图标
	 */
	public static List<Tab01ConstIcon> displayIcons = new ArrayList<>();
	
	/**
	 * 获取应该显示的图标列表
	 */
	public static List<Tab01ConstIcon> getShowTabList() {
		displayIcons.clear();
		for(Tab01ConstIcon icon:tab01List) {
			if(icon.isDisplay()) {
				displayIcons.add(icon);
			}else {
				icon.destroyTimer();
			}
		}
		return displayIcons;
	}
	
	/**
	 * 是否应该展示防御建筑图标
	 */
	public static boolean isShowTab01() {
		for(Tab01ConstIcon icon:tab01List) {
			if(icon.isDisplay()) {
				return true;
			}
		}
		return false;
	}
	
	public static void initTimer() {
		for(Tab01ConstIcon cb: tab01List) {
			cb.initTimer();
		}
	}
	
	/**
	 * 除去入参的  其他图标都禁用
	 */
	public static void banAllExceptOne(Tab01ConstIcon btn) {
		for(Tab01ConstIcon cb: tab01List) {
			if(!cb.equals(btn)) {
				cb.setStatus(Tab01ConstIcon.STATUS_BAN);
			}
		}
	}
	
	/**
	 * 把所有的都回归正常状态
	 */
	public static void freeAll() {
		for(Tab01ConstIcon cb: tab01List) {
			cb.setStatus(Tab01ConstIcon.STATUS_TEMP);
		}
	}
	
	/**
	 * 是否有某个存在就绪或建造中状态
	 */
	public static boolean isExistBuilding() {
		for(Tab01ConstIcon cb: tab01List) {
			int status = cb.getStatus();
			if(status==Tab01ConstIcon.STATUS_USING || status==Tab01ConstIcon.STATUS_READY) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否存在就绪状态的建筑
	 */
	public static boolean isExistReadyBuilding() {
		for(Tab01ConstIcon cb: tab01List) {
			int status = cb.getStatus();
			if(status==Tab01ConstIcon.STATUS_READY) {
				return true;
			}
		}
		return false;
	}
}

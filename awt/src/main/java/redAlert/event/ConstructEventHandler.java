package redAlert.event;

import java.awt.event.MouseEvent;

import redAlert.Constructor;
import redAlert.GlobalConfig;
import redAlert.OptionsPanel;
import redAlert.RuntimeParameter;
import redAlert.enums.ConstConfig;
import redAlert.enums.MouseStatus;
import redAlert.militaryBuildings.AfAirc;
import redAlert.militaryBuildings.AfCnst;
import redAlert.militaryBuildings.AfCsph;
import redAlert.militaryBuildings.AfDept;
import redAlert.militaryBuildings.AfOrep;
import redAlert.militaryBuildings.AfPile;
import redAlert.militaryBuildings.AfPill;
import redAlert.militaryBuildings.AfPowr;
import redAlert.militaryBuildings.AfPris;
import redAlert.militaryBuildings.AfRefn;
import redAlert.militaryBuildings.AfSam;
import redAlert.militaryBuildings.AfSpst;
import redAlert.militaryBuildings.AfTech;
import redAlert.militaryBuildings.AfWeap;
import redAlert.militaryBuildings.AfWeth;
import redAlert.militaryBuildings.AfYard;
import redAlert.militaryBuildings.SfMisl;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.tabIcon.Tab00Manager;
import redAlert.tabIcon.Tab01Manager;
import redAlert.utilBean.CenterPoint;
import redAlert.utilBean.Coordinate;
import redAlert.utils.CoordinateUtil;
import redAlert.utils.PointUtil;

/**
 * 处理军事建筑建造事件
 * 用户点击右侧建筑icon，再在左边进行建筑建造的事件
 */
public class ConstructEventHandler extends Thread{

	/**
	 * 建筑建造事件
	 */
	public ConstructEvent constEvent;
	/**
	 * 建造的建筑
	 */
	public ConstConfig constName;
	
	
	public ConstructEventHandler(ConstructEvent constEvent) {
		this.constEvent = constEvent;
		this.constName = constEvent.constConfig;
	}
	
	@Override
	public void run() {
		MouseEvent mouseEvent = constEvent.mouseEvent;
		Coordinate coord = CoordinateUtil.getCoordinate(mouseEvent);
		CenterPoint center = PointUtil.getCenterPoint(coord.getMapX(), coord.getMapY());
		
		if(constName==ConstConfig.AfCnst) {
			AfCnst sf = new AfCnst(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//基地车
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
			}
			return;
		}
		if(constName==ConstConfig.AfPowr) {
			AfPowr sf = new AfPowr(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//发电厂
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfRefn) {
			AfRefn sf = new AfRefn(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//采矿场
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfPile) {
			AfPile sf = new AfPile(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//盟军兵营
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfWeap) {
			AfWeap sf = new AfWeap(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//建设工厂
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfAirc) {
			AfAirc sf = new AfAirc(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//空指部
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
				if(ShapeUnitResourceCenter.isPowerOn) {
					OptionsPanel.radarLabel.triggleRadarShow();
				}
			}
			return;
		}
		if(constName==ConstConfig.AfYard) {
			AfYard sf = new AfYard(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//船坞
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfDept) {
			AfDept sf = new AfDept(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//维修厂
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfTech) {
			AfTech sf = new AfTech(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//盟军作战实验室
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfOrep) {
			AfOrep sf = new AfOrep(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//矿石精炼厂
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		
		if(constName==ConstConfig.AfPill) {
			AfPill sf = new AfPill(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//机枪碉堡
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		
		if(constName==ConstConfig.AfPris) {
			AfPris sf = new AfPris(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//光棱塔
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		
		if(constName==ConstConfig.AfSam) {
			AfSam sf = new AfSam(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//爱国者飞弹
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		
		if(constName==ConstConfig.AfCsph) {
			AfCsph sf = new AfCsph(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//超时空转换器
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfWeth) {
			AfWeth sf = new AfWeth(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//天气控制器
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.AfSpst) {
			AfSpst sf = new AfSpst(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//间谍卫星
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab01Manager.freeAll();
			}
			return;
		}
		if(constName==ConstConfig.SfMisl) {
			SfMisl sf = new SfMisl(center,GlobalConfig.sceneType,GlobalConfig.unitColor);
			boolean isSuccess = Constructor.putOneBuilding(sf);//苏军核弹井
			if(isSuccess) {
				RuntimeParameter.mouseStatus = MouseStatus.Idle;
				constName = null;
				Tab00Manager.freeAll();
			}
			return;
		}
		
		return;
	}

	
}

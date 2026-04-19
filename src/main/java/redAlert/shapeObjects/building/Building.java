package redAlert.shapeObjects.building;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.enums.BuildingStatus;
import redAlert.enums.ConstConfig;
import redAlert.enums.SceneType;
import redAlert.other.BloodBar;
import redAlert.other.BuildingBone;
import redAlert.shapeObjects.Bloodable;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.utilBean.CenterPoint;

/**
 * 对建筑的抽象
 */
public abstract class Building extends ShapeUnit implements Bloodable{

	/**
	 * 建筑的配置信息
	 */
	public ConstConfig constConfig = null;//给一个默认值
	/**
	 * 建筑的虚拟高度
	 * 这个变量涉及到血条和模型棱的显示位置
	 */
	public int height = 70;
	/**
	 * 建筑状态    受损或完好
	 */
	public BuildingStatus status = BuildingStatus.UNDEMAGED;
	/**
	 * 用于决定加载三种动画帧时使用哪套皮肤
	 */
	public SceneType sceneType;
	/**
	 * 血条对象
	 */
	public BloodBar bloodBar;
	/**
	 * 骨架对象
	 */
	public BuildingBone bone;
	
	
	public Building() {
		super();
	}
	/**
	 * 默认的构造方法
	 */
	public Building(SceneType sceneType,ConstConfig constConfig) {
		this.sceneType = sceneType;
		this.constConfig = constConfig;
		super.unitName = constConfig.constName;
		super.frameSpeed = 4;	
	}

	@Override
	public BloodBar getBloodBar() {
		return bloodBar;
	}
	
	/**
	 * 通过shp的中心点原理计算centerOffX和centerOffY
	 */
	public void setCenterOffXY(List <ShapeUnitFrame> constructFrames) {
		Point imgCenter = constructFrames.get(0).getCenterCoord();
		if(constConfig.fxNum==2 && constConfig.fyNum==2) {
			setCenterOffX(imgCenter.x);
			setCenterOffY(imgCenter.y+14);
		}
		if(constConfig.fxNum==1 && constConfig.fyNum==1) {
			setCenterOffX(imgCenter.x);
			setCenterOffY(imgCenter.y+14);
		}
		if(constConfig.fxNum==2 && constConfig.fyNum==3) {
			setCenterOffX(imgCenter.x+30);
			setCenterOffY(imgCenter.y+14+15);
		}
		if(constConfig.fxNum==3 && constConfig.fyNum==3) {
			setCenterOffX(imgCenter.x);
			setCenterOffY(imgCenter.y+14+30);
		}
		if(constConfig.fxNum==4 && constConfig.fyNum==4) {
			setCenterOffX(imgCenter.x);
			setCenterOffY(imgCenter.y+14+30);
		}
		if(constConfig.fxNum==3 && constConfig.fyNum==4) {
			setCenterOffX(imgCenter.x);
			setCenterOffY(imgCenter.y+14+30);
		}
		if(constConfig.fxNum==3 && constConfig.fyNum==5) {
			setCenterOffX(imgCenter.x+30);
			setCenterOffY(imgCenter.y+14+30+15);
		}
	}
	
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 */
	public abstract List<CenterPoint> getNoConstCpList();
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 */
	public abstract List<CenterPoint> getNoVehicleCpList();
	/**
	 * 或者这个建筑的阴影区域
	 * 也应该改成抽象方法  还没改
	 */
	public List<CenterPoint> getShadownCpList(){
		return new ArrayList<>();
	}
	
	//获取最左边的中心块
	public CenterPoint getLeftFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint leftFirst = null;
		int x = Integer.MAX_VALUE;
		for(CenterPoint cp:ls) {
			int cpx = cp.getX();
			if(cpx<x) {
				x = cp.getX();
				leftFirst = cp;
			}
		}
		return leftFirst;
	}
	//获取最上边的中心块
	public CenterPoint getTopFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint topFirst = null;
		int y = Integer.MAX_VALUE;
		for(CenterPoint cp:ls) {
			int cpy = cp.getY();
			if(cpy<y) {
				y = cp.getY();
				topFirst = cp;
			}
		}
		return topFirst;
	}
	//获取最右边的中心块
	public CenterPoint getRightFirst() {
		List<CenterPoint> ls = getNoConstCpList();
		CenterPoint rightFirst = null;
		int x = 0;
		for(CenterPoint cp:ls) {
			int cpx = cp.getX();
			if(cpx>x) {
				x = cp.getX();
				rightFirst = cp;
			}
		}
		return rightFirst;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + unitNo;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj==null) {
			return false;
		}
		if(obj==this) {
			return true;
		}
		if(obj instanceof Building) {
			Building o = (Building)obj;
			if(o.getUnitNo()==this.getUnitNo()) {
				return true;
			}
		}
		return false;
	}
	
	
	
	public BuildingBone getBone() {
		return bone;
	}

	public ConstConfig getConstConfig() {
		return constConfig;
	}

	public void setConstConfig(ConstConfig constConfig) {
		this.constConfig = constConfig;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BuildingStatus getStatus() {
		return status;
	}

	public void setStatus(BuildingStatus status) {
		this.status = status;
	}

	public SceneType getScene() {
		return sceneType;
	}

	public void setScene(SceneType scene) {
		this.sceneType = scene;
	}

	public void setBloodBar(BloodBar bloodBar) {
		this.bloodBar = bloodBar;
	}

	public void setBone(BuildingBone bone) {
		this.bone = bone;
	}
	
	
}

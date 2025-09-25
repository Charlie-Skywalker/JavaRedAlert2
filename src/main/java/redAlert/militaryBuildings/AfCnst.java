package redAlert.militaryBuildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.PointUtil;

/**
 * 盟军基地车
 * 
 */
public class AfCnst extends Building{
	/**
	 * shp文件基础名
	 */
	public String basicName = "cnst";
	/**
	 * 阵营 盟军
	 */
	public String team = "g";
	
	/**
	 * 夹箱子动画帧
	 */
	public List<ShapeUnitFrame> fetchCrateFrames;
	
	public AfCnst(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(centerPoint,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfCnst;
		super.height = 70;
		if(sceneType==SceneType.TEM) {
			super.constShpFilePrefix = team + temMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggcnst");
			super.aniShpPrefixLs.add("ggcnst_a");
		}
		if(sceneType==SceneType.URBAN) {
			super.constShpFilePrefix = team + urbMark + basicName + "mk";
			super.aniShpPrefixLs.add("ggcnst");
			super.aniShpPrefixLs.add("ggcnst_a");
		}
		if(sceneType==SceneType.SNOW) {
			super.constShpFilePrefix = team + snoMark + basicName + "mk";
			super.aniShpPrefixLs.add("gacnst");
			super.aniShpPrefixLs.add("gacnst_a");
		}
		//定义显示名称
		super.unitName = "盟军基地车";
		//夹箱子动画
		if(sceneType==SceneType.TEM || sceneType==SceneType.URBAN) {
			this.fetchCrateFrames = ShpResourceCenter.loadShpResource("ggcnst_b", sceneType.getPalPrefix()).subList(0, 19);
		}
		if(sceneType==SceneType.SNOW) {
			this.fetchCrateFrames = ShpResourceCenter.loadShpResource("gacnst_b", sceneType.getPalPrefix()).subList(0, 19);
		}
		
	}
	
	/**
	 * 获取建筑占地中限制建筑进入的菱形中心点
	 * 只是恰好人物也进入不了建筑限制区域
	 */
	public List<CenterPoint> getNoConstCpList() {
		int centerX = centerOffX + super.getPositionX();
		int centerY = centerOffY + super.getPositionY();
		List<CenterPoint> result = new ArrayList<>();
		CenterPoint center = PointUtil.getCenterPoint(centerX, centerY);
		result.add(center);
		result.add(center.getLeft());
		result.add(center.getLeftDn());
		result.add(center.getDn());
		result.add(center.getRightDn());
		result.add(center.getRight());
		result.add(center.getRightUp());
		result.add(center.getUp());
		result.add(center.getLeftUp());
		
		result.add(center.getLeft().getLeftDn());
		result.add(center.getLeftDn().getLeftDn());
		result.add(center.getLeftDn().getDn());
		result.add(center.getRight().getRightDn());
		result.add(center.getRightDn().getRightDn());
		result.add(center.getDn().getRightDn());
		result.add(center.getDn().getDn());
		return result;
	}
	
	/**
	 * 获取建筑占地中限制载具进入的菱形中心点
	 */
	@Override
	public List<CenterPoint> getNoVehicleCpList() {
		return getNoConstCpList();
	}
	
	/**
	 * 获取建筑的阴影区域
	 */
	@Override
	public List<CenterPoint> getShadownCpList(){
		CenterPoint cp = PointUtil.getCenterPoint(positionX+centerOffX, positionY+centerOffY);
		List<CenterPoint> list = new ArrayList<>();
		CenterPoint cp1 = cp.getLeftUp().getLeftUp();
		CenterPoint cp2 = cp.getUp().getLeftUp();
		CenterPoint cp3 = cp.getUp().getUp();
		CenterPoint cp4 = cp.getUp().getRightUp();
		CenterPoint cp5 = cp.getRightUp().getRightUp();
		CenterPoint cp6 = cp.getUp().getRightUp().getRightUp();
		CenterPoint cp7 = cp.getRightUp().getRightUp().getRightUp();
		CenterPoint cp8 = cp.getRight().getRightUp();
		list.add(cp1);
		list.add(cp2);
		list.add(cp3);
		list.add(cp4);
		list.add(cp5);
		list.add(cp6);
		list.add(cp7);
		list.add(cp8);
		return list;
	}
	
	/**
	 * 展示盟军主基地的夹箱子动画
	 */
	public boolean toFetchCrate = false;
	/**
	 * 
	 */
	public int fetchIndex = 0;
	
	/**
	 * 在原有基础上进行重绘
	 * 增加一个状态信息
	 */
	@Override
	public void calculateNextFrame() {
		
		super.calculateNextFrame();
		
		//夹箱子动画
		if(toFetchCrate) {
			if(ShapeUnitResourceCenter.isPowerOn) {
				ShapeUnitFrame suf = fetchCrateFrames.get(fetchIndex);
				BufferedImage img = suf.getImg();
				BufferedImage curImg = curFrame.getImg();
				Graphics2D g2d = curImg.createGraphics();
				g2d.drawImage(img, 0, 0, null);
				g2d.dispose();
				giveFrameUnitColor(curImg,suf);//上阵营色
				
				fetchIndex++;
				
				if(fetchIndex>=fetchCrateFrames.size()) {
					toFetchCrate = false;
					fetchIndex=0;
				}
			}
		}
	}
	public boolean isToFetchCrate() {
		return toFetchCrate;
	}
	public void setToFetchCrate(boolean toFetchCrate) {
		this.toFetchCrate = toFetchCrate;
	}
	
	
}

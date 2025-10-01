package redAlert.militaryBuildings;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import redAlert.Constructor;
import redAlert.MouseEventDeal;
import redAlert.ShapeUnitFrame;
import redAlert.enums.ConstConfig;
import redAlert.enums.UnitColor;
import redAlert.resourceCenter.ShapeUnitResourceCenter;
import redAlert.resourceCenter.ShpResourceCenter;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.MovableUnit;
import redAlert.shapeObjects.TankExpandable;
import redAlert.shapeObjects.vehicle.Mcv;
import redAlert.utilBean.CenterPoint;
import redAlert.utils.MoveUtil;
import redAlert.utils.PointUtil;

/**
 * 盟军基地车
 * 
 */
public class AfCnst extends Building implements TankExpandable {
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

	/**
	 * 是否可变回基地车
	 */
	public boolean canUnexpand = true;

	/**
	 * 基地的状态
	 */
	public int expandStatus = TANK_STATUS_NORMAL;//正常状态

	// 对应基地车的坐标
	public int mcvX;
	public int mcvY;


	public Mcv targetUnExpandUnit = null;//变回基地车的对象

	public CenterPoint targetUnExpandUnitMoveTargetCp = null;//变回基地车后移动到的位置
	
	
	public AfCnst(SceneType sceneType,UnitColor unitColor,int mouseX,int mouseY) {
		this(PointUtil.getCenterPoint(mouseX, mouseY),sceneType,unitColor);
	}
	public AfCnst(CenterPoint centerPoint,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		int positionX = centerPoint.getX()-centerOffX;
		int positionY = centerPoint.getY()-centerOffY;
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	public AfCnst(int positionX,int positionY,SceneType sceneType,UnitColor unitColor) {
		initShpSource(sceneType);
		super.initBuildingValue(positionX,positionY,sceneType,unitColor);
	}
	
	/**
	 * 此建筑独有的一些参数
	 */
	public void initShpSource(SceneType sceneType) {
		super.constConfig = ConstConfig.AfCnst;
		super.height = 70;
		setCenterOffX(139);
		setCenterOffY(153);
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
		CenterPoint center = PointUtil.fetchCenterPoint(centerX, centerY);
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
		if(expandStatus==TANK_STATUS_UNEXPANDING) {
			if(this.stage!=BuildingStage.Selling) {
				this.setStage(BuildingStage.Selling);
			}
			super.calculateNextFrame();
			return;
		}
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

	/**
	 * 基地占16个格子，取消部署后基地车总是面向方向6，占以下两格
	 * __________________
	 * \___\____\___\___\
	 *  \____\___\_*__\*__\
	 *   \____\____\____\___\
	 *    \____\____\_____\___\
	 *  //TODO:如何计算基地车的位置使得画面和取消部署的动画最后一帧的基地车位置一致？目前有一帧作用不自然的情况
	 *  //TODO:当触发”指定位置不可达“时，基地车可能会消失？ 还未找到bug原因
	 */

	@Override
	public void afterBuildingDestroy(){
		// 区分一下解除部署和被卖
		if (this.expandStatus == TANK_STATUS_UNEXPANDING) {
			CenterPoint centerPoint = PointUtil.getCenterPoint(this.mcvX, this.mcvY);
			// 在这初始化
			targetUnExpandUnit.init(centerPoint.x,  centerPoint.y, this.unitColor);
			targetUnExpandUnit.setCurTurn(6);
			targetUnExpandUnit.setTargetTurn(6);
			Constructor.putOneShapeUnit(targetUnExpandUnit);
			// 在这移动
			Thread thread = new Thread() {
				public void run() {
					MoveUtil.move(targetUnExpandUnit, targetUnExpandUnitMoveTargetCp);
				}
			};
			MouseEventDeal.threadPoolExecutor.execute(thread);

		}else{
			super.afterBuildingDestroy();
		}
	}

	@Override
	public void expand() {

	}

	@Override
	public void unexpand() {
		transferStatus(TankExpandable.TANK_STATUS_UNEXPANDING);
	}

	@Override
	public boolean isExpandable() {
		return false;
	}

	@Override
	public boolean isUnExpandable() {
		return canUnexpand;
	}


	@Override
	public void unexpandAndTransfer(MovableUnit targetUnit, CenterPoint moveTargetCp) {
		// 卖掉基地然后放个基地车
		Constructor.playOneMusic("uselbuil");
		this.unexpand();
		this.targetUnExpandUnit = (Mcv) targetUnit;
		this.targetUnExpandUnitMoveTargetCp = moveTargetCp;

	}

	@Override
	public MovableUnit getUnexpandUnit() {
		// 先不要初始化


		return new Mcv();
	}


	//TODO:

	@Override
	public Building getExpandBuilding() {
		return this;
	}

	@Override
	public void transferStatus(int newStatus) {
		this.expandStatus = newStatus;
	}

	@Override
	public int getExpandStatus() {
		return expandStatus;
	}
}

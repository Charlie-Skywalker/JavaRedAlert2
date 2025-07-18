package redAlert.utilBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

import redAlert.enums.BuildingAreaType;
import redAlert.enums.OverlayType;
import redAlert.enums.RampType;
import redAlert.enums.TerrainType;
import redAlert.shapeObjects.Building;
import redAlert.shapeObjects.ShapeUnit;
import redAlert.shapeObjects.Soldier;
import redAlert.shapeObjects.Vehicle;
import redAlert.shapeObjects.Vehicle.EngineStatus;
import redAlert.utils.PointUtil;

/**
 * 表示一个中心点对象
 * 
 * 红警中的重要概念之一,表示30像素横半轴长、15像素竖半轴长矩形的中心点
 * 移动单位的占位、寻路算法、建筑占位等功能依赖这个基本概念
 * 
 * 注意:本类只对外提供地块是否能使用、是否能占用、获取地块上单位的方法
 * 但是地块属性AreaType、OnAreaType 由类内部管理，不对外暴露
 * 
 */
public class CenterPoint {
	
	private static final int ox = 30;//菱形横半轴长
	private static final int oy = 15;//菱形竖半轴长
	
	/**
	 * 这个块是否在其他物体的遮挡区域中
	 */
	public boolean isInShadow = false;
	/**
	 * 并发处理锁
	 * 增删改查载具、修改地块类型等都需要上锁 避免冲突
	 */
	public ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 坡地类型,默认是平地
	 */
	public RampType rampType = RampType.Flat;
	/**
	 * 地形类型,默认是野地
	 */
	public TerrainType terrainType = TerrainType.Rough;
	/**
	 * 覆盖物类型,默认没有覆盖物
	 */
	public OverlayType overlayType = OverlayType.None;
	/**
	 * 建筑区域类型,默认没有建筑
	 */
	public BuildingAreaType buildingAreaType = BuildingAreaType.None;
	/**
	 * 中心点上的建筑引用
	 */
	public Building building;
	/**
	 * 中心点上的载具对象
	 * 只有当载具的中心位于中心点坐标上,才会赋予载具引用
	 */
	public Vehicle vehicle;
	/**
	 * 中心点所在菱形块上的步兵引用
	 */
	public List<Soldier> soldiers = new ArrayList<>();
	
	
	
	/**
	 * 中心块类型
	 * 默认是平地
	 */
//	public AreaType areaType = AreaType.Flat;
	/**
	 * 地面单位类型
	 * 默认为空
	 */
//	public OnAreaType onAreaType = OnAreaType.Empty;
	
	/**
	 * 中心点横坐标
	 * 该坐标为地图坐标
	 */
	public int x;
	/**
	 * 中心点纵坐标
	 * 该坐标为地图坐标
	 */
	public int y;
	/**
	 * 
	 * 
	 * 预约占用标志
	 * 当一个中心点被载具占用，且位于此中心点上载具正在移动中,其他载具不可以继续占领此中心点，需要阻塞等待其他载具释放占领标志
	 * 当一个中心点被载具占用，且此中心点上没有载具，则其他载具不可以继续占领此中心点，其他载具需要阻塞等待至其他载具释放占领标志
	 * 当一个中心点被载具占用，且此中心点上的载具不在移动中(实际上应该表述为即将结束移动),则其他载具不能占领此中心点，需要重新寻路
	 * 
	 * 当一个中心点未被载具占领，且此中心点上没有载具，其他载具可以申请占用,申请通过可以驶向此中心点
	 * 当一个中心点未被载具占领，且此中心点上有移动中载具，其他载具可以申请占用
	 * 当一个中心点未被载具占领，且此中心点上有静止载具，其他载具不可以申请占用
	 * 
	 * 不允许对一个中心点重复占用  这个值最大会变成1
	 */
	public volatile boolean bookedFlag = false;
	/**
	 * 预约占用者
	 */
	public volatile Vehicle booker = null;
	
	
	
	/**
	 * 地图标志 地块对应的瓦片
	 * 根据这个标志来绘制地形
	 */
	public int tileIndex = 0;//地图标志
	
	
	/**
	 * 申请占用
	 * 占用成功的条件是booker=null 并且bookedFlag=0
	 * @return true表示申请通过
	 */
	public boolean addBook(Vehicle vehicle) {
		synchronized (this) {
			boolean getLock = lock.tryLock();
			if(getLock) {
				if(booker==null) {
					bookedFlag = true;
					booker = vehicle;
					lock.unlock();
					return true;
				}else {
					lock.unlock();
					return false;
				}
			}else {
				return false;
			}
		
		}
	}
	/**
	 * 释放占用   防止把别人的占用的给释放了,所以传个参
	 * @return
	 */
	public boolean exitBook(Vehicle vehicle) {
		try {
			lock.lock();
			if(vehicle.equals(booker)) {//身份核验一致才行
				bookedFlag = false;
				booker = null;
				return true;
			}else {
				return false;
			}
		}catch (Exception e) {
			
		}finally {
			lock.unlock();
		}
		return false;
	}
	/**
	 * 是否被某个载具占用
	 * @return
	 */
	public boolean isBookedBy(Vehicle vehicle) {
		if(vehicle.equals(booker) && bookedFlag) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 是否被占用
	 * @return
	 */
	public boolean isBooked() {
		return bookedFlag;
	}
	
	/**
	 * 是否满足建筑安放条件
	 */
	public boolean isBuildingCanPutOn() {
		try {
			lock.lock();
			if(!rampType.buildingCondition()) {//是平地
				return false;
			}
			if(!terrainType.buildingCondition()) {//无特殊地形
				return false;
			}
			if(!overlayType.buildingCondition()) {//无特殊覆盖物
				return false;
			}
			if(!buildingAreaType.buildingCondition()) {//无建筑
				return false;
			}
			if(building!=null) {//无建筑
				return false;
			}
			if(vehicle!=null) {//无载具
				return false;
			}
			if(!soldiers.isEmpty()) {//无士兵
				return false;
			}
			if(bookedFlag) {//无载具的预约占用
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}
	/**
	 * 普通载具是否能进入
	 */
	public boolean isVehicleCanOn() {
		try {
			lock.lock();
			if(!rampType.vehicleCondition()) {
				return false;
			}
			if(!terrainType.vehicleCondition()) {
				return false;
			}
			if(!overlayType.vehicleCondition()) {
				return false;
			}
			if(!buildingAreaType.vehicleCondition()) {
				return false;
			}
			if(vehicle!=null) {//无载具
				return false;
			}
			if(!soldiers.isEmpty()) {//无士兵
				return false;
			}
			if(bookedFlag) {//无载具的预约占用
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}
	
	/**
	 * 载具一次寻路时  载具是否可入这个中心点
	 * 此中心点是平地或坡地的前提下，
	 *    此中心点地面单位类型为空、磅秤，或中心点存在移动中的单位,则认为可以作为寻路点
	 * 
	 * 注意：有移动中的单位,可以作为寻路点,这样可以让群体移动时整整齐齐
	 * 
	 */
	public boolean isVehicleCanOnXunLuFirst() {
		try {
			lock.lock();
			if(!rampType.vehicleCondition()) {
				return false;
			}
			if(!terrainType.vehicleCondition()) {
				return false;
			}
			if(!overlayType.vehicleCondition()) {
				return false;
			}
			if(!buildingAreaType.vehicleCondition()) {
				return false;
			}
			if(!soldiers.isEmpty()) {//无士兵
				return false;
			}
			//移动中的载具也可以作为寻路节点
			if(vehicle!=null) {
				if(vehicle.getEngineStatus()==EngineStatus.Stopped) {//存在停止的载具
					return false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}
	/**
	 * 载具二次寻路时   载具是否可入这个中心点
	 * 此中心点是平地或坡地的前提下，
	 *    此中心点地面单位类型为空、磅秤、载具，则认为可以作为寻路点
	 * 
	 * 注意：有静止单位也可以作为寻路点，移动时遇上了让单位避让
	 * 
	 */
	public boolean isVehicleCanOnXunLuSecond() {
		try {
			lock.lock();
			if(!rampType.vehicleCondition()) {
				return false;
			}
			if(!terrainType.vehicleCondition()) {
				return false;
			}
			if(!overlayType.vehicleCondition()) {
				return false;
			}
			if(!buildingAreaType.vehicleCondition()) {
				return false;
			}
			if(!soldiers.isEmpty()) {//无士兵
				return false;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}
	
	/**
	 * 是否存在载具
	 */
	public boolean isExistVehicle() {
		return vehicle!=null;
	}
	
	/**
	 * 是否存在正在旋转车身的载具
	 */
	public boolean isExistTurningVehicle() {
		try {
			lock.lock();
			if(vehicle.targetTurn!=vehicle.curTurn) {
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return false;
	}
	
	/**
	 * 是否存在速度大于0的载具
	 */
	public boolean isExistSpeedVehicle() {
		lock.lock();
		try {
			if(vehicle.speed>0) {
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return false;
	}
	
	/**
	 * 是否存在可单选单位
	 */
	public boolean isExistSingleSelectUnit() {
		if(building!=null) {
			return true;
		}
		if(vehicle!=null) {
			return true;
		}
		if(!soldiers.isEmpty()) {
			return true;
		}
		return false;
	}
	
	/**
	 * 鼠标单击获取此地单位
	 */
	public ShapeUnit mouseClickGetUnit() {
		if(vehicle!=null) {
			return vehicle;
		}else if(building!=null) {
			return building;
		}else if(!soldiers.isEmpty()) {
			return soldiers.get(0);
		}
		
		return null;
	}
	/**
	 * 步兵是否能进入
	 * 有载具的中心点不能进入
	 * 有三个步兵的格子不能进入
	 */
	public boolean isSoldierCanOn() {
		try {
			lock.lock();
			if(!rampType.soldierCondition()) {
				return false;
			}
			if(!terrainType.soldierCondition()) {
				return false;
			}
			if(!overlayType.soldierCondition()) {
				return false;
			}
			if(!buildingAreaType.soldierCondition()) {//无建筑
				return false;
			}
			if(building!=null) {//无建筑
				return false;
			}
			if(vehicle!=null) {//无载具
				return false;
			}
			if(bookedFlag) {//无载具的预约占用
				return false;
			}
			if(!soldiers.isEmpty()) {//士兵数<3
				if(soldiers.size()>=3) {
					return false;
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
		return true;
	}
	
	
	
	/**
	 * 添加建筑
	 */
	public void addBuilding(Building building,BuildingAreaType buildingAreaType) {
		try {
			lock.lock();
			this.building = building;
			this.buildingAreaType = buildingAreaType;
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	/**
	 * 添加载具  需要考虑是在空白地面  还是在建筑面
	 */
	public void addVehicle(Vehicle vehicle) {
		try {
			lock.lock();
			this.vehicle = vehicle;
			if(buildingAreaType==BuildingAreaType.WeighBridge) {
				buildingAreaType = BuildingAreaType.Vehicle_MBuilding;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	/**
	 * 添加步兵
	 */
	public void addSoldier(Soldier soldier) {
		try {
			lock.lock();
			if(isSoldierCanOn()) {
				soldiers.add(soldier);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	/**
	 * 移除单位   需要考虑是在空白地面  还是在建筑面
	 */
	public void removeUnit(ShapeUnit shapeUnit) {
		try {
			lock.lock();
			if(shapeUnit instanceof Vehicle) {
				vehicle = null;
				if(buildingAreaType==BuildingAreaType.Vehicle_MBuilding) {
					buildingAreaType = BuildingAreaType.WeighBridge;
				}
			}
			if(shapeUnit instanceof Soldier) {
				soldiers.remove(shapeUnit);
			}
			if(shapeUnit instanceof Building) {
				building = null;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	

	
	/**
	 * 确认点坐标就是中心点的情况下使用
	 * @param x
	 * @param y
	 */
	public CenterPoint(int x,int y) {
		this(x,y,false);
	}
	/**
	 * 不确认点坐标是中心点时的
	 */
	public CenterPoint(int oriX,int oriY,boolean relocation) {
		if(relocation) {
			CenterPoint cp = PointUtil.getCenterPoint(oriX, oriY);//计算原始点对应的中心点
			this.x = cp.getX();
			this.y = cp.getY();
		}else {
			this.x = oriX;
			this.y = oriY;
		}
	}
	
	
	
	/**
	 * 获取一个中心点的左上中心点
	 */
	public CenterPoint getLeftUp() {
		int x1 = x - ox;
		int y1 = y - oy;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的左中心点
	 */
	public CenterPoint getLeft() {
		int x1 = x - ox*2;
		int y1 = y;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的左下中心点
	 */
	public CenterPoint getLeftDn() {
		int x1 = x - ox;
		int y1 = y + oy;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的下中心点
	 */
	public CenterPoint getDn() {
		int x1 = x;
		int y1 = y + oy*2;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右下中心点
	 */
	public CenterPoint getRightDn() {
		int x1 = x + ox;
		int y1 = y + oy;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右中心点
	 */
	public CenterPoint getRight() {
		int x1 = x + ox*2;
		int y1 = y;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的右上中心点
	 */
	public CenterPoint getRightUp() {
		int x1 = x + ox;
		int y1 = y - oy;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	/**
	 * 获取一个中心点的上中心点
	 */
	public CenterPoint getUp() {
		int x1 = x;
		int y1 = y - oy*2;
		return PointUtil.fetchCenterPoint(x1, y1);
	}
	
	public LittleCenterPoint getLeftLittleCenterPoint() {
		int x1 = x - 16;
		return PointUtil.fetchLittleCenterPoint(x1, y);
	}
	public LittleCenterPoint getRightLittleCenterPoint() {
		int x1 = x + 16;
		return PointUtil.fetchLittleCenterPoint(x1, y);
	}
	public LittleCenterPoint getUpLittleCenterPoint() {
		int y1 = y - 8;
		return PointUtil.fetchLittleCenterPoint(x, y1);
	}
	public LittleCenterPoint getDownLittleCenterPoint() {
		int y1 = y + 8;
		return PointUtil.fetchLittleCenterPoint(x, y1);
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public boolean isInShadow() {
		return isInShadow;
	}
	public void setInShadow(boolean isInShadow) {
		this.isInShadow = isInShadow;
	}
	public int getTileIndex() {
		return tileIndex;
	}
	public void setTileIndex(int tileIndex) {
		this.tileIndex = tileIndex;
	}
	public Building getBuilding() {
		return building;
	}
	public void setBuilding(Building building) {
		this.building = building;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public List<Soldier> getSoldiers() {
		return soldiers;
	}
	public void setSoldiers(List<Soldier> soldiers) {
		this.soldiers = soldiers;
	}
	public RampType getRampType() {
		return rampType;
	}
	public void setRampType(RampType rampType) {
		this.rampType = rampType;
	}
	public TerrainType getTerrainType() {
		return terrainType;
	}
	public void setTerrainType(TerrainType terrainType) {
		this.terrainType = terrainType;
	}
	public OverlayType getOverlayType() {
		return overlayType;
	}
	public void setOverlayType(OverlayType overlayType) {
		this.overlayType = overlayType;
	}
	public BuildingAreaType getBuildingAreaType() {
		return buildingAreaType;
	}
	public void setBuildingAreaType(BuildingAreaType buildingAreaType) {
		this.buildingAreaType = buildingAreaType;
	}
	
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CenterPoint other = (CenterPoint) obj;
		return x == other.x && y == other.y;
	}
	
	
	@Override
	public String toString() {
		return x+"," + y;
	}
	

}

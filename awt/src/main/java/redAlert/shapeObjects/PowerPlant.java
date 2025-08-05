package redAlert.shapeObjects;

/**
 * 表示发电设备
 * 盟军发电厂、苏军磁能反应堆、核反应堆应实现此接口
 */
public interface PowerPlant {

	/**
	 * 获取发电量
	 */
	public int getPowerGeneration();
}

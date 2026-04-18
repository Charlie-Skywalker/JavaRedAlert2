package redAlert.enums;

/**
 * 建筑状态
 */
public enum BuildingStatus{
	
	DEMAGED("受损"),UNDEMAGED("完好");
	
	private final String cnDesc;
	
	BuildingStatus(String cnDesc){
		this.cnDesc = cnDesc;
	}
}

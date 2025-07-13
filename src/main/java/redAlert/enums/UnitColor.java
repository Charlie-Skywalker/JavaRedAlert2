package redAlert.enums;

/**
 * 阵营颜色
 */
public enum UnitColor{
	Red("红"),Blue("蓝"),Green("蓝"),Yellow("黄"),Purple("紫"),LightBlue("淡蓝"),Gray("中立灰"),Orange("橙猫猫"),Pink("粉");
	private final String colorName;
	
	UnitColor(String colorName){
		this.colorName = colorName;
	}
}

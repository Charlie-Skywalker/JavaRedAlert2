package redAlert.enums;

/**
 * 环境风格
 * 
 * 目前只用在建筑上
 */
public enum SceneType{
	URBAN("城市","uniturb"),SNOW("雪地","unitsno"),TEM("野外","unittem"),ANIM("动画","anim");
	
	/**
	 * 描述
	 */
	private final String cnDesc;
	/**
	 * 色盘文件前缀
	 */
	private final String palPrefix;
	SceneType(String cnDesc,String palPrefix){
		this.cnDesc = cnDesc;
		this.palPrefix = palPrefix;
	}
	public String getCnDesc() {
		return cnDesc;
	}
	public String getPalPrefix() {
		return palPrefix;
	}
}

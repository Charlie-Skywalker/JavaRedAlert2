package redAlert.enums;

/**
 * 环境风格
 * 
 */
public enum SceneType{
	URBAN("城市","uniturb","u"),SNOW("雪地","unitsno","a"),TEM("野外","unittem","t"),ANIM("动画","anim","");
	
	/**
	 * 描述
	 */
	private final String cnDesc;
	/**
	 * 色盘文件前缀
	 */
	private final String palPrefix;
	/**
	 * shp文件表示的环境标识
	 */
	private final String sceneMark;
	
	
	SceneType(String cnDesc,String palPrefix,String sceneMark){
		this.cnDesc = cnDesc;
		this.palPrefix = palPrefix;
		this.sceneMark = sceneMark;
	}
	public String getCnDesc() {
		return cnDesc;
	}
	public String getPalPrefix() {
		return palPrefix;
	}
	public String getSceneMark() {
		return sceneMark;
	}
	
}

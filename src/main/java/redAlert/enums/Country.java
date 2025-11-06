package redAlert.enums;

/**
 * 国家
 */
public enum Country {

	/**
	 * 盟军： 英法美德韩
	 * 苏军： 苏联、利比亚、古巴、伊拉克
	 * 
	 */
	USA("美帝"),France("法鸡"),UK("英吉利"),Germany("德意志"),Korea("南朝鲜"),
	CCCP("苏联"),Libya("利比亚"),Iraq("伊拉克"),Cuba("古巴");
	
	public final String desc;
	
	Country(String desc) {
		this.desc = desc;
	}
	
	/**
	 * 是否等于其中的某个
	 */
	public boolean in(Country...countries) {
		for(Country country:countries) {
			if(this==country) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 是否是盟军
	 */
	public boolean isAf(Country country) {
		return country==Country.USA || country==Country.France || country==Country.UK || country==Country.Germany || country==Korea;
	}
	/**
	 * 是否是苏军
	 */
	public boolean isSf(Country country) {
		return country==Country.CCCP || country==Country.Libya || country==Country.Iraq || country==Country.Cuba;
	}
}

package redAlert.enums;

/**
 * 步兵资源的信息汇总
 */
public enum SoldierEnum {

	
	AfGi("美国大兵",200,"giicon"),
	AfAdog("军犬",200,"adogicon"),
	AfJjet("火箭飞行兵",600,"jjeticon"),
	AfSnip("狙击手",600,"snipicon"),
	AfSpy("间谍",1000,"spyicon"),
	AfSeal("海豹部队",1000,"sealicon"),
	AfTany("谭雅",1000,"tanyicon"),
	AfCleg("时空军团兵",1500,"clegicon"),
	
	SfDog("警犬",200,"dogicon"),
	SfE2("动员兵",100,"e2icon"),
	SfDeso("辐射工兵",800,"desoicon"),
	SfFltk("防空步兵",800,"flkticon"),
	SfIvan("疯狂伊文",600,"ivanicon"),
	SfShk("磁暴步兵",500,"shkicon"),
	
	SfTrst("恐怖分子",600,"trsticon"),
	SfYuri("尤里",1500,"yuriicon"),
	
	AfCcom("时空突击队",2000,"ccomicon"),//萌偷萌
	SfIvnc("超时空伊文",2000,"ivncicon"),//苏偷萌
	Psic("心灵突击队",1000,"psicicon"),//萌偷苏
	SfYurp("尤里改",3000,"yurpicon"),//苏偷苏
	Engn("工程师",500,"engnicon");
	
	
	public final String soldierName;
	public final int price;
	public final String shpIconPrefix;
	
	SoldierEnum(String soldierName,int price,String shpIconPrefix){
		this.soldierName = soldierName;
		this.price = price;
		this.shpIconPrefix = shpIconPrefix;
	}
}

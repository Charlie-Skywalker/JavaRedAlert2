package redAlert.enums;

/**
 * constName 建筑的名称,鼠标点上后显示的名称以及图标上的名称
 * int fxNum 从西南向东北数占几个菱形（此方向即纺射X轴）
 * int fyNum 从东南向西北数占几个菱形（此方向即仿射Y轴）
 * int price 造价
 * String shpIconPrefix 对应图标的shp文件前缀
 * int maxBlood 建筑最大血量  这个变量似乎不该在这里配置,这个变量与fxNum有关
 * int powerLoad 电力负载
 * 
 * 此类已废弃
 * 枚举类的变量无法通过Java方法初始化,因此建筑的配置信息设置为枚举是不合适的
 * 考虑到战役模式或者其他特殊模式,建筑的配置信息是不同的,所以必须可以动态配置
 * 
 * 
 */
@Deprecated
public enum ConstEnum {

	
	AfCnst("盟军基地车",4,4,3000,"",3000,0);
//	AfPowr("发电厂",2,2,800,"powricon",1500,0),AfPile("盟军兵营",2,3,500,"brrkicon",1500,10),
//	AfRefn("盟军矿石精炼厂",3,4,2000,"reficon",2200,50),AfWeap("盟军战车工厂",3,5,2000,"gwepicon",2200,25),
//	AfAirc("空指部",2,3,1000,"heliicon",1500,50),AfYard("盟军造船厂",4,4,1000,"ayaricon",3000,25),
//	AfDept("盟军修理厂",3,3,800,"fixicon",2200,25),AfTech("盟军作战实验室",2,3,2000,"techicon",1500,100),
//	AfOrep("矿石精炼器",3,3,2500,"gorep",2200,200),
//	
//	AfPill("机枪碉堡",1,1,500,"pillicon",700,0),AfSam("爱国者导弹",1,1,1000,"samicon",700,50),
//	AfPris("光棱塔",1,1,1500,"prisicon",700,75),AfGcan("巨炮",2,2,1500,"gcanicon",1500,100),
//	AfGap("裂缝产生器",1,1,1000,"gapicon",700,100),AfCsph("超时空转换器",3,4,2500,"csphicon",2200,200),
//	AfWeth("天气控制器",3,3,5000,"wethicon",2200,200),AfSpst("间谍卫星",2,2,1500,"asaticon",1500,100),
//	AfWall("盟军围墙",1,1,100,"wallicon",700,0),
//	
//	SfCnst("苏军基地车",4,4,3000,"",3000,0),
//	SfNpwr("磁能反应炉",2,3,600,"npwricon",1500,0),SfHand("苏联兵营",2,2,500,"handicon",1500,10),
//	SfNref("苏联矿石精炼厂",3,4,2000,"nreficon",2200,50),SfNwep("苏联战车工厂",3,5,2000,"nwepicon",2200,25),
//	SfNrad("雷达",2,2,600,"nradicon",1500,50),SfYard("船坞",4,4,1000,"yardicon",3000,20),
//	SfRfix("苏联维修厂",3,4,800,"rfixicon",2200,20),SfNtch("苏联作战实验室",3,3,600,"ntchicon",2200,100),
//	SfNrct("核子反应炉",4,4,1000,"nrcticon",3000,0),SfClon("复制中心",2,2,2500,"clonicon",1500,200),
//	
//	SfPlt("哨界砲",1,1,500,"plticon",700,0),SfFlak("防空砲",1,1,1000,"flakicon",700,50),
//	SfTlsa("磁暴线圈",1,1,1500,"tlsaicon",700,75),
//	SfPsis("心灵探测器",2,2,1000,"psisicon",1500,50),SfIron("铁幕装置",3,3,2500,"ironicon",2200,200),
//	SfMisl("核弹发射井",3,3,5000,"msslicon",2200,200),
//	SfNwal("苏联围墙",1,1,100,"nwalicon",700,0);
	
	
	
	
	
	
	public final String constName;
	public final int fxNum;
	public final int fyNum;
	public final int price;
	public final String shpIconPrefix;
	public final int maxHp;
	public final int powerLoad;
	
	ConstEnum(String constName,int fxNum,int fyNum,int price,String shpIconPrefix,int maxHp,int powerLoad){
		this.constName = constName;
		this.fxNum = fxNum;
		this.fyNum = fyNum;
		this.price = price;
		this.shpIconPrefix = shpIconPrefix;
		this.maxHp = maxHp;
		this.powerLoad = powerLoad;
	}
}

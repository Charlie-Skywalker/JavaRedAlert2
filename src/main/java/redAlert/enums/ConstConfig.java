package redAlert.enums;

/**
 * 建筑类的配置信息大全
 */
public class ConstConfig {

	
	/*
	 * 盟军主建筑
	 */
	public static ConstConfig AfCnst;
	public static ConstConfig AfPowr;public static ConstConfig AfPile;
	public static ConstConfig AfRefn;public static ConstConfig AfWeap;
	public static ConstConfig AfAirc;public static ConstConfig AfYard;
	public static ConstConfig AfDept;public static ConstConfig AfTech;
	public static ConstConfig AfOrep;
	/**
	 * 盟军防御建筑
	 */
	public static ConstConfig AfWall;public static ConstConfig AfPill;
	public static ConstConfig AfSam;public static ConstConfig AfPris;
	public static ConstConfig AfGcan;public static ConstConfig AfGap;
	public static ConstConfig AfSpst;public static ConstConfig AfCsph;
	public static ConstConfig AfWeth;
	/**
	 * 苏军主建筑
	 */
	public static ConstConfig SfCnst;
	public static ConstConfig SfNpwr;public static ConstConfig SfHand;
	public static ConstConfig SfNref;public static ConstConfig SfNwep;
	public static ConstConfig SfNrad;public static ConstConfig SfYard;
	public static ConstConfig SfRfix;public static ConstConfig SfNtch;
	public static ConstConfig SfNrct;public static ConstConfig SfClon;
	/**
	 * 苏军防御建筑
	 */
	public static ConstConfig SfNwal;public static ConstConfig SfPlt;
	public static ConstConfig SfFlak;public static ConstConfig SfTlsa;
	public static ConstConfig SfPsis;public static ConstConfig SfIron;
	public static ConstConfig SfMisl;
	
	/**
	 * 民用建筑物
	 */
	public static ConstConfig CityNewy10;
	public static ConstConfig CityWash07;
	public static ConstConfig CityWash16;
	
	static {
		AfCnst = new ConstConfig("g","cnst","盟军建造场",4,4,3000,"",3000,0,true);
		AfPowr = new ConstConfig("g","powr","发电厂",2,2,800,"powricon",1500,0,true);
		AfPile = new ConstConfig("g","pile","盟军兵营",2,3,500,"brrkicon",1500,10,true);
		AfRefn = new ConstConfig("g","refn","盟军矿石精炼厂",3,4,2000,"reficon",2200,50,true);
		AfWeap = new ConstConfig("g","weap","盟军战车工厂",3,5,2000,"gwepicon",2200,25,true);
		AfAirc = new ConstConfig("g","airc","空指部",2,3,1000,"heliicon",1500,50,true);
		AfYard = new ConstConfig("g","yard","盟军造船厂",4,4,1000,"ayaricon",3000,25,true);
		AfDept = new ConstConfig("g","dept","盟军修理厂",3,3,800,"fixicon",2200,25,true);
		AfTech = new ConstConfig("g","tech","盟军作战实验室",2,3,2000,"techicon",1500,100,true);
		AfOrep = new ConstConfig("g","orep","矿石精炼器",3,3,2500,"gorep",2200,200,true);
		
		AfWall = new ConstConfig("g","Noooo","盟军围墙",1,1,100,"wallicon",700,0,true);
		AfPill = new ConstConfig("g","pill","机枪碉堡",1,1,500,"pillicon",700,0,true);
		AfSam = new ConstConfig("n","sam","爱国者导弹",1,1,1000,"samicon",700,50,true);
		AfPris = new ConstConfig("g","pris","光棱塔",1,1,1500,"prisicon",700,75,false);
		AfGcan = new ConstConfig("g","gcan","巨炮",2,2,1500,"gcanicon",1500,100,true);
		AfGap = new ConstConfig("g","gap","裂缝产生器",1,1,1000,"gapicon",700,100,true);
		AfSpst = new ConstConfig("g","spst","间谍卫星",2,2,1500,"asaticon",1500,100,false);
		AfCsph = new ConstConfig("g","csph","超时空转换器",3,4,2500,"csphicon",2200,200,false);
		AfWeth = new ConstConfig("g","weth","天气控制器",3,3,5000,"wethicon",2200,200,false);
		
		SfCnst = new ConstConfig("n","Noooo","苏联建造场",4,4,3000,"",3000,0,true);
		SfNpwr = new ConstConfig("n","Noooo","磁能反应炉",2,3,600,"npwricon",1500,0,true);
		SfHand = new ConstConfig("n","Noooo","苏联兵营",2,2,500,"handicon",1500,10,true);
		SfNref = new ConstConfig("n","Noooo","苏联矿石精炼厂",3,4,2000,"nreficon",2200,50,true);
		SfNwep = new ConstConfig("n","Noooo","苏联战车工厂",3,5,2000,"nwepicon",2200,25,true);
		SfNrad = new ConstConfig("n","Noooo","雷达",2,2,600,"nradicon",1500,50,false);
		SfYard = new ConstConfig("n","Noooo","船坞",4,4,1000,"yardicon",3000,20,true);
		SfRfix = new ConstConfig("n","Noooo","苏联维修厂",3,4,800,"rfixicon",2200,20,true);
		SfNtch = new ConstConfig("n","Noooo","苏联作战实验室",3,3,600,"ntchicon",2200,100,true);
		SfNrct = new ConstConfig("n","Noooo","核子反应炉",4,4,1000,"nrcticon",3000,0,true);
		SfClon = new ConstConfig("n","Noooo","复制中心",2,2,2500,"clonicon",1500,200,true);
		
		SfNwal = new ConstConfig("n","Noooo","苏联围墙",1,1,100,"nwalicon",700,0,true);
		SfPlt = new ConstConfig("n","Noooo","哨界砲",1,1,500,"plticon",700,0,true);
		SfFlak = new ConstConfig("n","Noooo","防空砲",1,1,1000,"flakicon",700,50,true);
		SfTlsa = new ConstConfig("n","Noooo","磁暴线圈",1,1,1500,"tlsaicon",700,75,false);
		SfPsis = new ConstConfig("n","Noooo","心灵探测器",2,2,1000,"psisicon",1500,50,false);
		SfIron = new ConstConfig("n","Noooo","铁幕装置",3,3,2500,"ironicon",2200,200,false);
		SfMisl = new ConstConfig("n","Noooo","核弹发射井",3,3,5000,"msslicon",2200,200,false);
		
		CityNewy10 = new ConstConfig("c","newy10","美国电报电话大厦",2,3,0,"",1500,0,true);
		CityWash07 = new ConstConfig("c","wash07","建筑物07",3,3,0,"",2200,0,true);
		CityWash16 = new ConstConfig("c","wash16","史密斯国家博物馆",3,5,0,"",2200,0,true);
	}
	
	/**
	 * 阵营   g表示盟军   n表示苏军   c表示中立建筑
	 */
	public String faction;
	/**
	 * shp文件对应的基础名称
	 */
	public String shpBasicName;
	/**
	 * 显示名称
	 * 右侧Icon上鼠标停留显示名称与左侧建筑上鼠标停留显示名称一致
	 */
	public String constName;
	/**
	 * 从西南向东北数占几个菱形（此方向即纺射X轴）
	 */
	public int fxNum;
	/**
	 * 从东南向西北数占几个菱形（此方向即仿射Y轴）
	 */
	public int fyNum;
	/**
	 * 造价
	 */
	public int price;
	/**
	 * 右侧Icon图标的shp文件前缀
	 */
	public String shpIconPrefix;
	/**
	 * 建筑最大血量  这个变量似乎不该在这里配置,这个变量与fxNum有关
	 */
	public int maxHp;
	/**
	 * 电力负载
	 */
	public int powerLoad;
	/**
	 * 停电是否可工作
	 * 哪些建筑不可以：雷达，光棱塔，间谍卫星，磁暴线圈，心灵探测器，超武
	 */
	public boolean lowPowerWorkable = true;
	
	
	public ConstConfig(String faction,String shpBasicName,String constName,int fxNum,int fyNum,int price,String shpIconPrefix,int maxHp,int powerLoad,boolean lowPowerWorkable){
		this.faction = faction;
		this.shpBasicName = shpBasicName;
		this.constName = constName;
		this.fxNum = fxNum;
		this.fyNum = fyNum;
		this.price = price;
		this.shpIconPrefix = shpIconPrefix;
		this.maxHp = maxHp;
		this.powerLoad = powerLoad;
		this.lowPowerWorkable = lowPowerWorkable;
	}
}

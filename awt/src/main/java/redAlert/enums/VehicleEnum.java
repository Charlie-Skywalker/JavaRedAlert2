package redAlert.enums;

public enum VehicleEnum {
	
	AfAhrv("超时空矿车",1200,"ahrvicon"),
	AfGtnk("灰熊坦克",700,"gtnkicon"),
	AfIfv("多功能步兵战斗车",600,"fvicon"),
	AfTnkd("坦克杀手",900,"tnkdicon"),
	AfBeag("黑鹰战机",1200,"beagicon"),
	AfFalc("入侵者战机",1200,"falcicon"),
	AfSref("光棱坦克",1200,"sreficon"),
	AfRtnk("幻影坦克",1000,"rtnkicon"),
	AfShad("夜鹰直升机",1000,"shadicon"),
	AfMcv("盟军基地车",3000,"mcvicon"),
	AfLand("两栖运输艇",900,"landicon"),
	AfDest("驱逐舰",1000,"desticon"),
	AfDlph("海豚",500,"dlphicon"),
	AfAgis("神盾巡洋舰",1200,"agisicon"),
	AfCarr("航空母舰",2000,"carricon"),
	
	
	
	
	
	
	
	SfHtnk("犀牛坦克",900,"htnkicon"),
	SfZep("基洛夫空艇",2000,"zepicon"),
	SfV3("V3火箭发射车",800,"v3icon"),
	SfTtnk("磁能坦克",1200,"ttnkicon"),
	SfSub("台风级攻击潜艇",1000,"subicon"),
	SfSqd("巨型乌贼",1000,"sqdicon"),
	SfSmcv("苏联基地车",3000,"smcvicon"),
	SfSapc("装甲运兵船",900,"sapcicon"),
	SfMtnk("天晵坦克",1750,"mtnkicon"),
	SfHtk("防空履带车",600,"htkicon"),
	SfHovr("海螺",600,"hovricon"),
	SfDred("无畏级战舰",2000,"dredicon"),
	SfDron("恐怖机器人",500,"dronicon"),
	SfTrka("自爆卡车",1000,"trkaicon"),
	SfHarv("武装采矿车",1200,"harvicon");
	
		
	
	
	public final String vehicleName;
	public final int price;
	public final String shpIconPrefix;
	
	VehicleEnum(String vehicleName,int price,String shpIconPrefix){
		this.vehicleName = vehicleName;
		this.price = price;
		this.shpIconPrefix = shpIconPrefix;
	}
}

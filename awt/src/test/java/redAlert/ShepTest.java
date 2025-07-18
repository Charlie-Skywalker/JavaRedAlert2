package redAlert;

import redAlert.resourceCenter.ShpResourceCenter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ShepTest {
    public static void main(String[] args) {
        long startTimestamp = System.currentTimeMillis();
        
        // testSaveToPng("nukedie", "E:/xxx", Building.SceneType.ANIM.getPalPrefix());
        
        // testSaveToPng("D:\\redAlertFile\\shp\\ntcnstmk.shp","D:/redAlertFile/my/苏联/A0基地展开","ntcnstmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntpowrmk.shp","D:/redAlertFile/my/苏联/A01发电厂","ntpowrmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntrefnmk.shp","D:/redAlertFile/my/苏联/A02采矿场","ntrefnmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\nthandmk.shp","D:/redAlertFile/my/苏联/A03兵营","nthandmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntweapmk.shp","D:/redAlertFile/my/苏联/A04建设工厂","ntweapmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntradrmk.shp","D:/redAlertFile/my/苏联/A05雷达","ntradrmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntyardmk.shp","D:/redAlertFile/my/苏联/A06船坞","ntyardmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntdeptmk.shp","D:/redAlertFile/my/苏联/A07维修场","ntdeptmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\nttechmk.shp","D:/redAlertFile/my/苏联/A08实验室","nttechmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntnrctmk.shp","D:/redAlertFile/my/苏联/A09核子反应堆","ntnrctmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntclonmk.shp","D:/redAlertFile/my/苏联/A10复制中心","ntclonmk");
        
        // testSaveToPng("D:\\redAlertFile\\shp\\ntlasrmk.shp","D:/redAlertFile/my/苏联哨戒炮建造","ntlasrmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntflakmk.shp","D:/redAlertFile/my/苏联防空炮建造","ntflakmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\nttslamk.shp","D:/redAlertFile/my/苏联磁暴线圈建造","nttslamk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntpsismk.shp","D:/redAlertFile/my/苏联心灵探测器建造","ntpsismk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntironmk.shp","D:/redAlertFile/my/苏联铁幕建造","ntironmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntmislmk.shp","D:/redAlertFile/my/苏联核弹建造","ntmislmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntpsybmk.shp","D:/redAlertFile/my/苏联小信标建造","ntpsybmk");
        
        // testSaveToPng("D:\\redAlertFile\\shp\\gtcnstmk.shp","D:/redAlertFile/my/盟军基地展开","gtcnstmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtpowrmk.shp","D:/redAlertFile/my/盟军发电厂建造","gtpowrmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtrefnmk.shp","D:/redAlertFile/my/盟军采矿场建造","gtrefnmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtpilemk.shp","D:/redAlertFile/my/盟军兵营建造","gtpilemk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtweapmk.shp","D:/redAlertFile/my/盟军工厂建造","gtweapmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gaweap_b.shp","D:/redAlertFile/my/盟军工厂建造11","gtweapmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtaircmk.shp","D:/redAlertFile/my/盟军空指部建造","gtaircmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtyardmk.shp","D:/redAlertFile/my/盟军船坞建造","gtyardmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtdeptmk.shp","D:/redAlertFile/my/盟军维修场建造","gtdeptmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gttechmk.shp","D:/redAlertFile/my/盟军实验室建造","gttechmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtcommmk.shp","D:/redAlertFile/my/盟军不明建筑","gtcommmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtorepmk.shp","D:/redAlertFile/my/盟军矿石精炼器建造","gtorepmk");
        
        // testSaveToPng("D:\\redAlertFile\\shp\\gtpillmk.shp","D:/redAlertFile/my/盟军碉堡建造","gtpillmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntsammk.shp","D:/redAlertFile/my/盟军爱国者飞弹建造","ntsammk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtprismk.shp","D:/redAlertFile/my/盟军光棱塔建造","gtprismk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtgapmk.shp","D:/redAlertFile/my/盟军裂缝产生器建造","gtgapmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtspstmk.shp","D:/redAlertFile/my/盟军间谍卫星建造","gtspstmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtcsphmk.shp","D:/redAlertFile/my/盟军超时空建造","gtcsphmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtwethmk.shp","D:/redAlertFile/my/盟军天气控制器建造","gtwethmk");
        // testSaveToPng("D:\\redAlertFile\\shp\\gtgcanmk.shp","D:/redAlertFile/my/盟军巨炮建造","gtgcanmk");
        
        // testSaveToPng("D:\\redAlertFile\\shp\\ntpsya_a.shp","D:/redAlertFile/my/苏联大信标a","ntpsya_a");
        // testSaveToPng("D:\\redAlertFile\\shp\\ntpsya.shp","D:/redAlertFile/my/苏联大信标底座","ntpsya");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctoild_a.shp","D:/redAlertFile/my/中立油田a","ctoild_a");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctoutp_a.shp","D:/redAlertFile/my/中立维修a","ctoutp_a");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctoutp_b.shp","D:/redAlertFile/my/中立维修b","ctoutp_b");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctoutp_c.shp","D:/redAlertFile/my/中立维修c","ctoutp_c");
        
        // testSaveToPng("D:\\redAlertFile\\shp\\ctchig05.shp","D:/redAlertFile/my/中立芝加哥威利斯大厦","ctchig05");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctnwy05.shp","D:/redAlertFile/my/中立世贸大厦","ctnwy05");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctnewy01.shp","D:/redAlertFile/my/中立建筑物01","ctnewy01");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctnewy06.shp","D:/redAlertFile/my/中立建筑物06","ctnewy06");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctpars08.shp","D:/redAlertFile/my/中立建筑物08","ctpars08");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctpars09.shp","D:/redAlertFile/my/中立建筑物09","ctpars09");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctnwy26.shp","D:/redAlertFile/my/中立建筑物26","ctnwy26");
        // testSaveToPng("D:\\redAlertFile\\shp\\cttech01.shp","D:/redAlertFile/my/中立实验室","cttech01");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctlab.shp","D:/redAlertFile/my/中立实验室lab","ctlab");
        // testSaveToPng("D:\\redAlertFile\\shp\\ctmex03.shp","D:/redAlertFile/my/中立mex03","ctmex03");
        // testSaveToPng("D:\\redAlertFile\\shp\\ammo01.shp","D:/redAlertFile/my/ammo01","ammo01");
        // testSaveToPng("D:\\redAlertFile\\shp\\wake1.shp","D:/redAlertFile/my/wake1","wake1");
        
        long endTimestamp = System.currentTimeMillis();
        System.out.println("cost time: " + (endTimestamp - startTimestamp));
    }
    
    /**
     * 把 shp 逐帧保存成 png 图片
     *
     * @param shpName shp 文件名
     * @param palName 调色板文件名
     * @param path 保存路径
     */
    private static void testSaveToPng(String shpName, String palName, String path) {
        File file = new File(path);
        if (!file.canWrite()) {
            throw new RuntimeException("Can't write to directory!");
        }
        if (!file.exists() || !file.isDirectory() || !file.mkdirs()) {
            throw new RuntimeException("Can't create directory!");
        }
        List<ShapeUnitFrame> resultBuildingList = ShpResourceCenter.loadShpResource(shpName, palName, false);
        System.out.println(resultBuildingList.size() + "帧画面");
        for (int i = 0; i < resultBuildingList.size(); i++) {
            ShapeUnitFrame ob = resultBuildingList.get(i);
            BufferedImage targetImage = ob.getImg();
            System.out.println(targetImage);
            try {
                ImageIO.write(targetImage, "png", new File(path + "/" + shpName + i + ".png"));
                Thread.sleep(10);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}

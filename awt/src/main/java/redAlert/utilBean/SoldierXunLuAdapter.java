package redAlert.utilBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 将步兵寻路类包装一下,使用缓存进行使用
 */
public class SoldierXunLuAdapter extends SoldierXunLuBean2{

	public static List<SoldierXunLuAdapter> cache = new ArrayList<>();
	
	static {
		for(int i=0;i<3;i++) {
			SoldierXunLuAdapter xlb = new SoldierXunLuAdapter();
			cache.add(xlb);
		}
	}
	
	/**
	 * 空闲
	 */
	public final static int XUNLU_IDLE = 0;
	/**
	 * 使用中 
	 */
	public final static int XUNLU_USING = 1;//在使用的状态
	/**
	 * 状态
	 */
	public AtomicInteger status = new AtomicInteger(XUNLU_IDLE);//状态  表示这个对象是否正在使用   正在初始化  可用等状态 
	
	/**
	 * 乐观锁的方式获取实例  获取不到就新建一个
	 */
	public static SoldierXunLuAdapter getInstance() {
		SoldierXunLuAdapter myXlb = null;
		for(SoldierXunLuAdapter xlb : cache) {
			if(xlb.status.compareAndSet(XUNLU_IDLE, XUNLU_USING)) {
				myXlb = xlb;
				break;
			}
		}
		if(myXlb==null) {
			return new SoldierXunLuAdapter();
		}else {
			return myXlb;
		}
		
	}
	
	/**
	 * 包装过的寻路方法
	 */
	@Override
	public List<LittleCenterPoint> xunlu(LittleCenterPoint startCp,LittleCenterPoint endCp) {
		List<LittleCenterPoint> path = super.xunlu(startCp, endCp);
		
		reInit();
		
		return path;
	}
	
	/**
	 * 重新初始化
	 * 结束时的初始化  主要是使用过的数据
	 */
	public void reInit() {
		Set<RaPoint> haveGetSet = getHaveGetSet();
		for(RaPoint rp:haveGetSet) {
			rp.setCurPrice(0);
			rp.setEuDistance(9999);
			rp.setLastPoint(null);
			rp.setTotalPrice(Integer.MAX_VALUE);
		}
		
		getHaveGetSet().clear();
		setCurRecNum(0);
		setMaxRecNum(0);
		setFoundWay(false);
		getRest().clear();
		status.set(XUNLU_IDLE);
		
	}
}

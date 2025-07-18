package redAlert.utilBean;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import redAlert.utils.PointUtil;

/**
 * 优化A*寻路方法
 * 减少Adapter类初始化所需的时间,采用了优先级队列,寻路方法由递归给改为循环
 */
public class XunLuBean2 {

	protected final int ox = 30;//菱形的长半径
	protected final int oy = 15;//菱形的短半径
	/**
	 * 所有中心点的集合
	 */
	private HashMap<String,RaPoint> myPointMap = new HashMap<>();
	/**
	 * 仅在寻路算法中使用
	 */
	protected class RaPoint implements Comparable<RaPoint>{

		public int x;
		public int y;
		
		public RaPoint(int x,int y){
			this.x = x;
			this.y = y;
		}
		/**
		 * 当前代价  已经走过的路的距离
		 */
		public int curPrice = 0;
		/**
		 * 欧式距离
		 */
		public int euDistance = 9999;
		/**
		 * 这个点的上一个点
		 * 在寻路成功后,需要回溯路径,需要设置和获取此变量
		 */
		public RaPoint lastPoint;
		/**
		 * 总代价
		 * 
		 */
		public int totalPrice = Integer.MAX_VALUE;
		
		public RaPoint getLastPoint() {
			return lastPoint;
		}

		public void setLastPoint(RaPoint lastPoint) {
			this.lastPoint = lastPoint;
		}

		public int getEuDistance() {
			return euDistance;
		}

		public void setEuDistance(int euDistance) {
			this.euDistance = euDistance;
		}

		public int getCurPrice() {
			return curPrice;
		}

		public void setCurPrice(int curPrice) {
			this.curPrice = curPrice;
		}
		
		public int getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		public RaPoint getLeft(){
			if(x-ox*2>=0) {
				return myPointMap.get( (x-ox*2)+","+y);
			}else {
				return null;
			}
		}
		
		public RaPoint getRight(){
			if(x+ox*2<10000) {
				return myPointMap.get( (x+ox*2)+","+y);
			}else {
				return null;
			}
		}
		
		public RaPoint getUp(){
			if(y-oy*2>=0) {
				return myPointMap.get( x+","+(y-oy*2));
			}else {
				return null;
			}
		}
		
		public RaPoint getDown(){
			if(y+oy*2<10000) {
				return myPointMap.get( x+","+(y+oy*2));
			}else {
				return null;
			}
		}
		
		public RaPoint getLeftUp(){
			if(x-ox>=0 && y-oy>=0) {
				return myPointMap.get( (x-ox)+","+ (y-oy) );
			}else {
				return null;
			}
		}
		
		public RaPoint getLeftDown(){
			if(x-ox>=0 && y+oy<=10000) {
				return myPointMap.get( (x-ox)+","+ (y+oy) );
			}else {
				return null;
			}
		}
		
		public RaPoint getRightDown(){
			if(x+ox<=10000 && y+oy<=10000) {
				return myPointMap.get( (x+ox)+","+ (y+oy) );
			}else {
				return null;
			}
		}
		
		public RaPoint getRightUp(){
			if(x+ox<=10000 && y-oy>=0) {
				return myPointMap.get( (x+ox)+","+ (y-oy) );
			}else {
				return null;
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			RaPoint other = (RaPoint) obj;
			return x == other.x && y == other.y;
		}

		@Override
		public String toString() {
			return "(" +x + "," + y + ")";
		}

		@Override
		public int compareTo(RaPoint o) {
			if(this.totalPrice>o.totalPrice) {
				return 1;
			}else if(this.totalPrice<o.totalPrice) {
				return -1;
			}
			return 0;
		}
		
		/**
		 * 地块是否能用,要看第几次寻路
		 * 第一次寻路,考虑单位阻挡,不能经过有静止单位的中心块
		 * 如果第一次寻路找不到路,进行第二次寻路,考虑可以经过有静止单位的中心块,经过静止中心块时,让静止单位让路
		 * 
		 */
		public boolean canUse(int xunluType) {
			CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
			if(cp!=null) {
				if(xunluType==FIRST_XUNLU) {
					return cp.isVehicleCanOnXunLuFirst();//此次寻路  有静止单位的中心块不能入
				}else {
					return cp.isVehicleCanOnXunLuSecond();//二次寻路  有静止单位的中心块  也在路径内
				}
			}
			return false;
		}
	}
	
	public final int FIRST_XUNLU = 1;
	public final int SECOND_XUNLU = 2;
	
	
	/**
	 * 初始化方法  应该考虑到地图的占用情况  适当设置一些不可使用的点
	 */
	public XunLuBean2(){
		for(int m=0;m<50;m++) {//一类
			int y = oy*2*m;
			for(int n=0;n<50;n++) {
				int x = ox*2*n;
				
				CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
				if(cp!=null) {
					RaPoint rp = new RaPoint(x,y);
					myPointMap.put(x+","+y, rp);
				}
			}
		}
		
		for(int m=0;m<50;m++) {//二类
			int y = oy+oy*2*m;
			for(int n=0;n<50;n++) {
				int x = ox+ox*2*n;
				
				CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
				if(cp!=null) {
					RaPoint rp = new RaPoint(x,y);
					myPointMap.put(x+","+y, rp);
				}
			}
		}
		
		
		
	}
	
	/**
	 * 已经寻找过的中心点
	 */
	private HashSet<RaPoint> haveGetSet = new HashSet<>();
	/**
	 * 是否找到路
	 */
	private boolean foundWay = false;
	/**
	 * 边界方块
	 */
	private PriorityQueue<RaPoint> rest = new PriorityQueue<>();//边界方块   寻路寻进死以后通过这里复活
	/**
	 * 寻路  返回路径中心点列表
	 * 
	 */
	public List<CenterPoint> xunlu(CenterPoint startCp,CenterPoint endCp) {
		RaPoint start = myPointMap.get(startCp.getX()+","+startCp.getY());
		RaPoint end = myPointMap.get(endCp.getX()+","+endCp.getY());
		
		//启动一次寻路
		//计算起始点和结束点的曼哈顿距离  以此来估计一个数,不宜太大或太小,限制递归次数,防止内存溢出
		int maxRecNum = (Math.abs(startCp.getX() - endCp.getX())/30+Math.abs(startCp.getY() - endCp.getY())/15)*10 ;
		
		start.setCurPrice(0);
		start.setTotalPrice(0);
		rest.add(start);
		haveGetSet.add(start);
		
		int curRecNum = 0;//当前寻路次数
		while(!foundWay) {
			if(rest.isEmpty()) {
				break;
			}else if(curRecNum>maxRecNum){
				break;
			}else {
				RaPoint rp = rest.poll();
				_xunlu(rp,end,FIRST_XUNLU);
				curRecNum++;
			}
		}
		
		if(foundWay) {
			RaPoint last = end;
			LinkedList<CenterPoint> result = new LinkedList<>();
			result.addFirst(PointUtil.fetchCenterPoint(last.x, last.y));
			while(true) {
				last = last.getLastPoint();
				result.addFirst(PointUtil.fetchCenterPoint(last.x, last.y));
				if(last.equals(start)) {
					break;
				}
			}
			return result;
		}
		
		
		//第一次寻路未果,启动二次寻路
		for(RaPoint rp:haveGetSet) {
			rp.setCurPrice(0);
			rp.setEuDistance(9999);
			rp.setLastPoint(null);
			rp.setTotalPrice(Integer.MAX_VALUE);
		}
		getHaveGetSet().clear();
		getRest().clear();
		
		//计算起始点和结束点的曼哈顿距离  以此来估计一个数,不宜太大或太小,限制递归次数,防止内存溢出
		maxRecNum = (Math.abs(startCp.getX() - endCp.getX())/30+Math.abs(startCp.getY() - endCp.getY())/15)*10 ;
		
		start.setCurPrice(0);
		start.setTotalPrice(0);
		rest.add(start);
		haveGetSet.add(start);
		
		curRecNum = 0;//当前寻路次数
		while(!foundWay) {
			if(rest.isEmpty()) {
				break;
			}else if(curRecNum>maxRecNum){
				break;
			}else {
				RaPoint rp = rest.poll();
				_xunlu(rp,end,SECOND_XUNLU);
				curRecNum++;
			}
		}
		
		if(foundWay) {
			RaPoint last = end;
			LinkedList<CenterPoint> result = new LinkedList<>();
			result.addFirst(PointUtil.fetchCenterPoint(last.x, last.y));
			while(true) {
				last = last.getLastPoint();
				result.addFirst(PointUtil.fetchCenterPoint(last.x, last.y));
				if(last.equals(start)) {
					break;
				}
			}
			return result;
		}else {
			return null;
		}
	}
	
	private void _xunlu(RaPoint start,RaPoint end,int xunluType) {
		RaPoint myL = start.getLeft();
		RaPoint myR = start.getRight();
		RaPoint myU = start.getUp();
		RaPoint myD = start.getDown();
		
		RaPoint myLU = start.getLeftUp();
		RaPoint myLD = start.getLeftDown();
		RaPoint myRD = start.getRightDown();
		RaPoint myRU = start.getRightUp();
		
		if(myRU!=null && !haveGetSet.contains(myRU) && myRU.canUse(xunluType)) {
			int depX = Math.abs(end.x-myRU.x);
			int depY = Math.abs( (end.y-myRU.y)*2  );//垂直距离上应该加倍,因为应该把菱形想象成正方形
			int euDistance = depX*depX+depY*depY;//减少开方计算,所以用距离的平方  使用欧拉距离作为预估代价,应为允许斜着走
			haveGetSet.add(myRU);
			myRU.setCurPrice(start.getCurPrice()+1750);//斜着走的距离是30*根号2, 平方一下 等于1800  改成1750是为了平衡平方带来的优势，让单位更偏向斜着走，斜着走本来就应该更省距离，
			myRU.setEuDistance(euDistance);
			myRU.setTotalPrice(euDistance+myRU.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			
			myRU.setLastPoint(start);
			if(myRU.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myRU);
			}
		}
		
		if(myRD!=null && !haveGetSet.contains(myRD) && myRD.canUse(xunluType)) {
			int depX = Math.abs(end.x-myRD.x);
			int depY = Math.abs( (end.y-myRD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myRD);
			myRD.setCurPrice(start.getCurPrice()+1750);
			myRD.setEuDistance(euDistance);
			myRD.setTotalPrice(euDistance+myRD.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myRD.setLastPoint(start);
			if(myRD.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myRD);
			}
		}
		
		if(myLD!=null && !haveGetSet.contains(myLD) && myLD.canUse(xunluType)) {
			int depX = Math.abs(end.x-myLD.x);
			int depY = Math.abs( (end.y-myLD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLD);
			myLD.setCurPrice(start.getCurPrice()+1750);
			myLD.setEuDistance(euDistance);
			myLD.setTotalPrice(euDistance+myLD.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myLD.setLastPoint(start);
			if(myLD.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myLD);
			}
		}
		
		if(myLU!=null && !haveGetSet.contains(myLU) && myLU.canUse(xunluType)) {
			int depX = Math.abs(end.x-myLU.x);
			int depY = Math.abs( (end.y-myLU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLU);
			myLU.setCurPrice(start.getCurPrice()+1750);
			myLU.setEuDistance(euDistance);
			myLU.setTotalPrice(euDistance+myLU.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myLU.setLastPoint(start);
			if(myLU.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myLU);
			}
		}
		
		
		if(myL!=null && !haveGetSet.contains(myL) && myL.canUse(xunluType)) {
			int depX = Math.abs(end.x-myL.x);
			int depY = Math.abs( (end.y-myL.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myL);
			myL.setCurPrice(start.getCurPrice()+3600);//横向距离是60  平方一下3600
			myL.setEuDistance(euDistance);
			myL.setTotalPrice(euDistance+myL.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myL.setLastPoint(start);
			if(myL.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myL);
			}
		}
		
		if(myR!=null && !haveGetSet.contains(myR) && myR.canUse(xunluType)) {
			int depX = Math.abs(end.x-myR.x);
			int depY = Math.abs( (end.y-myR.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myR);
			myR.setCurPrice(start.getCurPrice()+3600);
			myR.setEuDistance(euDistance);
			myR.setTotalPrice(euDistance+myR.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myR.setLastPoint(start);
			if(myR.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myR);
			}
		}
		
		if(myU!=null && !haveGetSet.contains(myU) && myU.canUse(xunluType)) {
			int depX = Math.abs(end.x-myU.x);
			int depY = Math.abs( (end.y-myU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myU);
			myU.setCurPrice(start.getCurPrice()+3600);
			myU.setEuDistance(euDistance);
			myU.setTotalPrice(euDistance+myU.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myU.setLastPoint(start);
			if(myU.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myU);
			}
		}
		
		if(myD!=null && !haveGetSet.contains(myD) && myD.canUse(xunluType)) {
			int depX = Math.abs(end.x-myD.x);
			int depY = Math.abs( (end.y-myD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myD);
			myD.setCurPrice(start.getCurPrice()+3600);
			myD.setEuDistance(euDistance);
			myD.setTotalPrice(euDistance+myD.getCurPrice());//当前代价+预估代价,用这个值来衡量下一次走哪一步
			myD.setLastPoint(start);
			if(myD.equals(end)) {
				end.setLastPoint(start);
				foundWay = true;
				return;
			}else {
				rest.add(myD);
			}
		}
		
	}

	public HashSet<RaPoint> getHaveGetSet() {
		return haveGetSet;
	}

	public void setHaveGetSet(HashSet<RaPoint> haveGetSet) {
		this.haveGetSet = haveGetSet;
	}

	public boolean isFoundWay() {
		return foundWay;
	}

	public void setFoundWay(boolean foundWay) {
		this.foundWay = foundWay;
	}

	public PriorityQueue<RaPoint> getRest() {
		return rest;
	}

	public void setRest(PriorityQueue<RaPoint> rest) {
		this.rest = rest;
	}
	
	
	
}

package redAlert.utilBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import redAlert.enums.Direction;
import redAlert.utils.PointUtil;

/**
 * 适合小兵的A星寻路方法
 */
public class SoldierXunLuBean {
	
	protected final int ox = 30;//菱形的长半径
	protected final int oy = 15;//菱形的短半径
	
	
	/**
	 * 所有中心点的集合
	 */
	private HashMap<String,RaPoint> myPointMap = new HashMap<>();
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
	private List<RaPoint> rest = new ArrayList<>();//边界方块   寻路寻进死以后通过这里复活
	
	/**
	 * 仅在寻路算法中使用
	 */
	protected class RaPoint {

		public int x;
		public int y;
		/**
		 * 是哪一块方块  上下左右
		 * 上0
		 * 左1
		 * 下2
		 * 右3
		 */
		public Direction direction;
		
		public RaPoint(int x,int y,Direction direction){
			this.x = x;
			this.y = y;
			this.direction = direction;
		}
		/**
		 * 这个点是否能作为移动使用
		 */
		public boolean canUse = true;
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

		public boolean isCanUse() {
			return canUse;
		}

		public void setCanUse(boolean canUse) {
			this.canUse = canUse;
		}
		
		
		public int getTotalPrice() {
			return totalPrice;
		}

		public void setTotalPrice(int totalPrice) {
			this.totalPrice = totalPrice;
		}

		public RaPoint getLeft(){
			int x1,y1;
			if(direction==Direction.Up) {
				x1 = x - 30;
				y1 = y + 1;
			}else if(direction==Direction.Down){
				x1 = x - 30;
				y1 = y - 1;
			}else if(direction==Direction.Left) {
				x1 = x - 28;
				y1 = y;
			}else{
				x1 = x - 32;
				y1 = y;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getRight(){
			int x1,y1;
			if(direction==Direction.Up) {
				x1 = x + 30;
				y1 = y + 1;
			}else if(direction==Direction.Down){
				x1 = x + 30;
				y1 = y - 1;
			}else if(direction==Direction.Left) {
				x1 = x + 32;
				y1 = y;
			}else{
				x1 = x + 28;
				y1 = y;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getUp(){
			int x1,y1;
			if(direction==Direction.Up) {
				x1 = x;
				y1 = y - 14;
			}else if(direction==Direction.Down){
				x1 = x;
				y1 = y - 16;
			}else if(direction==Direction.Left) {
				x1 = x + 2;
				y1 = y - 15;
			}else {
				x1 = x - 2;
				y1 = y - 15;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getDown(){
			int x1,y1;
			if(direction==Direction.Up) {
				x1 = x;
				y1 = y + 16;
			}else if(direction==Direction.Down){
				x1 = x;
				y1 = y + 14;
			}else if(direction==Direction.Left) {
				x1 = x + 2;
				y1 = y + 15;
			}else {
				x1 = x - 2;
				y1 = y + 15;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getLeftUp(){
			int x1,y1;
			if(direction==Direction.Up || direction==Direction.Left) {
				x1 = x - 14;
				y1 = y - 7;
			}else {
				x1 = x - 16;
				y1 = y - 8;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getLeftDown(){
			int x1,y1;
			if(direction==Direction.Up || direction==Direction.Right) {
				x1 = x - 16;
				y1 = y + 8;
			}else {
				x1 = x -14;
				y1 = y + 7;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getRightDown(){
			int x1,y1;
			if(direction==Direction.Down || direction==Direction.Right) {
				x1 = x + 14;
				y1 = y + 7;
			}else {
				x1 = x + 16;
				y1 = y + 8;
			}
			return myPointMap.get(x1+","+y1);
		}
		
		public RaPoint getRightUp(){
			int x1,y1;
			if(direction==Direction.Up || direction==Direction.Right) {
				x1 = x + 14;
				y1 = y - 7;
			}else{
				x1 = x + 16;
				y1 = y - 8;
			}
			return myPointMap.get(x1+","+y1);
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
		
	}
	
	/**
	 * 初始化方法  应该考虑到地图的占用情况  适当设置一些不可使用的点
	 */
	public SoldierXunLuBean(){
		for(int m=0;m<50;m++) {//一类
			int y = oy*2*m;
			for(int n=0;n<50;n++) {
				int x = ox*2*n;
				
				CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
				LittleCenterPoint leftLCP =  PointUtil.fetchLittleCenterPoint(x-16,y);
				LittleCenterPoint rightLCP =  PointUtil.fetchLittleCenterPoint(x+16,y);
				LittleCenterPoint upLCP =  PointUtil.fetchLittleCenterPoint(x,y-8);
				LittleCenterPoint downLCP =  PointUtil.fetchLittleCenterPoint(x,y+8);
				
				if(leftLCP!=null) {
					RaPoint rp = new RaPoint(x-16,y,Direction.Left);
					myPointMap.put( (x-16)+","+y, rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(rightLCP!=null) {
					RaPoint rp = new RaPoint(x+16,y,Direction.Right);
					myPointMap.put( (x+16)+","+y, rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(upLCP!=null) {
					RaPoint rp = new RaPoint(x,y-8,Direction.Up);
					myPointMap.put( x+","+(y-8), rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(downLCP!=null) {
					RaPoint rp = new RaPoint(x,y+8,Direction.Down);
					myPointMap.put( x+","+(y+8), rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
			}
		}
		
		for(int m=0;m<50;m++) {//二类
			int y = oy+oy*2*m;
			for(int n=0;n<50;n++) {
				int x = ox+ox*2*n;
				
				CenterPoint cp = PointUtil.fetchCenterPoint(x, y);
				LittleCenterPoint leftLCP =  PointUtil.fetchLittleCenterPoint(x-16,y);
				LittleCenterPoint rightLCP =  PointUtil.fetchLittleCenterPoint(x+16,y);
				LittleCenterPoint upLCP =  PointUtil.fetchLittleCenterPoint(x,y-8);
				LittleCenterPoint downLCP =  PointUtil.fetchLittleCenterPoint(x,y+8);
				
				if(leftLCP!=null) {
					RaPoint rp = new RaPoint(x-16,y,Direction.Left);
					myPointMap.put( (x-16)+","+y, rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(rightLCP!=null) {
					RaPoint rp = new RaPoint(x+16,y,Direction.Right);
					myPointMap.put( (x+16)+","+y, rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(upLCP!=null) {
					RaPoint rp = new RaPoint(x,y-8,Direction.Up);
					myPointMap.put( x+","+(y-8), rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
				if(downLCP!=null) {
					RaPoint rp = new RaPoint(x,y+8,Direction.Down);
					myPointMap.put( x+","+(y+8), rp);
					if(!cp.isSoldierCanOn()) {
						rp.canUse = false;
					}
				}
			}
		}
		
		
	}
	private int maxRecNum = 0;//允许的最大递归次数  也就是路径搜索次数
	
	private int curRecNum = 0;//当前递归次数
	
	/**
	 * 寻路  返回路径中心点列表
	 *
	 */
	public List<LittleCenterPoint> xunlu(LittleCenterPoint startCp,LittleCenterPoint endCp) {
		RaPoint start = myPointMap.get(startCp.getX()+","+startCp.getY());
		RaPoint end = myPointMap.get(endCp.getX()+","+endCp.getY());
		
		//计算起始点和结束点的曼哈顿距离  以此来估计一个数,不宜太大或太小,限制递归次数,防止内存溢出
		maxRecNum = (Math.abs(startCp.getX() - endCp.getX())/16+Math.abs(startCp.getY() - endCp.getY())/8)*10 ;
		
		start.setCurPrice(0);
		
		_xunlu(start,end);
		
		if(foundWay) {
			List<RaPoint> luxian = new ArrayList<>();
			RaPoint last = end;
			luxian.add(end);
			while(true) {
				last = last.getLastPoint();
				if(last.equals(start)) {
					luxian.add(last);
					break;
				}else {
					luxian.add(last);
				}
			}
			Collections.reverse(luxian);
			
			List<LittleCenterPoint> result = new ArrayList<>();
			for(RaPoint rp : luxian) {
				LittleCenterPoint cp = PointUtil.fetchLittleCenterPoint(rp.x,rp.y);
				result.add(cp);
			}
			
			return result;
		}else {
			return null;
		}
	}
	
	private void _xunlu(RaPoint start,RaPoint end) {
		RaPoint myL = start.getLeft();
		RaPoint myR = start.getRight();
		RaPoint myU = start.getUp();
		RaPoint myD = start.getDown();
		
		RaPoint myLU = start.getLeftUp();
		RaPoint myLD = start.getLeftDown();
		RaPoint myRD = start.getRightDown();
		RaPoint myRU = start.getRightUp();
		
		if(myRU!=null && !haveGetSet.contains(myRU) && myRU.canUse) {
			int depX = Math.abs(end.x-myRU.x);
			int depY = Math.abs( (end.y-myRU.y)*2  );//垂直距离上应该加倍,因为应该把菱形想象成正方形
			int euDistance = depX*depX+depY*depY;//减少开方计算,所以用距离的平方  使用欧拉距离作为预估代价,应为允许斜着走
			haveGetSet.add(myRU);
			myRU.setCurPrice(start.getCurPrice()+450);//斜着走的距离可能是16可能是14平均一下是15  是15*根号2, 再平方一下  是 (15*1.141)^2=450
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
		
		if(myRD!=null && !haveGetSet.contains(myRD) && myRD.canUse) {
			int depX = Math.abs(end.x-myRD.x);
			int depY = Math.abs( (end.y-myRD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myRD);
			myRD.setCurPrice(start.getCurPrice()+450);
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
		
		if(myLD!=null && !haveGetSet.contains(myLD) && myLD.canUse) {
			int depX = Math.abs(end.x-myLD.x);
			int depY = Math.abs( (end.y-myLD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLD);
			myLD.setCurPrice(start.getCurPrice()+450);
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
		
		if(myLU!=null && !haveGetSet.contains(myLU) && myLU.canUse) {
			int depX = Math.abs(end.x-myLU.x);
			int depY = Math.abs( (end.y-myLU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myLU);
			myLU.setCurPrice(start.getCurPrice()+450);
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
		
		
		if(myL!=null && !haveGetSet.contains(myL) && myL.canUse) {
			int depX = Math.abs(end.x-myL.x);
			int depY = Math.abs( (end.y-myL.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myL);
			myL.setCurPrice(start.getCurPrice()+31*31);//横着走 可能是30 可能是32  平局一下31
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
		
		if(myR!=null && !haveGetSet.contains(myR) && myR.canUse) {
			int depX = Math.abs(end.x-myR.x);
			int depY = Math.abs( (end.y-myR.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myR);
			myR.setCurPrice(start.getCurPrice()+31*31);
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
		
		if(myU!=null && !haveGetSet.contains(myU) && myU.canUse) {
			int depX = Math.abs(end.x-myU.x);
			int depY = Math.abs( (end.y-myU.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myU);
			myU.setCurPrice(start.getCurPrice()+31*31);
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
		
		if(myD!=null && !haveGetSet.contains(myD) && myD.canUse) {
			int depX = Math.abs(end.x-myD.x);
			int depY = Math.abs( (end.y-myD.y)*2  );
			int euDistance = depX*depX+depY*depY;
			haveGetSet.add(myD);
			myD.setCurPrice(start.getCurPrice()+31*31);
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
		
		//从rest中找到总代价最低的方块 然后递归
		RaPoint astart = getMinTotalPricePoint(rest);
		if(astart!=null) {
			if(curRecNum>maxRecNum) {
				System.out.println("寻路次数用尽");
				return;//结束  不找了
			}else {
				curRecNum++;
				_xunlu(astart,end);
			}
			
		}else {
			//没找到路  会自动出栈
		}
	}
	
	/**
	 * 获取最小总代价的点
	 */
	public RaPoint getMinTotalPricePoint(List<RaPoint> rest) {
		int totalPrice = Integer.MAX_VALUE;
		RaPoint minPricePoint = null;
		
		for(int i=0;i<rest.size();i++) {
			RaPoint mp = rest.get(i);
			if(mp.totalPrice<totalPrice) {
				totalPrice = mp.totalPrice;
				minPricePoint = mp;
			}
		}
		if(minPricePoint!=null) {
			rest.remove(minPricePoint);
		}
		
		return minPricePoint;
	}
}

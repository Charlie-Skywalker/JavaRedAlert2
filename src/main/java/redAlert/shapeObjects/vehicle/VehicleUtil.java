package redAlert.shapeObjects.vehicle;

import redAlert.utilBean.CenterPoint;

/**
 * 载具工具类
 */
public class VehicleUtil {

	public static double xtable [] = new double [32];
	public static double ytable [] = new double [32]; 
	/**
	 * 获取32个单位向量
	 * 顺时针方向排列
	 */
	 static{
		for(int i=0;i<32;i++) {
			xtable[i] = Math.cos(i*2*Math.PI/32);
			ytable[i] = Math.sin(i*2*Math.PI/32);
		}
	}
	
	/**
	 * 计算目标转向
	 */
	public static int getTargetTurn(CenterPoint startCp, CenterPoint endCp) {
		int targetX = endCp.getX();
		int targetY = endCp.getY();
		
		int startX = startCp.getX();
		int startY = startCp.getY();
		
		int targetTurn = 0;
		//确定使用车身角度
		if( startX-targetX<0 && startY-targetY==0 ) {//右运动
			targetTurn = 0;
		}
		else if( startX-targetX<0 && startY-targetY<0 ) {//右下运动
			targetTurn = 2;
		}
		else if( startX-targetX==0 && startY-targetY<0 ) {//下运动
			targetTurn = 4;
		}
		else if( startX-targetX>0 && startY-targetY<0 ) {//左下运动
			targetTurn = 6;
		}
		else if( startX-targetX>0 && startY-targetY==0 ) {//左运动
			targetTurn = 8;
		}
		else if( startX-targetX>0 && startY-targetY>0 ) {//左上运动
			targetTurn = 10;
		}
		else if( startX-targetX==0 && startY-targetY>0 ) {//上运动
			targetTurn = 12;
		}
		else if( startX-targetX<0 &&  startY-targetY>0 ) {//右上运动
			targetTurn = 14;
		}else {
			targetTurn = -1;//不需要旋转,使用时应该startCp与endCp不是一个点
		}
		
		return targetTurn;
	}
	
	/**
	 * 根据当前转向和目标转向,计算旋转方向
	 * 返回0表示不需要转动
	 * 返回1表示应该顺时针转动
	 * 返回-1表示应该逆时针转动
	 */
	public static int calRotateDirection(int curTurn,int targetTurn) {
		if(curTurn==targetTurn) {
			return 0;
		}
		
		//计算顺旋转需要的次数
		int cwRotateNum = 0;
		int curTurnCw = curTurn;
		while(true) {
			if(curTurnCw!=targetTurn) {
				if(curTurnCw==31) {
					curTurnCw = 0;
				}else {
					curTurnCw+=1;
				}
				cwRotateNum++;
			}else {
				break;
			}
		}
		
		//计算逆旋转需要的次数
		int acwRotateNum = 0;
		int curTurnAcw = curTurn;
		while(true) {
			if(curTurnAcw!=targetTurn) {
				if(curTurnAcw==0) {
					curTurnAcw = 31;
				}else {
					curTurnAcw-=1;
				}
				acwRotateNum++;
			}else {
				break;
			}
		}
		
		if(cwRotateNum<acwRotateNum) {
			return 1;
		}else {
			return -1;
		}
	}
	
	/**
	 * 计算炮塔转向
	 * 
	 * 更加精准的版本  可以更加精准  16个方向都能用上
	 */
//	public static int getTargetTurn2(CenterPoint startCp, CenterPoint endCp) {
//		int targetX = endCp.getX();
//		int targetY = endCp.getY();
//		
//		int startX = startCp.getX();
//		int startY = startCp.getY();
//		
//		int targetTurn = 0;
//		//确定使用车身角度
//		if( startX-targetX<0 && startY-targetY==0 ) {//右运动
//			targetTurn = 0;
//		}
//		else if( startX-targetX<0 && startY-targetY<0 ) {//右下运动
//			
//			float deltaX = targetX-startX;
//			if((targetY-startY)/deltaX<0.45) {
//				targetTurn = 1;
//			}else if((targetY-startY)/deltaX>0.55) {
//				targetTurn = 3;
//			}else {
//				targetTurn = 2;
//			}
//			
//		}
//		else if( startX-targetX==0 && startY-targetY<0 ) {//下运动
//			targetTurn = 4;
//		}
//		else if( startX-targetX>0 && startY-targetY<0 ) {//左下运动
//			
//			float deltaX = Math.abs(targetX-startX);
//			if((targetY-startY)/deltaX<0.45) {
//				targetTurn = 7;
//			}else if((targetY-startY)/deltaX>0.55) {
//				targetTurn = 5;
//			}else {
//				targetTurn = 6;
//			}
//		}
//		else if( startX-targetX>0 && startY-targetY==0 ) {//左运动
//			targetTurn = 8;
//		}
//		else if( startX-targetX>0 && startY-targetY>0 ) {//左上运动
//			
//			float deltaX = Math.abs(targetX-startX);
//			if((startY-targetY)/deltaX<0.45) {
//				targetTurn = 9;
//			}else if((startY-targetY)/deltaX>0.55) {
//				targetTurn = 11;
//			}else {
//				targetTurn = 10;
//			}
//		}
//		else if( startX-targetX==0 && startY-targetY>0 ) {//上运动
//			targetTurn = 12;
//		}
//		else if( startX-targetX<0 &&  startY-targetY>0 ) {//右上运动
//			float deltaX = Math.abs(targetX-startX);
//			if((startY-targetY)/deltaX<0.45) {
//				targetTurn = 15;
//			}else if((startY-targetY)/deltaX>0.55) {
//				targetTurn = 13;
//			}else {
//				targetTurn = 14;
//			}
//			
//		}else {
//			targetTurn = -1;//不需要旋转,使用时应该startCp与endCp不是一个点
//		}
//		
//		return targetTurn;
//	}
	
	
	/**
	 * 计算炮塔转向
	 * 
	 * 更加精准的支持32向的版本
	 */
	public static int getTargetTurn3(CenterPoint startCp, CenterPoint endCp) {
		int targetX = endCp.getX();
		int targetY = endCp.getY();
		
		int startX = startCp.getX();
		int startY = startCp.getY();
		
		int x = targetX - startX;
		int y = targetY - startY;
		
		double maxDot = 0;//最大点积
		int maxIndex = -1;//对应了方向
		for(int i=0;i<32;i++) {
			double dotValue = x*xtable[i]+(y*2)*ytable[i];
			if(dotValue>0) {
				if(dotValue>maxDot) {
					maxDot = dotValue;
					maxIndex = i;
				}
			}
		}
		return maxIndex;
	}
	
}

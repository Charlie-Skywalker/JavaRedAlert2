package redAlert.utils;

import java.util.Random;

/**
 * 随机数工具类
 */
public class RandomUtil {

	
	public static Random random;
	static {
		random = new Random();
	}
	
	/**
	 * 扔一次骰子  成功率是入参
	 */
	public static boolean isToTry(int successRate) {
		int result = random.nextInt(100);
		if(result<30) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获取单位的随机编号
	 */
	public static int newUnitNo() {
		return random.nextInt();
	}
	
	/**
	 * 获取一个范围里的一个随机整数  闭区间
	 */
	public static int randomInt(int min,int max) {
		return random.nextInt(max+1)+min;
	}
}



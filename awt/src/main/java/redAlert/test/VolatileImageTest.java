package redAlert.test;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.VolatileImage;

/**
 * 测试VolatileImage的使用
 */
public class VolatileImageTest {

	
	//正确获取VolatileImage的方法
	public static void main(String[] args) {
		 // 获取默认的图形环境
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        // 获取默认的图形设备
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        // 获取默认的图形配置
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        
        // 创建 VolatileImage 对象
        int width = 800; // 图像宽度
        int height = 600; // 图像高度
        VolatileImage vi = gc.createCompatibleVolatileImage(width, height);
        
		System.out.println(vi.getWidth());
	}
}

package redAlert.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
import java.nio.IntBuffer;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

import redAlert.ShapeUnitFrame;
import redAlert.shapeObjects.ShapeUnit;

/**
 * JOGL工具类  负责绘图
 */
public class DrawableUtil {
	/**
	 * 在指定位置画一幅帧图
	 */
	public static void drawOneSufAtPosition(GLAutoDrawable drawable,ShapeUnitFrame frame,int positionX,int positionY,int viewportOffX,int viewportOffY) {
		GL2 gl = drawable.getGL().getGL2();
		
        BufferedImage image = frame.getImg();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        
        int textureId = 0;
        if(frame.isAlreadyInitTexId()) {//图像已缓存在显存
        	textureId = frame.getTextureId();
        	
        	gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
        	//设置滤波
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
    		
        }else {
        	textureId = newTextureId(gl);
        	gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
        	//设置滤波
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
            
            WritableRaster raster = image.getRaster();
        	DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();
        	int [] pixels = dataBuffer.getData();
        	gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 4, imageWidth, imageHeight, 0,
        			GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE, IntBuffer.wrap(pixels));
        }
       
        int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
		int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f( viewX, viewY);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f( viewX+imageWidth, viewY);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f( viewX+imageWidth, viewY+imageHeight);
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f( viewX, viewY+imageHeight);
        gl.glEnd();
        gl.glFlush();//不调用就会内存占用飙升
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);//解绑纹理
        
        if(!frame.isShouldBeLoadedToGpu()) {
        	int[] textures = new int[]{textureId};
        	gl.glDeleteTextures(1, textures, 0);
        }else {
        	frame.setTextureId(textureId);
        	frame.setAlreadyInitTexId(true);
        }
	}
	
	/**
	 * 在指定位置画一个方块单位
	 */
	public static void drawOneShpAtPosition(GLAutoDrawable drawable,ShapeUnit shapeUnit,int viewportOffX,int viewportOffY) {
	    
		ShapeUnitFrame frame = shapeUnit.getCurFrame();
		int positionX = shapeUnit.getPositionX();
	    int positionY = shapeUnit.getPositionY();
		drawOneSufAtPosition(drawable,frame,positionX,positionY,viewportOffX,viewportOffY);
        
	}
	
	/**
	 * 画一个图片在指定位置
	 */
	public static void drawOneImgAtPosition(GLAutoDrawable drawable,BufferedImage image,int positionX,int positionY,int viewportOffX,int viewportOffY) {
		
		GL2 gl = drawable.getGL().getGL2();
		
        int textureId = newTextureId(gl);
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureId);
        //设置滤波 只能在glBindTexture之后调用  不然图形不显示
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
        
        WritableRaster raster = image.getRaster();
    	DataBufferInt dataBuffer = (DataBufferInt) raster.getDataBuffer();
    	int[] pixels = dataBuffer.getData();
    	IntBuffer buffer = IntBuffer.wrap(pixels);
    	 gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, 4, image.getWidth(), image.getHeight(),0,
    			 GL2.GL_BGRA, GL2.GL_UNSIGNED_BYTE,buffer);//画整个画板,这个OpenGL函数比较费时,需要2个毫秒
    	 
        int viewX = CoordinateUtil.getViewportX(positionX, viewportOffX);
		int viewY = CoordinateUtil.getViewportY(positionY, viewportOffY);
        
        gl.glBegin(GL2.GL_QUADS);
        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex2f( viewX, viewY);
        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex2f( viewX+image.getWidth(), viewY);
        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex2f( viewX+image.getWidth(), viewY+image.getHeight());
        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex2f( viewX, viewY+image.getHeight());
        gl.glEnd();
        gl.glFlush();//不调用就会内存占用飙升
        
        gl.glBindTexture(GL2.GL_TEXTURE_2D, 0);//解绑纹理
        
        int[] textures = new int[]{textureId};
        gl.glDeleteTextures(1, textures, 0);
	}
	
	
	/**
	 * 生成一个纹理ID
	 */
	private static int newTextureId(GL2 gl) {
		int[] textureIds = new int[1];
        gl.glGenTextures(1, textureIds, 0);
        int textureId = textureIds[0];
        
        return textureId;
        
	}
	
	/**
	 * 画直线
	 */
	public static void drawLine(GLAutoDrawable drawable,int x1,int y1,int x2,int y2) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(1f, 1f, 1f);
		gl.glVertex2i(x1, y1);
		gl.glVertex2i(x2, y2);
		gl.glEnd();
	    gl.glFlush();
	}
	
	/**
	 * 画绿色移动线
	 */
	public static void drawMoveLine(GLAutoDrawable drawable,int startx,int starty,int endx,int endy) {
		GL2 gl = drawable.getGL().getGL2();
		
		gl.glBegin(GL2.GL_LINES);
		gl.glColor3f(0f, 0.753f, 0f);
		gl.glVertex2i(startx, starty);
		gl.glVertex2i(endx, endy);
		gl.glEnd();
	    gl.glFlush();
	    
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glVertex2i(startx-1, starty-1);
	    gl.glVertex2i(startx+1, starty-1);
	    gl.glVertex2i(startx+1, starty+1);
	    gl.glVertex2i(startx-1, starty+1);
	    gl.glEnd();
	    gl.glFlush();
	    
	    gl.glBegin(GL2.GL_QUADS);
	    gl.glVertex2i(endx-1, endy-1);
	    gl.glVertex2i(endx+1, endy-1);
	    gl.glVertex2i(endx+1, endy+1);
	    gl.glVertex2i(endx-1, endy+1);
	    gl.glEnd();
	    gl.glColor3f(1f, 1f, 1f);//设置的颜色还原回去
	    gl.glFlush();
	    
	}
	
}

package redAlert.listener;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import redAlert.RuntimeParameter;
import redAlert.SysConfig;
import redAlert.manager.draw.DrawManager;

/**
 * JOGL负责渲染画面
 */
public class PanelGlListener implements GLEventListener {


    public DrawManager manager;

    public PanelGlListener(DrawManager manager) {
        this.manager = manager;
    }

    /**
     * OpenGL上下文初始化工作
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glEnable(GL2.GL_TEXTURE_2D);//开启纹理
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置glClear函数调用时覆盖颜色缓冲区的颜色值
        gl.glEnable(GL2.GL_BLEND);//启用颜色混合功能
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);//表示使用源颜色的alpha值来作为因子  表示用1.0减去源颜色的alpha值来作为因子
        gl.glLoadIdentity();//重置当前指定的矩阵为单位矩阵,与glMatrixMode函数一起调用
        gl.glOrtho(0, SysConfig.viewportWidth, SysConfig.viewportHeight, 0, 1, -1);//坐标系统的设置 X方向从左到右  Y方向从上到下
        gl.glMatrixMode(GL2.GL_MODELVIEW);//对模型视景矩阵堆栈应用随后的矩阵操作
    }



    /**
     * 渲染每一帧的画面  由此方法实现
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置glClear函数调用时覆盖颜色缓冲区的颜色值
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);//清除颜色缓冲区和深度缓冲区

        //获取视口偏移,由于这两个变量变化频繁,所以需要获取一个快照,否则移动视口内容会抖动
        int theSightOffX = RuntimeParameter.viewportOffX;
        int theSightOffY = RuntimeParameter.viewportOffY;

        //绘制地形（地形的代码块覆盖全图,所以就不用重新清空画板了）
        manager.drawTerrain(theSightOffX,theSightOffY,drawable);
        //绘制游戏内的ShapeUnit
        manager.drawMainInterface(theSightOffX,theSightOffY,drawable);
        //绘制预建造菱形红绿块
        manager.drawRhombus(theSightOffX,theSightOffY,drawable);
        //绘制选择框
        manager.drawSelectRect(drawable);
        //绘制鼠标指针
        manager.drawMouseCursor(drawable);

        RuntimeParameter.frameCount++;
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

    }
    @Override
    public void dispose(GLAutoDrawable drawable) {

    }
}

package redAlert.manager.draw;

import javax.swing.*;

/**
 *
 * 统一swing和OpenGL的刷新方式
 */
public class SwingAnimator {
    private final JComponent target;
    private final int fps;
    private final boolean scheduleAtFixedRate;
    private Thread thread;
    private volatile boolean running;

    public SwingAnimator(JComponent target, int fps, boolean scheduleAtFixedRate) {
        this.target = target;
        this.fps = fps;
        this.scheduleAtFixedRate = scheduleAtFixedRate;
    }

    public void start() {
        if (running) return;
        running = true;
        thread = new Thread(() -> {
            long framePeriod = 1000L / fps;
            long nextFrameTime = System.currentTimeMillis();

            while (running) {
                long start = System.currentTimeMillis();

                SwingUtilities.invokeLater(target::repaint);

                if (scheduleAtFixedRate) {
                    // 固定频率调度：每一帧严格按照 fps 间隔
                    nextFrameTime += framePeriod;
                    long sleep = nextFrameTime - System.currentTimeMillis();
                    if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException ignored) {}
                    } else {
                        nextFrameTime = System.currentTimeMillis();
                    }
                } else {
                    long elapsed = System.currentTimeMillis() - start;
                    long sleep = framePeriod - elapsed;
                    if (sleep > 0) {
                        try {
                            Thread.sleep(sleep);
                        } catch (InterruptedException ignored) {}
                    }
                }
            }
        }, "SwingAnimator");
        thread.start();
    }

    public void stop() {
        running = false;
        if (thread != null) {
            thread.interrupt();
        }
    }

    public boolean isAnimating() {
        return running;
    }
}
package org.example.redalert.audio;

/**
 * 混音器
 */
public class AudioMixer {
    /**
     * 默认播放器
     */
    public static Player sPlayer = getPlayer();
    
    private AudioMixer() {}
    
    /**
     * 播放音频
     *
     * @param name 音频名称
     */
    public static void sound(String name) {
        play(name);
    }
    
    /**
     * 播放音乐
     *
     * @param name 音频名称
     */
    public static void music(String name) {
        play(name);
    }
    
    /**
     * 播放音频
     *
     * @param name 音频名称
     */
    public static void play(String name) {
        sPlayer.play(name);
    }
    
    /**
     * 获取播放器
     *
     * @return 播放器
     */
    public static Player getPlayer() {
        return new JavaxSoundPlayer();
    }
}

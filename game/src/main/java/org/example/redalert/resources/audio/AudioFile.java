package org.example.redalert.resources.audio;

/**
 * 音频文件
 */
public class AudioFile {
    /**
     * 音频数据
     */
    public byte[] mData;
    
    public AudioFile(byte[] data) {
        this.mData = data;
    }
}

package org.example.redalert.audio;

import org.example.redalert.resources.GameResource;
import org.example.redalert.resources.audio.AudioFile;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;

/**
 * javax.sound 播放器
 */
public class JavaxSoundPlayer implements Player, Runnable {
    public Thread mThread = new Thread(this);
    public String mName;
    
    @Override
    public void play(String name) {
        this.mName = name;
        mThread.start();
    }
    
    @Override
    public void run() {
        try {
            AudioFile file = GameResource.sAudio.get(mName);
            AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(file.mData));
            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip) AudioSystem.getLine(info); // 创建音频剪辑流
            clip.open(stream); // 打开音频剪辑流
            clip.start(); // 播放
            clip.addLineListener(event->{
                if (event.getType() == LineEvent.Type.STOP) {
                    try {
                        clip.close();
                        stream.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

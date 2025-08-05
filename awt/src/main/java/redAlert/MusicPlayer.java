package redAlert;

import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

import redAlert.utils.WavFileReader;

/**
 * 获取一个音乐播放器
 */
public class MusicPlayer {
	
	public static Clip getClip(String musicName,int loop) {
		try {
			ByteArrayInputStream musicInputStream = WavFileReader.getMusicInputStream(musicName);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicInputStream);
			//获取音频格式
			AudioFormat format = audioStream.getFormat();
			DataLine.Info info = new DataLine.Info(Clip.class, format);
			//创建音频剪辑流  Clip是预先加载的数据
			Clip clip = (Clip)AudioSystem.getLine(info);
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					try {
						if(event.getType()==LineEvent.Type.OPEN) {
//							System.out.println("OpenEvent");
						}
						if(event.getType()==LineEvent.Type.START) {
//							System.out.println("StartEvent");
						}
						if(event.getType()==LineEvent.Type.STOP) {
//							System.out.println("StopEvent");//播放停止会自动触发   不一定需要调用clip.stop()
							synchronized (audioStream) {
								audioStream.notify();
							}
						}
						if(event.getType()==LineEvent.Type.CLOSE) {
//							System.out.println("CloseEvent");
						}
					}catch(Exception e) {
						e.printStackTrace();
					}
					
				}
			});
			
			//打开音频剪辑流
			clip.open(audioStream);
			//重复播放
			clip.loop(loop);
			return clip;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
	}
}

package redAlert;

import java.io.File;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;

public class Test {

	public static void main(String[] args) throws Exception{
		playWavFile("D:\\FFOutput\\ceva001.wav");
	}
	
	
	/**
	 * 基础用法
	 * 从硬盘中读取一个wav文件,然后播放一次
	 */
	public static void playWavFile(String filepath) throws Exception{
		//打开音频文件
		File musicFile = new File(filepath);
		AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
		//获取音频格式
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);
		
		//创建音频剪辑流  Clip是预先加载的数据
		Clip clip = (Clip)AudioSystem.getLine(info);
		
		//添加播放监听器,监听播放器状态
		clip.addLineListener(new LineListener() {
			@Override
			public void update(LineEvent event) {
				try {
					if(event.getType()==LineEvent.Type.OPEN) {
						System.out.println("OpenEvent");
					}
					if(event.getType()==LineEvent.Type.START) {
						System.out.println("StartEvent");
						System.out.println(Thread.currentThread().getName());//不是main线程
						System.out.println(Thread.currentThread().isDaemon());//是一个守护线程
					}
					if(event.getType()==LineEvent.Type.STOP) {//自动播放结束也会触发   不一定必须由clip.stop()主动触发
						System.out.println("StopEvent");
						clip.close();//会触发LineEvent.Type.CLOSE事件
					}
					if(event.getType()==LineEvent.Type.CLOSE) {
						System.out.println("CloseEvent");
						synchronized (audioStream) {
							audioStream.close();//audioStream.close并不会触发LineEvent.Type.CLOSE事件
							audioStream.notify();
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		//打开音频剪辑流
		clip.open(audioStream);
		//clip.loop(Clip.LOOP_CONTINUOUSLY);//设定循环次数  必须放在open方法后  Clip.LOOP_CONTINUOUSLY表示无穷次播放
		clip.loop(1);//循环一次   也就是播两遍
		
		clip.start();//播放  start方法开启了一个守护线程 clip不会立即进入running状态
		synchronized (audioStream) {
			audioStream.wait();//由于播放音乐的线程是一个守护线程,所以主线程不能立即结束,需要等待clip播放结束
		}
	}
	
}

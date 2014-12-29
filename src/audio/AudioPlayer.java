package audio;

import javax.sound.sampled.*;
import javax.sound.sampled.FloatControl.Type;

public class AudioPlayer {
	
	private Clip clip;
	private float vlume = -10f;
	private FloatControl volume;
	
	public AudioPlayer(String s) {
		try {
			AudioInputStream ais = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(s));
			AudioFormat baseFormat = ais.getFormat();
			AudioFormat decodeFormat = new AudioFormat(
				AudioFormat.Encoding.PCM_SIGNED,
				baseFormat.getSampleRate(),
				16,
				baseFormat.getChannels(),
				baseFormat.getChannels() * 2,
				baseFormat.getSampleRate(),
				false
			);
			AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, ais);
			clip = AudioSystem.getClip();
			clip.open(dais);
			volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
			setVolume(0f);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void playFromBeginning() {
		if(clip == null) return;
		stop();
		clip.setFramePosition(0);
		clip.start();
	}
	
	public void play(){
		if(clip == null) return;
		if(clip.getFramePosition() == clip.getFrameLength()) clip.setFramePosition(0);
		clip.start();
	}
	
	public void stop() {
		if(clip.isRunning()) clip.stop();
	}
	
	public void close() {
		stop();
		clip.close();
	}
	public void setLoop(){
		clip.loop(clip.LOOP_CONTINUOUSLY);
	}
		
	public void setStartPosition(int i){
		clip.setMicrosecondPosition(i*1000000);
	}
	
	public void setVolume(float f){
		vlume = f;
		volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		volume.setValue(vlume);
	}
	public float getVolume(){return  vlume;}

	public FloatControl getControl(Type masterGain) {return volume;}
	
}
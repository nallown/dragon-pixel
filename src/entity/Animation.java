package entity;

import java.awt.image.BufferedImage;

public class Animation {

	private BufferedImage[] frames;
	private int currentframe;
	
	private long starttime;
	private long delay;
	
	private boolean playedonce;
	
	public Animation(){
		playedonce = false;
	}
	
	public void setFrames(BufferedImage[] frames){
		this.frames = frames;
		currentframe = 0;
		starttime = System.nanoTime();
		playedonce = false;
	}
	
	public void setDelay(long d){ delay = d; }
	public void setFrame(int i){ currentframe = i; }
	
	public void update(){
		if(delay == -1 ) return;
		
		long elapsed = (System.nanoTime() - starttime) / 1000000;
		if(elapsed > delay){
			currentframe++;
			starttime = System.nanoTime();
		}
		if(currentframe == frames.length){
			currentframe = 0;
			playedonce = true;
		}
	}
	
	public int getFrame(){ return currentframe;	}
	public boolean hasPlayedOnce(){ return playedonce; }
	public BufferedImage getImage() { return frames[currentframe]; }
	
}

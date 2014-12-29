package entity.enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import main.GamePanel;
import tileMap.TileMap;
import entity.Animation;
import entity.Enemy;

public class Arachnik extends Enemy {
	
	private BufferedImage[] sprites;
	
	public Arachnik(TileMap tm) {
		super(tm);
		
		moveSpeed = 0.3;
		maxSpeed = 0.5;
		
		width = 30;
		height = 30;
		cwidth = 20;
		cheight = 20;
		
		health = maxHealth = 10;
		damage = 1;
		
		// load sprites
		try {
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Enemies/arachnik.gif"
				)
			);
			
			sprites = new BufferedImage[1];
			for(int i = 0; i < sprites.length; i++) {
				sprites[i] = spritesheet.getSubimage(
					i * width,
					0,
					width,
					height
				);
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		animation.setFrames(sprites);
		
		down = true;
		facingRight = true;
		
	}
	
	private void getNextPosition() {
		// movement
		if(up) {
			dy -= moveSpeed;
			if(dy < -maxSpeed) {
				dy = -maxSpeed;
			}
		}else if(down) {
			dy += moveSpeed;
			if(dy > maxSpeed) {
				dy = maxSpeed;
			}
		}
		
	}
	
	public void update() {
		// update position
		getNextPosition();
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchtimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// if it hits a wall, go other direction
		if(up && (getY() == 0||dy==0) ) {
			down = true;
			up = false;
		}else if(down && (dy == 0||getY() >= GamePanel.HEIGHT-16) ) {
			up = true;
			down = false;
		}
		
		// update animation
		animation.update();
		
	}
	
	//,
	public void draw(Graphics2D g) {
		setMapPosition();
		g.drawLine((int)(x + xmap), (int)(ymap), (int)(x + xmap), (int)(y + ymap));
		super.draw(g);
	}
	
}
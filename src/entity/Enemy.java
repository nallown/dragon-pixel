package entity;

import java.util.ArrayList;

import tileMap.TileMap;

public class Enemy extends MapObject{

	protected int health;
	protected int maxHealth;
	protected boolean dead;
	protected int damage;
	
	protected boolean flinching;
	protected long flinchtimer;
	
	private ArrayList<Enemy> enemies;
	
	public Enemy(TileMap tm){
		super(tm);
	}
	
	public boolean isDead(){ return dead; }
	public int getDamage(){ return damage; }
	
	public void hit(int arg0){
		if(dead || flinching) return;
		health -= arg0;
		if(health < 0) health = 0;
		if(health == 0) dead = true;
		flinching = true;
		flinchtimer = System.nanoTime();
	}
	
	public void update(){}
}

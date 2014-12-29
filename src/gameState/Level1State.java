package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.sound.sampled.FloatControl;

import audio.AudioPlayer;
import main.GamePanel;
import tileMap.Background;
import tileMap.TileMap;
import entity.Enemy;
import entity.Explosion;
import entity.HUD;
import entity.Player;
import entity.enemies.Arachnik;
import entity.enemies.Slugger;

public class Level1State extends GameState {
	private AudioPlayer bgMusic;
	private AudioPlayer beep;
	
	private boolean inMenu = false;
	private int menuX = 0;
	private int menuP = 0;
	private int currentChoice;
	private int selectChoice = 0;
	
	private boolean changingKey = false;
	
	int left = KeyEvent.VK_LEFT;
	int right = KeyEvent.VK_RIGHT;
	int jump = KeyEvent.VK_W;
	int glide = KeyEvent.VK_E;
	int scratch = KeyEvent.VK_R;
	int firing = KeyEvent.VK_F;
	
	private String[] options;
	private String[] menuOptions = {"Volume", "Controls", "Quit"};
	private String[] volumeOptions = {"Increase", "Decrease", "Reset","", "0.0 dB"};
	private String[] controlOptions = {
			"Left - "+KeyEvent.getKeyText(left),
			"Right - "+KeyEvent.getKeyText(right),
			"Jump - "+KeyEvent.getKeyText(jump),
			"Glide - "+KeyEvent.getKeyText(glide),
			"Scratch - "+KeyEvent.getKeyText(scratch),
			"Fireball - "+KeyEvent.getKeyText(firing)
		};
	
	private HUD hud;	
	private TileMap tileMap;
	private Background bg;
	
	private Player player;
	
	private ArrayList<Enemy> enemies;
	private ArrayList<Explosion> explosions;
	
	public Level1State(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	
	public void init() {
		tileMap = new TileMap(30);
		tileMap.loadTiles("/Tilesets/grasstileset.gif");
		tileMap.loadMap("/Maps/level1-1.map");
		tileMap.setPosition(0, 0);
		tileMap.setTween(0.04);
		
		bg = new Background("/Backgrounds/grassbg1.gif", 1);
		
		player = new Player(tileMap);
		player.setPosition(100, 100);
		
		populateEnemies();
		
		explosions = new ArrayList<Explosion>();
		
		hud = new HUD(player);
		bgMusic = new AudioPlayer("/Music/level1-1.mp3");
		beep = new AudioPlayer("/SFX/beep.mp3");
		bgMusic.setLoop();
		bgMusic.setStartPosition(2);
		bgMusic.playFromBeginning();
	}
	
	
	private void populateEnemies() {
		enemies = new ArrayList<Enemy>();
		
		Slugger s;
		Arachnik a;
		Point[] spoints = new Point[] {
			new Point(200, 100),
			new Point(860, 200),
			new Point(1525, 200),
			new Point(1680, 200),
			new Point(1800, 200)
		};
		Point[] apoints = new Point[] {
			new Point(2000, 150),
			new Point(2050, 175),
			new Point(2100, 200)
		};
		for(int i = 0; i < spoints.length; i++) {
			s = new Slugger(tileMap);
			s.setPosition(spoints[i].x, spoints[i].y);
			enemies.add(s);
		}
		for(int i = 0; i < apoints.length; i++) {
			a = new Arachnik(tileMap);
			a.setPosition(apoints[i].x, apoints[i].y);
			enemies.add(a);
		}
	}

	public void update() {		
		if(inMenu){
			if(menuX < 20){
				menuX ++;
			}
			if(menuP < 200){
				menuP += 10;
			}
			return;
		}
		selectChoice = currentChoice = menuX = menuP = 0;
		
		// update player
		player.update();
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getX(),
			GamePanel.HEIGHT / 2 - player.getY()
		);
		
		bg.setPosition(tileMap.getX(), tileMap.getY());
		
		player.checkAttack(enemies);
		
		for(int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			e.update();
			if(e.isDead()){
				enemies.remove(i);
				i--;
				explosions.add(new Explosion(e.getX(), e.getY()) );
			}
		}

		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).update();
			if(explosions.get(i).shouldRemove()){
				explosions.remove(i);
				i--;
			}
		}
		if(player.isDead()){
			player.setPosition(100, 50);
			player.setHealth(player.getMaxHealth());
		}
	}
	
	public void draw(Graphics2D g) {
		bg.draw(g);
		tileMap.draw(g);
		player.draw(g);
		
		for(int i =0; i < enemies.size(); i++){
			enemies.get(i).draw(g);
		}
		
		for(int i = 0; i < explosions.size(); i++){
			explosions.get(i).setMapPosition((int) TileMap.getX(),(int) TileMap.getY());
			explosions.get(i).draw(g);
		}
		hud.draw(g);

		drawShadow("X:"+player.getX(), 5, GamePanel.HEIGHT-20, g);
		drawShadow("Y:"+player.getY(), 5, GamePanel.HEIGHT-5, g);
		
		if(inMenu){
			bgMusic.stop();
			g.setColor(new Color(255, 255, 255, menuP));
			g.drawLine((int) menuX-1, 0, (int) menuX-1, GamePanel.HEIGHT);
			g.setColor(new Color(0,0,0,menuP));
			g.fillRect((int) menuX, 0, 120, GamePanel.HEIGHT);
			g.setColor(Color.WHITE);
			
			if(selectChoice == 1){
				g.drawString("Volume Settings", menuX+10, 20);
				options = volumeOptions;
			}else if(selectChoice == 2){
				g.drawString("Control Settings", menuX+10, 20);
				options = controlOptions;
			}else{
				g.drawString("Pause Menu", menuX+20, 20);
				options = menuOptions;
			}
			for(int i =0; i<options.length;i++){
				if(i == currentChoice){
					g.setColor(Color.LIGHT_GRAY);
				}else{
					g.setColor(Color.DARK_GRAY);
				}
				AffineTransform affinetransform = new AffineTransform();     
				FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
				Font font = g.getFont();
				Rectangle2D strBounds = font.getStringBounds(options[i], frc);
				drawShadow(options[i], menuX+((60/2)-((int) strBounds.getWidth()/2))+30, 125 + (i*15), g);
			}
			if(changingKey){
				g.setColor(Color.WHITE);
				g.drawRect(GamePanel.WIDTH/2-101, GamePanel.HEIGHT/2-51, 201, 101);
				g.setColor(new Color(0, 0, 0, 200));
				g.fillRect(GamePanel.WIDTH/2-100, GamePanel.HEIGHT/2-50, 200, 100);
				g.setColor(Color.WHITE);
				drawShadow("Press the replacement key", 75, 125, g);
			}
		}else{
			bgMusic.play();
		}
		
	}
	
	public void drawShadow(String str, int x, int y, Graphics2D g){
		Color strtc = g.getColor();
		
		g.setColor(Color.BLACK);
		g.drawString(str, x+1, y+1);
		g.setColor(strtc);
		g.drawString(str, x, y);
	}
	
	public void select(){
		if(options == volumeOptions){
			if(currentChoice == 0){
				if(bgMusic.getVolume() < 6f){
					bgMusic.setVolume(bgMusic.getVolume() + 1f);
					for(String key: player.sfx.keySet()){
						player.sfx.get(key).setVolume(bgMusic.getVolume());
					}
					beep.setVolume(bgMusic.getVolume());
					beep.playFromBeginning();
				}
			}else if(currentChoice == 1){
				if(bgMusic.getVolume() > -80f){
					bgMusic.setVolume(bgMusic.getVolume() - 1f);
					for(String key: player.sfx.keySet()){
						player.sfx.get(key).setVolume(bgMusic.getVolume());
					}
					beep.setVolume(bgMusic.getVolume());
					beep.playFromBeginning();
				}
			}else if(currentChoice == 2){
				bgMusic.setVolume(0f);
				for(String key: player.sfx.keySet()){
					player.sfx.get(key).setVolume(bgMusic.getVolume());
				}
				beep.setVolume(bgMusic.getVolume());
				beep.playFromBeginning();
			}
			FloatControl volume = (FloatControl) bgMusic.getControl(FloatControl.Type.MASTER_GAIN);
			System.out.println(volume);
			volumeOptions[4] = bgMusic.getVolume()+" dB";
		}else if(options == controlOptions){
			changingKey = true;
		}else{
			if(currentChoice == 0){
				selectChoice = 1;
			}else if(currentChoice == 1){
				selectChoice = 2;
			}else if(currentChoice == 2){
				System.exit(0);
			}
			currentChoice = 0;
		}
	}
	
	public void keyPressed(int k) {
		if(inMenu){
			if(changingKey){
				if(currentChoice == 0){
					left = k;
					controlOptions[0] = "Left - "+KeyEvent.getKeyText(left);
					System.out.println("Left --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}else if(currentChoice == 1){
					right = k;
					controlOptions[1] = "Right - "+KeyEvent.getKeyText(right);
					System.out.println("Right --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}else if(currentChoice == 2){
					jump = k;
					controlOptions[2] = "Jump - "+KeyEvent.getKeyText(jump);
					System.out.println("Jump --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}else if(currentChoice == 3){
					glide = k;
					controlOptions[3] = "Glide - "+KeyEvent.getKeyText(glide);
					System.out.println("Glide --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}else if(currentChoice == 4){
					scratch = k;
					controlOptions[4] = "Scratch - "+KeyEvent.getKeyText(scratch);
					System.out.println("Scratch --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}else if(currentChoice == 5){
					firing = k;
					controlOptions[5] = "Fireball - "+KeyEvent.getKeyText(firing);
					System.out.println("Firing --> "+KeyEvent.getKeyText(k));
					changingKey = false;
				}
				return;
			}
			if(k == KeyEvent.VK_ENTER){
				select();
			}
			if(k == KeyEvent.VK_BACK_SPACE){
				if(selectChoice > 0){
					currentChoice = 0;
					selectChoice  = 0;
				}
			}
			if(k == KeyEvent.VK_UP){
				currentChoice--;
				if(currentChoice == -1)
					currentChoice = options.length -1;
			}else if(k == KeyEvent.VK_DOWN){
				currentChoice++;
				if(currentChoice == options.length)
					currentChoice = 0;
			}
		}
		if(k == left){
			bg.setVector(1, 0);
			player.setLeft(true);
		}
		if(k == right){
			bg.setVector(1, 0);
			player.setRight(true);
		}
		if(k == KeyEvent.VK_UP) player.setUp(true);
		if(k == KeyEvent.VK_DOWN) player.setDown(true);
		if(k == jump) player.setJumping(true);
		if(k == glide) player.setGliding(true);
		if(k == scratch) player.setScratching();
		if(k == firing) player.setFiring();
		if(k == KeyEvent.VK_ESCAPE) inMenu = !inMenu;
	}
	
	public void keyReleased(int k) {
		if(k == left) player.setLeft(false);
		if(k == right) player.setRight(false);
		if(k == KeyEvent.VK_UP) player.setUp(false);
		if(k == KeyEvent.VK_DOWN) player.setDown(false);
		if(k == jump) player.setJumping(false);
		if(k == glide) player.setGliding(false);
	}
	
}
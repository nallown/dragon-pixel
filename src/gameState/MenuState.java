package gameState;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import tileMap.Background;
import main.GamePanel;

public class MenuState extends GameState{
	
	private Background bg;
	
	private int currentChoice;
	private String[] options = {"Start", "Help", "Quit"};
	
	private Color titleColor;
	private Font titleFont;
	
	private Font font;
	
	public MenuState(GameStateManager gsm){
		this.gsm = gsm;
		
		try{
			bg = new Background("/Backgrounds/menubg.gif", 1);
			bg.setVector(-0.1, 0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font("Century Gothic", Font.PLAIN, 28);
			
			font = new Font("Arial", Font.PLAIN, 12);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void init(){}
	public void update(){
		bg.update();
	}
	public void draw(Graphics2D g){
		bg.draw(g);
		g.setColor(titleColor);
		g.setFont(titleFont);
		g.drawString("Dragon Pixel", (GamePanel.WIDTH/2)-(160/2), 70);
		
		g.setFont(font);
		for(int i =0; i<options.length;i++){
			if(i == currentChoice){
				g.setColor(Color.BLACK);
			}else{
				g.setColor(Color.RED);
			}
			g.drawString(options[i], 145, 140 + (i*15));
		}
	}
	
	public void select(){
		if(currentChoice == 0){
			gsm.setState(GameStateManager.LEVEL1STATE);
		}else if(currentChoice == 1){
			//help
		}else if(currentChoice == 2){
			System.exit(0);
		}
	}
	
	public void keyPressed(int key){
		if(key == KeyEvent.VK_ENTER){
			select();
		}
		if(key == KeyEvent.VK_UP){
			currentChoice--;
			if(currentChoice == -1)
				currentChoice = options.length -1;
		}else if(key == KeyEvent.VK_DOWN){
			currentChoice++;
			if(currentChoice == options.length)
				currentChoice = 0;
		}
	}
	public void keyReleased(int key){}

}

package gameState;

import java.awt.Graphics2D;
import java.util.ArrayList;

public class GameStateManager {

	private GameState[] gameStates;
	private int currentState;
	
	public static final int NUMGAMESTATES = 2;
	public static final int MENUSTATE = 0;
	public static final int LEVEL1STATE = 1;
	
	public GameStateManager(){
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	private void loadState(int state){
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
	}
	
	private void unloadState(int state){
		gameStates[state] = null;
	}
	
	public void setState(int state){
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}
	
	public void update(){
		if(gameStates[currentState] != null){
			gameStates[currentState].update();
		}
	}
	
	public void draw(Graphics2D g){
		if(gameStates[currentState] != null){
			gameStates[currentState].draw(g);
		}
	}
	
	public void keyPressed(int k){
		if(gameStates[currentState] != null){
			gameStates[currentState].keyPressed(k);
		}
	}
	
	public void keyReleased(int k){
		if(gameStates[currentState] != null){
			gameStates[currentState].keyReleased(k);
		}
	}
	
}

package com.apptogo.runner.handlers;

import java.util.Stack;

import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.screens.MenuScreen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

//wydaje mi sie ze fajnie byloby to przerobic ostatecznie na singletona tak jak bylo w runalien
public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	
	private Runner runner;
	private BaseScreen menuScreen;
	private BaseScreen gameScreen;
	
	private ScreenType currentScreenType = ScreenType.SCREEN_MENU;
	private BaseScreen currentScreen;
	
	private ResourcesManager resourcesManager = ResourcesManager.getInstance();

	public enum ScreenType{
		SCREEN_MENU, SCREEN_GAME
	}
	
	/*---MENU SCREEN---*/
	public void createMenuScreen(){
		resourcesManager.loadMenuTextures();
		resourcesManager.loadMenuMusic();
		resourcesManager.loadMenuSounds();
		
		menuScreen = new MenuScreen(runner);
		setScreen(menuScreen);
	}
	
	/*---GAME SCREEN---*/
	public void createGameScreen(){
		resourcesManager.loadGameTextures();
		resourcesManager.loadGameMusic();
		resourcesManager.loadGameSounds();
		
		gameScreen = new GameScreen(runner);
		setScreen(gameScreen);
	}
	
	/*---OTHER---*/
	public void setScreen(BaseScreen screen){
		runner.setScreen(screen);
		currentScreen = screen;
		currentScreenType = screen.getSceneType();
	}
	
    public static ScreensManager getInstance(){ return INSTANCE; }
    
    public static void prepareManager(Runner runner){
    	getInstance().runner = runner;
    }
    
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
    
}

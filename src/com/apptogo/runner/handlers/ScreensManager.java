package com.apptogo.runner.handlers;

import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MenuScreen;
import com.apptogo.runner.screens.SplashScreen;

public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	
	private Runner runner;
	
	private BaseScreen splashScreen;
	private BaseScreen menuScreen;
	private BaseScreen gameScreen;
	private BaseScreen loadingScreen;
	
	private ScreenType currentScreenType;
	private BaseScreen currentScreen;

	public enum ScreenType{
		SCREEN_MENU, SCREEN_GAME, SCREEN_SPLASH, SCREEN_LOADING
	}
	
	/*---SPLASH SCREEN---*/
	public void createSplashScreen(){
		splashScreen = new SplashScreen(runner);
		setScreen(splashScreen);
	}
	
	/*---SPLASH SCREEN---*/
	public void createLoadingScreen(ScreenType screenToLoad){
		loadingScreen = new LoadingScreen(runner, screenToLoad);
		setScreen(loadingScreen);
	}
	
	/*---MENU SCREEN---*/
	public void createMenuScreen(){
		menuScreen = new MenuScreen(runner);
		setScreen(menuScreen);
	}
	
	/*---GAME SCREEN---*/
	public void createGameScreen(){
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

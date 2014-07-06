 package com.apptogo.runner.handlers;

import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.GameScreen;
//import com.apptogo.runner.screens.LoadingMenuScreen;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.apptogo.runner.screens.SplashScreen;
import com.apptogo.runner.screens.UpgradeScreen;
import com.apptogo.runner.vars.Box2DVars.GameCharacter;

public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	
	private Runner runner;
	
	private BaseScreen splashScreen;
	private BaseScreen loadingMenuScreen;
	private BaseScreen mainMenuScreen;
	private BaseScreen gameScreen;
	private BaseScreen loadingScreen;
	private BaseScreen upgradeScreen;
	private ScreenType currentScreenType;
	private BaseScreen currentScreen;

	public enum ScreenType{
		SCREEN_SPLASH,
		SCREEN_LOADING_MENU,
		SCREEN_MAIN_MENU,
		SCREEN_UPGRADE,
		SCREEN_LOADING, 
		SCREEN_GAME
	}
	
	/*---TO JEST NA PALE I POWINNO BYC POLEPSZONE W PRZYSZLOSCI---*/
	public void createProperScreen(ScreenType screenType)
	{
		if(screenType == ScreenType.SCREEN_GAME) createGameScreen();
		else if(screenType == ScreenType.SCREEN_MAIN_MENU) createMainMenuScreen();
		else if(screenType == ScreenType.SCREEN_SPLASH) createSplashScreen();
		else if(screenType == ScreenType.SCREEN_UPGRADE) createUpgradeScreen();
		else {/*--do nothing--*/}
	}
	
	/*---SPLASH SCREEN---*/
	public void createSplashScreen(){
		splashScreen = new SplashScreen(runner);
		setScreen(splashScreen);
	}
		
	/*---LOADING SCREEN---*/
	public void createLoadingScreen(ScreenType screenToLoad){
		loadingScreen = new LoadingScreen(runner, screenToLoad);
		setScreen(loadingScreen);
	}
	
	/*---MAIN MENU SCREEN---*/
	public void createMainMenuScreen(){
		mainMenuScreen = new MainMenuScreen(runner);
		setScreen(mainMenuScreen);
	}
	
	/*---GAME SCREEN---*/
	public void createGameScreen(){
		gameScreen = new GameScreen(runner);
		setScreen(gameScreen);
	}
	
	/*---UPGRADE SCREEN---*/
	public void createUpgradeScreen(){
		upgradeScreen = new UpgradeScreen(runner);
		setScreen(upgradeScreen);
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

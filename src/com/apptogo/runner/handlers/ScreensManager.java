package com.apptogo.runner.handlers;

import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.CharacterChooseScreen;
import com.apptogo.runner.screens.CharacterUpgradeScreen;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.screens.LevelChooseScreen;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.apptogo.runner.screens.MultiplayerScreen;
import com.apptogo.runner.screens.SplashScreen;
import com.apptogo.runner.vars.Box2DVars.GameCharacter;

public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	
	private Runner runner;
	
	private BaseScreen splashScreen;
	private BaseScreen mainMenuScreen;
	private BaseScreen multiplayerScreen;
	private BaseScreen gameScreen;
	private BaseScreen loadingScreen;
	private BaseScreen characterChooseScreen;
	private BaseScreen levelChooseScreen;
	private BaseScreen characterUpgradeScreen;
	
	private ScreenType currentScreenType;
	private BaseScreen currentScreen;

	public enum ScreenType{
		SCREEN_MAIN_MENU, SCREEN_GAME, SCREEN_SPLASH, SCREEN_LOADING, SCREEN_MULTIPLAYER, SCREEN_CHARACTER_CHOOSE, SCREEN_LEVEL_CHOOSE, SCREEN_CHARACTER_UPGRADE
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
	
	/*---MAIN MENU SCREEN---*/
	public void createMainMenuScreen(){
		mainMenuScreen = new MainMenuScreen(runner);
		setScreen(mainMenuScreen);
	}
	
	/*---CHARACTER CHOOSE SCREEN---*/
	public void createCharacterChooseScreen(boolean multiplayer){
		characterChooseScreen = new CharacterChooseScreen(runner, multiplayer);
		setScreen(characterChooseScreen);
	}
	
	/*---LEVEL CHOOSE SCREEN---*/
	public void createLevelChooseScreen(){
		levelChooseScreen = new LevelChooseScreen(runner);
		setScreen(levelChooseScreen);
	}
	
	/*---CHARACTER UPGRADE SCREEN---*/
	public void createCharacterUpgradeScreen(boolean multiplayer, GameCharacter choosenCharacter){
		characterUpgradeScreen = new CharacterUpgradeScreen(runner, multiplayer, choosenCharacter);
		setScreen(characterUpgradeScreen);
	}
	
	/*---MULTIPLAYER SCREEN---*/
	public void createMultiplayerScreen(){
		multiplayerScreen = new MultiplayerScreen(runner);
		setScreen(multiplayerScreen);
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

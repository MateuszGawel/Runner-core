 package com.apptogo.runner.handlers;

import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.CampaignScreen;
import com.apptogo.runner.screens.GameScreenMulti;
import com.apptogo.runner.screens.GameScreenSingle;
import com.apptogo.runner.screens.LoadingGameScreen;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.apptogo.runner.screens.MultiplayerScreen;
import com.apptogo.runner.screens.RegisterScreen;
import com.apptogo.runner.screens.SplashScreen;
import com.apptogo.runner.screens.WaitingRoomScreen;
import com.badlogic.gdx.utils.Array;

public class ScreensManager {
	
	private static ScreensManager INSTANCE;
	private Runner runner;
	
	public static void create()
	{
		INSTANCE = new ScreensManager();
	}
	public static void destroy()
	{
		INSTANCE = null;
	}
	public static ScreensManager getInstance(){ return INSTANCE; }
	public static void prepareManager(Runner runner)
	{ 
		getInstance().runner = runner; 
	}

	private BaseScreen currentScreen;
	private ScreenType currentScreenType;
			
	public void createScreen(ScreenType screenType)
	{
		BaseScreen screen = getScreen(screenType);
		setScreen( screen );
	}
	
	public void createGameScreen(ScreenType screenType, Level levelToLoad, Array<Player> enemies)
	{
		BaseScreen screen = getGameScreen(screenType, levelToLoad, enemies);
		setScreen( screen );
	}
	
	public void createLoadingScreen(ScreenType screenType)
	{
		BaseScreen loadingScreen = new LoadingScreen(runner, screenType);
		setScreen(loadingScreen);
	}
	
	public void createLoadingGameScreen(ScreenType screenType, Level levelToLoad)
	{		
		createLoadingGameScreen(screenType, levelToLoad, null);
	}	
	public void createLoadingGameScreen(ScreenType screenType, Level levelToLoad, Array<Player> enemies)
	{		
		BaseScreen loadingScreen = new LoadingGameScreen(runner, screenType, levelToLoad, enemies);
		setScreen(loadingScreen);
	}
	
	private BaseScreen getScreen(ScreenType screenType)
	{
		BaseScreen screen;
		
		if(screenType == ScreenType.SCREEN_SPLASH)
			screen = new SplashScreen(runner);
		else if(screenType == ScreenType.SCREEN_MAIN_MENU)
			screen = new MainMenuScreen(runner);
		else if(screenType == ScreenType.SCREEN_CAMPAIGN)
			screen = new CampaignScreen(runner);
		else if(screenType == ScreenType.SCREEN_MULTIPLAYER)
			screen = new MultiplayerScreen(runner);
		else if(screenType == ScreenType.SCREEN_WAITING_ROOM)
			screen = new WaitingRoomScreen(runner);
		else if(screenType == ScreenType.SCREEN_REGISTER)
			screen = new RegisterScreen(runner);
		else
			screen = null;
		
		return screen;
	}
	
	private BaseScreen getGameScreen(ScreenType screenType, Level levelToLoad, Array<Player> enemies)
	{
		BaseScreen screen;
		
		if(screenType == ScreenType.SCREEN_GAME_SINGLE)
			screen = new GameScreenSingle(runner, levelToLoad, enemies);
		else if(screenType == ScreenType.SCREEN_GAME_MULTI)
			screen = new GameScreenMulti(runner, levelToLoad, enemies);
		else
			return null;
		
		return screen;
	}
	
	public void setScreen(BaseScreen screen)
	{
		if( currentScreen != null ) currentScreen.dispose();
		
		currentScreen = screen;
		currentScreenType = screen.getSceneType();
	
		runner.setScreen(screen);
	}
	
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
}
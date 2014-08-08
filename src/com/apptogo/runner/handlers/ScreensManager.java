 package com.apptogo.runner.handlers;

import java.util.ArrayList;

import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.CampaignScreen;
import com.apptogo.runner.screens.CreateRoomScreen;
import com.apptogo.runner.screens.FindRoomScreen;
import com.apptogo.runner.screens.GameScreenMulti;
import com.apptogo.runner.screens.GameScreenSingle;
//import com.apptogo.runner.screens.LoadingMenuScreen;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.apptogo.runner.screens.MultiplayerScreen;
import com.apptogo.runner.screens.SplashScreen;
import com.apptogo.runner.screens.UpgradeScreen;
import com.apptogo.runner.screens.WaitingRoom;
import com.badlogic.gdx.utils.Array;

public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	private Runner runner;
	
	public static ScreensManager getInstance(){ return INSTANCE; }
	public static void prepareManager(Runner runner)
	{ 
		getInstance().runner = runner; 
	}

	public enum ScreenType
	{
		SCREEN_SPLASH,
		SCREEN_LOADING, 
		SCREEN_MAIN_MENU,
		SCREEN_UPGRADE,
		SCREEN_CAMPAIGN,
		SCREEN_MULTIPLAYER,
		SCREEN_CREATE_ROOM,
		SCREEN_FIND_ROOM,
		SCREEN_GAME_SINGLE,
		SCREEN_GAME_MULTI,
		SCREEN_WAITING_ROOM
	}
		
	private BaseScreen currentScreen;
	private ScreenType currentScreenType;
	
	private Level levelToLoad;
		
	public void createScreen(ScreenType screenType)
	{
		BaseScreen screen = getScreen(screenType);
		setScreen( screen );
	}
	
	public void createLoadingScreen(ScreenType screenToLoad)
	{
		BaseScreen loadingScreen = new LoadingScreen(runner, screenToLoad);
		setScreen(loadingScreen);
	}
	
	/** @param screenToLoad albo SCREEN_GAME_SINGLE albo ..._MULTI inaczej nie zadziala 
	 * 	@param level obiekt typu Level z informacjami o levelu do zaladowania
	 * 	@param characterTypes tablica WSZYSTKICH typow playerow wystepujacych na planszy - jesli null to ResourceManager zaladuje atlasy wszystkich dostepnych*/
	public void createLoadingScreen(ScreenType screenToLoad, Level level, Array<CharacterType> characterTypes) //przeladowanie dla gameScreen!
	{
		if(screenToLoad != ScreenType.SCREEN_GAME_SINGLE && screenToLoad != ScreenType.SCREEN_GAME_MULTI) screenToLoad = ScreenType.SCREEN_GAME_SINGLE; //mimo wszystko zabezpieczenie
		
		if(screenToLoad == ScreenType.SCREEN_GAME_SINGLE) 
		{
			Logger.log( this, "ADJUSTUJE" );
			ResourcesManager.getInstance().adjustCampaignResources(level.worldType, characterTypes);
		}
		
		this.levelToLoad = level;
		createLoadingScreen(screenToLoad);
	}
	
	private BaseScreen getScreen(ScreenType screenType)
	{
		BaseScreen screen;
		
		if(screenType == ScreenType.SCREEN_SPLASH)
			screen = new SplashScreen(runner);
		else if(screenType == ScreenType.SCREEN_MAIN_MENU)
			screen = new MainMenuScreen(runner);
		else if(screenType == ScreenType.SCREEN_UPGRADE)
			screen = new UpgradeScreen(runner);
		else if(screenType == ScreenType.SCREEN_CAMPAIGN)
			screen = new CampaignScreen(runner);
		else if(screenType == ScreenType.SCREEN_MULTIPLAYER)
			screen = new MultiplayerScreen(runner);
		else if(screenType == ScreenType.SCREEN_CREATE_ROOM)
			screen = new CreateRoomScreen(runner);
		else if(screenType == ScreenType.SCREEN_FIND_ROOM)
			screen = new FindRoomScreen(runner);
		else if(screenType == ScreenType.SCREEN_GAME_SINGLE)
			screen = new GameScreenSingle(runner);
		else if(screenType == ScreenType.SCREEN_GAME_MULTI)
			screen = new GameScreenMulti(runner);
		else if(screenType == ScreenType.SCREEN_WAITING_ROOM)
			screen = new WaitingRoom(runner);
		else
			screen = null;
		
		return screen;
	}
	
	public void setScreen(BaseScreen screen)
	{
		ScreenType previousScreenType = null;
		
		if( currentScreenType != null && currentScreenType != ScreenType.SCREEN_SPLASH )
			previousScreenType = currentScreenType;
		
		if( currentScreen != null ) currentScreen.dispose();
		currentScreen = screen;
		currentScreenType = screen.getSceneType();
	
		if( currentScreenType == ScreenType.SCREEN_GAME_SINGLE ) ((GameScreenSingle)screen).setLevel( levelToLoad );
		else if( currentScreenType == ScreenType.SCREEN_GAME_MULTI ) ((GameScreenMulti)screen).setLevel( levelToLoad );
		
		runner.setScreen(screen);
		
		if( previousScreenType != null ) Logger.log(this, "PREVIOUS: " + previousScreenType.toString());
		
		if( previousScreenType != null )
			ResourcesManager.getInstance().unloadAllResources( previousScreenType );
	}
	
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
}
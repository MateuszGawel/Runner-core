 package com.apptogo.runner.handlers;

import java.util.ArrayList;

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

public class ScreensManager {
	
	private static final ScreensManager INSTANCE = new ScreensManager();
	private Runner runner;
	
	public static ScreensManager getInstance(){ return INSTANCE; }
	public static void prepareManager(Runner runner)
	{ 
		getInstance().runner = runner; 
		getInstance().screens = new ArrayList<BaseScreen>();
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
		
	private ArrayList<BaseScreen> screens;
	private BaseScreen loadingScreen; //to niestety jest wyjatek i trzeba go obrobic osobno
	private BaseScreen currentScreen;
	private ScreenType currentScreenType;
	
	private Level levelToLoad;
		
	public void createScreen(ScreenType screenType)
	{
		int index = getScreenIndex(screenType);
		setScreen( screens.get(index) );
	}
	
	public void createLoadingScreen(ScreenType screenToLoad)
	{
		loadingScreen = new LoadingScreen(runner, screenToLoad);
		setScreen(loadingScreen);
	}
	/** @param screenType albo SCREEN_GAME_SINGLE albo ..._MULTI inaczej nie zadziala */
	public void createLoadingScreen(Level level, ScreenType screenType) //przeladowanie dla gameScreen!
	{
		if(screenType != ScreenType.SCREEN_GAME_SINGLE && screenType != ScreenType.SCREEN_GAME_MULTI) screenType = ScreenType.SCREEN_GAME_SINGLE; //mimo wszystko zabezpieczenie
		
		this.levelToLoad = level;
		createLoadingScreen(screenType);
	}
	
	private void addNewScreen(ScreenType screenType)
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
		
		if(screen != null)
			screens.add(screen);
	}
	private int getScreenIndex(ScreenType screenType)
	{
		//ta funkcja jest zasadniczo nadmiarowa ale moze bedziemy chcieli miec dostep do scen w danym stanie
		int index = -1;
		
		for(int i=0; i<screens.size(); i++)
		{
			if( screens.get(i).getSceneType() == screenType )
			{
				index = i;
				break;
			}
		}
		
		if( index == -1 )
		{
			addNewScreen(screenType);
			index = screens.size() - 1;
		}
		
		return index;
	}
	
	public void setScreen(BaseScreen screen)
	{
		//--brzydkie czyszczenie DO ZMIANY--//
		ScreenType previousScreenType = null;
		if( currentScreenType != null && currentScreenType != ScreenType.SCREEN_SPLASH )
			previousScreenType = currentScreenType;
		//----------------------------------//
		
		currentScreen = screen;
		currentScreenType = screen.getSceneType();
	
		if( currentScreenType == ScreenType.SCREEN_GAME_SINGLE ) ((GameScreenSingle)screen).setLevel( levelToLoad );
		else if( currentScreenType == ScreenType.SCREEN_GAME_MULTI ) ((GameScreenMulti)screen).setLevel( levelToLoad );
		
		runner.setScreen(screen);
		
		//--brzydkie czyszczenie DO ZMIANY--//
		if( previousScreenType != null && previousScreenType != ScreenType.SCREEN_SPLASH )
			ResourcesManager.getInstance().unloadAllResources( previousScreenType );
		//----------------------------------//
	}
	
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
}
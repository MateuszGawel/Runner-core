 package com.apptogo.runner.handlers;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.screens.CampaignScreen;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.screens.GameScreenMulti;
import com.apptogo.runner.screens.GameScreenSingle;
import com.apptogo.runner.screens.LoadingScreen;
import com.apptogo.runner.screens.MainMenuScreen;
import com.apptogo.runner.screens.MultiplayerScreen;
import com.apptogo.runner.screens.RegisterScreen;
import com.apptogo.runner.screens.SplashScreen;
import com.apptogo.runner.screens.WaitingRoomScreen;
import com.badlogic.gdx.Screen;
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
	
	private Level levelToLoad;
	private Array<Player> enemies;
		
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
	
	public void createLoadingScreen(ScreenType screenToLoad, Level levelToLoad)
	{
		BaseScreen loadingScreen = new LoadingScreen(runner, screenToLoad, levelToLoad);
		setScreen(loadingScreen);
	}
	
	/** @param screenToLoad albo SCREEN_GAME_SINGLE albo ..._MULTI inaczej nie zadziala 
	 * 	@param level obiekt typu Level z informacjami o levelu do zaladowania
	 * 	@param characterTypes tablica WSZYSTKICH typow playerow wystepujacych na planszy - jesli null to ResourceManager zaladuje atlasy wszystkich dostepnych*/
	public void createLoadingScreen(ScreenType screenToLoad, Level level, Array<Player> players) //przeladowanie dla gameScreen!
	{
		if(screenToLoad != ScreenType.SCREEN_GAME_SINGLE && screenToLoad != ScreenType.SCREEN_GAME_MULTI) screenToLoad = ScreenType.SCREEN_GAME_SINGLE; //mimo wszystko zabezpieczenie
		
		if(screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI) 
		{
			this.enemies = players;
			Array<CharacterType> characterTypes = new Array<CharacterType>();
			
			if( this.enemies != null)
			{
				for(int i = 0; i < this.enemies.size; i++) 
				{
					characterTypes.add( this.enemies.get(i).getCharacterType() );
				}
			}
			else //to oznacza ze jestesmy w singlu wiec musimy dopasowac characterType do planszy
			{
				characterTypes.add( GameWorldType.convertToCharacterType( level.worldType ) );
			}
			
			if(screenToLoad == ScreenType.SCREEN_GAME_SINGLE)
				ResourcesManager.getInstance().adjustResources(level.worldType, characterTypes, true);
			else if(screenToLoad == ScreenType.SCREEN_GAME_MULTI)
				ResourcesManager.getInstance().adjustResources(level.worldType, characterTypes, false);
		}
				
		this.levelToLoad = level;
		createLoadingScreen(screenToLoad, levelToLoad);
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
		else if(screenType == ScreenType.SCREEN_GAME_SINGLE)
			screen = new GameScreenSingle(runner);
		else if(screenType == ScreenType.SCREEN_GAME_MULTI)
			screen = new GameScreenMulti(runner);
		else if(screenType == ScreenType.SCREEN_WAITING_ROOM)
			screen = new WaitingRoomScreen(runner);
		else if(screenType == ScreenType.SCREEN_REGISTER)
			screen = new RegisterScreen(runner);
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
	
		if( currentScreenType == ScreenType.SCREEN_GAME_SINGLE || currentScreenType == ScreenType.SCREEN_GAME_MULTI )
		{
			((GameScreen)screen).setLevel( levelToLoad );
			((GameScreen)screen).setEnemies( this.enemies );
		}
		Screen prevScreen = runner.getScreen();
		runner.setScreen(screen);
		
//		Logger.log(this, "wykonuje setscreen");
//		if(prevScreen != null)
//			prevScreen.dispose();
		
		
		if( previousScreenType != null )
			ResourcesManager.getInstance().unloadAllResources( previousScreenType );
	}
	
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
}
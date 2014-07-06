 package com.apptogo.runner.handlers;

import java.util.ArrayList;

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
		SCREEN_GAME
	}
		
	private ArrayList<BaseScreen> screens;
	private BaseScreen loadingScreen; //to niestety jest wyjatek i trzeba go obrobic osobno
	private BaseScreen currentScreen;
	private ScreenType currentScreenType;
		
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
	
	private void addNewScreen(ScreenType screenType)
	{
		BaseScreen screen;
		
		if(screenType == ScreenType.SCREEN_SPLASH)
			screen = new SplashScreen(runner);
		else if(screenType == ScreenType.SCREEN_MAIN_MENU)
			screen = new MainMenuScreen(runner);
		else if(screenType == ScreenType.SCREEN_UPGRADE)
			screen = new UpgradeScreen(runner);
		else if(screenType == ScreenType.SCREEN_GAME)
			screen = new GameScreen(runner);
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
		runner.setScreen(screen);
		currentScreen = screen;
		currentScreenType = screen.getSceneType();
	}
	
    public ScreenType getCurrentScreenType(){ return currentScreenType; }
    public BaseScreen getCurrentScreen(){ return currentScreen; }
}
package com.apptogo.runner.main;

import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.SettingsManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.vars.Fonts;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Runner extends Game
{
	public static final int SCREEN_WIDTH = 1280;
	public static final int SCREEN_HEIGHT = 800;

	@Override
	public void create() 
	{
		Gdx.input.setCatchBackKey(true);
		Gdx.input.setCatchMenuKey(true); //znow - czy to odpowiednie miejsce dla tych funkcji?
		
		createManagers();
		
		ScreensManager.prepareManager(this);
		ResourcesManager.prepareManager(this);
		
		ScreensManager.getInstance().createScreen(ScreenType.SCREEN_SPLASH);
	}

	@Override
	public void render() 
	{
		super.render();	
	}
	
	@Override
	public void dispose() 
	{
		Logger.log(this, "THE APPLICATION IS DISPOSING NOW");
		
		super.dispose();
		ResourcesManager.getInstance().unloadAllApplicationResources();
		
		destroyManagers();
	}
	
	private void createManagers()
	{
		//wiem ze to troche psuje singletony ale musimy odswiezac managery za kazdym wlaczeniem aplikacji bo inaczej android je trzyma w pamieci i sie dzieja cuda 
		ResourcesManager.create(); 
		ScreensManager.create();
		SettingsManager.create();
		LanguageManager.create();
		TiledMapLoader.create();
		NotificationManager.create();
		LevelManager.create();
		SaveManager.create();
	}
	
	private void destroyManagers()
	{
		ResourcesManager.destroy();
		ScreensManager.destroy();
		SettingsManager.destroy();
		LanguageManager.destroy();
		TiledMapLoader.destroy();
		NotificationManager.destroy();
		LevelManager.destroy();
		SaveManager.destroy();
		
		FontType.dispose();
	}
}

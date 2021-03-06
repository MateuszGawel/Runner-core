package com.apptogo.runner.main;

import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.handlers.TipManager;
import com.apptogo.runner.handlers.VideoManager;
import com.apptogo.runner.levels.LevelManager;
import com.apptogo.runner.news.NewsManager;
import com.apptogo.runner.shop.ShopManager;
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
		
		try
		{
			super.render();	
			//Logger.log(this, "--------------------------------");
			//Logger.log( this, ResourcesManager.getInstance().getAssetManager(ScreenClass.SPLASH).getLoadedAssets() + " | " + ResourcesManager.getInstance().getAssetManager(ScreenClass.MENU).getLoadedAssets() + " | " + ResourcesManager.getInstance().getAssetManager(ScreenClass.GAME).getLoadedAssets() + " | " + ResourcesManager.getInstance().getAssetManager(ScreenClass.STILL).getLoadedAssets() );
		}
		catch(Exception e)
		{ 
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	@Override
	public void dispose() 
	{	
		super.dispose();
		ResourcesManager.getInstance().unloadAllApplicationResources();
		
		destroyManagers();
	}
	
	private void createManagers()
	{
		//wiem ze to troche psuje singletony ale musimy odswiezac managery za kazdym wlaczeniem aplikacji bo inaczej android je trzyma w pamieci i sie dzieja cuda 
		ShopManager.create(); //musi byc przed Resources!
		ResourcesManager.create(); 
		ScreensManager.create();
		LanguageManager.create();
		TiledMapLoader.create();
		NotificationManager.create();
		LevelManager.create();
		SaveManager.create();
		NewsManager.create();
		TipManager.create();
		CustomActionManager.create();
		VideoManager.create();
	}
	
	private void destroyManagers()
	{
		ShopManager.destroy();
		ResourcesManager.destroy();
		ScreensManager.destroy();
		LanguageManager.destroy();
		TiledMapLoader.destroy();
		NotificationManager.destroy();
		LevelManager.destroy();
		SaveManager.destroy();
		NewsManager.destroy();
		TipManager.destroy();
		CustomActionManager.destroy();
		VideoManager.destroy();
	}
}

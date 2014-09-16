package com.apptogo.runner.screens;

import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.TimeUtils;

public class LoadingScreen extends BaseScreen{	
    
	private ScreenType screenToLoad;
	private ResourcesManager resourcesManager;
	
	private TextButton slider;
	private Image sliderMask;
	
	private Label loadingLabel;
	private Label smallLabel;
	
	private Level levelToLoad;
	
	private long timeStart;
	private boolean loadingLabelIsAdded = false;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad)
	{		
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
		timeStart = TimeUtils.millis();
		
        this.screenToLoad = screenToLoad;
        this.levelToLoad = null;
        
        resourcesManager.loadResources(screenToLoad);

        if( screenToLoad == ScreenType.SCREEN_MAIN_MENU )
        {
        	resourcesManager.loadMenuResources();
        }   
	}
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad, Level levelToLoad)
	{
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
        this.screenToLoad = screenToLoad;
        this.levelToLoad = levelToLoad;
        Logger.log(this, "screenToLoad = " + screenToLoad.toString() );
        resourcesManager.loadResources(screenToLoad);
        resourcesManager.loadGameResources();
	}
	
	public void prepare() 
	{
		if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI )
		{
			if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE ) 
			{
				if( levelToLoad != null )
				{
					setBackground( CharacterType.convertToLoadingScreenBackground( GameWorldType.convertToCharacterType( levelToLoad.worldType ) ) );
				}
			}
			else if( screenToLoad == ScreenType.SCREEN_GAME_MULTI )
			{
				loadPlayer();
				setBackground( CharacterType.convertToLoadingScreenBackground( player.getCurrentCharacter() ) );
			}
					
			smallLabel = new Label( getLangString("loadingLabel"), skin, "default");
			smallLabel.setPosition( ((runner.SCREEN_WIDTH / Box2DVars.PPM) / 2.0f ) - (smallLabel.getWidth() / 2.0f), -250.0f);
			
			slider = new TextButton("", skin, "loader");
			slider.setSize(475.0f, 50.0f);
			slider.setPosition( -712.0f, -275.0f);
			
			sliderMask = new Image( new Texture( Gdx.files.internal( "gfx/menu/sliderMask.png" ) ) );
			sliderMask.setPosition( -640.0f, -275.0f);
			
			addToScreen(slider);
			addToScreen(sliderMask);
			addToScreen(smallLabel);
		}
		else
		{
			//setBackground("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
			
			loadingLabel = new Label( getLangString("loadingLabel"), skin, "default");
			loadingLabel.setPosition( ((runner.SCREEN_WIDTH / Box2DVars.PPM) / 2.0f ) - (loadingLabel.getWidth() / 2.0f), ((runner.SCREEN_HEIGHT / Box2DVars.PPM) / 2.0f ) - (loadingLabel.getHeight() / 2.0f) );
			
			//addToScreen(label);
		}
	}
	
	public void step()
	{	
		if( screenToLoad != ScreenType.SCREEN_GAME_SINGLE && screenToLoad != ScreenType.SCREEN_GAME_MULTI )
		{
			if( !loadingLabelIsAdded && ( (TimeUtils.millis() - timeStart) > 750 ) )
			{
				addToScreen(loadingLabel);
				loadingLabelIsAdded = true;
			}
		}
		
		if( resourcesManager.getAssetManager(screenToLoad).update() ) 
		{
			if( screenToLoad == ScreenType.SCREEN_MAIN_MENU )
			{
				if( ResourcesManager.getInstance().getMenuAssetManager().update() )
				{
					ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
				}
			}
			else if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI )
			{
				if( ResourcesManager.getInstance().getGameAssetManager().update() )
				{
					ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
				}
			}
			else
			{
				ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
			}
		}
		
		if( slider != null)
		{			
			AssetManager assetManager = resourcesManager.getGameAssetManager();
			float progress = assetManager.getProgress();
			
			slider.setPosition(-712.0f + (progress * 475.0f), slider.getY());	
		}
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
		//siedlisko bugow! wczesniej tego nie bylo i nic sie nie wyswietlalo - moze zrobic to w baseScreen?
		viewport.update(width, height);
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose()
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_LOADING;
	}


}

package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends BaseScreen{	
    
	private ScreenType screenToLoad;
	private ResourcesManager resourcesManager;

	//private ProgressBar slider;
	
	private TextButton slider;
	private Image sliderMask;
	
	private Label label;
	private Label smallLabel;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad)
	{
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
        this.screenToLoad = screenToLoad;
        
        resourcesManager.loadResources(screenToLoad);
	}
	
	public void prepare() 
	{
		if( screenToLoad == ScreenType.SCREEN_GAME_SINGLE || screenToLoad == ScreenType.SCREEN_GAME_MULTI )
		{
			loadPlayer();
			
			if( player.getCurrentCharacter() == CharacterType.BANDIT ) 
			{
				setBackground("ui/menuBackgrounds/loadingScreenBackgroundWildWest.png");
			}
			else if( player.getCurrentCharacter() == CharacterType.ARCHER ) 
			{
				setBackground("ui/menuBackgrounds/loadingScreenBackgroundForrest.png");
			}
					
			smallLabel = new Label( getLangString("loadingLabel"), skin, "default");
			smallLabel.setPosition( ((runner.SCREEN_WIDTH / Box2DVars.PPM) / 2.0f ) - (smallLabel.getWidth() / 2.0f), -250.0f);
			
			slider = new TextButton("", skin, "default");
			slider.setSize(475.0f, 50.0f);
			slider.setPosition( -712.0f, -275.0f);
			
			sliderMask = new Image( new Texture( Gdx.files.internal( "ui/menuBackgrounds/sliderMask.png" ) ) );
			sliderMask.setPosition( -640.0f, -275.0f);
			
			addToScreen(slider);
			addToScreen(sliderMask);
			addToScreen(smallLabel);
		}
		else
		{
			setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
			
			label = new Label( getLangString("loadingLabel"), skin, "default");
			label.setPosition( ((runner.SCREEN_WIDTH / Box2DVars.PPM) / 2.0f ) - (label.getWidth() / 2.0f), ((runner.SCREEN_HEIGHT / Box2DVars.PPM) / 2.0f ) - (label.getHeight() / 2.0f) );
			
			addToScreen(label);
		}
	}
	
	public void step()
	{	

		if( resourcesManager.getAssetManager(screenToLoad).update() ) 
		{
			ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
		}
		
		if( slider != null)
		{			
			float progress = resourcesManager.getAssetManager(screenToLoad).getProgress();
			
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
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_LOADING;
	}


}

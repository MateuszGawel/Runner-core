package com.apptogo.runner.screens;

import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.VideoManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.settings.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public abstract class BaseScreen implements Screen
{	
	protected Runner runner;
	public int currentWindowWidth, currentWindowHeight;
	protected LanguageManager languageManager;
	protected Image background;

	protected Settings settings;
	
	protected Stage menuStage;
	protected Stage menuBackgroundStage;
	protected Stage fadeStage;
	protected ExtendViewport viewport;
	protected FillViewport backgroundViewport;
	protected FillViewport fadeViewport;
	protected Skin skin;
	
	public Stage gameGuiStage;
	public Viewport guiViewport;
	public OrthographicCamera guiCamera;
	protected InputMultiplexer inputMultiplexer;
	
	protected float delta;
	
	protected boolean isFinished = false;
	
	protected Player player;
	
	protected Actor fade;
	AlphaAction fadeOutAction;
	AlphaAction fadeInAction;
	
	protected ScreenType screenToLoadAfterFadeOut;
	
	SaveManager saveManager;
	
	boolean recordVideo = false;
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	public abstract void step();
	public abstract void prepare();
	public String getLangString(String key){ return languageManager.getString(key);	}	
		
	protected BaseScreen(Runner runner) 
	{
		this.runner = runner;
		
		settings = Settings.load();
		
		saveManager = SaveManager.getInstance();
		
		this.languageManager = LanguageManager.getInstance();
		
		this.languageManager.setCurrentLanguage( settings.getLanguage() );		
		
		this.player = Player.load();
	}
	
	@Override
	public void show() 
	{
		skin = ResourcesManager.getInstance().getUiSkin( ScreenType.convertToScreenClass( this.getSceneType() ) );
		
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI)  )
		{
			menuStage = new Stage();
			viewport = new ExtendViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuStage.setViewport( viewport );
			
			menuBackgroundStage = new Stage();
			backgroundViewport = new FillViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuBackgroundStage.setViewport( backgroundViewport );
						
			Gdx.input.setInputProcessor(menuStage);
			
			this.prepare();
		}
		else
		{	
			gameGuiStage = new Stage();
			guiViewport = new FitViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			gameGuiStage.setViewport(guiViewport);
			gameGuiStage.getRoot().setTransform(false);
			this.prepare();
			
			inputMultiplexer = new InputMultiplexer(); 
			inputMultiplexer.addProcessor(gameGuiStage);
			inputMultiplexer.addProcessor(new InputHandler());
			
			Gdx.input.setInputProcessor(inputMultiplexer);
		}
		
		fadeStage = new Stage();
		fadeViewport = new FillViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		fadeStage.setViewport( fadeViewport );
					
		initializeFade();
	}
	
	private void initializeFade() 
	{
		fadeOutAction = new AlphaAction();
		fadeOutAction.setAlpha(1.0f);
		fadeOutAction.setDuration(0.3f);
		
		fadeInAction = new AlphaAction();
		fadeInAction.setAlpha(0f);
		fadeInAction.setDuration(0.3f);
		
		fade = new Image( ResourcesManager.getInstance().getAtlasRegion(ScreenClass.STILL, "blackNonTransparent") );

		fade.getColor().a = 1f;
		fade.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		fade.setPosition(-Runner.SCREEN_WIDTH/2.0f, -Runner.SCREEN_HEIGHT/2.0f);
		fade.addAction(fadeInAction);
				
	    fadeStage.addActor(fade);
	}
	
	@Override
	public void render(float delta) 
	{
		//recording video - generalnie chyba powinnismy to wywalic przed deployem lacznie z videomanagerem
		if( recordVideo )
		{
			VideoManager.getInstance().makeScreenshot();
			
			if( Gdx.input.isKeyJustPressed( Keys.V ) )
			{
				recordVideo = false;
				VideoManager.getInstance().saveScreenshots();
			}			
		}
		else
		{
			if( Gdx.input.isKeyJustPressed( Keys.V ) ) 
			{
				recordVideo = true;
			}
		}
		//end of recording video
		
		this.delta = delta;
		
		if( fade.getColor().a >= 1 && screenToLoadAfterFadeOut != null )
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			
			Logger.log(this, fade.getColor().a);
			
			ScreensManager.getInstance().createLoadingScreen( screenToLoadAfterFadeOut );
		}
		
		//Logger.log(this, "liczba rendercalli menuFadeStage: " + ((SpriteBatch)menuFadeStage.getBatch()).renderCalls);
		
		if (!isFinished) 
		{
			this.step();	
			
			if( this.getScreenClass() != ScreenClass.GAME )
			{
				Gdx.gl.glClearColor(0, 0, 0, 1);
				Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
				
				backgroundViewport.update(currentWindowWidth, currentWindowHeight);
				menuBackgroundStage.act();   
				menuBackgroundStage.draw();
						
				//Logger.log(this, "liczba rendercalli menuBackgroundStage: " + ((SpriteBatch)menuBackgroundStage.getBatch()).renderCalls);
				
				viewport.update(currentWindowWidth, currentWindowHeight);
				menuStage.act();
				menuStage.draw();
				
				//Logger.log(this, "liczba rendercalli menuStage: " + ((SpriteBatch)menuStage.getBatch()).renderCalls);
			}
			else
			{
				gameGuiStage.getViewport().update(currentWindowWidth, currentWindowHeight, true);
				gameGuiStage.act(delta);
	    		gameGuiStage.draw();
	    		//Logger.log(this, "liczba rendercalli gameGuiStage: " + ((SpriteBatch)gameGuiStage.getBatch()).renderCalls);
			}
			
			fadeViewport.update(currentWindowWidth, currentWindowHeight);
			fadeStage.act();
			
			if( fade.getColor().a > 0 )
			{
				fadeStage.draw();
			}
			
			//Logger.log(this, "liczba rendercalli fadeStage: " + ((SpriteBatch)fadeStage.getBatch()).renderCalls);
		}
				
		CustomActionManager.getInstance().act(delta);
	}	
	
	@Override
	public void resize(int width, int height) {
		currentWindowWidth = width;
		currentWindowHeight = height;
	}
	
	protected void setBackground(String regionName)
	{
		background = new Image ( ResourcesManager.getInstance().getAtlasRegion( getScreenClass(), regionName) );
		
		background.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		background.setPosition(0 - (Runner.SCREEN_WIDTH/2.0f), 0 - (Runner.SCREEN_HEIGHT/2.0f));
		menuBackgroundStage.addActor( background );
	}
		
	protected void addToBackground(Actor actor)
	{
		this.menuBackgroundStage.addActor(actor);
	}
	
	protected void addToScreen(Actor actor)
	{
		this.menuStage.addActor(actor);
	}
	
	protected void addToFade(Actor actor)
	{
		this.fadeStage.addActor(actor);
	}
		
	protected void loadScreenAfterFadeOut( ScreenType screenType )
	{
		fadeOutAction.reset();
		fade.addAction( fadeOutAction );		
		screenToLoadAfterFadeOut = screenType;
	}
		
	protected Image createImage(String imageName, float x, float y)
	{
		return createImage(imageName, x, y, TextureFilter.Linear, TextureFilter.Linear);
	}
	protected Image createImage(String imageName, float x, float y, TextureFilter minFilter, TextureFilter magFilter)
	{
		Image image;
		AtlasRegion atlasRegion = ResourcesManager.getInstance().getAtlasRegion( getScreenClass(), imageName);
		
		if( atlasRegion != null )
		{
			image = new Image( atlasRegion );
		}
		else
		{
			Texture texture = ResourcesManager.getInstance().getResource(this, imageName);
			texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
			image = new Image( texture );
		}
		
		//texture.setFilter(minFilter, magFilter);
		
		//Image image = new Image(texture);
		image.setPosition(x, y);
		
		return image;
	}
	
	protected ScrollPane createScroll(Table table, float width, float height, boolean vertical)
	{
		final ScrollPane scroller = new ScrollPane(table, skin);
		
		if(vertical)
		{
			scroller.setScrollingDisabled(true, false);
		}
		else
		{
			scroller.setScrollingDisabled(false, true);
		}
		
        scroller.setFadeScrollBars(false);
        scroller.setSize(width, height);
        
        return scroller;
	}
			
	protected void setCenterPosition(Actor actor, float y)
	{
		actor.setPosition(-(actor.getWidth() / 2.0f), y);
	}
	
	protected RepeatAction getBlinkAction(float maxAlpha, float minAlpha, float duration)
	{
		AlphaAction showAction = new AlphaAction();
		showAction.setAlpha(maxAlpha);
		showAction.setDuration(duration);
		
		AlphaAction hideAction = new AlphaAction();
		hideAction.setAlpha(minAlpha);
		hideAction.setDuration(duration);
		
		return Actions.forever( new SequenceAction(showAction, hideAction) );
	}
	
	protected RepeatAction getFlipAction(float pause)
	{
		RotateByAction action = new RotateByAction();
		action.setAmount(360);
		action.setInterpolation(Interpolation.elasticOut);
		action.setDuration(2f);
		
		RotateByAction delayAction = new RotateByAction();
		delayAction.setAmount(0);
		delayAction.setDuration(pause);
		
		return Actions.forever( new SequenceAction(action, delayAction) );
	}
	
	protected ScreenClass getScreenClass()
	{
		return ScreenType.convertToScreenClass( this.getSceneType() );
	}
	
	@Override
	public void dispose() 
	{
		isFinished = true;
		
		if( menuStage != null)
		{
			menuStage.clear();
			menuStage.dispose();
		}
		if( menuBackgroundStage != null)
		{
			menuBackgroundStage.clear();
			menuBackgroundStage.dispose();
		}
		if(gameGuiStage != null)
		{
			gameGuiStage.clear();
			gameGuiStage.dispose();
		}
		if( fadeStage != null)
		{
			fadeStage.clear();
			fadeStage.dispose();
		}
	}
}

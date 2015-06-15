package com.apptogo.runner.screens;

import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.enums.ScreenClass;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.settings.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
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
	protected int currentWindowWidth, currentWindowHeight;
	protected LanguageManager languageManager;
	protected Image background;

	protected Settings settings;
	
	protected Stage menuStage;
	protected Stage menuBackgroundStage;
	protected Stage menuFadeStage;
	protected ExtendViewport viewport;
	protected FillViewport backgroundViewport;
	protected FillViewport fadeViewport;
	protected Skin skin;
	
	public Stage gameGuiStage;
	public Viewport guiViewport;
	public OrthographicCamera guiCamera;
	protected InputMultiplexer inputMultiplexer;
	
	protected float delta;
	
	protected Player player;
	
	protected Actor fade;
	AlphaAction fadeOutAction;
	AlphaAction fadeInAction;
	
	protected ScreenType screenToLoadAfterFadeOut;
	
	SaveManager saveManager;
	
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
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI)  )
		{
			menuStage = new Stage();
			viewport = new ExtendViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuStage.setViewport( viewport );
			
			menuBackgroundStage = new Stage();
			backgroundViewport = new FillViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuBackgroundStage.setViewport( backgroundViewport );
			
			menuFadeStage = new Stage();
			fadeViewport = new FillViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuFadeStage.setViewport( fadeViewport );
			
			skin = ResourcesManager.getInstance().getUiSkin( ScreenType.convertToScreenClass( this.getSceneType() ) );
			
			initializeFade();
			
			Gdx.input.setInputProcessor(menuStage);
			
			this.prepare();
		}
		else
		{
			//TEGO NIE POWINNO TU BYC BO NIE CHCEMY MIEC SKINA W GS W PAMIECI!
			skin = ResourcesManager.getInstance().getUiSkin( ScreenType.convertToScreenClass( this.getSceneType() ) );
			//----------------------------------------------------------------
			//tylko na potrzeby testow
			
			
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

		fade.getColor().a = 0.99f;
		fade.setSize(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		fade.setPosition(-Runner.SCREEN_WIDTH/2.0f, -Runner.SCREEN_HEIGHT/2.0f);
		fade.addAction(fadeInAction);
				
	    menuFadeStage.addActor(fade);
	}
	
	@Override
	public void render(float delta) 
	{
		this.delta = delta;

		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI) )
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			this.step();
			
			if( fade.getColor().a >= 1 && screenToLoadAfterFadeOut != null )
			{
				ScreensManager.getInstance().createLoadingScreen( screenToLoadAfterFadeOut );
			}
			
			backgroundViewport.update(currentWindowWidth, currentWindowHeight);
			menuBackgroundStage.act();   
			menuBackgroundStage.draw();
					
			//Logger.log(this, "liczba rendercalli menuBackgroundStage: " + ((SpriteBatch)menuBackgroundStage.getBatch()).renderCalls);
			
			viewport.update(currentWindowWidth, currentWindowHeight);
			menuStage.act();
			menuStage.draw();
			
			//Logger.log(this, "liczba rendercalli menuStage: " + ((SpriteBatch)menuStage.getBatch()).renderCalls);
			
			fadeViewport.update(currentWindowWidth, currentWindowHeight);
			menuFadeStage.act();
			menuFadeStage.draw();
			
			//Logger.log(this, "liczba rendercalli menuFadeStage: " + ((SpriteBatch)menuFadeStage.getBatch()).renderCalls);
		}
		else
		{
			this.step();
			gameGuiStage.getViewport().update(currentWindowWidth, currentWindowHeight, true);
			gameGuiStage.act(delta);
    		gameGuiStage.draw();
    		//Logger.log(this, "liczba rendercalli gameGuiStage: " + ((SpriteBatch)gameGuiStage.getBatch()).renderCalls);
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
		this.menuFadeStage.addActor(actor);
	}
		
	protected void loadScreenAfterFadeOut( ScreenType screenType )
	{
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
		if( menuFadeStage != null)
		{
			menuFadeStage.clear();
			menuFadeStage.dispose();
		}
		
		if(gameGuiStage != null){
			gameGuiStage.clear();
			gameGuiStage.dispose();
		}
	}
}

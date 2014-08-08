package com.apptogo.runner.screens;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.handlers.SettingsManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.player.SaveManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseScreen implements Screen{
	
	protected Runner runner;
	protected LanguageManager languageManager;
	protected SettingsManager settingsManager;
	protected Image background;
	
	protected Stage stage;
	protected Viewport viewport;
	protected Skin skin;
	
	public Stage guiStage;
	public StretchViewport guiStretchViewport;
	public OrthographicCamera guiCamera;
	protected InputMultiplexer inputMultiplexer;
	
	protected float delta;
	
	protected Player player; //to jest wazne - musi byc dostep do playera juz na poziomie menu - chcemy miec o nim info w menu
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	public abstract void step();
	public abstract void prepare();
	public String getLangString(String key){ return languageManager.getString(key);	}
	
	protected BaseScreen(Runner runner) 
	{
		this.runner = runner;
		this.settingsManager = SettingsManager.getInstance();
		this.languageManager = LanguageManager.getInstance();
		this.languageManager.setCurrentLanguage( settingsManager.getLanguage() );
	}
	
	/** Powoduje zaladowanie playera z pamieci - powinno byc wywolywane tam, gdzie potrzeba dostepu do playera! */
	protected void loadPlayer()
	{
		this.player = SaveManager.getInstance().loadPlayer();
		Logger.log(this, "ZALADOWANY PLAYER " + player.getCurrentCharacter().toString() );
	}
	
	@Override
	public void show() 
	{
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI)  )
		{
			stage = new Stage();
			viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			stage.setViewport(viewport);
			skin = ResourcesManager.getInstance().getUiSkin();
			
			this.prepare();
			
			Gdx.input.setInputProcessor(stage);
		}
		else
		{
			guiStage = new Stage();
			guiCamera = (OrthographicCamera) guiStage.getCamera();  
			guiCamera.setToOrtho(false, Runner.SCREEN_WIDTH/PPM, Runner.SCREEN_HEIGHT/PPM);
			guiStretchViewport = new StretchViewport(Runner.SCREEN_WIDTH/PPM, Runner.SCREEN_HEIGHT/PPM, guiCamera);
			guiStage.setViewport(guiStretchViewport);
			
			
			this.prepare();
			
			inputMultiplexer = new InputMultiplexer(); 
			inputMultiplexer.addProcessor(guiStage);
			inputMultiplexer.addProcessor(new InputHandler());
			
			Gdx.input.setInputProcessor(inputMultiplexer);
		}
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
			
			stage.act();
			stage.draw();
		}
		else
		{
			this.step();
			
			guiStage.act(delta);
			guiCamera.update();
    		guiStage.draw();
		}
	}	
	
	protected void setBackground(String path)
	{
		background = new Image( new Texture( Gdx.files.internal(path) ) );
		background.setPosition(0 - (runner.SCREEN_WIDTH/2.0f), 0 - (runner.SCREEN_HEIGHT/2.0f));
		stage.addActor( background );
	}

	protected void addToScreen(Actor actor)
	{
		this.stage.addActor(actor);
	}
	
	@Override
	public void dispose() 
	{
		//skin.dispose();
		stage.clear();
		stage.dispose();
	}
}

package com.apptogo.runner.screens;

import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.LanguageManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class BaseScreen implements Screen{
	
	protected Runner runner;
	protected LanguageManager languageManager;
	protected Image background;
	
	protected Stage stage;
	protected Viewport viewport;
	protected Skin skin;
	
	public Stage guiStage;
	public StretchViewport guiStretchViewport;
	public OrthographicCamera guiCamera;
	protected InputMultiplexer inputMultiplexer;
	
	protected float delta;
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	public abstract void step();
	public abstract void prepare();
	public String getLangString(String key){ return languageManager.getString(key);	}
	
	protected BaseScreen(Runner runner) 
	{
		this.runner = runner;
		this.languageManager = LanguageManager.getInstance();
		this.languageManager.setCurrentLanguage("pl");
	}
	
	@Override
	public void show() 
	{
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME) )
		{
			stage = new Stage();
			viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			stage.setViewport(viewport);
			skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
			
			this.prepare();
			
			Gdx.input.setInputProcessor(stage);
		}
		else
		{
			guiStage = new Stage();
			guiCamera = (OrthographicCamera) guiStage.getCamera();  
			guiStretchViewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT, guiCamera);
			
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
		
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME) )
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
}

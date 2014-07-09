package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MultiplayerScreen extends BaseScreen{	
	
	private Stage stage;
	private Viewport viewport;
	
	private Skin skin;
	
	private Label label;
	private TextButton createRoomButton;
	private TextButton findRoomButton;
	private TextButton randomRoomButton;
	private TextButton backButton;
	
	public MultiplayerScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
				
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		label = new Label("MULTIPLAYER - DOLACZANIE DO GRY", skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
		
        createRoomButton = new TextButton("CREATE ROOM", skin, "default");
        createRoomButton.setWidth(200f);
        createRoomButton.setHeight(20f);
        createRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - createRoomButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - createRoomButton.getHeight()/2.0f );
        createRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_CREATE_ROOM);
            }
         });
        
        findRoomButton = new TextButton("FIND ROOM", skin, "default");
        findRoomButton.setWidth(200f);
        findRoomButton.setHeight(20f);
        findRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - findRoomButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - findRoomButton.getHeight()/2.0f - 30f );
        findRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_FIND_ROOM);
            }
         });
        
        randomRoomButton = new TextButton("JOIN RANDOM ROOM", skin, "default");
        randomRoomButton.setWidth(200f);
        randomRoomButton.setHeight(20f);
        randomRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - randomRoomButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - randomRoomButton.getHeight()/2.0f - 60f );
        randomRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
            }
         });
        
        backButton = new TextButton("BACK", skin, "default");
        backButton.setWidth(200f);
        backButton.setHeight(20f);
        backButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - backButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - backButton.getHeight()/2.0f - 90f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
		stage = new Stage();
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage.setViewport(viewport);
		
		stage.addActor(createRoomButton);
		stage.addActor(findRoomButton);
		stage.addActor(randomRoomButton);
		stage.addActor(backButton);
		stage.addActor(label);
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_UPGRADE;
	}


}

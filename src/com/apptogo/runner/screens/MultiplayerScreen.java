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
		
	private Label label;
	private TextButton createRoomButton;
	private TextButton findRoomButton;
	private TextButton backButton;
	
	public MultiplayerScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void prepare()
	{		
		label = new Label( getLangString("multiplayerLabel"), skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
		
        createRoomButton = new TextButton( getLangString("createRoomMenuButton"), skin, "default");
        createRoomButton.setWidth(200f);
        createRoomButton.setHeight(20f);
        createRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - createRoomButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - createRoomButton.getHeight()/2.0f );
        createRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_CREATE_ROOM);
            }
         });
        
        findRoomButton = new TextButton( getLangString("joinRoomMenuButton"), skin, "default");
        findRoomButton.setWidth(200f);
        findRoomButton.setHeight(20f);
        findRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - findRoomButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - findRoomButton.getHeight()/2.0f - 30f );
        findRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_FIND_ROOM);
            }
         });
                
        backButton = new TextButton( getLangString("backButton"), skin, "default");
        backButton.setWidth(200f);
        backButton.setHeight(20f);
        backButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - backButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - backButton.getHeight()/2.0f - 60f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        		
        addToScreen(createRoomButton);
        addToScreen(findRoomButton);
        addToScreen(backButton);
        addToScreen(label);
	}
	
	public void step()
	{
		
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

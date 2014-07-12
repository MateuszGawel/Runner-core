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

public class CreateRoomScreen extends BaseScreen{	
	
	private Label label;
	private TextButton button;
	private TextButton backButton;
	
	public CreateRoomScreen(Runner runner)
	{
		super(runner);	
	}

	public void prepare() 
	{
		label = new Label( getLangString("createRoomLabel"), skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
		
		button = new TextButton( getLangString("createRoomButton"), skin, "default");
        button.setWidth(200f);
        button.setHeight(20f);
        button.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - button.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - button.getHeight()/2.0f );
        button.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
            }
         });
        
        backButton = new TextButton( getLangString("backButton"), skin, "default");
        backButton.setWidth(200f);
        backButton.setHeight(20f);
        backButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - backButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - backButton.getHeight()/2.0f - 30f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MULTIPLAYER);
            }
         });
        		
        addToScreen(button);
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

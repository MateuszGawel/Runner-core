package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.LanguageManager;
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

public class MainMenuScreen extends BaseScreen{	
	
	private TextButton upgradeButton;
	private TextButton campaignButton;
	private TextButton multiplayerButton;
	private Label label;
	
	public MainMenuScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void prepare() 
	{		
		setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
		
        upgradeButton = new TextButton( getLangString("upgradeButton"), skin, "default");
        upgradeButton.setWidth(200f);
        upgradeButton.setHeight(20f);
        upgradeButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - upgradeButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - upgradeButton.getHeight()/2.0f );
        upgradeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_UPGRADE);
            }
         });
        
        campaignButton = new TextButton( getLangString("campaignButton"), skin, "default");  //campaignButton = new TextButton("CAMPAIGN", skin, "default");
		campaignButton.setWidth(200f);
		campaignButton.setHeight(20f);
		campaignButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - campaignButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - campaignButton.getHeight()/2.0f - 30f );
		campaignButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_CAMPAIGN);
            }
         });
		
		multiplayerButton = new TextButton( getLangString("multiplayerButton"), skin, "default");
		multiplayerButton.setWidth(200f);
		multiplayerButton.setHeight(20f);
		multiplayerButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - multiplayerButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - multiplayerButton.getHeight()/2.0f - 60f );
		multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MULTIPLAYER);
            }
         });
        
        label = new Label( getLangString("mainMenuLabel"), skin);
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 250 );
		
        addToScreen(upgradeButton);
        addToScreen(campaignButton);
        addToScreen(multiplayerButton);
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
		return ScreenType.SCREEN_MAIN_MENU;
	}


}

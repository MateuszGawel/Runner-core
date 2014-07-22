package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.handlers.Widget.WidgetFadingType;
import com.apptogo.runner.handlers.Widget.WidgetType;
import com.apptogo.runner.handlers.Widget;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;
import com.sun.org.apache.xml.internal.security.keys.content.KeyName;

public class MainMenuScreen extends BaseScreen{	
	
	//private TextButton upgradeButton;
	
	Button soundButton;
	Button settingsButton;
	
	private TextButton campaignButton;
	private TextButton multiplayerButton;
	Button joinRandomRoomButton;
	private Label label;
	private Widget widget;
	
	//DO WYWALENIA!!!!!!!!!!!!!!!!
	private boolean wasntPressed = true;
	private boolean wasntPressed2 = true;
	private int framesToChange = 160;
	//----------------------------
	
	public MainMenuScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void prepare() 
	{		
		setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
		
        /*upgradeButton = new TextButton( getLangString("upgradeButton"), skin, "default");
        upgradeButton.setWidth(200f);
        upgradeButton.setHeight(20f);
        upgradeButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - upgradeButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - upgradeButton.getHeight()/2.0f );
        upgradeButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_UPGRADE);
            }
         });*/
		
		soundButton = new Button(skin, "sound");
		soundButton.setWidth(80);
		soundButton.setHeight(80);
		soundButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - soundButton.getWidth()/2.0f - 530f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - soundButton.getHeight()/2.0f + 300f );
		
		settingsButton = new Button(skin, "settings");
		settingsButton.setWidth(80);
		settingsButton.setHeight(80);
		settingsButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - settingsButton.getWidth()/2.0f - 530f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - settingsButton.getHeight()/2.0f + 200f );
        
        campaignButton = new TextButton( getLangString("campaignButton"), skin, "default");
		campaignButton.setWidth(500f);
		campaignButton.setHeight(200f);
		campaignButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - campaignButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - campaignButton.getHeight()/2.0f + 50f );
		campaignButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_CAMPAIGN);
            }
         });
		
		multiplayerButton = new TextButton( getLangString("multiplayerButton"), skin, "default");
		multiplayerButton.setWidth(500f);
		multiplayerButton.setHeight(200f);
		multiplayerButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - multiplayerButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - multiplayerButton.getHeight()/2.0f - 170f );
		multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MULTIPLAYER);
            }
         });
        
		joinRandomRoomButton = new Button(skin, "joinRandomRoom");
		joinRandomRoomButton.setWidth(200);
		joinRandomRoomButton.setHeight(200);
		joinRandomRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - joinRandomRoomButton.getWidth()/2.0f + 370f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - joinRandomRoomButton.getHeight()/2.0f - 170f );
		joinRandomRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen( new Level("", "gfx/game/levels/map.tmx") );
            }
         });
		
        label = new Label( getLangString("mainMenuLabel"), skin, "title");
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 200 );
		
        widget = new Widget(Align.center, 410f, 400f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
        widget.setToggleButton(true);
        
        //addToScreen(upgradeButton);
        addToScreen(soundButton);
        addToScreen(settingsButton);
        addToScreen(campaignButton);
        addToScreen(multiplayerButton);
        addToScreen(joinRandomRoomButton);
        addToScreen(label);
        addToScreen( widget.actor() );
	}
	
	public void step()
	{
		if( Gdx.input.isKeyPressed(Keys.A) && wasntPressed )
		{
			widget.showWidget();
			wasntPressed = false;
		}
		if( Gdx.input.isKeyPressed(Keys.S) && wasntPressed2 )
		{
			widget.hideWidget();
			wasntPressed2 = false;
		}		
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

package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.InfoWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.apptogo.runner.widget.Widget.WidgetType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sun.corba.se.impl.oa.poa.ActiveObjectMap.Key;

public class MainMenuScreen extends BaseScreen{	
	
	Button soundButton;
	Button settingsButton;
	Button joinRandomRoomButton;
	
	private TextButton campaignButton;
	private TextButton multiplayerButton;
	
	private Label label;
	private Widget widget;
	private Widget playerNameWidget;
	private DialogWidget changeLanguageDialog;
		
	private boolean languageChanged = false;
	private ClickListener languageChangedListener = null;
	private String languageToChange;
	
	public MainMenuScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
	}
	
	@Override
	public void prepare() 
	{
		setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
				
		languageChangedListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
            	languageChanged = true;
            }
		};
		
		soundButton = new Button(skin, "sound");
		soundButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - soundButton.getWidth()/2.0f - 530f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - soundButton.getHeight()/2.0f + 300f );
		
		settingsButton = new Button(skin, "settings");
		settingsButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - settingsButton.getWidth()/2.0f - 530f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - settingsButton.getHeight()/2.0f + 200f );
        
        campaignButton = new TextButton( getLangString("campaignButton"), skin, "default");
		campaignButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - campaignButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - campaignButton.getHeight()/2.0f + 50f );
		campaignButton.addListener(new ClickListener() { //brzydkie jak chuj zmienic koniecznie [chociaz jakas metoda generujaca listenery
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_CAMPAIGN);
            }
         });
		
		multiplayerButton = new TextButton( getLangString("multiplayerButton"), skin, "default");
		multiplayerButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - multiplayerButton.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - multiplayerButton.getHeight()/2.0f - 170f );
		multiplayerButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MULTIPLAYER);
            }
         });
        
		joinRandomRoomButton = new Button(skin, "joinRandomRoom");
		joinRandomRoomButton.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - joinRandomRoomButton.getWidth()/2.0f + 370f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - joinRandomRoomButton.getHeight()/2.0f - 170f );
		joinRandomRoomButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_WAITING_ROOM );
            }
         });//na totalna pale na razie
		
        label = new Label( getLangString("mainMenuLabel"), skin, "title");
        label.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - label.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f + 200 );
        
        changeLanguageDialog = new DialogWidget("Are you sure?", null, languageChangedListener);
        InfoWidget i = new InfoWidget("cokolwiek");
        soundButton.addListener(i.getToggleListener());
        
        Image plflag = new Image( new Texture(languageManager.getIcoFile("pl")) );
        plflag.setPosition(-150f, 600f);        
        plflag.addListener( changeLanguageDialog.getToggleListener() );
        plflag.addListener( new ClickListener()
        {  
        	public void clicked(InputEvent event, float x, float y) 
            {
            	languageToChange = "pl";
            }
        } );
        
        Image enflag = new Image( new Texture(languageManager.getIcoFile("en")) );
        enflag.setPosition(-50f, 600f);
        enflag.addListener( changeLanguageDialog.getToggleListener() );
        enflag.addListener( new ClickListener()
        {  
        	public void clicked(InputEvent event, float x, float y) 
            {
            	languageToChange = "en";
            }
        } );
        
        widget = new Widget(Align.center, 400f, 400f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, false);
        widget.setToggleButton(true);
        widget.setEasing(Interpolation.bounceOut);
        
        widget.addActor(plflag);
        widget.addActor(enflag);
        
        
        //---
        final TextField textField = new TextField(player.getName(), skin);
        textField.setSize(300f, 50f);
        textField.setPosition(-230f, -25f);
        
        final SelectBox selectCharacter = new SelectBox<String>(skin, "default");
        selectCharacter.setSize(250f, 50f);
        selectCharacter.setPosition(90f, -25f);
        selectCharacter.setItems(new String[]{ CharacterType.BANDIT.toString(), CharacterType.ARCHER.toString() });
        selectCharacter.setSelected( player.getCurrentCharacter().toString() );
        
        ClickListener savePlayerListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				player.setName( textField.getText() );
				player.setCurrentCharacter( CharacterType.parseFromString( (String)selectCharacter.getSelected() ) );
				SaveManager.getInstance().savePlayer(player);
				playerNameWidget.toggleWidget();
            }
		};
        
        playerNameWidget = new DialogWidget("Imie gracza:", null, savePlayerListener);
        playerNameWidget.addActor(textField);
        playerNameWidget.addActor(selectCharacter);
        
        settingsButton.addListener(playerNameWidget.getToggleListener());
        //soundButton.addListener( widget.getToggleListener() ); //podpiecie listenera z widgetu do przycisku na scenie :)

        addToScreen(soundButton);
        addToScreen(settingsButton);
        addToScreen(campaignButton);
        addToScreen(multiplayerButton);
        addToScreen(joinRandomRoomButton);
        addToScreen(label);
        addToScreen( widget.actor() );
        addToScreen( changeLanguageDialog.actor() );
        addToScreen( i.actor() );
        addToScreen(playerNameWidget.actor());
        
        if( player.isAnonymous() ) playerNameWidget.toggleWidget();
	}
	
	public void step()
	{
		if( languageChanged ) 
		{
			languageChanged = false;
			settingsManager.setLanguage(languageToChange);
			ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_MAIN_MENU );		
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
	public void dispose() 
	{
		super.dispose();
		widget.dispose();
	    changeLanguageDialog.dispose();
	    playerNameWidget.dispose();
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_MAIN_MENU;
	}


}

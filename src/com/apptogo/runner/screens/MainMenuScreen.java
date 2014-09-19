package com.apptogo.runner.screens;

import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.apptogo.runner.widget.Widget.WidgetType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainMenuScreen extends BaseScreen{	
	
	Button settingsButton;
	Button soundButtonOn;
	Button soundButtonOff;
	Button googlePlusButton;
	Button facebookButton;
	Button joinRandomRoomButton;
	
	private TextButton campaignButton;
	private TextButton multiplayerButton;
	
	private Widget settingsWidget;
	
	private ClickListener settingsButtonListener;
	private ClickListener soundButtonOnListener;
	private ClickListener soundButtonOffListener;
	private ClickListener googlePlusButtonListener;
	private ClickListener facebookButtonListener;
	private ClickListener campaignButtonListener;
	private ClickListener multiplayerButtonListener;
	private ClickListener joinRandomRoomButtonListener;
	
	private Texture chainsDecorationTexture;
	private Image chainsDecoration;	
	
	private boolean languageChanged = false;
	private ClickListener languageChangedListener = null;
	private String languageToChange;
	
	public MainMenuScreen(Runner runner)
	{
		super(runner);	
		loadPlayer();
		
		ResourcesManager.getInstance().unloadLogoResources();
		ResourcesManager.getInstance().unloadGameResources();
		
		fadeInOnStart();
	}
	
	@Override
	public void prepare() 
	{		
		setBackground("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
					
		settingsButton = new Button(skin, "settings");
		settingsButton.setPosition(-570f, 240f);
		
		soundButtonOn = new Button(skin, "soundOn");
		soundButtonOn.setPosition(-570f, 105f);
		
		soundButtonOff = new Button(skin, "soundOff");
		soundButtonOff.setPosition(-570f, 105f);
		soundButtonOff.setVisible(false);
		
		googlePlusButton = new Button(skin, "googlePlus");
		googlePlusButton.setPosition(-570f, -30f);
		
		facebookButton = new Button(skin, "facebook");
		facebookButton.setPosition(-570f, -165f);
			
        campaignButton = new TextButton( getLangString("campaignButton"), skin, "default");
        
        campaignButton.setPosition( -(campaignButton.getWidth() / 2.0f), 0.0f );
		
		multiplayerButton = new TextButton( getLangString("multiplayerButton"), skin, "default");
		multiplayerButton.setPosition( -(multiplayerButton.getWidth() / 2.0f), -200.0f );
		
		joinRandomRoomButton = new Button(skin, "joinRandomRoom");
		joinRandomRoomButton.setPosition( 85.0f, -325.0f );
		
		settingsWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		settingsWidget.setEasing( Interpolation.elasticOut );
		
		createListeners();
		setListeners();
        
		chainsDecorationTexture = new Texture( Gdx.files.internal("gfx/menu/chainsDecoration.png") );
		chainsDecoration = new Image( chainsDecorationTexture );
		chainsDecoration.setPosition(100.0f, -240.0f);
		
        //Image enflag = new Image( new Texture(languageManager.getIcoFile("en")) );
        //enflag.setPosition(-50f, 600f);
        //enflag.addListener( changeLanguageDialog.getToggleListener() );
        /*enflag.addListener( new ClickListener()
        {  
        	public void clicked(InputEvent event, float x, float y) 
            {
            	languageToChange = "en";
            }
        } );
               
        final TextField textField = new TextField(player.getName(), skin);
        textField.setSize(300f, 50f);
        textField.setPosition(-230f, -25f);
        textField.setOnscreenKeyboard( textField.getOnscreenKeyboard() );
        
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
		};*/
        
		addToScreen(settingsButton);
		addToScreen(soundButtonOn);
		addToScreen(soundButtonOff);
        addToScreen(googlePlusButton);
        addToScreen(facebookButton);
        addToScreen(campaignButton);
        addToScreen(joinRandomRoomButton);
        addToScreen(multiplayerButton);
        addToScreen(chainsDecoration);
        
        addToScreen(settingsWidget.actor());
	}
	
	public void step()
	{
		handleInput();
		
		if( languageChanged ) 
		{
			languageChanged = false;
			settingsManager.setLanguage(languageToChange);
			ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_MAIN_MENU );		
		}
	}
	
	private void createListeners()
	{		
		settingsButtonListener = settingsWidget.getToggleListener();
		
		soundButtonOnListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				soundButtonOff.setVisible(true);
            	soundButtonOn.setVisible(false);
            }
		};
		
		soundButtonOffListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				soundButtonOn.setVisible(true);
				soundButtonOff.setVisible(false);
            }
		};
		
		googlePlusButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
            	languageChanged = true;
            }
		};
		
		facebookButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
            	languageChanged = true;
            }
		};
		
		campaignButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut(ScreenType.SCREEN_CAMPAIGN);
            }
		};
		
		multiplayerButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut(ScreenType.SCREEN_MULTIPLAYER);
            }
		};
		
		joinRandomRoomButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut(ScreenType.SCREEN_WAITING_ROOM);
            }
		};
		
		languageChangedListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
            	languageChanged = true;
            }
		};
	}
	
	private void setListeners()
	{
		settingsButton.addListener( settingsButtonListener );
		soundButtonOn.addListener( soundButtonOnListener );
		soundButtonOff.addListener( soundButtonOffListener );
		googlePlusButton.addListener( googlePlusButtonListener );
		facebookButton.addListener( facebookButtonListener );
		campaignButton.addListener(campaignButtonListener );
		multiplayerButton.addListener( multiplayerButtonListener );
		joinRandomRoomButton.addListener( joinRandomRoomButtonListener );
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			Gdx.app.exit();
		}
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
		
		settingsWidget.dispose();
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_MAIN_MENU;
	}


}

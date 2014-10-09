package com.apptogo.runner.screens;

import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

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
	
	private DialogWidget languageChangeDialog;
	
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
	
	private Texture logoTexture;
	private Image logoImage;
	private Array<Texture> flagTextures;	
		
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
					
		flagTextures = new Array<Texture>();
		
		settingsButton = new Button(skin, "settings");
		settingsButton.setPosition(-550f, 140f);
		
		soundButtonOn = new Button(skin, "soundOn");
		soundButtonOn.setPosition(-550f,-5f);
		
		soundButtonOff = new Button(skin, "soundOff");
		soundButtonOff.setPosition(-550f, -5f);
		soundButtonOff.setVisible(false);
		
		googlePlusButton = new Button(skin, "googlePlus");
		googlePlusButton.setPosition(-550f, -150f);
		
		facebookButton = new Button(skin, "facebook");
		facebookButton.setPosition(-550f, -295f);
			
        campaignButton = new TextButton( getLangString("campaignButton"), skin, "default");
        setTextButtonFont(campaignButton, FontType.WOODFONT);
        campaignButton.setPosition( -(campaignButton.getWidth() / 2.0f), -50.0f );
		
		multiplayerButton = new TextButton( getLangString("multiplayerButton"), skin, "default");
		setTextButtonFont(multiplayerButton, FontType.WOODFONT);
		multiplayerButton.setPosition( -(multiplayerButton.getWidth() / 2.0f), -250.0f );
		
		joinRandomRoomButton = new Button(skin, "joinRandomRoom");
		joinRandomRoomButton.setPosition( 85.0f, -375.0f );
		
		createSettingsWidget();
		
		createListeners();
		setListeners();
        
		chainsDecorationTexture = new Texture( Gdx.files.internal("gfx/menu/chainsDecoration.png") );
		chainsDecoration = new Image( chainsDecorationTexture );
		chainsDecoration.setPosition(100.0f, -290.0f);
		
		logoTexture = new Texture( Gdx.files.internal("gfx/menu/logoMenu.png") );
		logoImage = new Image( logoTexture );
		logoImage.setPosition(-387.0f, 150);
		
		languageChangeDialog = new DialogWidget("", null, null);
         
        /*
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
        addToScreen(logoImage);
        
        addToScreen(settingsWidget.actor());
        addToScreen(languageChangeDialog.actor());
	}
	
	public void step()
	{
		handleInput();
	}
	
	private void createSettingsWidget()
	{
		settingsWidget = new Widget(Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		settingsWidget.setEasing( Interpolation.elasticOut );
		
		Label label = null;
		CheckBox checkBox = null;
		Slider progressBar = null;
		
		//---Obrazki tabow
		Texture generalTabTexture = new Texture( Gdx.files.internal("gfx/menu/generalTab.png") );
        Texture resetTabTexture = new Texture( Gdx.files.internal("gfx/menu/resetTab.png") );
        Texture newsFeedTabTexture = new Texture( Gdx.files.internal("gfx/menu/newsFeedTab.png") );
        
        Image generalTabImageActive = new Image( generalTabTexture );
        generalTabImageActive.setPosition(-550f, 1200f);
        generalTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(1) );
        
        Image resetTabImageActive = new Image( resetTabTexture );
        resetTabImageActive.setPosition(-250f, 1200f);
        resetTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(2) );
        
        Image newsFeedTabImageActive  = new Image( newsFeedTabTexture );
        newsFeedTabImageActive.setPosition(50f, 1200f);
        newsFeedTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(3) );
        
        Image generalTabImageNonActive = new Image( generalTabTexture );
        generalTabImageNonActive.setPosition(-550f, 1200f);
        generalTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(1) );
        Color generalTabImageNonActiveColor = generalTabImageNonActive.getColor();
        generalTabImageNonActiveColor.a /= 3.0f;
        generalTabImageNonActive.setColor( generalTabImageNonActiveColor );
        
        Image resetTabImageNonActive = new Image( resetTabTexture );
        resetTabImageNonActive.setPosition(-250f, 1200f);
        resetTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(2) );
        Color resetTabImageNonActiveColor = resetTabImageNonActive.getColor();
        resetTabImageNonActiveColor.a /= 3.0f;
        resetTabImageNonActive.setColor( resetTabImageNonActiveColor );
        
        Image newsFeedTabImageNonActive  = new Image( newsFeedTabTexture );
        newsFeedTabImageNonActive.setPosition(50f, 1200f);
        newsFeedTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(3) );
        Color newsFeedTabImageNonActiveColor = newsFeedTabImageNonActive.getColor();
        newsFeedTabImageNonActiveColor.a /= 3.0f;
        newsFeedTabImageNonActive.setColor( newsFeedTabImageNonActiveColor );
        
        settingsWidget.addActorToTab( generalTabImageActive , 1 );
        settingsWidget.addActorToTab( resetTabImageNonActive , 1 );
        settingsWidget.addActorToTab( newsFeedTabImageNonActive , 1 );
        
        settingsWidget.addActorToTab( generalTabImageNonActive , 2 );
        settingsWidget.addActorToTab( resetTabImageActive , 2 );
        settingsWidget.addActorToTab( newsFeedTabImageNonActive , 2 );
        
        settingsWidget.addActorToTab( generalTabImageNonActive , 3 );
        settingsWidget.addActorToTab( resetTabImageNonActive , 3 );
        settingsWidget.addActorToTab( newsFeedTabImageActive , 3 );
        //---
		
        //---przyciski share
        Texture shareButtonFacebookTexture = new Texture( Gdx.files.internal("gfx/menu/shareButtonFacebook.png") );
        shareButtonFacebookTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        Texture shareButtonGoogleTexture = new Texture( Gdx.files.internal("gfx/menu/shareButtonGoogle.png") );
        shareButtonGoogleTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        Texture shareButtonTwitterTexture = new Texture( Gdx.files.internal("gfx/menu/shareButtonTwitter.png") );
        shareButtonTwitterTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        
        Image shareButtonFacebookImage = new Image( shareButtonFacebookTexture );
        shareButtonFacebookImage.setPosition(100, 700);
        
        Image shareButtonGoogleImage = new Image( shareButtonGoogleTexture );
        shareButtonGoogleImage.setPosition(200, 700);
        
        Image shareButtonTwitterImage  = new Image( shareButtonTwitterTexture );
        shareButtonTwitterImage.setPosition(300, 700);
        
        settingsWidget.addActorToTab(shareButtonFacebookImage, 1);
        settingsWidget.addActorToTab(shareButtonGoogleImage, 1);
        settingsWidget.addActorToTab(shareButtonTwitterImage, 1);
        //---
		//---Pierwszy tab
		
		label = new Label( getLangString("music"), skin);
		label.setPosition(-450f, 1100f);
		setLabelFont(label, FontType.WOODFONT);
		
		settingsWidget.addActorToTab(label, 1);
		
		checkBox = new CheckBox(" On", skin, "default");
		checkBox.setPosition(-400f, 1030f);
		
		settingsWidget.addActorToTab(checkBox, 1);
		
		progressBar = new Slider(0, 100, 20, false, skin);
		progressBar.setWidth(150f);
		progressBar.setPosition(-250f, 1010f);
		
		settingsWidget.addActorToTab(progressBar, 1);
		
		label = new Label( getLangString("sounds"), skin);
		label.setPosition(-450f, 910f);
		setLabelFont(label, FontType.WOODFONT);
		
		settingsWidget.addActorToTab(label, 1);
		
		checkBox = new CheckBox(" On", skin, "default");
		checkBox.setPosition(-400f, 840f);
		
		settingsWidget.addActorToTab(checkBox, 1);
		
		progressBar = new Slider(0, 100, 20, false, skin);
		progressBar.setWidth(150f);
		progressBar.setHeight(50f);
		progressBar.setPosition(-250f, 820f);
		
		settingsWidget.addActorToTab(progressBar, 1);
		
		label = new Label( getLangString("vibrations"), skin);
		label.setPosition(-450f, 720f);
		setLabelFont(label, FontType.WOODFONT);
		
		settingsWidget.addActorToTab(label, 1);
		
		checkBox = new CheckBox(" On", skin, "default");
		checkBox.setPosition(-400f, 650f);
		
		settingsWidget.addActorToTab(checkBox, 1);
		
		label = new Label( getLangString("language"), skin);
		label.setPosition(50f, 1100f);
		setLabelFont(label, FontType.WOODFONT);
		
		settingsWidget.addActorToTab(label, 1);
		
		Image enflag = getLanguageFlag("en", 100, 1000, true);
        Image plflag = getLanguageFlag("pl", 200, 1000, true);
        Image ruflag = getLanguageFlag("ru", 300, 1000, false);
        Image deflag = getLanguageFlag("de", 100, 900, false);
        Image esflag = getLanguageFlag("es", 200, 900, false);
        Image inflag = getLanguageFlag("in", 300, 900, false);
        
        
        settingsWidget.addActorToTab(enflag, 1);
        settingsWidget.addActorToTab(plflag, 1);
        settingsWidget.addActorToTab(ruflag, 1);
        
        settingsWidget.addActorToTab(deflag, 1);
        settingsWidget.addActorToTab(esflag, 1);
        settingsWidget.addActorToTab(inflag, 1);
        
        label = new Label( getLangString("share"), skin);
		label.setPosition(50f, 800f);
		setLabelFont(label, FontType.WOODFONT);
		
		settingsWidget.addActorToTab(label, 1);
		//---
		
		//---Drugi tab
		//---
        
        settingsWidget.setCurrentTab(1);
	}
	
	private void createListeners()
	{		
		settingsButtonListener = settingsWidget.toggleWidgetListener;
		
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
            	//languageChanged = true;
            }
		};
		
		facebookButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
            	//languageChanged = true;
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
	
	private ClickListener getChangeLanguageListener(final String language)
	{
		final ClickListener confirmChangeListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y)
			{
				settings.setLanguage( language );
				settings.save(); 
				
				ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_MAIN_MENU );	
			}
		};
		
		ClickListener languageChangedListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
            	languageChangeDialog.setYesListener( confirmChangeListener );
            	languageChangeDialog.setLabel( getLangString("languageChange") + " " + languageManager.getFullnameLanguage(language) + " ?" );
            	languageChangeDialog.toggleWidget();
            }
		};
		
		return languageChangedListener;
	}
	
	private Image getLanguageFlag(String language, float x, float y, boolean isActive)
	{
		Texture flagTexture = new Texture( languageManager.getIcoFile( language ) );
		flagTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		Image flag = new Image( flagTexture );
		
		flag.setPosition(x, y);
		
		if( !settings.getLanguage().equals(language) && isActive )
		{
			flag.addListener( getChangeLanguageListener( language ) );
		}
		
		flagTextures.add(flagTexture);
		
		flag.setSize(64, 64);
		
		return flag;
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
		
		chainsDecorationTexture.dispose();
		logoTexture.dispose();
		
		settingsWidget.dispose();
		
		for(Texture texture: flagTextures)
		{
			texture.dispose();
		}
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_MAIN_MENU;
	}


}

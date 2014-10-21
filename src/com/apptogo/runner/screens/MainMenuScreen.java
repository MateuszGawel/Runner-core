package com.apptogo.runner.screens;

import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.settings.Settings;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class MainMenuScreen extends BaseScreen
{		
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
	
	private Image chainsDecoration;	
	private Image logoImage;
		
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
		settingsButton.setPosition(-550f, 140f);
		
		soundButtonOn = new Button(skin, "soundOn");
		soundButtonOn.setPosition(-550f,-5f);
		
		soundButtonOff = new Button(skin, "soundOff");
		soundButtonOff.setPosition(-550f, -5f);
				
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
        
		chainsDecoration = createImage("gfx/menu/chainsDecoration.png", 100.0f, -290.0f);

		logoImage = createImage("gfx/menu/logoMenu.png", -387.0f, 150.0f);
		
		languageChangeDialog = new DialogWidget("", null, null);
                 
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

		//---Obrazki tabow        
        Image generalTabImageNonActive = createImage("gfx/menu/generalTab.png", -550f, 1200f);
        generalTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(1) );
        generalTabImageNonActive.getColor().a /= 3.0f;
        
        Image generalTabImageActive = createImage("gfx/menu/generalTab.png", -550f, 1200f);
        generalTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(1) );
        
        Image resetTabImageNonActive = createImage("gfx/menu/resetTab.png", -250f, 1200f);
        resetTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(2) );
        resetTabImageNonActive.getColor().a /= 3.0f;
        
        Image resetTabImageActive = createImage("gfx/menu/resetTab.png", -250f, 1200f);
        resetTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(2) );
        
        Image newsFeedTabImageNonActive = createImage("gfx/menu/newsFeedTab.png", 50f, 1200f);
        newsFeedTabImageNonActive.addListener( settingsWidget.getChangeWidgetTabListener(3) );
        newsFeedTabImageNonActive.getColor().a /= 3.0f;
        
        Image newsFeedTabImageActive  = createImage("gfx/menu/newsFeedTab.png", 50f, 1200f);
        newsFeedTabImageActive.addListener( settingsWidget.getChangeWidgetTabListener(3) );
        
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
		
        //---
		//---Pierwszy tab
		
		Label musicLabel = createLabel( getLangString("music"), FontType.WOODFONT, -450f, 1090f);
		
		final CheckBox musicCheckBox = new CheckBox(" On", skin, "default");
		musicCheckBox.setPosition(-400f, 1020f);
		musicCheckBox.setChecked( settings.musicState );
				
		final Slider musicVolume = new Slider(0, 100, 20, false, skin);
		musicVolume.setPosition(-400f, 910f);
		musicVolume.setValue( settings.getMusicLevel() );
		musicVolume.setDisabled( !musicCheckBox.isChecked() );
		musicVolume.getColor().a = musicCheckBox.isChecked()?1.0f:0.5f;
		
		musicCheckBox.addListener( 
				new ChangeListener(){
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setMusicState( musicCheckBox.isChecked() );
						saveManager.save( settings );
						musicVolume.setDisabled( !musicCheckBox.isChecked() );
						musicVolume.getColor().a = musicCheckBox.isChecked()?1.0f:0.5f;
					}
				});
		musicVolume.addListener( 
				new ChangeListener(){
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setMusicLevel( musicVolume.getValue() );
						saveManager.save( settings );
					}
				});
		
		Label soundsLabel = createLabel( getLangString("sounds"), FontType.WOODFONT, -450f, 840f);
				
		final CheckBox soundsCheckBox = new CheckBox(" On", skin, "default");
		soundsCheckBox.setPosition(-400f, 770f);
		soundsCheckBox.setChecked( settings.soundState );
		
		final Slider soundsVolume = new Slider(0, 100, 20, false, skin);
		soundsVolume.setPosition(-400f, 660f);
		soundsVolume.setValue( settings.getSoundLevel() );
		soundsVolume.setDisabled( !soundsCheckBox.isChecked() );
		soundsVolume.getColor().a = soundsCheckBox.isChecked()?1.0f:0.5f;
		
		soundsCheckBox.addListener( 
			new ChangeListener(){
				@Override
				public void changed (ChangeEvent event, Actor actor) 
				{
					settings.setSoundState( soundsCheckBox.isChecked() );
					saveManager.save( settings );
					soundsVolume.setDisabled( !soundsCheckBox.isChecked() );
					soundsVolume.getColor().a = soundsCheckBox.isChecked()?1.0f:0.5f;
				}
			});
		
		soundsVolume.addListener( 
				new ChangeListener(){
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setSoundLevel( soundsVolume.getValue() );
						saveManager.save( settings );
					}
				});
		
		Label vibrationsLabel = createLabel( getLangString("vibrations"), FontType.WOODFONT, -50f, 1090f);
		
		final CheckBox vibrationsCheckBox = new CheckBox(" On", skin, "default");
		vibrationsCheckBox.setPosition(0f, 1020f);
		vibrationsCheckBox.setChecked( settings.vibrationState );
		
		vibrationsCheckBox.addListener( 
				new ChangeListener(){
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setVibrationState( vibrationsCheckBox.isChecked() );
						saveManager.save( settings );
					}
				});
				
		Label languageLabel = createLabel( getLangString("language"), FontType.WOODFONT, -50f, 900f);
				
		Image enflag = getLanguageFlag("en", 0, 800, true);
        Image plflag = getLanguageFlag("pl", 100, 800, true);
        Image ruflag = getLanguageFlag("ru", 200, 800, false);
        Image deflag = getLanguageFlag("de", 0, 700, false);
        Image esflag = getLanguageFlag("es", 100, 700, false);
        Image inflag = getLanguageFlag("in", 200, 700, false);
        
        
        settingsWidget.addActorToTab(musicLabel, 1);
		settingsWidget.addActorToTab(musicCheckBox, 1);
		settingsWidget.addActorToTab(musicVolume, 1);
		settingsWidget.addActorToTab(soundsLabel, 1);
		settingsWidget.addActorToTab(soundsCheckBox, 1);
		settingsWidget.addActorToTab(soundsVolume, 1);
		settingsWidget.addActorToTab(vibrationsLabel, 1);
		settingsWidget.addActorToTab(vibrationsCheckBox, 1);
		settingsWidget.addActorToTab(languageLabel, 1);
        settingsWidget.addActorToTab(enflag, 1);
        settingsWidget.addActorToTab(plflag, 1);
        settingsWidget.addActorToTab(ruflag, 1);
        settingsWidget.addActorToTab(deflag, 1);
        settingsWidget.addActorToTab(esflag, 1);
        settingsWidget.addActorToTab(inflag, 1);
		//---
		
        //---Drugi tab
        //---
      
        //---Trzeci tab
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
		Image flag = createImage(languageManager.getIcoFile( language ), x, y);
		
		if( !settings.getLanguage().equals(language) && isActive )
		{
			flag.addListener( getChangeLanguageListener( language ) );
		}
				
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
		
		settingsWidget.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_MAIN_MENU;
	}


}

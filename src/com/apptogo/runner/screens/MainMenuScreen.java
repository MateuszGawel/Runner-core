package com.apptogo.runner.screens;

import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.enums.WidgetType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.news.News;
import com.apptogo.runner.news.NewsManager;
import com.apptogo.runner.widget.DialogWidget;
import com.apptogo.runner.widget.Widget;
import com.apptogo.runner.widget.Widget.WidgetFadingType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class MainMenuScreen extends BaseScreen
{			
	private Button settingsButton;
	private Button soundButtonOn;
	private Button soundButtonOff;
	private Button googlePlusButton;
	private Button facebookButton;
	private Button joinRandomRoomButton;
	
	private CheckBox musicCheckBox;
	private CheckBox soundsCheckBox;
	private CheckBox vibrationsCheckBox;
	
	private Slider musicVolume;
	private Slider soundsVolume;
	
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
	
	private Image newContent;
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
		refreshSettingsControls();
		
		createListeners();
		setListeners();
        
		newContent = createImage("gfx/menu/newIcon.png", -450, 230);
		newContent.setVisible( NewsManager.getInstance().isNewContent() );
		
		chainsDecoration = createImage("gfx/menu/chainsDecoration.png", 100.0f, -290.0f);

		logoImage = createImage("gfx/menu/logoMenu.png", -387.0f, 150.0f);
		
		languageChangeDialog = new DialogWidget("", null, null);
                 
		addToScreen(settingsButton);
		addToScreen(newContent);
		
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
		settingsWidget = new Widget("settings", Align.center, 600.0f, 950.0f, WidgetType.BIG, WidgetFadingType.TOP_TO_BOTTOM, true);
		settingsWidget.setEasing( Interpolation.elasticOut );

		//---Obrazki tabow        
		settingsWidget.addTabButton(1, "generalTab");
		settingsWidget.addTabButton(2, "newsFeedTab");
		
		Image tabNewContent = createImage("gfx/menu/newIcon.png", -100.0f, 1245.0f);
		tabNewContent.setVisible( NewsManager.getInstance().isNewContent() );
		settingsWidget.addActor(tabNewContent);
		//---
			
        //---
		//---Pierwszy tab
		
		Label musicLabel = createLabel( getLangString("music"), FontType.WOODFONT);
		
		musicCheckBox = new CheckBox(" On", skin, "default");
		musicCheckBox.align(Align.left);
				
		musicVolume = new Slider(0, 100, 20, false, skin);
		musicVolume.setWidth(300.0f);
		
		Label soundsLabel = createLabel( getLangString("sounds"), FontType.WOODFONT);
				
		soundsCheckBox = new CheckBox(" On", skin, "default");
		soundsCheckBox.align(Align.left);
		
		soundsVolume = new Slider(0, 100, 20, false, skin);
		soundsVolume.setWidth(300.0f);
				
		Label vibrationsLabel = createLabel( getLangString("vibrations"), FontType.WOODFONT);
		
		vibrationsCheckBox = new CheckBox(" On", skin, "default");
		vibrationsCheckBox.align(Align.left);
				
		Label languageLabel = createLabel( getLangString("language"), FontType.WOODFONT, 50f, 900f);
				
		Image enflag = getLanguageFlag("en", 30, 820, true);
        Image plflag = getLanguageFlag("pl", 130, 820, true);
        Image ruflag = getLanguageFlag("ru", 230, 820, false);
        Image deflag = getLanguageFlag("de", 30, 720, false);
        Image esflag = getLanguageFlag("es", 130, 720, false);
        Image inflag = getLanguageFlag("in", 230, 720, false);
        
        musicCheckBox.addListener
        ( 
        		new ClickListener()
        		{
        			public void clicked(InputEvent event, float x, float y) 
                    {
        				settings.musicState = musicCheckBox.isChecked();      				
        				settings.save();
        				
        				refreshSettingsControls();
                    }
		});
        		
        musicVolume.addListener
        ( 
				new ChangeListener()
				{
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setMusicLevel( musicVolume.getValue() );
						settings.save();
					}
		});
        
		soundsCheckBox.addListener
		( 
				new ClickListener()
				{
					public void clicked(InputEvent event, float x, float y) 
		            {
						//runner.setScreen(new ParticleScreen());
						settings.soundsState = soundsCheckBox.isChecked();        				
						settings.save();
        				
        				refreshSettingsControls();
		            }
		});
				
		soundsVolume.addListener
		( 
				new ChangeListener()
				{
					@Override
					public void changed (ChangeEvent event, Actor actor) 
					{
						settings.setSoundLevel( soundsVolume.getValue() );
						settings.save();
					}
		});
		
		vibrationsCheckBox.addListener
		( 
				new ClickListener()
				{
					public void clicked(InputEvent event, float x, float y) 
		            {
						settings.vibrationState = vibrationsCheckBox.isChecked();        				
						settings.save();
        				
        				refreshSettingsControls();
		            }
		});
				
		Table settingsTable = new Table();
				
		settingsTable.add( musicLabel ).colspan(2).width(440.0f).height(45.0f).left().pad(0, 0, 0, 50);
		settingsTable.add( vibrationsLabel ).colspan(2).width(440.0f).height(45.0f).left().pad(0, 0, 0, 0);
		
		settingsTable.row();
		settingsTable.add().width(40.0f).height(45.0f).left().pad(30, 0, 0, 0);
		settingsTable.add( musicCheckBox ).width(400.0f).height(45.0f).left().pad(30, 0, 0, 50);
		settingsTable.add().width(40.0f).height(45.0f).left().pad(30, 0, 0, 0);
		settingsTable.add( vibrationsCheckBox ).width(400.0f).height(45.0f).left().pad(30, 0, 0, 0);
		
		settingsTable.row();
		settingsTable.add().width(40.0f).height(65.0f).left().pad(15, 0, 0, 0);
		settingsTable.add( musicVolume ).width(350.0f).height(65.0f).left().pad(15, 0, 0, 100);
		settingsTable.add( languageLabel ).width(440.0f).height(65.0f).left().colspan(2).pad(15, 0, 0, 0);
			
		settingsTable.row();
		settingsTable.add( soundsLabel ).colspan(2).width(440.0f).height(45.0f).left().pad(30, 0, 0, 50);
		settingsTable.add().colspan(2).width(440.0f).height(45.0f).left().pad(30, 0, 0, 0);
		
		settingsTable.row();
		settingsTable.add().width(40.0f).height(45.0f).left().pad(30, 0, 0, 0);
		settingsTable.add( soundsCheckBox ).width(400.0f).height(45.0f).left().pad(30, 0, 0, 50);
		settingsTable.add().width(40.0f).height(45.0f).left().pad(30, 0, 0, 0);
		settingsTable.add().width(400.0f).height(45.0f).left().pad(30, 0, 0, 0);
		
		settingsTable.row();
		settingsTable.add().width(40.0f).height(65.0f).left().pad(15, 0, 0, 0);
		settingsTable.add( soundsVolume ).width(350.0f).height(65.0f).left().pad(15, 0, 0, 100);
		settingsTable.add().width(40.0f).height(65.0f).left().pad(15, 0, 0, 0);
		settingsTable.add().width(400.0f).height(65.0f).left().pad(15, 0, 0, 0);
		
		settingsTable.setSize(930.0f, 430.0f);
		settingsTable.setPosition(-500.0f, 680.0f);
		//settingsTable.debug();
		
		settingsWidget.addActorToTab(settingsTable, 1);
		
        settingsWidget.addActorToTab(enflag, 1);
        settingsWidget.addActorToTab(plflag, 1);
        settingsWidget.addActorToTab(ruflag, 1);
        settingsWidget.addActorToTab(deflag, 1);
        settingsWidget.addActorToTab(esflag, 1);
        settingsWidget.addActorToTab(inflag, 1);
		//---
		
        //---Drugi tab
        
        Image newsfeed = createImage("gfx/menu/newsfeed.png", -530.0f, 750.0f);
        
        Array<News> newsArray = NewsManager.getInstance().getNewsArray();
      
        Table newsfeedTable = new Table();
       
        for(News news: newsArray)
        {
        	Label date = createLabel(news.date, FontType.WOODFONTSMALL);
        	
        	newsfeedTable.add(date).width(700.0f).left().pad(50, 0, 0, 0);
        	newsfeedTable.row();
        	
        	Label topic = createLabel(news.topic, FontType.WOODFONT);
        	topic.setWrap(true);
        	
        	newsfeedTable.add(topic).width(700.0f).left().pad(10, 0, 0, 0);
        	newsfeedTable.row();
        	
        	Label message = createLabel(news.message, FontType.WOODFONTSMALL);
        	message.setWrap(true);
        	
        	newsfeedTable.add(message).width(700.0f).left().pad(20, 0, 0, 0);
        	newsfeedTable.row();
        }    
                
        Container<ScrollPane> newsFeedContainer = createScroll(newsfeedTable, 700.0f, 400.0f, true);
        newsFeedContainer.setPosition(-200.0f, 720.0f);
        
        settingsWidget.addActorToTab(newsfeed, 2);
        settingsWidget.addActorToTab(newsFeedContainer, 2);
        
        //---
      
        //---Trzeci tab
        //---
        
        settingsWidget.setCurrentTab(1);
	}
	
	private void refreshSettingsControls()
	{   			
        if( !settings.musicState && !settings.soundsState )
        {
        	soundButtonOff.setVisible(true);
        	soundButtonOn.setVisible(false);
        }
        else
        {
        	soundButtonOff.setVisible(false);
        	soundButtonOn.setVisible(true);
        }
        
    	musicCheckBox.setChecked( settings.musicState );
    	soundsCheckBox.setChecked( settings.soundsState );
        vibrationsCheckBox.setChecked( settings.vibrationState );
        
        musicVolume.setValue( settings.musicLevel );
        soundsVolume.setValue( settings.soundsLevel );
        
        if( musicCheckBox.isChecked() )
        {
        	musicCheckBox.setText(" On");
        	musicVolume.setDisabled(false);
        }
        else
        {
        	musicCheckBox.setText(" Off");
        	musicVolume.setDisabled(true);
        }
        
        if( soundsCheckBox.isChecked() )
        {
        	soundsCheckBox.setText(" On");
        	soundsVolume.setDisabled(false);
        }
        else
        {
        	soundsCheckBox.setText(" Off");
        	soundsVolume.setDisabled(true);
        }
        
        if( vibrationsCheckBox.isChecked() )
        {
        	vibrationsCheckBox.setText(" On");
        }
        else
        {
        	vibrationsCheckBox.setText(" Off");
        }
	}
	
	private void createListeners()
	{		
		settingsButtonListener = settingsWidget.toggleWidgetListener;
		
		soundButtonOnListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				settings.musicState = false;
				settings.soundsState = false;
				settings.save();
				
				refreshSettingsControls();
            }
		};
		
		soundButtonOffListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {				
				settings.musicState = true;
				settings.soundsState = true;
				settings.save();
				
				refreshSettingsControls();
            }
		};
		
		googlePlusButtonListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
            	//languageChanged = true;
            }
		};
		
		facebookButtonListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
            	//languageChanged = true;
            }
		};
		
		campaignButtonListener = new ClickListener()
		{
			public void clicked(InputEvent event, float x, float y) 
            {
				loadScreenAfterFadeOut(ScreenType.SCREEN_CAMPAIGN);
            }
		};
		
		multiplayerButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				if( player.getName().equals("") )
				{
					loadScreenAfterFadeOut(ScreenType.SCREEN_REGISTER);
				}
				else
				{
					loadScreenAfterFadeOut(ScreenType.SCREEN_MULTIPLAYER);
				}
            }
		};
		
		joinRandomRoomButtonListener = new ClickListener(){
			public void clicked(InputEvent event, float x, float y) 
            {
				if( player.getName().equals("") )
				{
					loadScreenAfterFadeOut(ScreenType.SCREEN_REGISTER);
				}
				else
				{
					loadScreenAfterFadeOut(ScreenType.SCREEN_WAITING_ROOM);
				}
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
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() 
	{
		// TODO Auto-generated method stub	
	}

	@Override
	public void resume() 
	{
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

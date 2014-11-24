package com.apptogo.runner.screens;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.LanguageManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.settings.Settings;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public abstract class BaseScreen implements Screen
{	
	protected Runner runner;
	protected LanguageManager languageManager;
	protected Image background;
	protected Texture backgroundTexture;
	
	protected Array<Texture> textures;
	
	protected Settings settings;
	
	protected Stage menuStage;
	protected Viewport viewport;
	protected Skin skin;
	
	public Stage gameGuiStage;
	public StretchViewport guiStretchViewport;
	public OrthographicCamera guiCamera;
	protected InputMultiplexer inputMultiplexer;
	
	protected float delta;
	
	protected Player player; //to jest wazne - musi byc dostep do playera juz na poziomie menu - chcemy miec o nim info w menu
	
	protected Button fadeButton;
	public boolean fadeOutScreen;
	public boolean fadeInScreen;
	public boolean isFadedOut;
	protected float currentFadeOutLevel;
	protected float currentFadeInLevel;
	
	protected ScreenType screenToLoadAfterFadeOut;
	
	SaveManager saveManager;
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	public abstract void step();
	public abstract void prepare();
	public String getLangString(String key){ return languageManager.getString(key);	}
	
	protected BaseScreen(Runner runner) 
	{
		this.runner = runner;
		
		settings = Settings.load();
		
		saveManager = SaveManager.getInstance();
		
		this.languageManager = LanguageManager.getInstance();
		
		//Logger.log(this, settings.getLanguage());
		
		this.languageManager.setCurrentLanguage( settings.getLanguage() );
		
		textures = new Array<Texture>();
		
	}
	
	/** Powoduje zaladowanie playera z pamieci - powinno byc wywolywane tam, gdzie potrzeba dostepu do playera! */
	protected void loadPlayer()
	{
		this.player = Player.load();
	}
	
	@Override
	public void show() 
	{
		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI)  )
		{
			menuStage = new Stage();
			viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
			menuStage.setViewport(viewport);
			skin = ResourcesManager.getInstance().getUiSkin();
			initializeFadeOutButton();
			
			this.prepare();
			
			Gdx.input.setInputProcessor(menuStage);
		}
		else
		{
			gameGuiStage = new Stage();
			guiCamera = (OrthographicCamera) gameGuiStage.getCamera();  
			guiCamera.setToOrtho(false, Runner.SCREEN_WIDTH/PPM, Runner.SCREEN_HEIGHT/PPM);
			guiStretchViewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT, guiCamera);
			gameGuiStage.setViewport(guiStretchViewport);
			
			this.prepare();
			
			inputMultiplexer = new InputMultiplexer(); 
			inputMultiplexer.addProcessor(gameGuiStage);
			inputMultiplexer.addProcessor(new InputHandler());
			
			Gdx.input.setInputProcessor(inputMultiplexer);
		}
	}
	
	@Override
	public void render(float delta) 
	{
		this.delta = delta;

		if( !(this.getSceneType() == ScreenType.SCREEN_GAME_SINGLE) && !(this.getSceneType() == ScreenType.SCREEN_GAME_MULTI) )
		{
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			this.step();
			
			handleFadingOutScreen();
			handleFadingInScreen();
			
			menuStage.act();
			menuStage.draw();
		}
		else
		{
			this.step();
			
			gameGuiStage.act(delta);
			guiCamera.update();
    		gameGuiStage.draw();
		}
	}	
	
	protected void setBackground(String path)
	{
		backgroundTexture = new Texture( Gdx.files.internal(path) );
		
		textures.add(backgroundTexture);
		
		background = new Image( backgroundTexture );
		background.setPosition(0 - (Runner.SCREEN_WIDTH/2.0f), 0 - (Runner.SCREEN_HEIGHT/2.0f));
		menuStage.addActor( background );
	}

	protected void addToScreen(Actor actor)
	{
		this.menuStage.addActor(actor);
	}
	
	protected void initializeFadeOutButton()
	{
		fadeButton = new Button(skin, "fadeOut");
		fadeButton.setSize(1280.0f, 800.0f);
		fadeButton.setPosition(-640.0f, -400.0f);
		fadeButton.setVisible(false);
		fadeButton.toBack();
		
		currentFadeOutLevel = 0.0f;
		currentFadeInLevel = 1.0f;
		
		isFadedOut = false;
		
		menuStage.addActor(fadeButton);
	}
		
	public void handleFadingOutScreen()
	{
		if(fadeOutScreen)
		{
			if( currentFadeOutLevel < 1.0f )
			{
				isFadedOut = false;
				currentFadeOutLevel += 0.1f;
				
				AlphaAction fadeOutAction = new AlphaAction();
				fadeOutAction.setAlpha(currentFadeOutLevel);
				
				fadeButton.setVisible(true);
				fadeButton.toFront();
				fadeButton.addAction(fadeOutAction);
			}
			else
			{
				isFadedOut = true;
				currentFadeOutLevel = 0.0f;
				fadeOutScreen = false;
				
				if( screenToLoadAfterFadeOut != null )
				{
					ScreensManager.getInstance().createLoadingScreen( screenToLoadAfterFadeOut );
				}
			}
		}
	}
	
	protected void handleFadingInScreen()
	{
		if(fadeInScreen)
		{
			if( currentFadeInLevel > 0.0f )
			{
				currentFadeInLevel -= 0.1f;
				
				AlphaAction fadeInAction = new AlphaAction();
				fadeInAction.setAlpha(currentFadeInLevel);
				
				fadeButton.setVisible(true);
				fadeButton.toFront();
				fadeButton.addAction(fadeInAction);
			}
			else
			{
				fadeButton.setVisible(false);
				currentFadeInLevel = 1.0f;
				fadeInScreen = false;
			}
		}
	}
	
	protected void loadScreenAfterFadeOut( ScreenType screenType )
	{
		screenToLoadAfterFadeOut = screenType;
		fadeOutScreen = true;
	}
	
	protected void fadeInOnStart()
	{
		fadeInScreen = true;
	}
	
	protected Image createImage(String imagePath, float x, float y)
	{
		return createImage(imagePath, x, y, TextureFilter.Linear, TextureFilter.Linear);
	}
	protected Image createImage(String imagePath, float x, float y, TextureFilter minFilter, TextureFilter magFilter)
	{
		//to musi byc z resources managera. Tak jest bez sensu
		Texture texture = new Texture( Gdx.files.internal(imagePath) );
		texture.setFilter(minFilter, magFilter);
		
		textures.add(texture);
		
		Image image = new Image(texture);
		image.setPosition(x, y);
		
		return image;
	}
	
	protected Label createLabel(String text, FontType fontType)
	{
		return createLabel(text, fontType, 0, 0);
	}
	
	protected Label createLabel(String text, FontType fontType, float x, float y)
	{
		LabelStyle labelStyle = new LabelStyle();	
		labelStyle.font = FontType.convertToFont(fontType);
		
		Label label = new Label(text, labelStyle);
		label.setColor( FontType.convertToColor(fontType) );
		label.setPosition(x, y);
		
		return label;
	}
	
	protected Container<ScrollPane> createScroll(Table table, float width, float height, boolean vertical)
	{
		final ScrollPane scroller = new ScrollPane(table, skin);
		
		if(vertical)
		{
			scroller.setScrollingDisabled(true, false);
		}
		else
		{
			scroller.setScrollingDisabled(false, true);
		}
		
        scroller.setFadeScrollBars(false);
        
        Container<ScrollPane> container = new Container<ScrollPane>();
        container.setSize(width, height);
        container.setActor(scroller);
        
        return container;
	}
		
	protected void setTextButtonFont(TextButton textButton, FontType fontType)
	{
		TextButtonStyle textButtonStyle = new TextButtonStyle(textButton.getStyle());
        
		textButtonStyle.font = FontType.convertToFont(fontType);
		textButtonStyle.fontColor = FontType.convertToColor(fontType) ;
        
		textButton.setStyle(textButtonStyle);
	}
	
	protected void setCenterPosition(Actor actor, float y)
	{
		actor.setPosition(-(actor.getWidth() / 2.0f), y);
	}
	
	@Override
	public void dispose() 
	{
		for(Texture texture: textures)
		{
			if( texture != null )
			{
				texture.dispose();
			}
		}
		
		if( menuStage != null)
		{
			Logger.log(this, "dispose menuStage");
			menuStage.clear();
			menuStage.dispose();
		}
		
		if(gameGuiStage != null){
			Logger.log(this, "dispose gameGuiStage");
			gameGuiStage.clear();
			gameGuiStage.dispose();
		}
	}
}

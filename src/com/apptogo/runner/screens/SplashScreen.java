package com.apptogo.runner.screens;

import com.apptogo.runner.animation.ObjectAnimation;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.FontManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SplashScreen extends BaseScreen
{			
	private enum SplashPhase
	{
		SPLASH_IMAGE_IN,
		SPLASH_IMAGE_WAITING,
		LOGO_IN,
		LOGO_ANIMATING,
		LOGO_WAITING,
		BACKGROUND_WAITING,
		FINISHED
	}
	
	private SplashPhase currentPhase;
	
	private AssetManager menuAssetManager;
	private AssetManager stillAssetManager;
	
	private Group logo;
	
	private ObjectAnimation letterDAnimation;
	private ObjectAnimation dustAnimation;
	private ObjectAnimation loadingAnimation;
		
	private Texture splashImageTexture;
	private Image splashImage;
	private Texture logoTexture;
	private Image logoImage;
	
	private MoveToAction splashImageInAction;
	private MoveToAction splashImageOutAction;
	private MoveToAction logoInAction;
	private AlphaAction backgroundInAction;
	private AlphaAction logoWaitingAction;
	
	private Label loadingLabel;
	
	public SplashScreen(Runner runner)
	{
		super(runner);
	}
	
	@Override
	public void prepare()
	{	
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();
		
		setBackground("gfx/menu/menuBackgrounds/splashScreenBackground.png");
		background.setColor(background.getColor().r, background.getColor().g, background.getColor().b, 0.0f);
		
		logo = new Group();
		logo.setPosition(0, 0);
		
		initializeActions();
		
		splashImageTexture = (Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_SPLASH, "gfx/splash/splash.png");
		
		splashImage = new Image( splashImageTexture );
		splashImage.setPosition( -106.0f, -800.0f );
		splashImage.addAction( splashImageInAction );
	
		addToScreen(splashImage);
		
		currentPhase = SplashPhase.SPLASH_IMAGE_IN;
	}
	
	private void initializeActions()
	{
		splashImageInAction = new MoveToAction();
		splashImageInAction.setDuration(1.5f);
		splashImageInAction.setPosition(-106.0f, -200.0f);
		splashImageInAction.setInterpolation(Interpolation.pow5Out);
		
		splashImageOutAction = new MoveToAction();
		splashImageOutAction.setDuration(0.4f);
		splashImageOutAction.setPosition(-106.0f, -800.0f);
		splashImageOutAction.setInterpolation(Interpolation.sineOut);
		
		logoInAction = new MoveToAction();
		logoInAction.setDuration(0.8f);
		logoInAction.setPosition(0, -600.0f);
		logoInAction.setInterpolation(Interpolation.exp10In);
				
		backgroundInAction = new AlphaAction();
		backgroundInAction.setDuration(1.0f);
		backgroundInAction.setAlpha(1.0f);
		
		logoWaitingAction = new AlphaAction();
		logoWaitingAction.setDuration(0.1f);
		logoWaitingAction.setAlpha(1.0f);
	}
	
	public void step()
	{		
		if( currentPhase == SplashPhase.FINISHED ) 
		{
			if( menuAssetManager.update() )
			{
				if( stillAssetManager.update() )
				{
						loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
				}
			}
		}
		
		//Animation flow
		if( currentPhase == SplashPhase.SPLASH_IMAGE_IN && splashImage.getActions().size <= 0)
		{
			currentPhase = SplashPhase.SPLASH_IMAGE_WAITING;
			FontManager.getInstance().initializeFonts();
			ResourcesManager.getInstance().loadLogoResources();
		}
		else if( currentPhase == SplashPhase.SPLASH_IMAGE_WAITING && ResourcesManager.getInstance().getLogoAssetManager().update())
		{
			//tworze animacje itd tutaj bo musimy czekac na zaladowanie sie animacji do pamieci
			logoTexture = (Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_SPLASH, "gfx/splash/logoSplash.png");
			logoImage = new Image( logoTexture );
			logoImage.setPosition( -421.0f, 400.0f );
			
			letterDAnimation = new ObjectAnimation("gfx/splash/logoSplashLetterD.pack", "d", 16, -422.0f, 270.0f, false, false);
			
			logo.addActor(logoImage);
			logo.addActor(letterDAnimation);
			
			dustAnimation = new ObjectAnimation("gfx/splash/dust.pack", "dust", 20, -600.0f, -400.0f, false, false);
			dustAnimation.scaleFrames(2.0f);
			dustAnimation.setVisible(false);
			
			loadingLabel = new Label(getLangString("loadingLabel"), skin);
			setLabelFont(loadingLabel, FontType.LOADINGSMALL);
			loadingLabel.setPosition( -60.0f, 260.0f );
			loadingLabel.setVisible(false);
			
			loadingAnimation = new ObjectAnimation("gfx/splash/loading.pack", "loading", 10, -110.0f, 250.0f, false, true);
			loadingAnimation.setVisible(false);
			
			addToScreen(logo);
			addToScreen(dustAnimation);
			addToScreen(loadingLabel);
			addToScreen(loadingAnimation);
			
			splashImage.addAction(splashImageOutAction);
			logo.addAction(logoInAction);
		
			currentPhase = SplashPhase.LOGO_IN;
		}
		else if( currentPhase == SplashPhase.LOGO_IN && logo.getActions().size <= 0)
		{
			dustAnimation.setVisible(true);
			dustAnimation.start();
			
			letterDAnimation.start();
			
			background.addAction(backgroundInAction);
			
			currentPhase = SplashPhase.LOGO_ANIMATING;
		}
		else if( currentPhase == SplashPhase.LOGO_ANIMATING && letterDAnimation.isFinished()  && dustAnimation.isFinished())
		{
			dustAnimation.setVisible(false);
			
			logo.addAction(logoWaitingAction);
			
			currentPhase = SplashPhase.LOGO_WAITING;
		}
		else if( currentPhase == SplashPhase.LOGO_WAITING && logo.getActions().size <= 0 )
		{
			currentPhase = SplashPhase.BACKGROUND_WAITING;
		}
		else if( currentPhase == SplashPhase.BACKGROUND_WAITING && background.getActions().size <= 0)
		{
			loadingLabel.setVisible(true);
			loadingAnimation.setVisible(true);
			loadingAnimation.start();
			
			ResourcesManager.getInstance().loadMenuResources();
			ResourcesManager.getInstance().loadStillResources();
			
			menuAssetManager = ResourcesManager.getInstance().getMenuAssetManager();
			stillAssetManager = ResourcesManager.getInstance().getStillAssetManager();
			
			currentPhase = SplashPhase.FINISHED;
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
		splashImageTexture.dispose();
		logoTexture.dispose();
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

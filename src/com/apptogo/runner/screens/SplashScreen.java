package com.apptogo.runner.screens;

import com.apptogo.runner.animation.ObjectAnimation;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

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
	
	private ObjectAnimation logoAnimation;
	private ObjectAnimation dustAnimation;
	private ObjectAnimation loadingAnimation;
		
	private Texture splashImageTexture;
	private Image splashImage;
	
	private MoveToAction splashImageInAction;
	private MoveToAction splashImageOutAction;
	private MoveToAction logoInAction;
	private AlphaAction backgroundInAction;
	private AlphaAction logoWaitingAction;
	
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
		logoInAction.setPosition(-421.0f, -200.0f);
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
			ResourcesManager.getInstance().loadLogoResources();
		}
		else if( currentPhase == SplashPhase.SPLASH_IMAGE_WAITING && ResourcesManager.getInstance().getLogoAssetManager().update())
		{
			logoAnimation = new ObjectAnimation("gfx/splash/logo.pack", "logo", 17, -421.0f, 400.0f, false, false);
			
			dustAnimation = new ObjectAnimation("gfx/splash/dust.pack", "dust", 20, -600.0f, -400.0f, false, false);
			dustAnimation.scaleFrames(2.0f);
			dustAnimation.setVisible(false);
			
			loadingAnimation = new ObjectAnimation("gfx/splash/loading.pack", "loading", 27, -80.0f, -320.0f, false, true);
			loadingAnimation.setVisible(false);
			
			addToScreen(logoAnimation);
			addToScreen(dustAnimation);
			addToScreen(loadingAnimation);
			
			splashImage.addAction(splashImageOutAction);
			logoAnimation.addAction(logoInAction);
		
			currentPhase = SplashPhase.LOGO_IN;
		}
		else if( currentPhase == SplashPhase.LOGO_IN && logoAnimation.getActions().size <= 0)
		{
			dustAnimation.setVisible(true);
			dustAnimation.start();
			
			logoAnimation.start();
			
			background.addAction(backgroundInAction);
			logoAnimation.addAction(logoWaitingAction);
			
			currentPhase = SplashPhase.LOGO_ANIMATING;
		}
		else if( currentPhase == SplashPhase.LOGO_ANIMATING && logoAnimation.isFinished()  && dustAnimation.isFinished())
		{
			dustAnimation.setVisible(false);
			
			logoAnimation.addAction(logoWaitingAction);
			
			currentPhase = SplashPhase.LOGO_WAITING;
		}
		else if( currentPhase == SplashPhase.LOGO_WAITING && logoAnimation.getActions().size <= 0 )
		{
			currentPhase = SplashPhase.BACKGROUND_WAITING;
		}
		else if( currentPhase == SplashPhase.BACKGROUND_WAITING && background.getActions().size <= 0)
		{
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
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

package com.apptogo.runner.screens;

import com.apptogo.runner.animation.LogoAnimation;
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
		LOGO_WAITING,
		DUST_IN,
		DUST_WAITING,
		FINISHED
	}
	
	private LogoAnimation logoAnimation;
	private LogoAnimation dustAnimation;
	
	private AssetManager menuAssetManager;
	private AssetManager stillAssetManager;
		
	private Texture splashImageTexture;
	public Image splashImage;
	
	private MoveToAction splashImageInAction;
	private AlphaAction splashImageWaitingAction;
	private MoveToAction splashImageOutAction;
	
	private MoveToAction logoInAction;
	private AlphaAction logoWaitingAction;
	
	private AlphaAction dustWaitingAction;
	
	private SplashPhase currentPhase;
	
	private boolean startLoadingResources = false;
	
	public SplashScreen(Runner runner)
	{
		super(runner);
	}
	
	@Override
	public void prepare()
	{	
		setBackground("gfx/menu/menuBackgrounds/splashScreenBackground.png");
		
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();
		
		currentPhase = SplashPhase.SPLASH_IMAGE_IN;
		
		logoAnimation = new LogoAnimation("gfx/splash/logo.pack", "logo", 17, -421.0f, 400.0f, false);
		dustAnimation = new LogoAnimation("gfx/splash/dust.pack", "dust", 20, -600.0f, -320.0f, false);
		dustAnimation.setVisible(false);
		
		splashImageTexture = (Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_SPLASH, "gfx/splash/splash.png");
		
		splashImage = new Image( splashImageTexture );
		splashImage.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - splashImage.getWidth()/2.0f, -800.0f );

		splashImageInAction = new MoveToAction();
		splashImageInAction.setDuration(0.8f);
		splashImageInAction.setPosition(splashImage.getX(), -200.0f);
		splashImageInAction.setInterpolation(Interpolation.elasticOut);
		
		splashImageWaitingAction = new AlphaAction();
		splashImageWaitingAction.setDuration(0.5f);
		splashImageWaitingAction.setAlpha(1.0f);
		
		splashImageOutAction = new MoveToAction();
		splashImageOutAction.setDuration(0.4f);
		splashImageOutAction.setPosition(splashImage.getX(), -800.0f);
		splashImageOutAction.setInterpolation(Interpolation.exp10Out);
		
		logoInAction = new MoveToAction();
		logoInAction.setDuration(0.8f);
		logoInAction.setPosition(logoAnimation.getX(), -122.0f);
		logoInAction.setInterpolation(Interpolation.bounceOut);
				
		logoWaitingAction = new AlphaAction();
		logoWaitingAction.setDuration(0.1f);
		logoWaitingAction.setAlpha(1.0f);
		
		dustWaitingAction = new AlphaAction();
		dustWaitingAction.setDuration(0.5f);
		dustWaitingAction.setAlpha(1.0f);
		
		splashImage.addAction( splashImageInAction );
	
		addToScreen(splashImage);
		addToScreen(logoAnimation);
		addToScreen(dustAnimation);
	}
	
	public void step()
	{
		
		if( currentPhase == SplashPhase.FINISHED && !startLoadingResources ) 
		{
			startLoadingResources = true;
			
			ResourcesManager.getInstance().loadMenuResources();
			menuAssetManager = ResourcesManager.getInstance().getMenuAssetManager();
			
			ResourcesManager.getInstance().loadStillResources();
			stillAssetManager = ResourcesManager.getInstance().getStillAssetManager();
		}
		
		if( currentPhase == SplashPhase.FINISHED && startLoadingResources ) 
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
			splashImage.addAction(splashImageWaitingAction);
		}
		else if( currentPhase == SplashPhase.SPLASH_IMAGE_WAITING && splashImage.getActions().size <= 0)
		{
			currentPhase = SplashPhase.LOGO_IN;
			logoAnimation.addAction(logoInAction);
			splashImage.addAction(splashImageOutAction);
		}
		else if( currentPhase == SplashPhase.LOGO_IN && logoAnimation.getActions().size <= 0)
		{
			currentPhase = SplashPhase.LOGO_WAITING;
			logoAnimation.addAction(logoWaitingAction);
			
			dustAnimation.setVisible(true);
			dustAnimation.start();
		}
		else if( currentPhase == SplashPhase.LOGO_WAITING && logoAnimation.getActions().size <= 0)
		{
			currentPhase = SplashPhase.DUST_IN;
			logoAnimation.start();
		}
		else if( currentPhase == SplashPhase.DUST_IN && dustAnimation.isFinished())
		{
			dustAnimation.setVisible(false);
			dustAnimation.addAction(dustWaitingAction);
			currentPhase = SplashPhase.DUST_WAITING;
		}
		else if( currentPhase == SplashPhase.DUST_WAITING && dustAnimation.getActions().size <= 0)
		{
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

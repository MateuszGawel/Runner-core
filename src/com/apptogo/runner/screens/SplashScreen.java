package com.apptogo.runner.screens;

import com.apptogo.runner.animation.LogoAnimation;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;

public class SplashScreen extends BaseScreen
{			
	private LogoAnimation logoAnimation;
	private AssetManager menuAssetManager;
	private AssetManager stillAssetManager;
	
	private Actor splashImage;
	
	public SplashScreen(Runner runner)
	{
		super(runner);
	}
	
	@Override
	public void prepare()
	{	
		/* uwaga caly myk z animacja jest na razie na pale - to trzeba zrobic tak jak character Animation no ale wtedy kiedy bd juz miec animacje logo :) */
		
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();

		ResourcesManager.getInstance().loadMenuResources();
		menuAssetManager = ResourcesManager.getInstance().getMenuAssetManager();
		
		ResourcesManager.getInstance().loadStillResources();
		stillAssetManager = ResourcesManager.getInstance().getStillAssetManager();
		
		logoAnimation = new LogoAnimation();
		
		splashImage = logoAnimation.splashImage;
		
		addToScreen(splashImage);
	}
	
	public void step()
	{
		if( logoAnimation.isFinished() ) 
		{
			if( menuAssetManager.update() )
			{
				if( stillAssetManager.update() )
				{
					ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
				}
			}
		}
		else
		{
			AlphaAction action = logoAnimation.animate(splashImage);
			splashImage.addAction(action);
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
		logoAnimation.dispose();
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen extends BaseScreen{	
	
	private Image splashImage;
	private float splashImageOpacity;
	private AlphaAction action;
	
	private final float FADE_IN_TIME = 0.5f; //finalnie powinno byc ~80
	
	public SplashScreen(Runner runner)
	{
		super(runner);
		
		loadPlayer();
		NotificationManager.prepareManager( player.getName() );
	}
	
	@Override
	public void prepare()
	{	
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();

		splashImage = new Image(((Texture)ResourcesManager.getInstance().getResource(this, "gfx/splash/splash.png")));
		splashImage.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - splashImage.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - splashImage.getHeight()/2.0f );
		splashImageOpacity = 0.0f;
		action = new AlphaAction();
		
		addToScreen(splashImage);
	}
	
	public void step()
	{
		if( splashImageOpacity >= 2.0f ) ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		
		splashImageOpacity += 2.0f / FADE_IN_TIME;
		
		action.reset();
		action.setAlpha( ((splashImageOpacity>1.0f)?1.0f:splashImageOpacity) * (float)Math.sin(((splashImageOpacity>1.0f)?1.0f:splashImageOpacity)) );
		splashImage.addAction( action );
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
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

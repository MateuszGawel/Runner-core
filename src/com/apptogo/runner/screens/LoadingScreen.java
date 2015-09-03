package com.apptogo.runner.screens;

import com.apptogo.runner.animation.Loading;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.utils.TimeUtils;

public class LoadingScreen extends BaseScreen
{	    
	private ScreenType screenToLoad;
	private ResourcesManager resourcesManager;
	
	private Loading loadingAnimation;
		
	private long timeStart;
	private boolean loadingLabelIsAdded = false;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad)
	{		
		super(runner);	Logger.log(this,"LOADING CREATED");
		resourcesManager = ResourcesManager.getInstance();
		
		timeStart = TimeUtils.millis();
		
        this.screenToLoad = screenToLoad;
        
        resourcesManager.loadResources(screenToLoad);
	}
		
	public void prepare() 
	{	
		loadingAnimation = new Loading();
		loadingAnimation.setVisible(false);
		setCenterPosition(loadingAnimation, 200);
		
		addToScreen(loadingAnimation);
	}
	
	public void step()
	{Logger.log(this,"LOADING STEP");
		if( !loadingLabelIsAdded )// && ( (TimeUtils.millis() - timeStart) > 500 ) )
		{			
			loadingAnimation.setVisible(true);
			loadingAnimation.start();
			
			loadingLabelIsAdded = true;
		}
		
		if( resourcesManager.getAssetManager(screenToLoad).update() )
		{
			ScreensManager.getInstance().createScreen(screenToLoad);
		}
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
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
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_LOADING;
	}


}

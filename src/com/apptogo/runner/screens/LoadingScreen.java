package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class LoadingScreen extends BaseScreen{	
    
	private ScreenType screenToLoad;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad){
		super(runner);	
        this.screenToLoad = screenToLoad;
        loadProperResources();
	}
	
	private void loadProperResources(){
		if(screenToLoad == ScreenType.SCREEN_GAME){
	        ResourcesManager.getInstance().loadGameMusic();
	        ResourcesManager.getInstance().loadGameSounds();
	        ResourcesManager.getInstance().loadGameTextures();
		}
		else if(screenToLoad == ScreenType.SCREEN_MAIN_MENU){
	        ResourcesManager.getInstance().loadMenuMusic();
	        ResourcesManager.getInstance().loadMenuSounds();
	        ResourcesManager.getInstance().loadMenuTextures();
		}
	}
	
	@Override
	public void show() {

	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0.5f, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(screenToLoad == ScreenType.SCREEN_MAIN_MENU && ResourcesManager.getInstance().getMenuManager().update()) {
			ScreensManager.getInstance().createMainMenuScreen();
		}
		else if(screenToLoad == ScreenType.SCREEN_GAME && ResourcesManager.getInstance().getGameManager().update()) {
			ScreensManager.getInstance().createGameScreen();
		}
		float progress = 0;
		if(screenToLoad == ScreenType.SCREEN_MAIN_MENU)
			progress = ResourcesManager.getInstance().getMenuManager().getProgress();
		else if(screenToLoad == ScreenType.SCREEN_GAME)
			progress = ResourcesManager.getInstance().getGameManager().getProgress();
		Logger.log(this, "progress: " + progress);
		
	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resize(int width, int height) {
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

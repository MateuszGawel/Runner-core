package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends BaseScreen{	
    
	private ScreenType screenToLoad;
	private Stage stage;
	private Viewport viewport;
	private ProgressBar slider;
	
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
		
		Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		slider = new ProgressBar(0.0f, 1.0f, 0.05f, false, skin);
		
		slider.setSize(400, 25);
		slider.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - slider.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - slider.getHeight()/2.0f );
		
		stage = new Stage();
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage.setViewport(viewport);
		
		stage.addActor(slider);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
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
		
		slider.setValue(progress);
		
		stage.act();
		stage.draw();
		
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

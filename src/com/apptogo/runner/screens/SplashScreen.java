package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen extends BaseScreen{	
	
	private Stage stage;
	private Viewport viewport;
	private Skin skin;
	
	private ProgressBar progressBar;
	
	float percent;
	
	public SplashScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
	
		ResourcesManager.getInstance().loadSplashTextures();
		ResourcesManager.getInstance().getSplashManager().finishLoading();
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        progressBar = new ProgressBar(0f, 1f, 0.001f, false, skin);
        
		stage = new Stage();
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT, camera);
		stage.setViewport(viewport);
		
		stage.addActor(progressBar);
        ResourcesManager.getInstance().loadMenuMusic();
        ResourcesManager.getInstance().loadMenuSounds();
        ResourcesManager.getInstance().loadMenuTextures();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if(ResourcesManager.getInstance().getMenuManager().update()) {
			ScreensManager.getInstance().createMainMenuScreen();
		}

		percent = Interpolation.linear.apply(percent, ResourcesManager.getInstance().getMenuManager().getProgress(), 0.1f);
		progressBar.setValue(percent);
		Logger.log(this, "progress: " + percent);
		
		stage.act();
		stage.draw();
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
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

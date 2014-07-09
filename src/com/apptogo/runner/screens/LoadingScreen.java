package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
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
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends BaseScreen{	
    
	private Skin skin;
	private ScreenType screenToLoad;
	private Stage stage;
	private Viewport viewport;
	
	private ResourcesManager resourcesManager;
	
	private ProgressBar slider;
	
	public LoadingScreen(Runner runner, ScreenType screenToLoad)
	{
		super(runner);	
		resourcesManager = ResourcesManager.getInstance();
		
        this.screenToLoad = screenToLoad;
        resourcesManager.loadResources(screenToLoad);
	}
	
	@Override
	public void show() {
		
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		slider = new ProgressBar(0.1f, 1.0f, 0.05f, false, skin);
		
		slider.setSize(400, 25);
		slider.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - slider.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - slider.getHeight()/2.0f );
		slider.setValue(0.1f);

		stage = new Stage();
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage.setViewport(viewport);
		
		stage.addActor(slider);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(resourcesManager.getAssetManager(screenToLoad).update()) {
			ScreensManager.getInstance().createScreen(screenToLoad); //nie zapakowac tu loadinga kolejnego bo bd dziwnie
		}	
		
		float progress = resourcesManager.getAssetManager(screenToLoad).getProgress();
		
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
		//siedlisko bugow! wczesniej tego nie bylo i nic sie nie wyswietlalo - moze zrobic to w baseScreen?
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
		return ScreenType.SCREEN_LOADING;
	}


}

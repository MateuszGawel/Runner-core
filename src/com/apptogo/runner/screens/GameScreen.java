package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Input;
import com.apptogo.runner.handlers.InputHandler;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends BaseScreen{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Stage guiStage;
	private OrthographicCamera guiCamera;
	public StretchViewport guiStretchViewport;
    
	public GameScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
		guiStage = new Stage();
		guiCamera = (OrthographicCamera) guiStage.getCamera();  
		guiStretchViewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT, guiCamera);
		guiStage.setViewport(guiStretchViewport);
		world = new GameWorld();
		worldRenderer = new GameWorldRenderer(world);
		Gdx.input.setInputProcessor(new InputHandler());
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		handleInput();
		
		guiCamera.position.set(Runner.SCREEN_WIDTH/2, Runner.SCREEN_HEIGHT/2, 0); 
		guiCamera.update();
		
		world.update(delta);
		guiStage.act(delta);
		
		worldRenderer.render();
		guiStage.draw();
		
		Input.update();
	}
	
	@Override
	public void handleInput() {
		world.handleInput();
		
	}
	
	@Override
	public void resize(int width, int height) {
		
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
		return ScreenType.SCREEN_GAME;
	}


}

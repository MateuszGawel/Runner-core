package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends BaseScreen implements WarpListener{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Stage guiStage;
	private OrthographicCamera guiCamera;
	public StretchViewport guiStretchViewport;
	private Image back;
    
	public GameScreen(Runner runner){
		super(runner);	
		WarpController.getInstance().setListener(this);
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
		
		back = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/back.png")));
		back.setPosition(0, 0);
		back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "BACK CLICKED");
                 ScreensManager.getInstance().createMainMenuScreen();
             }
         });
		
		//zeby to zadzialalo to trzeba zrobic input multiplexer
		//guiStage.addActor(back);
		//Gdx.input.setInputProcessor(guiStage);
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

	@Override
	public void onWaitingStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameUpdateReceived(String message) {
		try {
			JSONObject data = new JSONObject(message);
			boolean jump = (boolean)data.getBoolean("jump");
			boolean startRunning = (boolean)data.getBoolean("startRunning");
			if(jump){
				world.enemy.jump(2);
				Logger.log(this, "enemy skok");
			}
			if(startRunning)
				world.enemy.startRunning();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}

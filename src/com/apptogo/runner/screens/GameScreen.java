package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.controller.InputHandler;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameScreen extends BaseScreen implements WarpListener{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Image jumpButton;
	private Image slideButton;
	private Image slowButton;
	private Image bombButton;
	
	private boolean slideButtonTouched;
	
	public GameScreen(Runner runner){
		super(runner);	
		//WarpController.getInstance().setListener(this);
	}
	
	public void prepare() 
	{
		world = new GameWorld();
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
	}
	
	private void createGui()
	{
		jumpButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/jumpButton.png"));
		jumpButton.setPosition(Runner.SCREEN_WIDTH - jumpButton.getWidth() - 20, jumpButton.getHeight() + 20 + 40);
		jumpButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.player.jump();
		        return true;
		    }
		});
		guiStage.addActor(jumpButton);
		
		slowButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/slowButton.png"));
		slowButton.setPosition(20, 20);
		slowButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(world.player.isAlive())
					world.player.setRunning(false);
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				world.player.setRunning(true);
		    }
		});
		guiStage.addActor(slowButton);
		
		slideButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/slideButton.png"));
		slideButton.setPosition(Runner.SCREEN_WIDTH - slideButton.getWidth() - 20, 20);
		slideButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				slideButtonTouched = true;
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				slideButtonTouched = false;
				world.player.standUp();
		    }
		});
		guiStage.addActor(slideButton);
		
		bombButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/bombButton.png"));
		bombButton.setPosition(20, bombButton.getHeight() + 20 + 40);
		bombButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.player.throwBombs();
		        return true;
		    }
		});
		guiStage.addActor(bombButton);
	}
	
	public void step() 
	{
		handleInput();
		world.update(delta);
		worldRenderer.render();
		Input.update();
	}
	
	@Override
	public void handleInput() {
		world.handleInput();
		if(slideButtonTouched)
			world.player.slide();
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

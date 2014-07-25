package com.apptogo.runner.screens;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameScreen extends BaseScreen implements WarpListener{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Level level;
	
	private Image jumpButton;
	private Image slideButton;
	private Image slowButton;
	private Image bombButton;
	
	private boolean slideButtonTouched;
	
	public GameScreen(Runner runner){
		super(runner);	
		//WarpController.getInstance().setListener(this);
	}
	
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public void prepare() 
	{
		world = new GameWorld( level.mapPath );
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
	}
	
	
	
	private void createGui()
	{
		jumpButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/jumpButton.png"));
		jumpButton.setPosition(Runner.SCREEN_WIDTH/PPM - jumpButton.getWidth()/PPM - 20/PPM, jumpButton.getHeight()/PPM + 20/PPM + 40/PPM);
		jumpButton.setWidth(jumpButton.getWidth()/PPM);
		jumpButton.setHeight(jumpButton.getHeight()/PPM);
		jumpButton.setBounds(jumpButton.getX(), jumpButton.getY(), jumpButton.getWidth(), jumpButton.getHeight());
		jumpButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				world.player.jump();
		        return true;
		    }
		});
		guiStage.addActor(jumpButton);
		
		slowButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/slowButton.png"));
		slowButton.setPosition(20/PPM, 20/PPM);
		slowButton.setWidth(slowButton.getWidth()/PPM);
		slowButton.setHeight(slowButton.getHeight()/PPM);
		slowButton.setBounds(slowButton.getX(), slowButton.getY(), slowButton.getWidth(), slowButton.getHeight());
		slowButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(world.player.isAlive() && world.player.isStarted())
					world.player.setRunning(false);
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(world.player.isAlive() && world.player.isStarted())
					world.player.setRunning(true);
		    }
		});
		guiStage.addActor(slowButton);
		
		slideButton = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/characters/buttons/slideButton.png"));
		slideButton.setPosition(Runner.SCREEN_WIDTH/PPM - slideButton.getWidth()/PPM - 20/PPM, 20/PPM);
		slideButton.setWidth(slideButton.getWidth()/PPM);
		slideButton.setHeight(slideButton.getHeight()/PPM);
		slideButton.setBounds(slideButton.getX(), slideButton.getY(), slideButton.getWidth(), slideButton.getHeight());
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
		bombButton.setPosition(20/PPM, bombButton.getHeight()/PPM + 20/PPM + 40/PPM);
		bombButton.setWidth(bombButton.getWidth()/PPM);
		bombButton.setHeight(bombButton.getHeight()/PPM);
		bombButton.setBounds(bombButton.getX(), bombButton.getY(), bombButton.getWidth(), bombButton.getHeight());
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
		
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		guiStage.getViewport().update(width, height, true);
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

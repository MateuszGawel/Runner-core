package com.apptogo.runner.screens;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreen extends BaseScreen implements WarpListener{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Level level;
	
	private Button jumpButton;
	private Button slideButton;
	private Button slowButton;
	private Button bombButton;
	
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
		world = new GameWorld( level.mapPath, player.getCurrentCharacter() );
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
	}
	
	
	
	private void createGui()
	{
		Skin guiskin = ResourcesManager.getInstance().getGuiSkin();
		
		jumpButton = world.player.getJumpButton();
		guiStage.addActor(jumpButton);
		
		slowButton = world.player.getSlowButton();
		guiStage.addActor(slowButton);
		
		slideButton = world.player.getSlideButton();
		guiStage.addActor(slideButton);
		
		bombButton = world.player.getAbilityButton(CharacterAbilityType.BOMB);
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
		
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
	}
	
	@Override
	public void resize(int width, int height) {
		guiStage.getViewport().update(width, height, true);
		world.backgroundStage.getViewport().update(width, height, true);
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

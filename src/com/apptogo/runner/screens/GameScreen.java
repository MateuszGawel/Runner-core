package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreen extends BaseScreen implements WarpListener{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Level level;
	
	private Button jumpButton;
	private Button slideButton;
	private Button slowButton;
	private Button abilityButton;
	
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
		world = new GameWorld( level.mapPath, player );
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
		
		NotificationManager.getInstance().screamMyName();
	}
		
	private void createGui()
	{
		Skin guiskin = ResourcesManager.getInstance().getGuiSkin();
		Type type;
		
		if( player.getCurrentCharacter() == CharacterType.BANDIT )
		{
			jumpButton = ((Bandit)world.character).getJumpButton();
			slowButton = ((Bandit)world.character).getSlowButton();
			slideButton = ((Bandit)world.character).getSlideButton();
			abilityButton = ((Bandit)world.character).getAbilityButton(CharacterAbilityType.BOMB);
		}
		//else if()
		//else if() kolejne typy
		
		guiStage.addActor(abilityButton);
		guiStage.addActor(slideButton);
		guiStage.addActor(jumpButton);
		guiStage.addActor(slowButton);
		
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
	public void onGameUpdateReceived(String message) 
	{
		try 
		{
			JSONObject data = new JSONObject(message);
			
			//jesli to przedstawienie sie
			if( data.has("INITIAL_NOTIFICATION") )
			{
				String enemy_name = (String)data.getString("PLAYER_NAME");
				
				Player enemy = new Player();
				enemy.setName( enemy_name );
				enemy.setCurrentCharacter(CharacterType.BANDIT);
				
				world.addEnemy(enemy);
				
				NotificationManager.getInstance().screamMyName();
			}
			else
			{
				//rozparsowuje do zmiennych
				String enemyName = (String)data.getString("PLAYER_NAME");
				
				boolean startRunning = (boolean)data.getBoolean("START_RUNNING");
				boolean dieTop = (boolean)data.getBoolean("DIE_TOP");
				boolean dieBottom = (boolean)data.getBoolean("DIE_BOTTOM");
				boolean jump = (boolean)data.getBoolean("JUMP");
				boolean slide = (boolean)data.getBoolean("SLIDE");
				boolean standUp = (boolean)data.getBoolean("STAND_UP");
				boolean slow = (boolean)data.getBoolean("SLOW");
				boolean abortSlow = (boolean)data.getBoolean("ABORT_SLOW");
				boolean ability = (boolean)data.getBoolean("ABILITY");
				CharacterAbilityType abilityType = CharacterAbilityType.parseFromString( data.getString("ABILITY_TYPE") );
				
				//i teraz wykonuje dzialanie na odpowiednim enemy
				Character enemy = world.getEnemy(enemyName);
				
				if(startRunning) enemy.start();
				if(dieTop) enemy.dieTop();
				if(dieBottom) enemy.dieBottom();
				if(jump) enemy.jump();
				if(slide) enemy.slide();
				if(standUp) enemy.standUp();
				if(slow) enemy.setRunning(false);
				if(abortSlow) enemy.setRunning(true);
				
				//bardziej skomplikowany przypadek
				if(ability) 
				{
					enemy.useAbility(abilityType);
				}
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
		
	}


}

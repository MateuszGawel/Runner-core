package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreenMulti extends GameScreen implements WarpListener{
		
	public GameScreenMulti(Runner runner)
	{
		super(runner);	
		//WarpController.getInstance().setListener(this);
	}
		
	public void prepare() 
	{			
		super.prepare();	
		
		createGui();
		NotificationManager.getInstance().screamMyName();
	}
					
	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_GAME_MULTI;
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
				Logger.log(this, "ktos dolaczyl :) jego imie to: "+enemy_name);
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
				
				Character enemy = world.getEnemy(enemyName);
				
				if( data.has("START_RUNNING") && (boolean)data.getBoolean("START_RUNNING") ) 
				{
					enemy.start();
				}
				if( data.has("DIE_TOP") && (boolean)data.getBoolean("DIE_TOP") )
				{
					enemy.dieTop();
				}
				if( data.has("DIE_BOTTOM") && (boolean)data.getBoolean("DIE_BOTTOM") )
				{
					enemy.dieBottom();
				}
				if( data.has("JUMP") && (boolean)data.getBoolean("JUMP") )
				{
					enemy.jump();
				}
				if( data.has("SLIDE") && (boolean)data.getBoolean("SLIDE") )
				{
					enemy.slide();
				}
				if( data.has("STAND_UP") && (boolean)data.getBoolean("STAND_UP") )
				{
					enemy.standUp();
				}
				if( data.has("SLOW") && (boolean)data.getBoolean("SLOW") )
				{
					enemy.setRunning(false);
				}
				if( data.has("ABORT_SLOW") && (boolean)data.getBoolean("ABORT_SLOW") )
				{
					enemy.setRunning(true);
				}
				if( data.has("ABILITY") && (boolean)data.getBoolean("ABILITY") )
				{
					if( !(data.getString("ABILITY_TYPE").equals("")) )
					{
						CharacterAbilityType abilityType = CharacterAbilityType.parseFromString( data.getString("ABILITY_TYPE") );
						enemy.useAbility(abilityType);
					}
				}
				//mala uwaga
				//wydaje mi sie ze wydajnosc bylaby lepsza gdyby uzyc else ifow ale raczej tylko ciut a na dodatek stracilibysmy mozliwosci przeslania dwoch rzeczy na raz [choc nie wiem czy w ogole tego potrzebujemy :)]
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
		
	}


}

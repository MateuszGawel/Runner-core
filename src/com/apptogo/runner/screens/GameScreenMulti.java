package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.SaveManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
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
		
		NotificationManager.getInstance().enableAppWarpNotifications(); //uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
		WarpController.getInstance().setGameListener(this);
	}
		
	public void prepare() 
	{			
		super.prepare();	

		createGui();
		//NotificationManager.getInstance().screamMyName();
	}
					
	@Override
	public ScreenType getSceneType() 
	{
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
							
			//rozparsowuje do zmiennych
			Player enemy = null;
			String enemyName = (String)data.getString("PLAYER_NAME");
			
			try
			{
				enemy = world.getEnemy(enemyName);
				Logger.log(this, "Dostalem wiadomosc od: " + enemyName + " tresc: " + data);
			}
			catch(PlayerDoesntExistException e)
			{
				Logger.log(this, "Player " + enemyName + " is not created and cannot be used");
			}
			
			if( data.has("START_RUNNING") && (boolean)data.getBoolean("START_RUNNING") ) 
			{
				enemy.character.start();
			}
			else if( data.has("DIE_TOP") && (boolean)data.getBoolean("DIE_TOP") )
			{
				enemy.character.dieTop();
			}
			else if( data.has("DIE_BOTTOM") && (boolean)data.getBoolean("DIE_BOTTOM") )
			{
				enemy.character.dieBottom();
			}
			else if( data.has("JUMP") && (boolean)data.getBoolean("JUMP") )
			{
				Logger.log(this, "mam jumpa");
				enemy.character.jump();
			}
			else if( data.has("SLIDE") && (boolean)data.getBoolean("SLIDE") )
			{
				enemy.character.slide();
			}
			else if( data.has("STAND_UP") && (boolean)data.getBoolean("STAND_UP") )
			{
				enemy.character.standUp();
			}
			else if( data.has("SLOW") && (boolean)data.getBoolean("SLOW") )
			{
				enemy.character.setRunning(false);
			}
			else if( data.has("ABORT_SLOW") && (boolean)data.getBoolean("ABORT_SLOW") )
			{
				enemy.character.setRunning(true);
			}
			else if( data.has("ABILITY") && (boolean)data.getBoolean("ABILITY") )
			{
				if( !(data.getString("ABILITY_TYPE").equals("")) )
				{
					CharacterAbilityType abilityType = CharacterAbilityType.parseFromString( data.getString("ABILITY_TYPE") );
					enemy.character.useAbility(abilityType);
				}
			}
			//mala uwaga
			//wydaje mi sie ze wydajnosc bylaby lepsza gdyby uzyc else ifow ale raczej tylko ciut a na dodatek stracilibysmy mozliwosci przeslania dwoch rzeczy na raz [choc nie wiem czy w ogole tego potrzebujemy :)]
		} 
		catch (JSONException e) { e.printStackTrace(); }
		
	}


}

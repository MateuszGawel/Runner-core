package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class GameScreenMulti extends GameScreen implements WarpListener
{	
	public GameScreenMulti(Runner runner, Level level, Array<Player> enemies)
	{
		super(runner, level, enemies);	
	}
		
	public void prepare() 
	{			
		super.prepare();
		createGui();
		NotificationManager.getInstance().enableAppWarpNotifications(); //uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
		WarpController.getInstance().setGameListener(this);
		NotificationManager.getInstance().notifyReadyToRun();
		createLabels();
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

	
	private void handleReadyToRun(Player sender){

		if( gameWorld.player.getName().equals(sender.getName()) )
		{
			return;
		}
		else if(sender.readyToRun){
			return;
		}
		else if(!sender.readyToRun){
			sender.readyToRun = true;
			NotificationManager.getInstance().notifyReadyToRun();
		}
		
			
		for(Player enemy : enemies){
			if(!enemy.readyToRun)
				return;
		}
		
		startLabel.remove();
		Countdown countdown = new Countdown(gameWorld);
		countdown.startCountdown();
		
		for(Player enemy : enemies){
			CustomActionManager.getInstance().registerAction(new CustomAction(1f, 4, enemy.character) {
				@Override 
				public void perform() {
					if(timeElapsed == 4)
						((Character)args[0]).start();
				}
			});
		}
		CustomActionManager.getInstance().registerAction(new CustomAction(1f, 4, gameWorld.player.character) {
			@Override
			public void perform() {
				if(timeElapsed == 4){
					((Character)args[0]).start(); 
				}
					
			}
		});
	}
	
	@Override
	public void onGameUpdateReceived(String message) 
	{
		try 
		{
			JSONObject data = new JSONObject(message);
							
			//rozparsowuje do zmiennych
			Player sender = null;
			String enemyName = (String)data.getString("PLAYER_NAME");
			
			try
			{
				sender = gameWorld.getEnemy(enemyName);
			}
			catch(PlayerDoesntExistException e)
			{
				Logger.log(this, "Player " + enemyName + " is not created and cannot be used");
			}

			if( gameWorld.player.getName().equals(enemyName) )
			{
				return;
			}
			else if( data.has("READY_TO_RUN") && (boolean)data.getBoolean("READY_TO_RUN") ) 
			{
				handleReadyToRun(sender);
			}
			else if( data.has("JUMP") && (boolean)data.getBoolean("JUMP") )
			{
				CustomActionManager.getInstance().registerAction(new CustomAction(0, 1, sender.character, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						((Character)args[0]).getBody().setTransform(new Vector2((Float)args[1], (Float)args[2]), 0);
						((Character)args[0]).jump(1, 1, 0, 0);
					}
				});
			}
			else if( data.has("SLIDE") && (boolean)data.getBoolean("SLIDE") )
			{
				CustomActionManager.getInstance().registerAction(new CustomAction(0, 1, sender.character, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						((Character)args[0]).getBody().setTransform(new Vector2((Float)args[1], (Float)args[2]), 0);
						((Character)args[0]).slide();
					}
				});
			}
			else if( data.has("STAND_UP") && (boolean)data.getBoolean("STAND_UP") )
			{
				CustomActionManager.getInstance().registerAction(new CustomAction(0, 1, sender.character, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						((Character)args[0]).getBody().setTransform(new Vector2((Float)args[1], (Float)args[2]), 0);
						((Character)args[0]).standUp();
					}
				});
			}/*
			else if( data.has("SLOW") && (boolean)data.getBoolean("SLOW") )
			{
				enemy.character.setRunning(false);
			}
			else if( data.has("ABORT_SLOW") && (boolean)data.getBoolean("ABORT_SLOW") )
			{
				enemy.character.setRunning(true);
			}*/
			else if( data.has("ABILITY") && (boolean)data.getBoolean("ABILITY") )
			{
				if( !(data.getString("ABILITY_TYPE").equals("")) )
				{
					CharacterAbilityType abilityType = CharacterAbilityType.parseFromString( data.getString("ABILITY_TYPE") );
					
					//uwaga w tej linijce nie jestem pewien czy ten sender ma abilities uzupelnione!
					sender.character.useSuperAbility(abilityType, sender.abilities.get( abilityType.toString() ));
				}
			}
			//mala uwaga
			//wydaje mi sie ze wydajnosc bylaby lepsza gdyby uzyc else ifow ale raczej tylko ciut a na dodatek stracilibysmy mozliwosci przeslania dwoch rzeczy na raz [choc nie wiem czy w ogole tego potrzebujemy :)]
		} 
		catch (JSONException e) { e.printStackTrace(); }
		
	}


}

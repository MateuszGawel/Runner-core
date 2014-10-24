package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.handlers.CharacterAction;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.math.Vector2;

public class GameScreenMulti extends GameScreen implements WarpListener
{	
	public GameScreenMulti(Runner runner)
	{
		super(runner);	
		
		NotificationManager.getInstance().enableAppWarpNotifications(); //uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
		WarpController.getInstance().setGameListener(this);
	}
		
	public void prepare() 
	{			
		super.prepare();	
		super.multiplayer = true;
		createGui();
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
		Logger.log(this, "jest to wiadomosc readytorun");

		if( world.player.getName().equals(sender.getName()) )
		{
			Logger.log(this, "zaraz zaraz, przecie¿ ten gracz to ja. No nieŸle");
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
		
		label.remove();
		Logger.log(this, "OK 2 GRACZY SIE POLACZYLO DO GRY! MOZNA ODPALAC ODLICZANIE");
		Countdown countdown = new Countdown(world);
		countdown.startCountdown();
		
		for(Player enemy : enemies){
			enemy.character.registerAction(new CharacterAction(enemy.character, 1f, 4) {
				@Override
				public void perform() {
					if(timeElapsed == 4)
						character.start();
				}
			});
		}
		world.player.character.registerAction(new CharacterAction(world.player.character, 1f, 4) {
			@Override
			public void perform() {
				if(timeElapsed < 4)
					Logger.log(this, "ODLICZAM: " + (4 - timeElapsed));
				else if(timeElapsed == 4){
					Logger.log(this, "ODLICZAM: GO!!!!");
					character.start(); 
				}
					
			}
		});
	}
	
	@Override
	public void onGameUpdateReceived(String message) 
	{
		Logger.log(this, "³apiê w gamescreenulti");
		try 
		{
			JSONObject data = new JSONObject(message);
							
			//rozparsowuje do zmiennych
			Player sender = null;
			String enemyName = (String)data.getString("PLAYER_NAME");
			
			try
			{
				sender = world.getEnemy(enemyName);
				Logger.log(this, "Dostalem wiadomosc od: " + enemyName + " tresc: " + data);
			}
			catch(PlayerDoesntExistException e)
			{
				Logger.log(this, "Player " + enemyName + " is not created and cannot be used");
			}

			if( world.player.getName().equals(enemyName) )
			{
				Logger.log(this, "zaraz zaraz, przecie¿ ten gracz to ja. No nieŸle");
				return;
			}
			else if( data.has("READY_TO_RUN") && (boolean)data.getBoolean("READY_TO_RUN") ) 
			{
				handleReadyToRun(sender);
			}
			else if( data.has("JUMP") && (boolean)data.getBoolean("JUMP") )
			{
				sender.character.registerAction(new CharacterAction(sender.character, 0, 1, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						character.getBody().setTransform(new Vector2((Float)args[0], (Float)args[1]), 0);
						character.jump();
					}
				});
			}
			else if( data.has("SLIDE") && (boolean)data.getBoolean("SLIDE") )
			{
				sender.character.registerAction(new CharacterAction(sender.character, 0, 1, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						character.getBody().setTransform(new Vector2((Float)args[0], (Float)args[1]), 0);
						character.slide();
					}
				});
			}
			else if( data.has("STAND_UP") && (boolean)data.getBoolean("STAND_UP") )
			{
				sender.character.registerAction(new CharacterAction(sender.character, 0, 1, (float)data.getDouble("X"), (float)data.getDouble("Y")) {
					@Override
					public void perform() {
						character.getBody().setTransform(new Vector2((Float)args[0], (Float)args[1]), 0);
						character.standUp();
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
					sender.character.useAbility(abilityType);
				}
			}
			//mala uwaga
			//wydaje mi sie ze wydajnosc bylaby lepsza gdyby uzyc else ifow ale raczej tylko ciut a na dodatek stracilibysmy mozliwosci przeslania dwoch rzeczy na raz [choc nie wiem czy w ogole tego potrzebujemy :)]
		} 
		catch (JSONException e) { e.printStackTrace(); }
		
	}


}

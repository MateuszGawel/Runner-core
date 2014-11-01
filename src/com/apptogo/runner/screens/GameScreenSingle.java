package com.apptogo.runner.screens;

import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.main.Runner;

public class GameScreenSingle extends GameScreen
{		
	public GameScreenSingle(Runner runner)
	{
		super(runner);
		NotificationManager.getInstance().disableAppWarpNotifications();//uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
	}
	
	public void prepare() 
	{
		player.setCharacterType( GameWorldType.convertToCharacterType( level.worldType ) );
		
		super.prepare();	
		
		createGui();
		createLabels();
		
		
		CustomActionManager.getInstance().registerAction(new CustomAction(2f, 1, player.character) {
			
			@Override
			public void perform() {
				Countdown countdown = new Countdown(world);
				countdown.startCountdown();
				
			}
		});

	}
	
	@Override
	public void handleInput()
	{
		if( Input.isPressed() ) 
		{
			label.remove();
		}
		super.handleInput();
	}
	
	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_GAME_SINGLE;
	}
}

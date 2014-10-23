package com.apptogo.runner.screens;

import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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

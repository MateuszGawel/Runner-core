package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreenSingle extends GameScreen{
		
	public GameScreenSingle(Runner runner)
	{
		super(runner);
		NotificationManager.getInstance().disableAppWarpNotifications();//uwaga - to powoduje ze tak czy siak jest wywolywana funkcja z notifManagera w Character (i na starcie w gameWorld) - moze spowalniac program :<
	}
		
	public void prepare() 
	{	
		player.setCurrentCharacter( GameWorldType.convertToCharacterType( level.worldType ) );
		
		super.prepare();	
		
		createGui();
	}
			
	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_GAME_SINGLE;
	}
}

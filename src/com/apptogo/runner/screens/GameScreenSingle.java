package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.controller.Input;
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
	}
		
	public void prepare() 
	{	
		//world = new WildWestWorld( level.mapPath, player );
		world = new ForestWorld( level.mapPath, player );
		
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
	}
			
	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_GAME_SINGLE;
	}
}

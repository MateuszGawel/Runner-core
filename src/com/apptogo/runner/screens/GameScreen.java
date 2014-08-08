package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorld.GameWorldType;
import com.apptogo.runner.world.GameWorldRenderer;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public abstract class GameScreen extends BaseScreen{
	
	protected GameWorld world;
	protected GameWorldRenderer worldRenderer;
	
	protected Level level;
	
	protected Button jumpButton;
	protected Button slideButton;
	protected Button slowButton;
	protected Button abilityButton;
	
	public GameScreen(Runner runner)
	{
		super(runner);		
		loadPlayer();
	}
		
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public void prepare()
	{Logger.log(this, "TYPE: "+level.worldType.toString());
		if( level.worldType == GameWorldType.WILDWEST ) 
		{Logger.log(this, "A TU DOSTAJE: " + player.getCurrentCharacter().toString() );
			world = new WildWestWorld( level.mapPath, player );
		}
		else if( level.worldType == GameWorldType.FOREST )
		{
			Logger.log(this, "MAMY FORERSTA!");
			world = new ForestWorld( level.mapPath, player );
		}
		//else if( level.worldType == GameWorldType.SPACE )
		//{
		//	world = new ForestWorld( level.mapPath, player );
		//}
		
		worldRenderer = new GameWorldRenderer(world);
	}
	
	protected void createGui()
	{
		Skin guiskin = ResourcesManager.getInstance().getGuiSkin();
		Type type;
		
		jumpButton = world.character.getJumpButton();
		slowButton = world.character.getSlowButton();
		slideButton = world.character.getSlideButton();
		abilityButton = world.character.getAbilityButton(CharacterAbilityType.BOMB);
		
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
	public abstract ScreenType getSceneType();
}

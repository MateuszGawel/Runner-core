package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerExistsException;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.ForestWorld;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.apptogo.runner.world.WildWestWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;

public abstract class GameScreen extends BaseScreen{
	
	protected GameWorld world;
	protected GameWorldRenderer worldRenderer;
	
	protected Level level;
	protected Array<Player> enemies;
	
	protected Button jumpButton;
	protected Button slideButton;
	protected Button slowButton;
	protected Array<Button> powerupButtons;
	protected boolean multiplayer;
	
	public GameScreen(Runner runner)
	{
		super(runner);		
		loadPlayer();
		
		ResourcesManager.getInstance().unloadMenuResources(); //czy to na pewno dobre miejsce na ta funkcje? tu sie fajnie wpasowuje ale tak naprawde to powinno byc zrobione na etapie loadingu
	}
		
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public void setEnemies(Array<Player> enemies)
	{
		this.enemies = enemies;
	}
	
	public void prepare()
	{
		powerupButtons = new Array<Button>();
		
		world = GameWorldType.convertToGameWorld( level.worldType, level.mapPath, player );
		
		if(enemies != null)
		{
			for(int i = 0; i < enemies.size; i++)
			{
				try
				{
					world.addEnemy( enemies.get(i) );
				}
				catch(PlayerExistsException e)
				{
					Logger.log(this, "Player " + enemies.get(i).getName() + " is created already!");
				}
			}
		}
		
		worldRenderer = new GameWorldRenderer(world);
	}
	
	protected void createGui()
	{		
		jumpButton = world.player.character.getJumpButton();
		slideButton = world.player.character.getSlideButton();

		powerupButtons = world.player.character.initializePowerupButtons();
		
		guiStage.addActor(slideButton);
		guiStage.addActor(jumpButton);		
		
		for(Button powerupButton: powerupButtons)
		{
			guiStage.addActor(powerupButton);
		}
		
		world.player.character.setPowerup(PowerupType.NONE);
	}
	
	public void step() 
	{
		handleInput();
		world.update(delta);
		worldRenderer.render();
		Input.update();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
		else if( Input.isPressed() ) 
		{
			if(!multiplayer) 
				this.player.character.start();
				//NotificationManager.getInstance().notifyStartRunning(this.player.character.getBody().getPosition());
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
		world.dispose();
	}

	@Override
	public abstract ScreenType getSceneType();
}

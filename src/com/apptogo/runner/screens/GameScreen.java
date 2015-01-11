package com.apptogo.runner.screens;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.GameProgressBar;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerExistsException;
import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public abstract class GameScreen extends BaseScreen{
	
	public GameWorld world;
	protected GameWorldRenderer worldRenderer;
	
	protected Level level;
	protected Array<Player> enemies;
	
	protected Button jumpButton;
	protected Button slideButton;
	protected Button slowButton;
	protected Actor gameProgressBar;
	protected Array<Button> powerupButtons;
	protected boolean multiplayer;
	
	protected Label startLabel;
	public Label coinLabel;

	public GameScreen(Runner runner)
	{
		super(runner);		
		loadPlayer();
		
		ResourcesManager.getInstance().unloadMenuResources(); //czy to na pewno dobre miejsce na ta funkcje? tu sie fajnie wpasowuje ale tak naprawde to powinno byc zrobione na etapie loadingu
	
		CoinsManager.create();
	}
	
	protected void createLabels()
	{
		startLabel = createLabel(getLangString("tapToStart"), FontType.GAMEWORLDFONT);
		startLabel.setPosition( (Runner.SCREEN_WIDTH / 2.0f) - (startLabel.getWidth() / 2.0f), Runner.SCREEN_HEIGHT/2 + 300.0f);
		//gameGuiStage.addActor(startLabel);
		
		coinLabel = createLabel("0", FontType.COINFONT);
		coinLabel.setPosition(40, Runner.SCREEN_HEIGHT - 100);
		gameGuiStage.addActor(coinLabel);

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
		Logger.log(this, "prepare z gamescreen");
		powerupButtons = new Array<Button>();
		
		world = GameWorldType.convertToGameWorld( level.worldType, level.mapPath, player );
		Logger.log(this, "twroze swait");
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
		
		gameGuiStage.addActor(slideButton);
		gameGuiStage.addActor(jumpButton);		
		//TEMP
		gameGuiStage.addActor(world.player.character.getTempButton());
		
		for(Button powerupButton: powerupButtons)
		{
			gameGuiStage.addActor(powerupButton);
		}
		
		world.player.character.setPowerup(PowerupType.ABILITY1);
		createGameProgressBar();
	}
	
	protected void createGameProgressBar(){
		gameProgressBar = new GameProgressBar(world);
		gameGuiStage.addActor(gameProgressBar);
	}
	
	public void step() 
	{
		handleInput();	
		handleCoinLabel();
		world.update(delta);
		worldRenderer.resize(currentWindowWidth, currentWindowHeight);
		worldRenderer.render();
		Input.update();
	}
	
	private void handleCoinLabel(){
		if(Integer.valueOf(coinLabel.getText().toString()) < world.player.character.getCoinCounter()){
			coinLabel.setText(String.valueOf(world.player.character.getCoinCounter()));
			
			ParticleEffectActor coinCounterEffectActor = new ParticleEffectActor("coinCounter.p");
			coinCounterEffectActor.setPosition(coinLabel.getX() + coinLabel.getWidth()/2, coinLabel.getY() + coinLabel.getHeight()/2);
			coinCounterEffectActor.start();
			coinCounterEffectActor.removeAfterComplete = true;
			coinLabel.remove();
			gameGuiStage.addActor(coinCounterEffectActor);
			gameGuiStage.addActor(coinLabel);
		}
	}
	
	private Player currentPlayerInput = this.player;

	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		}
//		else if( Input.isPressed() ) 
//		{
//			if(!multiplayer) 
//				this.currentPlayerInput.character.start();
//				//NotificationManager.getInstance().notifyStartRunning(this.player.character.getBody().getPosition());
//		}
		
		if(currentPlayerInput != null){
			//sterowanie klawiatura
			if( Gdx.input.isKeyPressed(Keys.Z) && !currentPlayerInput.slidePressed)
			{
				currentPlayerInput.character.flags.setSlideButtonPressed(true);
				currentPlayerInput.character.slide();
				currentPlayerInput.slidePressed = true;
			}
			else if(!Gdx.input.isKeyPressed(Keys.Z) && currentPlayerInput.slidePressed){
				currentPlayerInput.character.flags.setSlideButtonPressed(false);
				currentPlayerInput.character.standUp();
				currentPlayerInput.slidePressed = false;
		    }
			
			if( Gdx.input.isKeyPressed(Keys.M) && !currentPlayerInput.jumpPressed)
			{
				currentPlayerInput.character.flags.setQueuedJump(true);
				currentPlayerInput.jumpPressed = true;
			}
			else if(!Gdx.input.isKeyPressed(Keys.M) && currentPlayerInput.jumpPressed)
				currentPlayerInput.jumpPressed = false;
			
			if( Gdx.input.isKeyPressed(Keys.A) && !currentPlayerInput.abilityPressed)
			{
				if(currentPlayerInput.character.flags.isCanUseAbility()) 
					currentPlayerInput.character.usePowerup( currentPlayerInput.character.currentPowerupSet );
				currentPlayerInput.abilityPressed = true;
			}
			else if(!Gdx.input.isKeyPressed(Keys.A) && currentPlayerInput.abilityPressed)
				currentPlayerInput.abilityPressed = false;
			
			if( Gdx.input.isKeyPressed(Keys.K) && !currentPlayerInput.tempPressed)
			{
				if(!currentPlayerInput.character.flags.isTempRunFlag())
					currentPlayerInput.character.flags.setTempRunFlag(true);
				else
					currentPlayerInput.character.flags.setTempRunFlag(false);
				currentPlayerInput.tempPressed = true;
			}
			else if( !Gdx.input.isKeyPressed(Keys.K) && currentPlayerInput.tempPressed)
				currentPlayerInput.tempPressed = false;
			
			//zmiana numeru gracza
			if( Gdx.input.isKeyPressed(Keys.NUM_1))
			{
				currentPlayerInput = this.player;
			}
			else if( Gdx.input.isKeyPressed(Keys.NUM_2))
			{
				Logger.log(this, "zmieniam na: " + world.getEnemyNumber(1).getName());
				currentPlayerInput = world.getEnemyNumber(0);
			}
			else if( Gdx.input.isKeyPressed(Keys.NUM_3))
			{
				currentPlayerInput = world.getEnemyNumber(1);
			}
			else if( Gdx.input.isKeyPressed(Keys.NUM_4))
			{
				currentPlayerInput = world.getEnemyNumber(2);
			}
		}
		else
			currentPlayerInput = this.player;

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
	public void dispose() 
	{
		super.dispose();
		world.dispose();
		CoinsManager.destroy();
		//CustomActionManager.destroy();
	}

	@Override
	public abstract ScreenType getSceneType();
}

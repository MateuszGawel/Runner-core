package com.apptogo.runner.screens;

import com.apptogo.runner.actors.CharacterButton;
import com.apptogo.runner.actors.GameProgressBar;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.FontType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.exception.PlayerExistsException;
import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.logger.Logger.LogLevel;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

public abstract class GameScreen extends BaseScreen{
	
	public GameWorld gameWorld;
	public GameWorldType gameWorldType;
	
	public GameWorldRenderer worldRenderer;
	
	protected Level level;
	protected Array<Player> enemies;
	
	protected Array<CharacterButton> powerupButtons;
	protected Button jumpButton;
	protected Button slideButton;
	protected Button slowButton;
	
	protected Actor gameProgressBar;
	
	ParticleEffectActor coinCounterEffectActor;
	
	public Label startLabel;
	public Label coinLabel;
	
	private int coinLabelCounter;
	private int prevCoinCounter;
	private CustomAction stopAction;

	public GameScreen(Runner runner, Level levelToLoad, Array<Player> enemiesList)
	{
		super(runner);
		
		loadPlayer();

		this.level = levelToLoad;
		this.enemies = enemiesList;
		
		CoinsManager.create();
		
		powerupButtons = new Array<CharacterButton>();
		
		gameWorld = GameWorldType.convertToGameWorld(level.mapPath, level.worldType, player );
		gameWorldType = level.worldType;
		
		if(this.enemies != null)
		{
			for(int i = 0; i < this.enemies.size; i++)
			{
				try
				{
					gameWorld.addEnemy( this.enemies.get(i) );
				}
				catch(PlayerExistsException ex)
				{
					Logger.log(this, "Player " + this.enemies.get(i).getName() + " is created already!", LogLevel.HIGH);
				}
			}
		}
		
		worldRenderer = new GameWorldRenderer(gameWorld);
	}
	
	protected void createLabels()
	{
		startLabel = new Label(getLangString("tapToStart"), skin, "default");
		startLabel.setPosition( (Runner.SCREEN_WIDTH / 2.0f) - (startLabel.getWidth() / 2.0f), Runner.SCREEN_HEIGHT/2 + 300.0f);
		
		gameGuiStage.addActor(coinLabel);
	}
	
	protected void createCoinLabel(){
		coinLabelCounter = 0;
		coinCounterEffectActor = new ParticleEffectActor("coinCounter.p", (TextureAtlas)ResourcesManager.getInstance().getResource(this, "gfx/game/characters/charactersAtlas.pack"));

		coinLabel = new Label("0", ResourcesManager.getInstance().getStillSkin(), "default");
		coinLabel.setPosition(40, Runner.SCREEN_HEIGHT - 100);
		coinLabel.setText( String.valueOf(coinLabelCounter) );
		gameGuiStage.addActor(coinLabel);
		
		stopAction = new CustomAction(0.10f) {
			@Override
			public void perform() {
				if(!coinCounterEffectActor.isComplete()){
					coinCounterEffectActor.allowCompletion();
				}
			}
		};
		coinCounterEffectActor.setPosition(coinLabel.getX() + coinLabel.getWidth()/2, coinLabel.getY() + coinLabel.getHeight()/2);
		gameGuiStage.addActor(coinCounterEffectActor);
	}
		
	public void prepare()
	{
		createCoinLabel();
	}
	
	protected void createGui()
	{		
		Logger.log(this, gameWorld.player.character.getCharacterType().toString() + "JumpButton");
		gameGuiStage.addActor( gameWorld.player.character.getJumpButton(gameWorld.player.character.getCharacterType().toString().toLowerCase() + "JumpButton") );
		gameGuiStage.addActor( gameWorld.player.character.getSlideButton(gameWorld.player.character.getCharacterType().toString().toLowerCase() + "SlideButton") );
		gameGuiStage.addActor(gameWorld.player.character.getTempButton(gameWorld.player.character.getCharacterType().toString().toLowerCase() + "JumpButton"));
		
		powerupButtons = gameWorld.player.character.initializePowerupButtons();
		for(CharacterButton powerupButton: powerupButtons)
		{
			gameGuiStage.addActor(powerupButton);
		}
		gameWorld.player.character.setPowerup(PowerupType.ABILITY1);
		
		createGameProgressBar();
	}
	
	protected void createGameProgressBar()
	{
		gameProgressBar = new GameProgressBar(gameWorld);
		gameGuiStage.addActor(gameProgressBar);
	}
	
	public void step() 
	{
		handleInput();	
		handleCoinLabel();
		gameWorld.update(delta);
		worldRenderer.resize(currentWindowWidth, currentWindowHeight);
		worldRenderer.render();
		Input.update();
	}
	
	private void handleCoinLabel()
	{
		coinLabelCounter = gameWorld.player.character.getCoinCounter();
		if(coinLabelCounter > prevCoinCounter){
			coinLabel.remove();
			coinLabel.setText( String.valueOf(coinLabelCounter) );
			gameGuiStage.addActor(coinLabel);
			prevCoinCounter = coinLabelCounter;
			coinCounterEffectActor.disallowCompletion();
			if(coinCounterEffectActor.isComplete()){
				coinCounterEffectActor.reset();
				coinCounterEffectActor.start();
			}
			stopAction.resetAction();
			CustomActionManager.getInstance().unregisterAction(stopAction);
			CustomActionManager.getInstance().registerAction(stopAction);	
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
				currentPlayerInput = gameWorld.getEnemyNumber(0);
			}
			else if( Gdx.input.isKeyPressed(Keys.NUM_3))
			{
				currentPlayerInput = gameWorld.getEnemyNumber(1);
			}
			else if( Gdx.input.isKeyPressed(Keys.NUM_4))
			{
				currentPlayerInput = gameWorld.getEnemyNumber(2);
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
		gameWorld.dispose();
		CoinsManager.destroy();
		//CustomActionManager.destroy();
	}

	@Override
	public abstract ScreenType getSceneType();
}

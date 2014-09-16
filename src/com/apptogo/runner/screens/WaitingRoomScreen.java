package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class WaitingRoomScreen extends BaseScreen implements WarpListener
{			
	private Label label;
	
	private TextButton playButton;
	private Button backButton;
	
	private Array<Player> players;
	
	private Array<CharacterAnimation> playersAnimation;
	private final Array<Vector2> playerAnimationPosition = new Array<Vector2>( new Vector2[]{ new Vector2(-400, 150), 
																			   				  new Vector2(-100, 150), 
																			   				  new Vector2(-400, -150), 
																			   				  new Vector2(-100, -150) } );
	
	private int currentPlayersCount;
	
	//do wywalenia
	boolean a=true, s=true, d=true;
	//--
	
	private float lastLabelY = 250f;
	
	public WaitingRoomScreen(Runner runner)
	{
		super(runner);	
		
		loadPlayer();
		NotificationManager.prepareManager( player.getName(), player.getCurrentCharacter() );
		
		fadeInOnStart();
		
		if( !(WarpController.getInstance().isOnline) )
		{
			Logger.log(this, "Jeszcze nie byl online //waiting");
			WarpController.getInstance().startApp( player.getName() );
		}
		WarpController.getInstance().setListener(this);
	}
	
	public void prepare() 
	{		
		setBackground("gfx/menu/menuBackgrounds/mainMenuScreenBackground.png");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
            }
        });
        
        playButton = new TextButton("PLAY", skin, "default");
		playButton.setPosition( -( playButton.getWidth() / 2.0f ), -300f);
		playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/map.tmx", "", GameWorldType.WILDWEST), players );
            }
        });
        
		players = new Array<Player>();
		playersAnimation = new Array<CharacterAnimation>();
		currentPlayersCount = 0;
		
        //addToScreen(playButton);
        addToScreen(backButton);
        
        addPlayer( player );
	}
	
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) || Gdx.input.isKeyPressed(Keys.BACK) )
		{
			WarpController.getInstance().stopApp();
			loadScreenAfterFadeOut( ScreenType.SCREEN_MAIN_MENU );
		}
		
		if( Gdx.input.isKeyPressed(Keys.A) )
		{
			if(a)
			{
				Player playerA = new Player();
				playerA.setName("MACIEK");
				playerA.setCurrentCharacter(CharacterType.ALIEN);
			
				addPlayer( playerA );
			
				a = false;
			}
		}
		
		if( Gdx.input.isKeyPressed(Keys.S) )
		{
			if(s)
			{
				Player playerA = new Player();
				playerA.setName("MARIAN");
				playerA.setCurrentCharacter(CharacterType.BANDIT);
				
				addPlayer( playerA );
				
				s = false;
			}
		}
		
		if( Gdx.input.isKeyPressed(Keys.D) )
		{
			if(d)
			{
				Player playerA = new Player();
				playerA.setName("WILHELM");
				playerA.setCurrentCharacter(CharacterType.ARCHER);
				
				addPlayer( playerA );
				
				d = false;
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
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
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_WAITING_ROOM;
	}

	@Override
	public void onGameUpdateReceived(String message) 
	{
		try 
		{
			JSONObject data = new JSONObject(message);
			
			//jesli to przedstawienie sie
			if( data.has("INITIAL_NOTIFICATION") )
			{
				String enemyName = (String)data.getString("PLAYER_NAME");
				
				boolean presentAlready = false;

				if( player.getName().equals(enemyName) )
				{
					presentAlready = true;
				}
								
				for(int i = 0; i < players.size; i++)
				{
					if( players.get(i).getName().equals(enemyName) ) 
					{
						presentAlready = true;
						break;
					}
				}
				
				if( !presentAlready )			
				{
					CharacterType enemyCharacter = CharacterType.parseFromString( (String)data.getString("PLAYER_CHARACTER") );
					Logger.log(this, "OTO CO DOSTALEM: " + (String)data.getString("PLAYER_CHARACTER"));
					Player enemy = new Player();
					enemy.setName(enemyName);
					enemy.setCurrentCharacter(enemyCharacter);
					
					Logger.log(this, "ktos dolaczyl :) jego imie to: "+enemyName);
														
					players.add(enemy);
					addPlayer(enemy);
					
					NotificationManager.getInstance().screamMyName();
				}
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
	}
	
	private void addPlayer(Player player)
	{
		if( currentPlayersCount < 4 )
		{
			CharacterAnimation playerAnimation = CharacterType.convertToCharacterAnimation(player.getCurrentCharacter(), playerAnimationPosition.get(currentPlayersCount).x, playerAnimationPosition.get(currentPlayersCount).y, true);
			
			Label playerNameLabel = new Label(player.getName(), skin);
			playerNameLabel.setPosition(playerAnimationPosition.get(currentPlayersCount).x, playerAnimationPosition.get(currentPlayersCount).y - 50.0f);
			
			addToScreen(playerAnimation);
			addToScreen(playerNameLabel);
			
			currentPlayersCount++;
		}
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
	public void onGameStarted(String message) 
	{
		Gdx.app.postRunnable(new Runnable() {
			@Override
			public void run () {
				//nie chcemy na razie nic robic - dopiero jak ktos kliknie play
				//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/map.tmx", "", GameWorldType.WILDWEST), player, enemies);
			
					NotificationManager.getInstance().screamMyName();
			
			}
		});
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
	}
}

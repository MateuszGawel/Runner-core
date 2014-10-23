package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.animation.CharacterAnimation;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.FontType;
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
import com.shephertz.app42.gaming.multiplayer.client.WarpClient;

public class WaitingRoomScreen extends BaseScreen implements WarpListener
{			
	private TextButton playButton;
	private Button backButton;
	
	private Array<Player> enemyPlayers;
	
	private final Array<Vector2> playerAnimationPosition = new Array<Vector2>( new Vector2[]{ new Vector2(-400, 150), 
																			   				  new Vector2(-100, 150), 
																			   				  new Vector2(-400, -150), 
																			   				  new Vector2(-100, -150) } );
	
	private int currentPlayersCount;
		
	public WaitingRoomScreen(Runner runner)
	{
		super(runner);	
		
		loadPlayer();
		//NotificationManager.prepareManager( player.getName(), player.getCurrentCharacter() );
		
		fadeInOnStart();
		
		if( !(WarpController.getInstance().isOnline) )
		{
			Logger.log(this, "Jeszcze nie byl online //waiting");
			//WarpController.getInstance().startApp( player.getName() );
		}
		Logger.log(this,  "ustawiam waiting room listener");
		WarpController.getInstance().setWaitingRoomListener(this);
		try {
			WarpClient.getInstance().joinRoomInRange(0, 4, false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/map.tmx", "", "", "0", GameWorldType.WILDWEST), enemyPlayers );
            }
        });
        
		enemyPlayers = new Array<Player>();
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
	public void dispose() 
	{
		super.dispose();
	}

	@Override
	public ScreenType getSceneType() 
	{
		return ScreenType.SCREEN_WAITING_ROOM;
	}

	@Override
	public void onGameUpdateReceived(String message) 
	{
		Logger.log(this, "³apiê w waitongroom");
		Logger.log(this, "Ja jestem: " + player.getName() + " przyszed³ update: " + message);
		try 
		{
			JSONObject data = new JSONObject(message);
			
			//jesli to przedstawienie sie
			if( data.has("INITIAL_NOTIFICATION") )
			{
				String enemyName = (String)data.getString("PLAYER_NAME");
				Logger.log(this, "jest to wiadomosc initial");
				boolean presentAlready = false;

				if( player.getName().equals(enemyName) )
				{
					Logger.log(this, "zaraz zaraz, przecie¿ ten gracz to ja. No nieŸle");
					presentAlready = true;
				}
					
				for(Player player : enemyPlayers){
					
					Logger.log(this, "porownuje: " + player.getName() + " oraz " + enemyName);
					
					if(player.getName().equals(enemyName)){
						Logger.log(this, "juz taki gracz sie raz przedstawial i jest na liscie");
						presentAlready = true;
						break;
					}
					else
						Logger.log(this, "nie mam takiego gracza na liscie jeszcze");
				}

				if( !presentAlready )			
				{
					CharacterType enemyCharacter = CharacterType.parseFromString( (String)data.getString("PLAYER_CHARACTER") );
					Player enemy = new Player(enemyName, enemyCharacter);

					enemyPlayers.add(enemy);
					addPlayer(enemy);
					
					NotificationManager.getInstance().screamMyName();
				}
				Logger.log(this, "ostatecznie takich mam graczy na swojej liscie: ");
				for(Player player : enemyPlayers){
					Logger.log(this, "gracz: " + player.getName());
				}
				if(enemyPlayers.size == 1){
					Logger.log(this, "OK JEST 2 GRACZY, ODPALAM!");
					WarpController.getInstance().startGame();
				}
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
	}
	
	private void addPlayer(Player player)
	{
		if( currentPlayersCount < 4 )
		{
			CharacterAnimation playerAnimation = CharacterType.convertToCharacterAnimation(player.getCharacterType(), playerAnimationPosition.get(currentPlayersCount).x, playerAnimationPosition.get(currentPlayersCount).y, true);
			
			Label playerNameLabel = createLabel(player.getName(), FontType.BIG);
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
				ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/wildwest1.tmx", "", "0", "0", GameWorldType.WILDWEST), enemyPlayers);
			
			}
		});
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
	}
}

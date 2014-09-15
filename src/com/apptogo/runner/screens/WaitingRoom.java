package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class WaitingRoom extends BaseScreen implements WarpListener
{			
	private Label label;
	
	private TextButton playButton;
	private Button backButton;
	
	private Array<Player> enemies;
	
	private float lastLabelY = 250f;
	
	public WaitingRoom(Runner runner)
	{
		super(runner);	
		
		loadPlayer();
		NotificationManager.prepareManager( player.getName(), player.getCurrentCharacter() );
		
		WarpController.getInstance().startApp( player.getName() );
		WarpController.getInstance().setListener(this);
	}
	
	public void prepare() 
	{		
		setBackground("ui/menuBackgrounds/mainMenuScreenBackground.png");
		
		backButton = new Button( skin, "back");
        backButton.setPosition( -580f, 240f );
        backButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
            }
         });
        
		enemies = new Array<Player>();
		
		playButton = new TextButton("PLAY", skin, "default");
		playButton.setPosition( -( playButton.getWidth() / 2.0f ), -300f);
		playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/map.tmx", "", GameWorldType.WILDWEST), player, enemies );
            }
         });
		
        addToScreen(playButton);
        addToScreen(backButton);
        
        addLabel(player.getName());
        
        //NotificationManager.getInstance().screamMyName();
	}
	
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() 
	{
		if( Gdx.input.isKeyPressed(Keys.ESCAPE) )
		{
			WarpController.getInstance().stopApp();
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
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
	public ScreenType getSceneType() {
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
				
				for(int i = 0; i < enemies.size; i++)
				{
					if( enemies.get(i).getName().equals(enemyName) ) 
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
									
					addLabel(enemyName);
					
					enemies.add(enemy);
					
					NotificationManager.getInstance().screamMyName();
				}
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
	}
	
	private void addLabel(String enemyName)
	{
		Label nameLabel = new Label(enemyName, skin, "default");
		nameLabel.setSize(300f, 60f);
		nameLabel.setPosition(-300f, lastLabelY - 20f);
		lastLabelY -= 80f;
		
		stage.addActor(nameLabel);
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

package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

public class WaitingRoom extends BaseScreen implements WarpListener{	
		
	private Label label;
	private TextButton playButton;
	private float lastLabelY = 300f;
	
	public WaitingRoom(Runner runner)
	{
		super(runner);		
		loadPlayer();
		WarpController.getInstance().startApp( player.getName() );
		WarpController.getInstance().setListener(this);
	}
	
	public void prepare() 
	{		
		setBackground("ui/menuBackgrounds/waitingRoomScreenBackground.png");
		
		playButton = new TextButton("PLAY", skin, "default");
		playButton.setPosition(-500f, -300f);
		playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
            	Array<CharacterType> characterTypes = new Array<CharacterType>();
            	//for(int i=0; i<players.size; i++)
            	//{
            	//	Player player = player.get(i);
            		characterTypes.add(player.getCurrentCharacter());            	
                //} --obslugujemy wszystkich podlaczonych
            		
            	ScreensManager.getInstance().createLoadingScreen( ScreenType.SCREEN_GAME_MULTI, new Level("", "gfx/game/levels/map.tmx", ""), characterTypes );
            }
         });
		
        addToScreen(playButton);
        
        addLabel(player.getName());
        NotificationManager.getInstance().screamMyName();
	}
	
	public void step()
	{
		handleInput();
	}
	
	@Override
	public void handleInput() {
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
				
				Logger.log(this, "ktos dolaczyl :) jego imie to: "+enemyName);
								
				addLabel(enemyName);
				
				NotificationManager.getInstance().screamMyName();
			}
		} 
		catch (JSONException e) { e.printStackTrace(); }
	}
	
	private void addLabel(String enemyName)
	{
		Label nameLabel = new Label(enemyName, skin, "default");
		nameLabel.setSize(300f, 60f);
		nameLabel.setPosition(-500f, lastLabelY - 20f);
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
				//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME_MULTI);
			}
		});
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
	}
}

package com.apptogo.runner.screens;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WaitingRoom extends BaseScreen implements WarpListener{	
		
	private Label label;
	private TextButton playButton;
	private float lastLabelY = 300f;
	
	public WaitingRoom(Runner runner)
	{
		super(runner);	
	}
	
	public void prepare() 
	{		
		setBackground("ui/menuBackgrounds/waitingRoomScreenBackground.png");
		
		playButton = new TextButton("PLAY", skin, "default");
		playButton.setPosition(-500f, -300f);
		playButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) 
            {
                 ScreensManager.getInstance().createLoadingScreen( new Level("", "gfx/game/levels/map.tmx", "") );
            }
         });
		
        addToScreen(playButton);
        
        addLabel(player.getName());
        NotificationManager.getInstance().screamMyName();
	}
	
	public void step()
	{

	}
	
	@Override
	public void handleInput() {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
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
				Player enemy = new Player();
				enemy.setName( enemyName );
				enemy.setCurrentCharacter(CharacterType.BANDIT);
				
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
	public void onGameStarted(String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
		
	}
}

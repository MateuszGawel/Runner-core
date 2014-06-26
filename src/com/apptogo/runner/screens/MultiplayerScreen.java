package com.apptogo.runner.screens;

import java.util.Random;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class MultiplayerScreen extends BaseScreen implements WarpListener{	
	
	private Stage stage;
	private StretchViewport viewport;
	private Label screenName;
	private Label message;
	private Image back;
	private Skin skin;
	private String playerName;
	
	public MultiplayerScreen(Runner runner){
		super(runner);	
		WarpController.getInstance().setListener(this);
		playerName = getRandomHexString(5);
    	WarpController.getInstance().startApp(playerName);
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		back = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/back.png")));
		back.setPosition(0, 0);
		back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "BACK CLICKED");
                 ScreensManager.getInstance().createMainMenuScreen();
             }
         });
		
		screenName = new Label("MULTIPLAYER ROOM", skin);
		screenName.setPosition(Runner.SCREEN_WIDTH/2 - screenName.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 200);
		
		message = new Label("HI " + playerName + "! WAITING FOR ANOTHER PLAYER...", skin);
		message.setPosition(Runner.SCREEN_WIDTH/2 - message.getWidth()/2, Runner.SCREEN_HEIGHT/2);
		
		stage.addActor(back);
		stage.addActor(screenName);
		stage.addActor(message);
		
		Gdx.input.setInputProcessor(stage);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
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
	
	private String getRandomHexString(int numchars){
		Random r = new Random();
		StringBuffer sb = new StringBuffer();
		while(sb.length() < numchars){
		    sb.append(Integer.toHexString(r.nextInt()));
		}
		return sb.toString().substring(0, numchars);
	}
	
	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_MULTIPLAYER;
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
		ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
	}

	@Override
	public void onGameFinished(int code, boolean isRemote) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameUpdateReceived(String message) {

	}


}
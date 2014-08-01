package com.apptogo.runner.screens;

import java.lang.reflect.Type;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Character.CharacterAbilityType;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.appwarp.WarpListener;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.levels.Level;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.player.SaveManager;
import com.apptogo.runner.world.GameWorld;
import com.apptogo.runner.world.GameWorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class GameScreenSingle extends BaseScreen{
	
	private GameWorld world;
	private GameWorldRenderer worldRenderer;
	
	private Level level;
	
	private Button jumpButton;
	private Button slideButton;
	private Button slowButton;
	private Button abilityButton;
	
	public GameScreenSingle(Runner runner){
		super(runner);	
		//WarpController.getInstance().setListener(this);
	}
	
	public void setLevel(Level level)
	{
		this.level = level;
	}
	
	public void prepare() 
	{	
		world = new GameWorld( level.mapPath, player );
		worldRenderer = new GameWorldRenderer(world);
		
		createGui();
		
		NotificationManager.getInstance().screamMyName();
	}
		
	private void createGui()
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
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_GAME_SINGLE;
	}
}

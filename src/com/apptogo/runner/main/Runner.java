package com.apptogo.runner.main;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Runner extends Game{

	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 480;

	@Override
	public void create() {	
		ScreensManager.prepareManager(this);
		ResourcesManager.prepareManager(this);
		ScreensManager.getInstance().createSplashScreen();
	}

	@Override
	public void render() {
		super.render();	
	}
	
	@Override
	public void dispose() {

	}
	

}

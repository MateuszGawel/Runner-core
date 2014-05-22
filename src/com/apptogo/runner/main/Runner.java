package com.apptogo.runner.main;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Runner extends Game{

	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 480;
	
	private SpriteBatch sb;
	private OrthographicCamera cam;	
	
	@Override
	public void create() {		
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
		sb = new SpriteBatch();
	
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
		sb.dispose();
	}

	
	public SpriteBatch getSpriteBatch() { return sb; }
	public OrthographicCamera getCamera() { return cam; }

}

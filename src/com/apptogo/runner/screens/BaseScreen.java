package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseScreen implements Screen{
	
	protected Runner runner;
	
	protected BaseScreen(Runner runner) {
		this.runner = runner;
	}
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	
}

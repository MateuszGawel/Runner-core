package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.LanguageManager;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class BaseScreen implements Screen{
	
	protected Runner runner;
	protected LanguageManager languageManager;
	
	protected BaseScreen(Runner runner) {
		this.runner = runner;
		this.languageManager = LanguageManager.getInstance();
		this.languageManager.setCurrentLanguage("en");
	}
	
	public abstract void handleInput();
	public abstract ScreenType getSceneType();
	public String getLangString(String key){ return languageManager.getString(key);	}
	
}

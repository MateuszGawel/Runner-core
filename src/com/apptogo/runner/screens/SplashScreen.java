package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Box2DVars;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen extends BaseScreen{	
	
	private Stage stage;
	private Viewport viewport;
	private Image splashImage;
	private float splashImageOpacity;
	private AlphaAction action;
	
	float percent;
	
	private TextButton button;
	private Skin skin;
	
	public SplashScreen(Runner runner){
		super(runner);	
	}
	
	@Override
	public void show() {
	
		ResourcesManager.getInstance().loadResources(this);
		ResourcesManager.getInstance().getAssetManager(this).finishLoading();

		splashImage = new Image(((Texture)ResourcesManager.getInstance().getResource(this, "gfx/splash/splash.png")));
		splashImage.setPosition( (Runner.SCREEN_WIDTH/Box2DVars.PPM)/2.0f - splashImage.getWidth()/2.0f, (Runner.SCREEN_HEIGHT/Box2DVars.PPM)/2.0f - splashImage.getHeight()/2.0f );
		splashImageOpacity = 0.0f;
		action = new AlphaAction();

		stage = new Stage();
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage.setViewport(viewport);
		
		stage.addActor(splashImage);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if( splashImageOpacity >= 2.0f ) ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_MAIN_MENU);
		
		splashImageOpacity += 0.025f;
		
		action.reset();
		action.setAlpha( ((splashImageOpacity>1.0f)?1.0f:splashImageOpacity) * (float)Math.sin(((splashImageOpacity>1.0f)?1.0f:splashImageOpacity)) );
		splashImage.addAction( action );
		
		stage.act();
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

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_SPLASH;
	}


}

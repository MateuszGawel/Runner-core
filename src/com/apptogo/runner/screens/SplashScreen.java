package com.apptogo.runner.screens;

import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen extends BaseScreen{	
    
	private Image splashTexture;
	private Image background;
	private Image frameBackground;
	private Image frame;
	private Image bar;
	private Image hidden;
	
	private Stage stage;
	private Viewport viewport;
	private Skin skin;
	
	private ProgressBar progressBar;
	
	float startX, endX, percent;
	
	public SplashScreen(Runner runner){
		super(runner);	
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        progressBar = new ProgressBar(0f, 1f, 0.001f, false, skin);

	}
	
	@Override
	public void show() {
		ResourcesManager.getInstance().loadSplashTextures();
		ResourcesManager.getInstance().getSplashManager().finishLoading();
		
		
		stage = new Stage();
		viewport = new FitViewport(runner.V_WIDTH, runner.V_HEIGHT, camera);
		
		/*
		splashTexture = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/splash.png"));
		background = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/background.png"));
		frameBackground = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/frame-background.png"));
		frame = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/frame.png"));
		bar = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/bar.png"));
		hidden = new Image((Texture)ResourcesManager.getInstance().getSplashResource("gfx/splash/hidden.png"));
		
		stage.addActor(splashTexture);
		stage.addActor(background);
		stage.addActor(frameBackground);
		stage.addActor(frame);
		stage.addActor(bar);
		*/
		stage.addActor(progressBar);
        ResourcesManager.getInstance().loadMenuMusic();
        ResourcesManager.getInstance().loadMenuSounds();
        ResourcesManager.getInstance().loadMenuTextures();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(ResourcesManager.getInstance().getMenuManager().update()) {
			ScreensManager.getInstance().createMenuScreen();
		}
		
		float progress = ResourcesManager.getInstance().getMenuManager().getProgress();
		Logger.log(this, "progress: " + progress);
		
		percent = Interpolation.linear.apply(percent, ResourcesManager.getInstance().getMenuManager().getProgress(), 0.1f);
		progressBar.setValue(percent);
		/*
		hidden.setX(startX + endX * percent);
		frameBackground.setX(hidden.getX());
		frameBackground.setWidth(450 - 450 * percent);
		frameBackground.invalidate();
		*/
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
		/*
		background.setSize(width, height);
		splashTexture.setPosition(runner.V_WIDTH/2 - splashTexture.getWidth()/2, runner.V_HEIGHT/2 - splashTexture.getHeight()/2 + 100);
		frame.setPosition(stage.getWidth() - frame.getWidth()/2, stage.getHeight() - frame.getHeight()/2 -100);
		bar.setPosition(frame.getX(), frame.getY());
		hidden.setPosition(bar.getX(), bar.getY());
		
		startX = hidden.getX();
		endX = 440;
		
		frameBackground.setSize(450, 50);
		frameBackground.setPosition(hidden.getX(), hidden.getY());
		*/
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

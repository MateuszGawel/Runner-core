package com.apptogo.runner.screens;



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


//ten screen bedzie do przepisania na scene2d ui
public class LevelChooseScreen extends BaseScreen{
	
	private Image levelOne;
	private Image levelTwo;
	private Image back;
	private Stage stage;
	private StretchViewport viewport;
	private Skin skin;
	private Label screenName;
	private Label levelOneName;
	private Label levelTwoName;
	
	public LevelChooseScreen(Runner runner){
		super(runner);	
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		levelOne = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/level1.png")));
		levelOne.setPosition(Runner.SCREEN_WIDTH/2 - levelOne.getWidth() - 50, Runner.SCREEN_HEIGHT/2 - 100);
		levelOne.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "LEVEL ONE CLICKED");
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
             }
         });

		levelTwo = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/level2.png")));
		levelTwo.setPosition(Runner.SCREEN_WIDTH/2 + 50, Runner.SCREEN_HEIGHT/2 - 100);
		levelTwo.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "LEVEL TWO CLICKED");
                 ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
             }
         });
		
		back = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/back.png")));
		back.setPosition(0, 0);
		back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "BACK CLICKED");
                 ScreensManager.getInstance().createMainMenuScreen();
             }
         });
		
		screenName = new Label("LEVEL CHOOSE", skin);
		screenName.setPosition(Runner.SCREEN_WIDTH/2 - screenName.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 200);
		
		levelOneName = new Label("GUMPY FOREST", skin);
		levelOneName.setPosition(levelOne.getX() + levelOne.getWidth()/2 - levelOneName.getWidth()/2, Runner.SCREEN_HEIGHT/2 - 150);
		
		levelTwoName = new Label("URBAN MADNESS", skin);
		levelTwoName.setPosition(levelTwo.getX() + levelTwo.getWidth()/2 - levelTwoName.getWidth()/2 , Runner.SCREEN_HEIGHT/2 - 150);
		
		stage.addActor(levelOne);
		stage.addActor(levelTwo);
		stage.addActor(back);
		stage.addActor(screenName);
		stage.addActor(levelOneName);
		stage.addActor(levelTwoName);
		
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

	@Override
	public ScreenType getSceneType() {
		return ScreenType.SCREEN_LEVEL_CHOOSE;
	}

}

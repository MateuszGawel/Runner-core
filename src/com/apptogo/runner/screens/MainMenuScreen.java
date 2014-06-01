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
public class MainMenuScreen extends BaseScreen{
	
	
	private Image singlePlayer;
	private Image multiPlayer;
	private Stage stage;
	private StretchViewport viewport;
	private Skin skin;
	private Label screenName;
	
	public MainMenuScreen(Runner runner){
		super(runner);	
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		singlePlayer = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/singlePlayer.png")));
		singlePlayer.setPosition(Runner.SCREEN_WIDTH/2 - singlePlayer.getWidth()/2, Runner.SCREEN_HEIGHT/2);
		singlePlayer.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "SINGLE CLICKED");
                 //ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
                 ScreensManager.getInstance().createCharacterChooseScreen(false);
             }
         });
		
		multiPlayer = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/multiPlayer.png")));
		multiPlayer.setPosition(Runner.SCREEN_WIDTH/2 - singlePlayer.getWidth()/2, Runner.SCREEN_HEIGHT/2 - 100);
		multiPlayer.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "MULTI CLICKED");
                 //ScreensManager.getInstance().createMultiplayerScreen();
                 ScreensManager.getInstance().createCharacterChooseScreen(true);
             }
         });
		
		screenName = new Label("MAIN MENU", skin);
		screenName.setPosition(Runner.SCREEN_WIDTH/2 - screenName.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 200);
		
		stage.addActor(singlePlayer);
		stage.addActor(multiPlayer);
		stage.addActor(screenName);
		
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
		return ScreenType.SCREEN_MAIN_MENU;
	}

}

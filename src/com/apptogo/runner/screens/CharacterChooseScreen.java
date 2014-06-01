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
public class CharacterChooseScreen extends BaseScreen{
	boolean multiplayer;
	
	private Image playerOne;
	private Image playerTwo;
	private Image back;
	private Stage stage;
	private StretchViewport viewport;
	private Skin skin;
	private Label screenName;
	private Label playerOneName;
	private Label playerTwoName;
	private Label multiplayerLabel;
	
	public CharacterChooseScreen(Runner runner, boolean multiplayer){
		super(runner);	
		this.multiplayer = multiplayer;
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		playerOne = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/player1.png")));
		playerOne.setPosition(Runner.SCREEN_WIDTH/2 - playerOne.getWidth() - 50, Runner.SCREEN_HEIGHT/2 - 100);
		playerOne.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "PLAYER ONE CLICKED");
                 if(!multiplayer)
                	 ScreensManager.getInstance().createLevelChooseScreen();
                 else
                	 ScreensManager.getInstance().createMultiplayerScreen();
            }
         });

		playerTwo = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/player2.png")));
		playerTwo.setPosition(Runner.SCREEN_WIDTH/2 + 50, Runner.SCREEN_HEIGHT/2 - 100);
		playerTwo.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "PLAYER TWO CLICKED");
                 if(!multiplayer)
                	 ScreensManager.getInstance().createLevelChooseScreen();
                 else
                	 ScreensManager.getInstance().createMultiplayerScreen();
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
		
		screenName = new Label("CHOOSE YOUR CHARACTER", skin);
		screenName.setPosition(Runner.SCREEN_WIDTH/2 - screenName.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 200);
		
		if(multiplayer)
			multiplayerLabel = new Label("MULTIPLAYER", skin);
		else
			multiplayerLabel = new Label("SINGLEPLAYER", skin);
		multiplayerLabel.setPosition(Runner.SCREEN_WIDTH/2 - multiplayerLabel.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 180);
		
		playerOneName = new Label("MASTER OF DISAESTER", skin);
		playerOneName.setPosition(playerOne.getX() + playerOne.getWidth()/2 - playerOneName.getWidth()/2, Runner.SCREEN_HEIGHT/2 - 150);
		
		playerTwoName = new Label("NAKED MAN", skin);
		playerTwoName.setPosition(playerTwo.getX() + playerTwo.getWidth()/2 - playerTwoName.getWidth()/2 , Runner.SCREEN_HEIGHT/2 - 150);
		
		stage.addActor(playerOne);
		stage.addActor(playerTwo);
		stage.addActor(back);
		stage.addActor(screenName);
		stage.addActor(multiplayerLabel);
		stage.addActor(playerOneName);
		stage.addActor(playerTwoName);
		
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
		return ScreenType.SCREEN_CHARACTER_CHOOSE;
	}

}

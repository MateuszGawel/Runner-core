package com.apptogo.runner.screens;


import static com.apptogo.runner.vars.Box2DVars.GameCharacter;
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
public class CharacterUpgradeScreen extends BaseScreen{
	boolean multiplayer;
	
	private Image choosenCharacterImage;
	private GameCharacter choosenCharacter;
	private Image back;
	private Image start;
	private Stage stage;
	private StretchViewport viewport;
	private Skin skin;
	private Label screenName;
	private Label multiplayerLabel;
	
	public CharacterUpgradeScreen(Runner runner, boolean multiplayer, GameCharacter choosenCharacter){
		super(runner);	
		this.choosenCharacter = choosenCharacter;
		this.multiplayer = multiplayer;
		viewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT);
		stage = new Stage(viewport);
	}
	
	@Override
	public void show() {
		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		
		choosenCharacterImage = new Image(((Texture)ResourcesManager.getInstance().getMenuResource(choosenCharacter)));
		choosenCharacterImage.setPosition(Runner.SCREEN_WIDTH/2 - choosenCharacterImage.getWidth()/2, Runner.SCREEN_HEIGHT/2 - 100);
		
		
		back = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/back.png")));
		back.setPosition(0, 0);
		back.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "BACK CLICKED");
                 ScreensManager.getInstance().createCharacterChooseScreen(multiplayer);
             }
         });
		
		start = new Image(((Texture)ResourcesManager.getInstance().getMenuResource("gfx/menu/start.png")));
		start.setPosition(Runner.SCREEN_WIDTH/2 - start.getWidth()/2, Runner.SCREEN_HEIGHT/2 - 200);
		start.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                 Logger.log(this, "START CLICKED");
                 if(!multiplayer)
                	 ScreensManager.getInstance().createLevelChooseScreen();
                 else
                	 ScreensManager.getInstance().createMultiplayerScreen();
             }
         });
		
		screenName = new Label("UPGRADE YOUR CHARACTER", skin);
		screenName.setPosition(Runner.SCREEN_WIDTH/2 - screenName.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 200);
		
		if(multiplayer)
			multiplayerLabel = new Label("MULTIPLAYER", skin);
		else
			multiplayerLabel = new Label("SINGLEPLAYER", skin);
		multiplayerLabel.setPosition(Runner.SCREEN_WIDTH/2 - multiplayerLabel.getWidth()/2, Runner.SCREEN_HEIGHT/2 + 180);
		
		stage.addActor(back);
		stage.addActor(choosenCharacterImage);
		stage.addActor(screenName);
		stage.addActor(multiplayerLabel);
		stage.addActor(start);
		
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

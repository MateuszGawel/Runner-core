package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.apptogo.runner.actors.Player;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameWorld {

	public static final float WIDTH = Runner.SCREEN_WIDTH / PPM;
	public static final float HEIGHT = Runner.SCREEN_HEIGHT / PPM;
	public static final float WORLD_STEP = 1/60f;
	public static final Vector2 GRAVITY = new Vector2(0f, -9.2f);
	
	public World world;
	public Stage stage;
	public StretchViewport stretchViewport;
	public Player player;
	public OrthographicCamera camera;
	
	public RayHandler rayHandler;
	
	public GameWorld(){
		world = new World(GRAVITY, true);
		stage = new Stage();
		stretchViewport = new StretchViewport(WIDTH, HEIGHT, stage.getCamera());
		stage.setViewport(stretchViewport);
		camera = (OrthographicCamera)stage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
	
		createWorld();
	}
	
	private void createWorld(){
		player = new Player(world);
		
		stage.addActor(player);
		
		TiledMapLoader.getInstance().setWorld(world);
		TiledMapLoader.getInstance().loadMap("gfx/game/levels/map.tmx");
		
		rayHandler = new RayHandler(world);
		//rayHandler.setCombinedMatrix(camera.combined);
		
		new PointLight(rayHandler, 5000, Color.CYAN, 20, 400 / PPM, 240 / PPM);
	}
	
	public void handleInput(){
		if(Input.isPressed(Input.RIGHT)) {
			Logger.log(this, "PRAWO");
			player.jump();
		}
		if(Input.isPressed(Input.LEFT)) {
			Logger.log(this, "LEWO");
			player.startRunning();
		}
		

		if(Input.isPressed()) {
			if(Input.x < Gdx.graphics.getWidth() / 2) {
				Logger.log(this, "LEWO");
				player.startRunning();
			}
			else {
				Logger.log(this, "PRAWO");
				player.jump();
			}
		}
	}
	
    public void update(float delta) {  
        world.step(delta, 3, 3);
        stage.act(delta);
    }  
}

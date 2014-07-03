package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import box2dLight.RayHandler;

import com.apptogo.runner.actors.Enemy;
import com.apptogo.runner.actors.Player;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyContactListener;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.main.Runner;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameWorld {

	public static final float WIDTH = Runner.SCREEN_WIDTH / PPM;
	public static final float HEIGHT = Runner.SCREEN_HEIGHT / PPM;
	public static final float WORLD_STEP = 1/60f;
	public static final Vector2 GRAVITY = new Vector2(0f, -60f);
	
	public World world;
	
	public Stage worldStage;
	public StretchViewport viewport;
	public OrthographicCamera camera;
	public float minCameraX;
	public float maxCameraX;
	public float minCameraY;
	public float maxCameraY;
    
	private Stage backgroundStage;
	public StretchViewport backgroundStretchViewport;
	private OrthographicCamera backgroundCamera;

	public Player player;
	public Enemy enemy;
	
	public Group background;
	public Image mountains;
	public ParallaxBackground rocks;
	public Image skyBlue;
	public Actor sand;
	
	private Vector2 mapSize;
	
	public RayHandler rayHandler;
	
	public GameWorld(){
		world = new World(GRAVITY, true);
		world.setContactListener(new MyContactListener(this));
		
		worldStage = new Stage();
		viewport = new StretchViewport(WIDTH, HEIGHT, worldStage.getCamera());
		worldStage.setViewport(viewport);
		camera = (OrthographicCamera)worldStage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		minCameraX = camera.zoom * (camera.viewportWidth / 2); 
	    minCameraY = camera.zoom * (camera.viewportHeight / 2);
	    

		background = new Group();
		backgroundStage = new Stage();
		backgroundCamera = (OrthographicCamera) backgroundStage.getCamera();  
		backgroundStretchViewport = new StretchViewport(Runner.SCREEN_WIDTH, Runner.SCREEN_HEIGHT, backgroundCamera);
		backgroundStage.setViewport(backgroundStretchViewport);
		
		createWorld();
	}
	
	private void createWorld(){
		backgroundStage.addActor(background);

		player = new Player(world);
		worldStage.addActor(player);
		
		TiledMapLoader.getInstance().setWorld(world);
		mapSize = TiledMapLoader.getInstance().loadMap("gfx/game/levels/map.tmx");
		maxCameraX = (mapSize.x - minCameraX)/PPM - camera.viewportWidth/2;
		maxCameraY = (mapSize.y - minCameraY)/PPM - camera.viewportWidth/2;
		createBackground();
		rayHandler = TiledMapLoader.getInstance().getRayHandler();
	}
	
	private void createBackground(){

		skyBlue = new Image((Texture)ResourcesManager.getInstance().getGameResource("gfx/game/levels/skyBlue.png"));
		skyBlue.setPosition(0, 500);
		background.addActor(skyBlue);
		
		mountains = new ParallaxBackground((Texture)ResourcesManager.getInstance().getGameResource("gfx/game/levels/mountains.png"), mapSize, -0.05f, player, 0, 350);
		background.addActor(mountains);
		
		rocks = new ParallaxBackground((Texture)ResourcesManager.getInstance().getGameResource("gfx/game/levels/rocks.png"), mapSize, -0.1f, player, 0, 400);
		background.addActor(rocks);
		
		sand = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getGameResource("gfx/game/levels/sand.png"), -0.5f, -0.15f, mapSize, player, 0, 80);
		background.addActor(sand);
	}
	
	public void handleInput(){
		if(Input.isDown(Input.RIGHT)) {
			player.jump();
		}
		if(Input.isDown(Input.LEFT)) {
			player.slide();
		}
		if(Input.isReleased(Input.LEFT)) {
			player.standUp();
		}
		if(Input.isPressed(Input.START)) {
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
        worldStage.act(delta);
        
		backgroundCamera.position.set(Runner.SCREEN_WIDTH/2, Runner.SCREEN_HEIGHT/2, 0); 
		backgroundCamera.update();
		backgroundStage.act(delta);
		backgroundStage.draw();
    }  
}

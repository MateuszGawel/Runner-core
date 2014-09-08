package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import box2dLight.RayHandler;

import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyContactListener;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameWorld 
{

	public static final float WIDTH = Runner.SCREEN_WIDTH / PPM;
	public static final float HEIGHT = Runner.SCREEN_HEIGHT / PPM;
	public static final float WORLD_STEP = 1/60f;
	public static final Vector2 DEFAULT_GRAVITY = new Vector2(0f, -60f);
	
	public World world;
	
	public Stage worldStage;
	public StretchViewport viewport;
	public OrthographicCamera camera;
	public float minCameraX;
	public float maxCameraX;
	public float minCameraY;
	public float maxCameraY;
    
	public Stage backgroundStage;
	public StretchViewport backgroundStretchViewport;
	public OrthographicCamera backgroundCamera;
	
	private Player player;
	public Character character;
	public HashMap<String, Character> enemies;
	public Group background;
	protected Vector2 mapSize;
	private MyContactListener contactListener;
	public RayHandler rayHandler;
	public FPSLogger fpsLogger; //odkomentuj linijke w update() aby uruchomic
	
	public GameWorld(String mapPath, Player player)
	{
		world = new World(DEFAULT_GRAVITY, true);
		contactListener = new MyContactListener(this);
		world.setContactListener(contactListener);
		
		worldStage = new Stage();
		camera = (OrthographicCamera)worldStage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new StretchViewport(WIDTH, HEIGHT, camera);
		worldStage.setViewport(viewport);
		minCameraX = camera.zoom * (camera.viewportWidth / 2); 
	    minCameraY = camera.zoom * (camera.viewportHeight / 2);
		
		this.enemies = new HashMap<String, Character>();
		this.player = player;
		
		background = new Group();
		backgroundStage = new Stage();
		backgroundCamera = (OrthographicCamera) backgroundStage.getCamera(); 
		backgroundCamera.setToOrtho(false, WIDTH, HEIGHT);
		backgroundStretchViewport = new StretchViewport(WIDTH, HEIGHT, backgroundCamera);
		backgroundStage.setViewport(backgroundStretchViewport);
		backgroundStage.addActor(background);
		createWorld(mapPath, player.getCurrentCharacter());
		
		fpsLogger = new FPSLogger();
	}
	
	private void createWorld(String mapPath, CharacterType characterType)
	{
		character = createCharacter(characterType);
		worldStage.addActor(character);
		
		TiledMapLoader.getInstance().setWorld(world);
		TiledMapLoader.getInstance().setGameWorld(this);
		TiledMapLoader.getInstance().loadMap(mapPath);
		
		mapSize = TiledMapLoader.getInstance().getMapSize();
		maxCameraX = (mapSize.x - minCameraX)/PPM - camera.viewportWidth/2;
		maxCameraY = (mapSize.y - minCameraY)/PPM - camera.viewportWidth/2;
		rayHandler = TiledMapLoader.getInstance().getRayHandler();
	}
	
	public void destroyWorld()
	{
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for(Body body: bodies)
		{
			world.destroyBody(body);
		}
	}
	
	public void handleInput()
	{
		if( Input.isPressed() ) 
		{
			if( character.start() ) NotificationManager.getInstance().notifyStartRunning();
		}
	}
	
    public void update(float delta) {  
        world.step(delta, 3, 3);

		backgroundStage.act(delta);
        worldStage.act(delta);
        contactListener.postStep();
       // fpsLogger.log();
    }  
    
    public Stage getWorldStage(){ return this.worldStage; }
    
    
    /** ta i metode getEnemy() nalezy obstawic wyjatkami */
    public void addEnemy(Player enemy) //na razie przesylanie Playera jest mocno nadmiarowe ale bd konieczne gdy zaczniemy dodawac umiejetnosci itp
    {
    	if( enemies.containsKey( enemy.getName() ) )
		{
    		Logger.log(this, "tu powinien byc wyjatek, ale generalnie gosc juz jest dodany ;)");
		}
    	else //dodajemy wroga
    	{
    		Character enemyCharacter = createCharacter( enemy.getCurrentCharacter() );
    		enemies.put(enemy.getName(), enemyCharacter);
    		
    		worldStage.addActor(enemyCharacter);
    	}
    }
    
    public Character getEnemy(String enemyName)
    {
    	Character characterTemp;
    	
    	characterTemp = enemies.get(enemyName); //zwroci nulla jesli nie ma!
    	
    	return characterTemp;
    }
    
    private Character createCharacter(CharacterType characterType)
    {
    	return CharacterType.convertToCharacter(characterType, world, this);
    }
}

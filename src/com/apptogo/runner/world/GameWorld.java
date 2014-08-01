package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.HashMap;

import box2dLight.RayHandler;

import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Character.CharacterType;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.MyContactListener;
import com.apptogo.runner.handlers.NotificationManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.badlogic.gdx.graphics.FPSLogger;
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
    
	public Stage backgroundStage;
	public StretchViewport backgroundStretchViewport;
	public OrthographicCamera backgroundCamera;
	
	private Player player;
	public Character character;
	public HashMap<String, Character> enemies;
	
	public Group background;
	public Image mountains;
	public ParallaxBackground rocks;
	public Image skyBlue;
	public Actor sand;
	
	private Vector2 mapSize;
	
	public RayHandler rayHandler;
	public FPSLogger fpsLogger; //odkomentuj linijke w update() aby uruchomic
	
	public GameWorld(String mapPath, Player player)
	{
		world = new World(GRAVITY, true);
		world.setContactListener(new MyContactListener(this));
		
		worldStage = new Stage();
		camera = (OrthographicCamera)worldStage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new StretchViewport(WIDTH, HEIGHT, camera);
		worldStage.setViewport(viewport);
		minCameraX = camera.zoom * (camera.viewportWidth / 2); 
	    minCameraY = camera.zoom * (camera.viewportHeight / 2);
	    

		background = new Group();
		backgroundStage = new Stage();
		backgroundCamera = (OrthographicCamera) backgroundStage.getCamera(); 
		backgroundCamera.setToOrtho(false, WIDTH, HEIGHT);
		backgroundStretchViewport = new StretchViewport(WIDTH, HEIGHT, backgroundCamera);
		backgroundStage.setViewport(backgroundStretchViewport);	
		
		this.enemies = new HashMap<String, Character>();
		this.player = player;

		createWorld(mapPath, player.getCurrentCharacter());
		
		fpsLogger = new FPSLogger();
	}
	
	private void createWorld(String mapPath, CharacterType characterType)
	{
		backgroundStage.addActor(background);
		
		character = createCharacter(characterType);
		worldStage.addActor(character);
		
		TiledMapLoader.getInstance().setWorld(world);
		TiledMapLoader.getInstance().setGameWorld(this);
		mapSize = TiledMapLoader.getInstance().loadMap(mapPath);
		maxCameraX = (mapSize.x - minCameraX)/PPM - camera.viewportWidth/2;
		maxCameraY = (mapSize.y - minCameraY)/PPM - camera.viewportWidth/2;
		createBackground();
		rayHandler = TiledMapLoader.getInstance().getRayHandler();
	}
	
	private void createBackground(){

		skyBlue = new Image((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/levels/skyBlue.png"));
		skyBlue.setPosition(0, 500/PPM);
		background.addActor(skyBlue);
		
		mountains = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/levels/mountains.png"), mapSize, -0.05f, character, 0, 350/PPM);
		background.addActor(mountains);
		
		rocks = new ParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/levels/rocks.png"), mapSize, -0.1f, character, 0, 400/PPM);
		background.addActor(rocks);
		
		sand = new RepeatingParallaxBackground((Texture)ResourcesManager.getInstance().getResource(ScreenType.SCREEN_GAME, "gfx/game/levels/sand.png"), -0.5f, -0.15f, mapSize, character, 0, 80/PPM);
		background.addActor(sand);
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
        fpsLogger.log();
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
    	Character characterTemp = null;
    	
    	if( characterType == CharacterType.BANDIT ) characterTemp = new Bandit(world);
    	else if( characterType == CharacterType.ARCHER ) characterTemp = new Archer(world);
    	
    	return characterTemp;
    }
}

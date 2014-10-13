package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import box2dLight.RayHandler;

import com.apptogo.runner.actors.Character;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.controller.Input;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.exception.PlayerExistsException;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.listeners.MyContactListener;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.userdata.UserData;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public abstract class GameWorld 
{
	public static final float WIDTH = Runner.SCREEN_WIDTH / PPM;
	public static final float HEIGHT = Runner.SCREEN_HEIGHT / PPM;
	public static final float WORLD_STEP = 1/60f;
	public static final Vector2 DEFAULT_GRAVITY = new Vector2(0f, -60f);
	
	public World world;
	
	Array<Body> bodiesToDestroy;
	
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
	
	public Player player;
	public Array<Player> enemies;
	public Group background;
	protected Vector2 mapSize;
	private MyContactListener contactListener;
	public RayHandler rayHandler;
	public FPSLogger fpsLogger; //odkomentuj linijke w update() aby uruchomic
	
	public GameWorld(String mapPath, Player player)
	{
		world = new World(DEFAULT_GRAVITY, true);
		contactListener = new MyContactListener(this, world);
		world.setContactListener(contactListener);
		
		bodiesToDestroy = new Array<Body>();
		
		worldStage = new Stage();
		camera = (OrthographicCamera)worldStage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new StretchViewport(WIDTH, HEIGHT, camera);
		worldStage.setViewport(viewport);
		minCameraX = camera.zoom * (camera.viewportWidth / 2); 
	    minCameraY = camera.zoom * (camera.viewportHeight / 2);
		
		this.enemies = new Array<Player>();
		this.player = player;
		
		background = new Group();
		backgroundStage = new Stage();
		backgroundCamera = (OrthographicCamera) backgroundStage.getCamera(); 
		backgroundCamera.setToOrtho(false, WIDTH, HEIGHT);
		backgroundStretchViewport = new StretchViewport(WIDTH, HEIGHT, backgroundCamera);
		backgroundStage.setViewport(backgroundStretchViewport);
		backgroundStage.addActor(background);
		
		createWorld(mapPath);
		
		fpsLogger = new FPSLogger();
	}
	
	public abstract void dispose();
	
	private void createWorld(String mapPath)
	{
		this.player.character = createCharacter( this.player.getCharacterType() );
		((UserData)this.player.character.getBody().getUserData()).playerName = this.player.getName();
		((UserData)this.player.character.getBody().getUserData()).me = true;
		this.player.character.setMe(true);
		
		worldStage.addActor( this.player.character );
		
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
		world.clearForces();
		
		Array<Body> bodies = new Array<Body>();
		world.getBodies(bodies);
		
		for(Body body: bodies)
		{
			world.destroyBody(body);
		}
		
		Array<Joint> joints = new Array<Joint>();
		world.getJoints(joints);
		
		for(Joint joint: joints)
		{
			world.destroyJoint(joint);
		}
	}
	
	public void handleInput()
	{
		if( Input.isPressed() ) 
		{
			if( this.player.character.start() ) 
				NotificationManager.getInstance().notifyStartRunning(this.player.character.getBody().getPosition());
		}
	}
	
    public void update(float delta) 
    {  
        world.step(delta, 3, 3);
        
        handleBodyToDestroy();

		backgroundStage.act(delta);
        worldStage.act(delta);
        contactListener.postStep();
       // fpsLogger.log();
    }  
    
    public void addBodyToDestroy(Body bodyToDestroy)
    {
    	bodiesToDestroy.add(bodyToDestroy);
    }
    
    private void handleBodyToDestroy()
    {
    	for(Body body: bodiesToDestroy)
    	{Logger.log(this, "BANG");
    		//for(Fixture fixture: body.getFixtureList())
    		//{
    			//body.destroyFixture(fixture);
    		//}

    		//world.destroyBody(body);
    	}
    	
    	bodiesToDestroy.clear();
    }
    
    public Stage getWorldStage(){ return this.worldStage; }
    
    
    /** ta i metode getEnemy() nalezy obstawic wyjatkami 
     * @throws PlayerExistsException */
    public void addEnemy(Player enemy) throws PlayerExistsException //na razie przesylanie Playera jest mocno nadmiarowe ale bd konieczne gdy zaczniemy dodawac umiejetnosci itp
    {
    	for(Player enemyPlayer: enemies)
    	{
    		if( enemyPlayer.equals( enemy.getName() ) )
    		{
    			throw new PlayerExistsException();
    		}
    	}
    	
		enemy.character = createCharacter( enemy.getCharacterType() );
		((UserData)enemy.character.getBody().getUserData()).playerName = enemy.getName();
		enemies.add( enemy );
		
		worldStage.addActor( enemy.character );
    }
    
    public Player getEnemy(String enemyName) throws PlayerDoesntExistException //tak naprawde tu tez ladniej by bylo przesylac calego playera ale to chyba troche kosztuje wiec zdecydowalem ze samo imie
    {
    	for(Player enemyPlayer: enemies)
    	{
    		if( enemyPlayer.getName().equals( enemyName ) )
    		{
    			return enemyPlayer;
    		}
    	}
    	
    	throw new PlayerDoesntExistException();
    }
    
    private Character createCharacter(CharacterType characterType)
    {
    	return CharacterType.convertToCharacter(characterType, world, this);
    }
}

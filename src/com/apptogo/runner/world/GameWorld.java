package com.apptogo.runner.world;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.actors.Alien;
import com.apptogo.runner.actors.Archer;
import com.apptogo.runner.actors.Bandit;
import com.apptogo.runner.actors.Barrel;
import com.apptogo.runner.actors.BodyMember;
import com.apptogo.runner.actors.Bomb;
import com.apptogo.runner.actors.Bonfire;
import com.apptogo.runner.actors.Character;
import com.apptogo.runner.actors.Coin;
import com.apptogo.runner.actors.CoinField;
import com.apptogo.runner.actors.Countdown;
import com.apptogo.runner.actors.GameProgressBarHead;
import com.apptogo.runner.actors.ParticleEffectActor;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.exception.PlayerDoesntExistException;
import com.apptogo.runner.exception.PlayerExistsException;
import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.listeners.MyContactListener;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.player.Player;
import com.apptogo.runner.userdata.UserData;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameWorld 
{
	public static final float WIDTH = Runner.SCREEN_WIDTH / PPM;
	public static final float HEIGHT = Runner.SCREEN_HEIGHT / PPM;
	public static final float WORLD_STEP = 1/60f;
	public static final Vector2 DEFAULT_GRAVITY = new Vector2(0f, -60f);
	
	public World world;
	Array<Body> bodiesToDestroy;
	
	public Stage worldStage;
	public Group worldBackgroundGroup;
	public Viewport viewport;
	public OrthographicCamera camera;
	public float minCameraX;
	public float maxCameraX;
	public float minCameraY;
	public float maxCameraY;
	
	private Stage backgroundStage;
	public Viewport backgroundViewport;
	public OrthographicCamera backgroundCamera;
	
	public Player player;
	public Array<Player> enemies;
	public Group background;
	public Vector2 mapSize;
	private MyContactListener contactListener;
//	public RayHandler rayHandler;
	public FPSLogger fpsLogger; //odkomentuj linijke w update() aby uruchomic
	
	public Music music;
	
	private Array<Integer> availablePosition; //UWAGA TEN MECHANIZM MUSI BYC PRZEROBIONY NA MULTI> MUSI KORZYSTAC Z NOTYFIKACJI
	private Random randomGenerator = new Random();
	
	public GameWorld(String mapPath, Player player)
	{
		world = new World(DEFAULT_GRAVITY, true);
		contactListener = new MyContactListener(this);
		world.setContactListener(contactListener);
		
		bodiesToDestroy = new Array<Body>();
		
		worldStage = new Stage();
		camera = (OrthographicCamera)worldStage.getCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		viewport = new FillViewport(WIDTH, HEIGHT);
		worldStage.setViewport(viewport);
		worldStage.setDebugAll(false);
		
		minCameraX = camera.zoom * (camera.viewportWidth / 2); 
	    minCameraY = camera.zoom * (camera.viewportHeight / 2);
	    worldBackgroundGroup = new Group();
		
		this.enemies = new Array<Player>();
		this.player = player;
		
		background = new Group();
		backgroundStage = new Stage();
		backgroundCamera = (OrthographicCamera) backgroundStage.getCamera();  
		backgroundCamera.setToOrtho(false, WIDTH, HEIGHT);
		backgroundViewport = new FillViewport(WIDTH, HEIGHT, backgroundCamera);
		backgroundStage.setViewport(backgroundViewport);
		backgroundStage.addActor(background);

		availablePosition = new Array<Integer>();
		availablePosition.add(0);
		availablePosition.add(1);
		availablePosition.add(2);
		availablePosition.add(3);
		
		createWorld(mapPath);
		fpsLogger = new FPSLogger();
	}
	
	
	public void dispose(){
		TiledMapLoader.getInstance().getPlayersPosition().clear();
		music.stop();
		music.dispose();
		music = null;
	}
	
	private void createWorld(String mapPath)
	{	
		TiledMapLoader.getInstance().setWorld(world);
		TiledMapLoader.getInstance().setGameWorld(this);
		TiledMapLoader.getInstance().loadMap(mapPath);
		
		mapSize = TiledMapLoader.getInstance().getMapSize();
		maxCameraX = (mapSize.x - minCameraX)/PPM - camera.viewportWidth/2;
		maxCameraY = (mapSize.y - minCameraY)/PPM - camera.viewportWidth/2;
//		rayHandler = TiledMapLoader.getInstance().getRayHandler();
		
		int index = randomGenerator.nextInt(availablePosition.size);
		int randomPosition = availablePosition.get(index);
		availablePosition.removeIndex(index);
		
		this.player.character = createCharacter( this.player.getCharacterType(), randomPosition, this.player.getName());
		((UserData)this.player.character.getBody().getUserData()).playerName = this.player.getName();
		this.player.character.flags.setMe(true);
		
		worldStage.addActor( this.player.character );
		
		CoinsManager.getInstance().createCoinsToPool(100);
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
	
	boolean bdsA = false;
	public Body groundBody;
	
	private void handleBodyCulling(){
		if(!bdsA || bdsA)
    	{
    		bdsA = true;
    		Array<Body> bs = new Array<Body>();
    		world.getBodies(bs);
    		
    		Body tempB = this.player.character.getBody();
    		int ciala = 0; int ciali = 0;
    		for(Body b : bs) 
    		{
    			if( Math.abs( tempB.getPosition().x - b.getPosition().x ) < 5 )
				{ciala++;
					b.setActive(true);
				}
    			else
    			{ciali++;
    				b.setActive(false);
    			}
    		}
    		
    		this.player.character.getBody().setActive(true);
    		groundBody.setActive(true);
    	}
	}
	
    public void update(float delta) 
    {
    	handleBodyCulling();
        world.step(delta, 3, 3);
        backgroundStage.act(delta);
        worldBackgroundGroup.act(delta);
        worldStage.act(delta);
		
        contactListener.postStep();
        fpsLogger.log();
        
    }  
    
    public Stage getWorldStage(){ return this.worldStage; }
    public Stage getBackgroundStage(){ return this.backgroundStage; }
    
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
    	
		int index = randomGenerator.nextInt(availablePosition.size);
		int randomPosition = availablePosition.get(index);
		availablePosition.removeIndex(index);
		enemy.character = createCharacter( enemy.getCharacterType(), randomPosition, enemy.getName());
		
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
    
    public Player getEnemyNumber(int index){
    	return this.enemies.get(index);
    }
    
    private Character createCharacter(CharacterType characterType, int startingPosition, String playerName)
    {
    	return CharacterType.convertToCharacter(characterType, world, this, startingPosition, playerName);
    }
}

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
		printAverages();
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
		
		CoinsManager.getInstance().createCoinsToPool(1);
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
	

	private Array<Long> actWorldStep = new Array<Long>();
	private Array<Long> actBackground = new Array<Long>();
	private Array<Long> actBackgroundGroup = new Array<Long>();
	private Array<Long> actWorldStage = new Array<Long>();
	public Array<Long> drawCamera = new Array<Long>();
	public Array<Long> drawBackground = new Array<Long>();
	public Array<Long> drawTiled = new Array<Long>();
	public Array<Long> drawWorldStage = new Array<Long>();
	
	public Array<Long> alienArray = new Array<Long>();
	public Array<Long> archerArray = new Array<Long>();
	public Array<Long> banditArray = new Array<Long>();
	public Array<Long> barrelArray = new Array<Long>();
	public Array<Long> bodyMemberArray = new Array<Long>();
	public Array<Long> bombArray = new Array<Long>();
	public Array<Long> bonfireArray = new Array<Long>();
	public Array<Long> coinArray = new Array<Long>();
	public Array<Long> coinFieldArray = new Array<Long>();
	public Array<Long> countdown = new Array<Long>();
	public Array<Long> gameProgressBarHead = new Array<Long>();
	public Array<Long> particleEffectActorArray = new Array<Long>();
	
	private long countAverage(Array<Long> array){
		long sum = 0;
		for(long value : array){
			sum+=value;
		}
		if(array.size != 0)
			return (sum/array.size);
		else
			return -1;
	}
	private long countSum(Array<Long> array){
		long sum = 0;
		for(long value : array){
			sum+=value;
		}
		return sum;
	}
	
	public void printAverages(){
		Logger.log(this, "ACT WORLD STEP: " + countSum(actWorldStep));
		Logger.log(this, "ACT BACKGROUND STAGE: " + countSum(actBackground));
		Logger.log(this, "ACT BACKGROUND GROUP: " + countSum(actBackgroundGroup));
		Logger.log(this, "ACT WORLD STAGE: " + countSum(actWorldStage));
		
		Logger.log(this, "DRAW CAMERA: " + countSum(drawCamera));
		Logger.log(this, "DRAW BACKGROUND: " + countSum(drawBackground));
		Logger.log(this, "DRAW TILED MAP: " + countSum(drawTiled));
		Logger.log(this, "DRAW WORLD STAGE: " + countSum(drawWorldStage));	
				
		Logger.log(this, "ACTOR Alien: " + countSum(alienArray));
		Logger.log(this, "ACTOR Archer: " + countSum(archerArray));
		Logger.log(this, "ACTOR Bandit: " + countSum(banditArray));
		Logger.log(this, "ACTOR Barrel: " + countSum(barrelArray));
		Logger.log(this, "ACTOR BodyMember: " + countSum(bodyMemberArray));
		Logger.log(this, "ACTOR Bomb: " + countSum(bombArray));
		Logger.log(this, "ACTOR Bonfire: " + countSum(bonfireArray));
		Logger.log(this, "ACTOR Coin: " + countSum(coinArray));
		Logger.log(this, "ACTOR CoinField: " + countSum(coinFieldArray));
		Logger.log(this, "ACTOR Countdown: " + countSum(countdown));
		Logger.log(this, "ACTOR GameProgressBarHead: " + countSum(gameProgressBarHead));
		Logger.log(this, "ACTOR ParticleEffectActor: " + countSum(particleEffectActorArray));

		int otherActor = 0;
        for(Actor actor : worldStage.getActors()){
        	if(actor instanceof Alien)
        		continue;
        	else if(actor instanceof Archer)
        		continue;
        	else if(actor instanceof Bandit)
        		continue;
        	else if(actor instanceof Barrel)
        		continue;
        	else if(actor instanceof BodyMember)
        		continue;
        	else if(actor instanceof Bomb)
        		continue;
        	else if(actor instanceof Bonfire)
        		continue;
        	else if(actor instanceof Coin)
        		continue;
        	else if(actor instanceof CoinField)
        		continue;
        	else if(actor instanceof Countdown)
        		continue;
        	else if(actor instanceof GameProgressBarHead)
        		continue;
        	else if(actor instanceof ParticleEffectActor)
        		continue;
        	else 
        		otherActor++;
        }
        Logger.log(this, "other actors: " + otherActor);
	}
	
    public void update(float delta) 
    {
    	long startTime = System.nanoTime();
        world.step(delta, 3, 3);
    	long endTime = System.nanoTime();
    	actWorldStep.add(endTime - startTime);
    	
    	startTime = System.nanoTime();
        backgroundStage.act(delta);
    	endTime = System.nanoTime();
    	actBackground.add(endTime - startTime);
    	
    	startTime = System.nanoTime();
        worldBackgroundGroup.act(delta);
    	endTime = System.nanoTime();
    	actBackgroundGroup.add(endTime - startTime);
    	
    	startTime = System.nanoTime();
        worldStage.act(delta);
    	endTime = System.nanoTime();
    	actWorldStage.add(endTime - startTime);
		
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

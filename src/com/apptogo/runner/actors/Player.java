package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.animators.PlayerAnimator;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Player extends Actor{

	private World world;
	private PlayerAnimationState currentAnimationState;
	private Body playerBody;
	private PlayerAnimator playerAnimator;
	private TextureRegion currentFrame;
	private float playerSpeed;
	private float playerMaxSpeed = 8;
	private float playerJumpHeight;
	private int jumpSensor;
	private Vector2 playerBodySize;
	private Vector2 deathPosition;
	private Player player = this;
	
    private final Array<Bomb> activeBombs = new Array<Bomb>();

    private final Pool<Bomb> bombsPool = new Pool<Bomb>() {
	    @Override
	    protected Bomb newObject() {
	    	Bomb bomb = new Bomb(player, world);
	    	player.getStage().addActor(bomb);
	    	return bomb;
	    }
    };
    
	//flags
	private boolean alive;
	private boolean inAir;
	private boolean sliding;
	private boolean immortal;
	private boolean blinking;
	private boolean visible;
	private boolean running;
	
	public Player(World world){
		this.world = world;
		playerAnimator = new PlayerAnimator(this);
		currentAnimationState = PlayerAnimationState.IDLE;
		jumpSensor = 0;
		alive = true;
		blinking = false;
		visible = true;
		playerJumpHeight = 4;
		createPlayerBody();
        setOrigin(0, 0);
	}
	
	public enum PlayerAnimationState{
		IDLE, RUNNING, JUMPING, DIEINGTOP, DIEINGBOTTOM, CROUCHING, MOONWALKING, LANDING, FLYING, BEGINSLIDING, SLIDING, STANDINGUP, FLYBOMB, RUNBOMB
	}
	
	private void createPlayerBody(){
		playerBodySize = new Vector2(25 / PPM, 65 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef;
		
		playerBody = world.createBody(bodyDef);
		playerBody.setUserData("player");
		
		//main fixture
		shape.setAsBox(playerBodySize.x, playerBodySize.y);
		fixtureDef = Materials.playerBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
		
		//sliding fixture
		shape.setAsBox(playerBodySize.y - 15/PPM, playerBodySize.x, new Vector2(-playerBodySize.x, -40/PPM), 0);
		fixtureDef = Materials.playerSlidingBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
		playerBody.getFixtureList().get(1).setSensor(true);
		/*
		//wall sensor
		shape.setAsBox(2 / PPM, 25 / PPM, new Vector2(50 / PPM, 0), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
		
		//foot sensor
		shape.setAsBox(30 / PPM, 30 / PPM, new Vector2(0 / PPM, -40 / PPM), 0);
		fixtureDef = Materials.footSensorBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
		*/
		
		
	}
	
	/*---ACTION METHODS---*/
	public void jump(){
		if(!inAir && alive){
			sliding = false;
			currentAnimationState = PlayerAnimationState.JUMPING;
			playerAnimator.resetTime();
			float v0 = (float) sqrt(-world.getGravity().y*2 * playerJumpHeight );
			playerBody.setLinearVelocity(0, v0); 
			notifyJump();
		}
	}
	
	public void land(){
		if(alive && inAir){
			currentAnimationState = PlayerAnimationState.LANDING;
			playerAnimator.resetTime();
			notifyJump();
		}
	}
	
	public void slide(){
		if(alive && !inAir && !sliding){
			sliding = true;
			//this.addAction(Actions.rotateTo(90 * MathUtils.degreesToRadians, 0.2f));
			playerBody.getFixtureList().get(0).setSensor(true);
			playerBody.getFixtureList().get(1).setSensor(false);
			currentAnimationState = PlayerAnimationState.BEGINSLIDING;
			playerAnimator.resetTime();
		}
	}
	
	public void standUp(){
		if(alive && sliding){
			sliding = false;
			//this.addAction(Actions.rotateTo(0, 0.2f));
			playerBody.getFixtureList().get(0).setSensor(false);
			playerBody.getFixtureList().get(1).setSensor(true);
			currentAnimationState = PlayerAnimationState.STANDINGUP;
			playerAnimator.resetTime();
		}
	}
	
	public void dieTop(){
		if(alive){
			alive = false;
			currentAnimationState = PlayerAnimationState.DIEINGTOP;
			playerAnimator.resetTime();
			running = false;
			notifyDie();
			//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
		}
	}
	
	public void dieBottom(){
		if(alive){
			alive = false;
			deathPosition = new Vector2(playerBody.getPosition());
			currentAnimationState = PlayerAnimationState.DIEINGBOTTOM;
			playerAnimator.resetTime();
			running = false;
			notifyDie();
			respawn();
			//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
		}
	}
	
	public void throwBombs(){
		if(alive){
			if(inAir){
				currentAnimationState = PlayerAnimationState.FLYBOMB;
				playerAnimator.resetTime();
			}
			else{
				currentAnimationState = PlayerAnimationState.RUNBOMB;
				playerAnimator.resetTime();
			}
			
			Bomb bomb = bombsPool.obtain();
			bomb.init();
	        activeBombs.add(bomb);
		}
	}
	
	public void startRunning(){
		if(!running && alive){
			currentAnimationState = PlayerAnimationState.RUNNING;
			playerAnimator.resetTime();
			//playerSpeed = 8;
			//speedUp();
			notifyStartRunning();
			running = true;
		}
	}
	
	/*---NOTIFIERS---*/
	private void notifyJump(){
		JSONObject data = new JSONObject();  
	    try {
			data.put("jump", true);
			data.put("startRunning", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}    
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	private void notifyDie(){
		
	}
	
	private void notifyStartRunning(){
		JSONObject data = new JSONObject();  
	    try {
			data.put("startRunning", true);
			data.put("jump", false);
		} catch (JSONException e) {
			e.printStackTrace();
		}    
	    WarpController.getInstance().sendGameUpdate(data.toString()); 
	}
	
	private void freePools(){
		Bomb item;
        int len = activeBombs.size;
        for (int i = len; --i >= 0;) {
            item = activeBombs.get(i);
            if (item.alive == false) {
            	activeBombs.removeIndex(i);
                bombsPool.free(item);
            }
        }
	}
	
	private void speedUp(){
		Timer.schedule(new Task() {
			@Override
			public void run() {
				playerSpeed++;
				if(playerSpeed < playerMaxSpeed)
					speedUp();
			}
		}, 0.1f);
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		freePools();
		handleBlinking();
		if(jumpSensor > 0)
			inAir = false;
		else{
			inAir = true;
			//if(currentAnimationState != PlayerAnimationState.JUMPING)
			//	currentAnimationState = PlayerAnimationState.FLYING;
		}
		playerSpeed = playerBody.getLinearVelocity().x;
		if(running && !sliding && playerSpeed < playerMaxSpeed)
			playerBody.applyForceToCenter(new Vector2(4000, 0), true);
			//playerBody.setLinearVelocity(playerSpeed, playerBody.getLinearVelocity().y);
		
		currentFrame = playerAnimator.animate(delta);
		
        setPosition(playerBody.getPosition().x + 10/PPM, playerBody.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);
             
        //playerBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y, this.getRotation());
	}
	
	public void respawn(){
		Timer.schedule(new Task() {
			@Override
			public void run() {
				handleImmortality(2);
				alive = true;
				playerBody.setTransform(deathPosition, 0);
				playerAnimator.resetTime();
				currentAnimationState = PlayerAnimationState.IDLE;
				startRunning();
			}
		}, 1);
	}
	
	public void handleImmortality(float immortalityLenght){
		immortal = true;
		blinking = true;
		Timer.schedule(new Task() {
			@Override
			public void run() {
				immortal = false;
				blinking = false;
			}
		}, immortalityLenght);
	}
	
	private void handleBlinking(){
		setVisible(visible);
		
		if(blinking && visible){
			Timer.schedule(new Task() {
				@Override
				public void run() {
					visible = false;
				}
			}, 0.1f);
		}
		else if(blinking && !visible){
			Timer.schedule(new Task() {
				@Override
				public void run() {
					visible = true;
				}
			}, 0.08f);
		}
		else if(!blinking){
			visible = true;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX() - (110 / PPM), getY() - (110 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());	
	}
	
	public void incrementJumpSensor(){
		if(jumpSensor <= 0 && alive){
			if(currentAnimationState == PlayerAnimationState.JUMPING || currentAnimationState == PlayerAnimationState.FLYING || currentAnimationState == PlayerAnimationState.FLYBOMB)
				land();
		}
		jumpSensor++;
	}
	
	public void decrementJumpSensor(){
		jumpSensor--;
	}
	
	public Body getPlayerBody(){ return this.playerBody; }
	public PlayerAnimationState getCurrentAnimationState(){ return this.currentAnimationState; }
	public void setCurrentAnimationState(PlayerAnimationState state){ this.currentAnimationState = state; }
	public float getPlayerSpeed(){ return this.playerSpeed; }
	public boolean isAlive(){ return this.alive; }
	public boolean isImmortal(){ return this.immortal; }
	public void setAlive(boolean alive){ this.alive = alive; }
	public PlayerAnimator getPlayerAnimator(){ return this.playerAnimator; }
	public boolean isInAir(){ return inAir; }
	public boolean isRunning(){ return running; }
	public void setRunning(boolean running){ this.running = running; }
}

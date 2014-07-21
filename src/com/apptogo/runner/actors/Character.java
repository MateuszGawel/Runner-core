package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Character extends Actor{
	private World world;
	
	protected boolean alive = true;
	protected boolean touchGround = false;
	protected boolean sliding = false;
	protected boolean immortal = false;
	protected boolean blinking = false;
	protected boolean visible = true;
	protected boolean running = false;
	protected boolean stopped = false;
	protected boolean touchWall = false;
	protected boolean jumped = false;
	
	protected int wallSensor = 0;
	protected int footSensor = 0;
	protected float speed = 0;
	protected float jumpHeight = 4;
	protected float playerMaxSpeed = 8;
	
	protected Body body;
	private Vector2 deathPosition;
	
	protected TextureRegion currentFrame;
	
	public enum CharacterAnimationState{
		IDLE, RUNNING, JUMPING, DIEINGTOP, DIEINGBOTTOM, CROUCHING, MOONWALKING, LANDING, FLYING, BEGINSLIDING, SLIDING, STANDINGUP, FLYBOMB, RUNBOMB
	}
			
	protected AnimationManager animationManager;
	
	public Character(World world, String atlasName){
		this.world = world;
		animationManager = new AnimationManager(atlasName);
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
	}
	
	protected void createBody(Vector2 bodySize){
		bodySize = new Vector2(25 / PPM, 65 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef;
		
		body = world.createBody(bodyDef);
		body.setUserData("player");
		
		//main fixture
		shape.setAsBox(bodySize.x, bodySize.y);
		fixtureDef = Materials.playerBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData("player");
		
		//sliding fixture
		shape.setAsBox(bodySize.y - 15/PPM, bodySize.x, new Vector2(-bodySize.x, -40/PPM), 0);
		fixtureDef = Materials.playerSlidingBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData("player");
		body.getFixtureList().get(1).setSensor(true);
		
		//wall sensor
		shape.setAsBox(5 / PPM, 50 / PPM, new Vector2(30 / PPM, 0), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData("wallSensor");
		
		
		//foot sensor
		shape.setAsBox(25 / PPM, 20 / PPM, new Vector2(-10 / PPM, -70 / PPM), 0);
		fixtureDef = Materials.footSensorBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData("footSensor");
	}
	
	
	public void start(){
		if(!running && alive){
			notifyStartRunning();
			running = true;
			if(touchGround){
				Logger.log(this, "JAZDA");
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		}
	}
	public void jump(){
		if(alive && (touchWall || touchGround)){
			sliding = false;
			jumped = true;
			float v0 = (float) sqrt(-world.getGravity().y*2 * jumpHeight );
			body.setLinearVelocity(0, v0); 
			notifyJump();
			animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
		}
	}	
	public void land(){
		if(alive && !sliding && footSensor > 0){
			if(animationManager.getCurrentAnimationState() == CharacterAnimationState.JUMPING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYBOMB){
				touchGround = true;
				jumped = false;
				animationManager.setCurrentAnimationState(CharacterAnimationState.LANDING);
			}
		}
	}
	public void slide(){
		if(alive && touchGround && !sliding){
			sliding = true;
			body.getFixtureList().get(0).setSensor(true); //wy��cz kolizje stoj�cego body
			body.getFixtureList().get(1).setSensor(false); //w��cz kolizj� le��cego body
			animationManager.setCurrentAnimationState(CharacterAnimationState.BEGINSLIDING);
		}
	}
	public void standUp(){
		if(alive && sliding){
			sliding = false;
			body.getFixtureList().get(0).setSensor(false);
			body.getFixtureList().get(1).setSensor(true);
			animationManager.setCurrentAnimationState(CharacterAnimationState.STANDINGUP);
		}
	}
	public void dieTop(){
		die();
		animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGTOP);
	}
	public void dieBottom(){
		die();
		animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGBOTTOM);
	}
	private void die(){
		if(alive){
			alive = false;
			running = false;
			sliding = false;
			jumped = false;
			body.getFixtureList().get(0).setSensor(true); //wy��cz kolizje stoj�cego body
			body.getFixtureList().get(1).setSensor(false); //w��cz kolizj� le��cego body
			deathPosition = new Vector2(body.getPosition());
			notifyDie();
			Timer.schedule(new Task() {
				@Override
				public void run() {
					respawn();
				}
			}, 1);
		}
	}
	public void respawn(){
		body.getFixtureList().get(0).setSensor(false);
		body.getFixtureList().get(1).setSensor(true);
		handleImmortality(2);
		alive = true;
		body.setTransform(deathPosition, 0);
		start();
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
	
	public void incrementWallSensor(){
		wallSensor++;
	}
	
	public void decrementWallSensor(){
		wallSensor--;
	}
	
	public void incrementFootSensor(){
		footSensor++;
	}
	
	public void decrementFootSensor(){
		footSensor--;
	}
	
	/*--- HANDLERS ---*/
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
	
	private void handleStopping(){
		if(alive && speed < 0.001f && !stopped && touchGround && !sliding && (animationManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING)){
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			stopped = true;
			Logger.log(this,  "STOPPED");
		}
		else if(alive && speed > 0.001f && stopped){
			if(touchGround)
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
			stopped = false;
			Logger.log(this,  " KONIEC STOPPED");
		}
	}

	private void handleSensors(){
		if(wallSensor > 0)
			touchWall = true;
		else
			touchWall = false;
		
		if(footSensor > 0)
			touchGround = true;
		else
			touchGround = false;
	}
	
	private void handleRunning(){
		speed = body.getLinearVelocity().x;
		if(running && !sliding && speed < playerMaxSpeed)
			body.applyForceToCenter(new Vector2(3000, 0), true);
		else if(sliding){
			body.applyForceToCenter(new Vector2(200, 0), true);
		}
	}

	@Override
	public void act(float delta) {
		handleBlinking();
		handleStopping();
		handleSensors();
		handleRunning();
		
		currentFrame = animationManager.animate(delta);
		
        setPosition(body.getPosition().x + 10/PPM, body.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
	}
	
	public boolean isAlive(){ return this.alive; }
	public void setRunning(boolean running){ this.running = running; }
	public Body getBody(){ return this.body; }
	public boolean isImmortal(){ return this.immortal; }
	public float getSpeed(){ return this.speed; }
}
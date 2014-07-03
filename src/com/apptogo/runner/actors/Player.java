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
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Player extends Actor{

	private World world;
	private PlayerAnimationState currentAnimationState;
	private Body playerBody;
	private PlayerAnimator playerAnimator;
	private TextureRegion currentFrame;
	private float playerSpeed;
	private float playerJumpHeight;
	private int jumpSensor;
	private Vector2 playerBodySize;
	
	//flags
	private boolean alive;
	private boolean inAir;
	private boolean sliding;
	
	public Player(World world){
		this.world = world;
		playerAnimator = new PlayerAnimator(this);
		currentAnimationState = PlayerAnimationState.IDLE;
		playerSpeed = 0;
		jumpSensor = 0;
		alive = true;
		playerJumpHeight = 4;
		createPlayerBody();
        setOrigin(0, 0);
	}
	
	public enum PlayerAnimationState{
		IDLE, RUNNING, JUMPING, DIEINGTOP, DIEINGBOTTOM, CROUCHING, MOONWALKING, LANDING, FLYING, BEGINSLIDING, SLIDING, STANDINGUP
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
		shape.setAsBox(playerBodySize.y, playerBodySize.x, new Vector2(-playerBodySize.x, -40/PPM), 0);
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
			playerSpeed = 0;
			notifyDie();
			//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
		}
	}
	
	public void dieBottom(){
		if(alive){
			//alive = false;
			currentAnimationState = PlayerAnimationState.DIEINGBOTTOM;
			playerAnimator.resetTime();
			playerSpeed = 0;
			notifyDie();
			//ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
		}
	}
	
	public void startRunning(){
		currentAnimationState = PlayerAnimationState.RUNNING;
		playerAnimator.resetTime();
		playerSpeed = 8;
		notifyStartRunning();
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
	

	@Override
	public void act(float delta) {
		super.act(delta);
		
		if(jumpSensor > 0)
			inAir = false;
		else{
			inAir = true;
			//if(currentAnimationState != PlayerAnimationState.JUMPING)
			//	currentAnimationState = PlayerAnimationState.FLYING;
		}
		
		if(playerSpeed > 0 && !sliding)
			playerBody.setLinearVelocity(playerSpeed, playerBody.getLinearVelocity().y);
		
		currentFrame = playerAnimator.animate(delta);
		
        setPosition(playerBody.getPosition().x + 10/PPM, playerBody.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);
             
        //playerBody.setTransform(playerBody.getPosition().x, playerBody.getPosition().y, this.getRotation());
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX() - (110 / PPM), getY() - (110 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public void incrementJumpSensor(){
		if(jumpSensor <= 0 && alive){
			if(currentAnimationState == PlayerAnimationState.JUMPING || currentAnimationState == PlayerAnimationState.FLYING)
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
	public void setAlive(boolean alive){ this.alive = alive; }
}

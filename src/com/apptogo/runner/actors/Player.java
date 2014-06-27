package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.animators.PlayerAnimator;
import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.ScreensManager.ScreenType;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import static java.lang.Math.*;

public class Player extends Actor{

	private World world;
	private PlayerAnimationState currentAnimationState;
	private Body playerBody;
	private PlayerAnimator playerAnimator;
	private TextureRegion currentFrame;
	private float playerSpeed;
	private float playerJumpHeight;
	private int jumpSensor;
	private boolean alive;
	
	public Player(World world){
		this.world = world;
		playerAnimator = new PlayerAnimator(this);
		currentAnimationState = PlayerAnimationState.IDLE;
		playerSpeed = 0;
		jumpSensor = 0;
		alive = true;
		playerJumpHeight = 3;
		createPlayerBody();
	}
	
	public enum PlayerAnimationState{
		IDLE, RUNNING, JUMPING, DIETOP, LANDING, FLYING, BEGINSLIDING, SLIDING, STANDINGUP
	}
	
	private void createPlayerBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(50 / PPM, 800 / PPM));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(25 / PPM, 65 / PPM); // ludzik ma 0.5m x 1m (troche wiecej niz 1m)
		
		FixtureDef fixtureDef = Materials.playerBody;
		fixtureDef.shape = shape;
		
		playerBody = world.createBody(bodyDef);
		playerBody.createFixture(fixtureDef).setUserData("player");
		playerBody.setUserData("player");
		
		//wall sensor
		shape.setAsBox(2 / PPM, 25 / PPM, new Vector2(10 / PPM, 0), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
		
		//foot sensor
		shape.setAsBox(30 / PPM, 30 / PPM, new Vector2(0 / PPM, -40 / PPM), 0);
		fixtureDef = Materials.footSensorBody;
		fixtureDef.shape = shape;
		playerBody.createFixture(fixtureDef).setUserData("player");
	}
	
	/*---ACTION METHODS---*/
	public void jump(){
		if(jumpSensor > 0 && alive){
			currentAnimationState = PlayerAnimationState.JUMPING;
			playerAnimator.resetTime();
			float v0 = (float) sqrt( 60 * playerJumpHeight );
			playerBody.setLinearVelocity(0, v0); 
			notifyJump();
		}
	}
	
	public void slide(){
		if(alive){
			currentAnimationState = PlayerAnimationState.BEGINSLIDING;
			playerAnimator.resetTime();
		}
	}
	
	public void standUp(){
		if(alive){
			currentAnimationState = PlayerAnimationState.STANDINGUP;
			playerAnimator.resetTime();
		}
	}
	
	public void die(){
		if(alive){
			alive = false;
			currentAnimationState = PlayerAnimationState.DIETOP;
			playerAnimator.resetTime();
			playerSpeed = 0;
			notifyDie();
			ScreensManager.getInstance().createLoadingScreen(ScreenType.SCREEN_GAME);
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
		if(playerSpeed > 0)
			playerBody.setLinearVelocity(playerSpeed, playerBody.getLinearVelocity().y);
		
		currentFrame = playerAnimator.animate(delta);
		
        setPosition(playerBody.getPosition().x + 20/PPM, playerBody.getPosition().y + 15/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(playerBody.getAngle() * MathUtils.radiansToDegrees);
        setOrigin(getWidth() / 2, getHeight() / 2);
	}
	
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentFrame, getX() - (110 / PPM), getY() - (110 / PPM), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public void incrementJumpSensor(){
		if(jumpSensor <= 0 && alive){
			playerAnimator.resetTime();
			if(currentAnimationState == PlayerAnimationState.JUMPING || currentAnimationState == PlayerAnimationState.FLYING)
				currentAnimationState = PlayerAnimationState.LANDING;
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

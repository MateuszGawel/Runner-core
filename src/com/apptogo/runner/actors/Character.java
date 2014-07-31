package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import org.json.JSONException;
import org.json.JSONObject;

import com.apptogo.runner.appwarp.WarpController;
import com.apptogo.runner.handlers.AnimationManager;
import com.apptogo.runner.handlers.Logger;
import com.apptogo.runner.handlers.ResourcesManager;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
	protected boolean started = false;
	
	protected int wallSensor = 0;
	protected int footSensor = 0;
	protected float speed = 0;
	protected float jumpHeight = 4;
	protected float playerMaxSpeed = 8;
	
	protected Body body;
	private Vector2 deathPosition;
	
	protected TextureRegion currentFrame;
	
	protected Skin guiSkin;
	protected Character character = this;
	
	public enum CharacterAnimationState{
		IDLE, RUNNING, JUMPING, DIEINGTOP, DIEINGBOTTOM, CROUCHING, MOONWALKING, LANDING, FLYING, BEGINSLIDING, SLIDING, STANDINGUP, FLYBOMB, RUNBOMB
	}
	
	public enum CharacterAbilityType
	{
		BOMB
	}
			
	protected AnimationManager animationManager;
	
	public Character(World world, String atlasName){
		this.world = world;
		animationManager = new AnimationManager(atlasName);
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		
		guiSkin = ResourcesManager.getInstance().getGuiSkin();
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
			started = true;
			if(touchGround){
				Logger.log(this, "JAZDA");
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
		}
	}
	public void jump(){
		if(started && alive && (touchWall || touchGround)){
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
		if(started && alive && touchGround && !sliding){
			sliding = true;
			body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
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
			body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
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
		//Logger.log(this, body.getPosition().x + " ");
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
	public boolean isStarted(){ return this.started; }
	
	public Button getJumpButton()
	{
		Button jumpButton = new Button(guiSkin, "banditJumpButton");
		
		jumpButton.setPosition(Runner.SCREEN_WIDTH/PPM - jumpButton.getWidth()/PPM - 20/PPM, jumpButton.getHeight()/PPM + 20/PPM + 40/PPM);
		jumpButton.setSize(jumpButton.getWidth()/PPM, jumpButton.getHeight()/PPM);
		jumpButton.setBounds(jumpButton.getX(), jumpButton.getY(), jumpButton.getWidth(), jumpButton.getHeight());
		
		jumpButton.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.jump();
		        return true;
		    }
		});
		
		return jumpButton;
	}
	public Button getSlideButton()
	{
		Button slideButton = new Button(guiSkin, "banditSlideButton");
		
		slideButton.setPosition(Runner.SCREEN_WIDTH/PPM - slideButton.getWidth()/PPM - 20/PPM, 20/PPM);
		slideButton.setSize(slideButton.getWidth()/PPM, slideButton.getHeight()/PPM);
		slideButton.setBounds(slideButton.getX(), slideButton.getY(), slideButton.getWidth(), slideButton.getHeight());
		
		slideButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				character.slide();
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				character.standUp();
		    }
		});
		
		return slideButton;
	}
	public Button getSlowButton()
	{
		Button slowButton = new Button(guiSkin, "banditSlowButton");
		
		slowButton.setPosition(20/PPM, 20/PPM);
		slowButton.setSize(slowButton.getWidth()/PPM, slowButton.getHeight()/PPM);
		slowButton.setBounds(slowButton.getX(), slowButton.getY(), slowButton.getWidth(), slowButton.getHeight());
		
		slowButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if(character.isAlive() && character.isStarted())
					character.setRunning(false);
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if(character.isAlive() && character.isStarted())
					character.setRunning(true);
		    }
		});
		
		return slowButton;
	}
	public Button getAbilityButton(CharacterAbilityType ability)
	{
		return new Button();
	}
}

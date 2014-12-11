package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.Array;

public abstract class Character extends Actor{
	private World world;
	private CustomActionManager customActionManager = CustomActionManager.getInstance();
	
	protected boolean me = false;
	protected boolean alive = true;
	protected boolean touchGround = false;
	protected boolean canJump = true;
	protected boolean touchBarrel = false;
	protected boolean sliding = false;
	protected boolean immortal = false;
	protected boolean blinking = false;
	protected boolean visible = true;
	protected boolean running = false;
	protected boolean stopped = false;
	protected boolean touchWall = false;
	protected boolean canStandup = true;
	protected boolean jumped = false;
	protected boolean doubleJump = false;
	protected boolean started = false;
	protected boolean dismemberment = false;
	protected boolean actDismemberment = false; //zdarzenie smierci odpala sie z contact listenera podczas world.step() a wtedy nie mozna korzystac z poola lub tworzyc obiektow
	protected boolean forceStandup = false;
	protected boolean shouldLand = false;
	protected boolean jumpedFromIdle = false;
	protected boolean minimumSlidingTimePassed = false;
	protected boolean slideButtonTouched = false;
	protected boolean touchSwamp = false;
	protected boolean boostedOnce = false;
	protected HashMap<CharacterSound, Sound> sounds = new HashMap<CharacterSound, Sound>();
	protected HashMap<String, Boolean> soundInstances = new HashMap<String, Boolean>();
	protected boolean stepSoundPlayed = false;
	protected long stepSoundId = 0;
	protected boolean leftRotationSensorTouching, rightRotationSensorTouching;
	protected boolean gameIsEnded = false;
	protected boolean rotatingLeft, rotatingRight;
	protected boolean forceNormalJump;
	protected boolean headTouch;
	
	protected int wallSensor = 0;
	protected int footSensor = 0;
	protected int standupSensor = 0;
	protected int jumpSensor = 0;
	protected int barrelSensor = 0;
	protected int leftRotationSensor, rightRotationSensor;
	protected float speed = 0;
	protected float jumpHeight = 4;
	protected float headSensor;
	
	protected float playerSpeedLimit = 12;
	protected float playerMinSpeed = 3;
	protected float playerSlowAmmount = 0;
	
	protected float playerSwampSlowAmmount = 0;
	
	protected Body body;
	protected Vector2 deathPosition;
	
	protected TextureRegion currentFrame;
	
	protected Character character = this;
	
	protected Skin guiSkin;
	
	protected String jumpButtonStyleName;
	protected String slideButtonStyleName; 
	protected String slowButtonStyleName;
	
	protected AnimationManager animationManager;
		
	protected ArrayList<BodyMember> bodyMembers;

	protected Array<Button> powerupButtons;

	private boolean isPowerupSet = false;
	
	public Character(World world, String atlasName, String jumpButtonStyleName, String slideButtonStyleName, String slowButtonStyleName)
	{
		this.world = world;
		animationManager = new AnimationManager(atlasName);
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		guiSkin = ResourcesManager.getInstance().getGuiSkin();
		bodyMembers = new ArrayList<BodyMember>();
		
		this.jumpButtonStyleName = jumpButtonStyleName;
		this.slideButtonStyleName = slideButtonStyleName;
		this.slowButtonStyleName = slowButtonStyleName;
		
		powerupButtons = new Array<Button>();
		addSounds();
	}
	
//	
//	private void playSound(CharacterSound sound, boolean loop){
//		sounds.get(sound).play();
//	}
//
//	private void stopSound(CharacterSound sound, boolean loop){
//		sounds.get(sound).stop();
//	}
//	

//	private void playSound(CharacterSound soundType){
//		if(soundInstances.get(soundType) == null){
//			soundInstances.put(soundType, sounds.get(soundType).play());
//			sounds.get(soundType).setLooping(soundInstances.get(soundType), true);
//		}
//		else{
//			sounds.get(soundType).resume();
//		}
//	}
	
	
    private void addSounds(){
    	ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.STEPS, (Sound)rm.getResource(cs, "mfx/game/characters/steps.ogg"));
    	sounds.put(CharacterSound.LAND, (Sound)rm.getResource(cs, "mfx/game/characters/land.ogg"));
    	sounds.put(CharacterSound.SLIDE, (Sound)rm.getResource(cs, "mfx/game/characters/slide.ogg"));
    }
    
	protected void createBody(){
		Vector2 bodySize = new Vector2(23 / PPM, 55 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		bodyDef.position.set(TiledMapLoader.getInstance().getPlayersPosition().get(0));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		FixtureDef fixtureDef;
		
		body = world.createBody(bodyDef);
		body.setUserData( new UserData("player") );
		
		//main fixture
		shape.setAsBox(bodySize.x, bodySize.y);
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("player") );

		//wall sensor body
		shape.setAsBox(6 / PPM, 54 / PPM, new Vector2(25 / PPM, -1/PPM), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensorBody") );
		
		//sliding fixture
		shape.setAsBox(bodySize.y -5/PPM, bodySize.x, new Vector2(-bodySize.x, -32/PPM), 0);
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("player") );
		body.getFixtureList().get(2).setSensor(true);
		
		//wall sensor
		shape.setAsBox(5 / PPM, 50 / PPM, new Vector2(30 / PPM, 0), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensor") );
		
		//coin collector sensor
		shape.setAsBox(60 / PPM, 70 / PPM, new Vector2(0, 0), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("coinCollectorSensor") );
		

		//foot sensor
		shape.setAsBox(25 / PPM, 25 / PPM, new Vector2(-10 / PPM, -75 / PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("footSensor") );
		
		//jump sensor
		shape.setAsBox(70 / PPM, 40 / PPM, new Vector2(-40 / PPM, -80 / PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("jumpSensor") );
		
		//standup sensor
		shape.setAsBox(bodySize.x-5/PPM, bodySize.y - 6/PPM, new Vector2(0, 8/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("standupSensor") );
		
		//head sensor
		shape.setAsBox(bodySize.x-5/PPM, 15/PPM, new Vector2(0, 60/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("headSensor") );
		
		//Left sensor
		shape.setAsBox(5/PPM, 5/PPM, new Vector2(-25/PPM, -55/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("leftRotationSensor") );
		
		//Right sensor
		shape.setAsBox(5/PPM, 5/PPM, new Vector2(25/PPM, -55/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("rightRotationSensor") );
	}
	
	public boolean start()
	{
		if(!running && alive && !gameIsEnded)
		{		
			if(!stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
			running = true;
			started = true;
			if(touchGround || touchBarrel){
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			return true;
		}
		else return false;
	}
	
	/** Defaults - 1, 1, 0, 0 - Jumps for jumpHeight set to character and current x speed **/
	public boolean jump(float xMultiplier, float yMultiplier, float xAdd, float yAdd, boolean forceJump)
	{
		if(forceJump){
			land();
			doubleJump = false;
		}
		
		if(/*started && */alive && canStandup && !headTouch && ((doubleJump || forceNormalJump) || forceJump || touchWall || canJump || touchBarrel || !me) && (!touchSwamp || (touchSwamp && touchWall)))
		{
			boostedOnce = false;
			if(sliding){
				body.getFixtureList().get(0).setSensor(false);
				body.getFixtureList().get(1).setSensor(false);
				body.getFixtureList().get(2).setSensor(true);
				sliding = false;
			}
			jumped = true;
			if(speed < 0.1f) jumpedFromIdle = true;

			if(!doubleJump || forceNormalJump){
				float y = (float) sqrt(-world.getGravity().y * 2 * jumpHeight);
				float x = body.getLinearVelocity().x;
				body.setLinearVelocity(x*xMultiplier + xAdd, y*yMultiplier + yAdd);
				animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
				doubleJump = true;
			}
			else{
				float y = (float) sqrt(-world.getGravity().y * 2 * jumpHeight);
				float x = body.getLinearVelocity().x;
				body.setLinearVelocity(x * 0.5f, y * 0.7f );
				doubleJump = false;
			}
			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			sounds.get(CharacterSound.JUMP).play();
			return true;
		}
		else return false;
	}	
	public void land()
	{
		if(shouldLand && animationManager.getCurrentAnimationState() != CharacterAnimationState.LANDING)
		{
			doubleJump = false;
			jumpedFromIdle = false;
			touchGround = true;
			jumped = false;
			if(speed > 0.05f)
				animationManager.setCurrentAnimationState(CharacterAnimationState.LANDING);
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.LANDINGIDLE);
			sounds.get(CharacterSound.LAND).play(0.3f);
			if(body.getLinearVelocity().x > 0.01f && !stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
		}
	}
	
	public void boostAfterLand(){
		//Logger.log(this, "boost " + shouldLand + running + touchWall);
		if((animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animationManager.getCurrentAnimationState() == CharacterAnimationState.LANDING) &&
			!boostedOnce && alive && !sliding && running && !touchWall){
			body.setLinearVelocity(((UserData)getBody().getUserData()).speedBeforeLand, 0);
			Logger.log(this, "boost do predkosci: " + ((UserData)getBody().getUserData()).speedBeforeLand);
			boostedOnce = true;
		}
	}
	
	public boolean slide()
	{
		if(/*started && */ alive && (touchGround || !me) && !sliding)
		{
			sliding = true;
			minimumSlidingTimePassed = false;
			body.getFixtureList().get(0).setSensor(true); 
			body.getFixtureList().get(1).setSensor(true);//wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(2).setSensor(false); //w³¹cz kolizjê le¿¹cego body
			animationManager.setCurrentAnimationState(CharacterAnimationState.BEGINSLIDING);
			//slowPlayerBy(0.5f);
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			sounds.get(CharacterSound.SLIDE).play();
			
			customActionManager.registerAction(new CustomAction(0.6f) {
				
				@Override
				public void perform() {
					minimumSlidingTimePassed = true;
					if(!slideButtonTouched) standUp();
				}
			});
			NotificationManager.getInstance().notifySlide(getBody().getPosition());
			return true;
		}
		else if(!touchGround){
			body.setLinearVelocity( body.getLinearVelocity().x, -30f );
			return true;
		}
		else return false;
	}
	
	public boolean standUp()
	{
		if(alive && sliding && canStandup && minimumSlidingTimePassed){
			sliding = false;
			body.getFixtureList().get(0).setSensor(false);
			body.getFixtureList().get(1).setSensor(false);
			body.getFixtureList().get(2).setSensor(true);

			animationManager.setCurrentAnimationState(CharacterAnimationState.STANDINGUP);
			//speedPlayerBy(0.5f);

			if(body.getLinearVelocity().x > 0.01f && !stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
			NotificationManager.getInstance().notifyStandUp(getBody().getPosition());
			return true;
		}
		else if(!canStandup){
			forceStandup = true;
			return false;
		}
		else return false;
	}

	public boolean dieTop()
	{
		if(alive){
			sounds.get(CharacterSound.DEATH).play();
			boolean success = die();
			animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGTOP);
			return success;
		}
		return false;
	}

	public boolean dieBottom()
	{
		if(alive){
			sounds.get(CharacterSound.DEATH).play();
			boolean success = die();
			animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGBOTTOM);
			return success;
		}
		return false;
	}

	public boolean dieDismemberment()
	{
		if(alive){
			getBody().setLinearVelocity(0,0);
			sounds.get(CharacterSound.EXPLODE).play();
			dismemberment = true;
			visible = false;
			actDismemberment = true;   
			boolean success = die();
				
			return success;
		}
		return false;
	}
	
	private boolean die()
	{
		playerSlowAmmount = 0;
		if(alive)
		{
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			alive = false;
			running = false;
			sliding = false;
			jumped = false;
			
			deathPosition = new Vector2(body.getPosition());
			customActionManager.registerAction(new CustomAction(1f) {
				
				@Override
				public void perform() {
					dismemberment = false;
					respawn();
				}
			});
			return true;
		}
		else return false;
	}
	public void respawn()
	{
		handleImmortality(2.0f);
		
		alive = true;
		body.setTransform(deathPosition, 0);
		
		
		start();
	}
	
	abstract public void useAbility(CharacterAbilityType abilityType);
		
	public void incrementWallSensor(){
		wallSensor++;
	}
	
	public void incrementBarrelSensor(){
		if(!touchBarrel)
			barrelSensor++;
	}
	
	public void decrementWallSensor(){
		wallSensor--;
	}
	
	public void incrementFootSensor(){
		footSensor++;
	}
	
	public void decrementJumpSensor(){
		jumpSensor--;
	}
	
	public void incrementJumpSensor(){
		jumpSensor++;
	}
	
	public void decrementFootSensor(){
		footSensor--;
	}
	public void incrementHeadSensor(){
		headSensor++;
	}
	public void decrementHeadSensor(){
		headSensor--;
	}
	public void incrementStandupSensor(){
		standupSensor++;
	}
	public void decrementStandupSensor(){
		standupSensor--;
	}
	public void decrementBarrelSensor(){
		if(touchBarrel)
			barrelSensor--;
	}
	
	public void incrementLeftRotationSensor(){
		leftRotationSensor++;
	}
	public void decrementLeftRotationSensor(){
		leftRotationSensor--;
	}
	public void incrementRightRotationSensor(){
		rightRotationSensor++;
	}
	public void decrementRightRotationSensor(){
		rightRotationSensor--;
	}
	
	public boolean slowPlayerBy(float percent){
		if(percent < 0 || percent > 1)
			return false;
		else
			playerSlowAmmount += playerSpeedLimit * percent;
		return true;
	}
	public boolean speedPlayerBy(float percent){
		if(percent < 0 || percent > 1)
			return false;
		else
			playerSlowAmmount -= playerSpeedLimit * percent;
		return true;
	}
	
	public boolean superJump()
	{
		return jump(2, 2, 0, 0, true);
	}
	private void superRun()
	{
		speedPlayerBy(0.7f);
		customActionManager.registerAction(new CustomAction(3f) {	
			@Override
			public void perform() {
				if(playerSlowAmmount < 0)
					slowPlayerBy(0.7f);
			}
		});
	}
	
	
	/*--- HANDLERS ---*/
	public void handleImmortality(float immortalityLenght)
	{
		immortal = true;
		blinking = true;
		customActionManager.registerAction(new CustomAction(immortalityLenght) {
			
			@Override
			public void perform() {
				immortal = false;
				blinking = false;
			}
		});
	}
	
	private void handleBlinking()
	{
		setVisible(visible);
		
		if(!dismemberment){
			if(blinking && visible){
				customActionManager.registerAction(new CustomAction(0.08f) {
					@Override
					public void perform() {
						visible = false;
					}
				});
			}
			else if(blinking && !visible){
				customActionManager.registerAction(new CustomAction(0.08f) {
					@Override
					public void perform() {
						visible = true;
					}
				});
			}
			else if(!blinking){
				visible = true;
			}
		}
	}
	
	private void handleStopping(){
		if(alive && Math.abs(speed) < 0.5f && !stopped && touchGround && !sliding){			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			stopped = true;
			if(animationManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING){
				animationManager.setCurrentAnimationState(CharacterAnimationState.LANDINGIDLE);
			}
		}
		else if(alive && Math.abs(speed) > 0.5f && stopped && !sliding){
			if(touchGround){
				if(!stepSoundPlayed){
					stepSoundId = sounds.get(CharacterSound.STEPS).loop();
					stepSoundPlayed = true;
				}
			}
			stopped = false;
		}
	}

	private void handleSensors(){
		if(wallSensor > 0){
			touchWall = true;
			forceNormalJump = true;
		}
		else{
			touchWall = false;
			forceNormalJump = false;
		}
		
		if(footSensor > 0){
			if(!touchGround && !touchBarrel)
				land();
			touchGround = true;
		}
		else
			touchGround = false;
		
		if(standupSensor > 0)
			canStandup = false;
		else
			canStandup = true;
		if(headSensor > 0)
			headTouch = true;
		else
			headTouch = false;
		
		if(barrelSensor > 0){
			if(!touchGround && !touchBarrel)
				land();
			touchBarrel = true;
		}
		else
			touchBarrel = false;
		
		if(jumpSensor > 0){
			canJump = true;
		}
		else
			canJump = false;
		
		if(leftRotationSensor > 0)
			leftRotationSensorTouching = true;
		else
			leftRotationSensorTouching = false;
		if(rightRotationSensor > 0)
			rightRotationSensorTouching = true;
		else
			rightRotationSensorTouching = false;
		
		touchSwamp = ((UserData)getBody().getUserData()).touchSwamp;
	}
	

	
	/**metoda obslugujaca slowy z ContactListnera*/
	private void handleSlowing(){
		playerSwampSlowAmmount = playerSpeedLimit * ((UserData)body.getUserData()).slowPercent;
	}
	

	
	private void handleRunning(){
		speed = body.getLinearVelocity().x;
		if(running && (speed < playerSpeedLimit - playerSlowAmmount - playerSwampSlowAmmount || speed <= playerMinSpeed))
			//body.setLinearVelocity(playerSpeedLimit, 0);
			if(!jumped){
				body.applyForceToCenter(new Vector2(5000, 0), true);
			}
			else
				body.applyForceToCenter(new Vector2(5000 - 30*getBody().getLinearVelocity().x*getBody().getLinearVelocity().x, 0), true);
		if((touchGround || touchBarrel) && speed > 1f && animationManager.getCurrentAnimationState() == CharacterAnimationState.IDLE)
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
	}
	
	private void handleStepSoundSpeed(){
		if(stepSoundId != 0)
			sounds.get(CharacterSound.STEPS).setPitch(stepSoundId, getSpeed()/12);
	}
	
	private void handleDismemberment(){
		if(actDismemberment){
			actDismemberment = false;
			for(BodyMember bodyMember : bodyMembers){
				bodyMember.init();
			}
		}
	}
	private void handleStandingUp(){
		if(forceStandup && canStandup && sliding){
			standUp();
			forceStandup = false;
		}
	}
	
	private void handleShouldLand(){
		if(alive && !sliding && !(touchGround || touchBarrel) && 
				(animationManager.getCurrentAnimationState() == CharacterAnimationState.JUMPING 
				|| animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYING 
				|| animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYBOMB) &&
				animationManager.getCurrentAnimationState() != CharacterAnimationState.LANDING
			){
				shouldLand = true;
		}
		else{
			shouldLand = false;
		}
	}
	
	private void handleDying(){
		if(((UserData)body.getUserData()).dieBottom){
			dieBottom();
			((UserData)body.getUserData()).dieBottom = false;
		}
		else if(((UserData)body.getUserData()).dieTop){
			dieTop();
			((UserData)body.getUserData()).dieTop = false;
		}
	}
	
	private void handleFlying(){
		if(!canJump && !touchGround && !touchWall && !touchBarrel){
			CustomAction action = new CustomAction(0.1f, 1, this) {
				@Override
				public void perform() {
					if(!canJump && !touchGround && !touchWall && !touchBarrel && alive && animationManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING){
						animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
						boostedOnce = false;
						if(stepSoundPlayed){
							sounds.get(CharacterSound.STEPS).stop();
							stepSoundPlayed = false;
						}
					}
						
				}
			};
			
			customActionManager.registerAction(action);
			if(canJump || touchGround || touchWall || touchBarrel){
				action.setFinished(true);
			}
		}
	}
	
	private void rotateLeft(boolean toZero){
		customActionManager.registerAction(new CustomAction(0.00001f, 0, toZero) {	
			@Override
			public void perform() {
				Logger.log(this, "rotateLeft angle: " + getBody().getAngle());
				if(leftRotationSensorTouching || ((Boolean)args[0] && getBody().getAngle() >= 0)){
					rotatingLeft = false;
					setFinished(true);
				}
				else
					getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, getBody().getAngle() + 0.05f);
			}
		});
	}
	
	private void rotateRight(boolean toZero){
		customActionManager.registerAction(new CustomAction(0.00001f, 0, toZero) {	
			@Override
			public void perform() {
				Logger.log(this, "rotateRight angle: " + getBody().getAngle());
				if(rightRotationSensorTouching || ((Boolean)args[0] && getBody().getAngle() <= 0)){
					rotatingRight = false;
					setFinished(true);
				}
				else
					getBody().setTransform(getBody().getPosition().x, getBody().getPosition().y, getBody().getAngle() - 0.05f);
			}
		});
	}
	

	private void handleRotation(){
		if(!touchWall){
			if(!leftRotationSensorTouching && !rightRotationSensorTouching){
				if(getBody().getAngle() <= 0.1){
					if(!rotatingLeft)rotateLeft(true);
					rotatingLeft = true;
				}
				else if(getBody().getAngle() >= 0.1){
					if(!rotatingRight)rotateRight(true);
					rotatingRight = true;
				}
			}
			else if(!rightRotationSensorTouching){
				if(!rotatingRight) rotateRight(false);
				rotatingLeft = false;
				rotatingRight = true;
			}
			else if(!leftRotationSensorTouching){
				if(!rotatingLeft) rotateLeft(false);
				rotatingLeft = true;
				rotatingRight = false;
			}
		}
	}

	@Override
	public void act(float delta) 
	{
		handleBlinking();
		handleSensors();
		handleSlowing();
		handleRunning();
		handleStopping();
		handleDismemberment();
		handleStandingUp();
		handleStepSoundSpeed();
		handleDying();
		handleShouldLand();
		handleFlying();
		//handleRotation();
		
		currentFrame = animationManager.animate(delta);
		//Logger.log(this, "char wykonuje sie z takim delta: " + delta + " i statetime: " + animationManager.stateTime);
        setPosition(body.getPosition().x + 10/PPM, body.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        
	}
	
	public boolean isAlive(){ return this.alive; }
	public boolean setRunning(boolean running)
	{ 
		if( !gameIsEnded )
		{
			this.running = running; 
			return true;
		}
		else
		{
			this.running = false;
			return false;
		}
	} 
	public Body getBody(){ return this.body; }
	public boolean isImmortal(){ return this.immortal; }
	public float getSpeed(){ return this.speed; }
	public boolean isStarted(){ return this.started; }
	
	public void endGame()
	{
		sounds.get(CharacterSound.VICTORY).play(0.4f);
		gameIsEnded = true;
	}
	
	public abstract CharacterType getCharacterType();	

	/* do zaimplementowania potem przy umiejetnosciach klasowych
	public abstract Button getAbilityButton();
	*/
	
	public Button getJumpButton()
	{
		Button jumpButton = new Button(guiSkin, this.jumpButtonStyleName);
		
		jumpButton.setPosition(Runner.SCREEN_WIDTH - jumpButton.getWidth() - 20, 20);
		jumpButton.setSize(jumpButton.getWidth(), jumpButton.getHeight());
		jumpButton.setBounds(jumpButton.getX(), jumpButton.getY(), jumpButton.getWidth(), jumpButton.getHeight());
		jumpButton.setOrigin(jumpButton.getWidth()/2, jumpButton.getHeight()/2);
		jumpButton.setScaleX(5f);
		jumpButton.setScaleY(2f);
		jumpButton.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				if( character.jump(1, 1, 0, 0, false))
				{
					NotificationManager.getInstance().notifyJump(getBody().getPosition(), 1, 1, 0, 0, false);
				}
				
		        return true;
		    }
		});
		
		return jumpButton;
	}
	public Button getSlideButton()
	{
		Button slideButton = new Button(guiSkin, this.slideButtonStyleName);
		
		slideButton.setPosition(20, 20);
		slideButton.setSize(slideButton.getWidth(), slideButton.getHeight());
		slideButton.setBounds(slideButton.getX(), slideButton.getY(), slideButton.getWidth(), slideButton.getHeight());
		slideButton.setOrigin(slideButton.getWidth()/2, slideButton.getHeight()/2);
		slideButton.setScaleX(5f);
		slideButton.setScaleY(2f);
		slideButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
			{
				slideButtonTouched = true;
				character.slide();
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
			{
				slideButtonTouched = false;
				character.standUp();
		    }
		});
		
		return slideButton;
	}

	public Array<Button> initializePowerupButtons()
	{
		for( final PowerupType powerupType: new Array<PowerupType>(PowerupType.values()) )
		{
			Button button = PowerupType.convertToPowerupButton(powerupType, this.getCharacterType());
			button.setUserObject(powerupType);
			button.addListener(new InputListener() 
			{
				@Override
			    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
				{
					if(started && alive) character.usePowerup( powerupType );
			        return true;
			    }
			});
			
			this.powerupButtons.add(button);
		}
		
		return this.powerupButtons;
	}
	
	protected void usePowerup(PowerupType powerupType) 
	{
		if( powerupType == PowerupType.NONE )
		{
			//pass
		}
		else if( powerupType == PowerupType.SUPERJUMP )
		{
			if(character.superJump())
				removePowerup(PowerupType.SUPERJUMP);
		}
		else if( powerupType == PowerupType.SUPERSPEED )
		{
			character.superRun();
			removePowerup(PowerupType.SUPERSPEED);
		}
		else if( powerupType == PowerupType.ABILITY1 )
		{
			if(getCharacterType() == CharacterType.BANDIT)
				character.useAbility(CharacterAbilityType.BOMB);
			else if(getCharacterType() == CharacterType.ARCHER)
				character.useAbility(CharacterAbilityType.ARROW);
			else if(getCharacterType() == CharacterType.ALIEN)
				character.useAbility(CharacterAbilityType.LIFT);
			
			removePowerup(PowerupType.ABILITY1);
		}
		
		this.isPowerupSet  = false;
	}

	public void setPowerup(PowerupType powerupType) 
	{
		for(Button button: powerupButtons){
			if( ( (PowerupType)button.getUserObject() ) == powerupType ){
				button.setVisible(true);
			}
		}
		this.isPowerupSet  = true;
	}
	
	public void removePowerup(PowerupType powerupType) 
	{
		for(Button button: powerupButtons){
			if( ( (PowerupType)button.getUserObject() ) == powerupType ){
				button.setVisible(false);
			}
		}
		this.isPowerupSet  = false;
	}

	public boolean isPowerupSet() 
	{
		return this.isPowerupSet;
	}
	public void setMe(boolean me){ this.me = me; }

}

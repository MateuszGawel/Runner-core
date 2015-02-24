package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.CoinsManager;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.FlagsHandler;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.handlers.TiledMapLoader;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.screens.BaseScreen;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
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
	
	public final float coinFixtureRadius = 64;
	
	public String playerName;
	public FlagsHandler flags;
	
	private World world;
	private CustomActionManager customActionManager = CustomActionManager.getInstance();
	protected Body body;
	protected TextureRegion currentFrame;
	protected Character character = this;
	public AnimationManager animationManager;
	protected ArrayList<BodyMember> bodyMembers;
	protected boolean flipX, flipY;
	
	protected Random randonGenerator = new Random();
	
	protected HashMap<CharacterSound, Sound> sounds = new HashMap<CharacterSound, Sound>();
	protected boolean stepSoundPlayed;
	protected long stepSoundId;
	
	public float speed = 0;
	public float playerSpeedLimit = 12;
	public float playerMinSpeed = 3;
	public float playerSlowAmmount = 0;
	public float speedBeforeLand;
	private int gravityModificator = 1;
	
	protected Vector2 deathPosition;
	
	protected Skin guiSkin;
	protected String jumpButtonStyleName;
	protected String slideButtonStyleName; 
	protected String slowButtonStyleName;
	protected Array<Button> powerupButtons;
	private int coinCounter;
	
	protected boolean blinkShow = false;
	
	public Character(World world, String atlasName, String jumpButtonStyleName, String slideButtonStyleName, String slowButtonStyleName, String playerName)
	{
		this.world = world;
		animationManager = new AnimationManager(atlasName);
		animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		
		guiSkin = ResourcesManager.getInstance().getGuiSkin();
		this.jumpButtonStyleName = jumpButtonStyleName;
		this.slideButtonStyleName = slideButtonStyleName;
		this.slowButtonStyleName = slowButtonStyleName;
		powerupButtons = new Array<Button>();
		
		addSounds();
		
		flags = new FlagsHandler();
		flags.setCharacter(this);

		this.setOrigin(90/PPM, 90/PPM);
	}
	
	
    private void addSounds(){
    	ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.STEPS, (Sound)rm.getResource(cs, "mfx/game/characters/steps.ogg"));
    	sounds.put(CharacterSound.LAND, (Sound)rm.getResource(cs, "mfx/game/characters/land.ogg"));
    	sounds.put(CharacterSound.SLIDE, (Sound)rm.getResource(cs, "mfx/game/characters/slide.ogg"));
    }
    
	protected void createBody(int startingPosition){
		bodyMembers = new ArrayList<BodyMember>();
		Vector2 bodySize = new Vector2(23 / PPM, 55 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		bodyDef.position.set(TiledMapLoader.getInstance().getPlayersPosition().get(startingPosition));
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(bodySize.x, bodySize.y);
		
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		Logger.log(this, "Player body width: " + shapeWidth);
		UserData userData = new UserData("mainBody");
		userData.bodyWidth = shapeWidth;
				
		body = world.createBody(bodyDef);
		
		FixtureDef fixtureDef;
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
				
		//main fixture
		Fixture fix = body.createFixture(fixtureDef);
		fix.setUserData( userData );
		
		//przyjrzec sie pozostalym userData - nie mozna uzywac tej samej referencji! :(
		UserData bodyUserData = new UserData("player");
		
		body.setUserData( bodyUserData );
		
		createNormalFixtures(fixtureDef, bodySize, shape);
		createMirrorFixtures(fixtureDef, bodySize, shape);
	}
	
	private void createNormalFixtures(FixtureDef fixtureDef, Vector2 bodySize, PolygonShape shape){
		//wall sensor body
		shape.setAsBox(0.5f / PPM, 54.5f / PPM, new Vector2(24 / PPM, 1/PPM), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensorBody") );
		
		//sliding fixture
		shape.setAsBox(bodySize.y -5/PPM, bodySize.x, new Vector2(-bodySize.x -4/PPM, -32/PPM), 0);
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("mainBoddy") );
		body.getFixtureList().get(2).setSensor(true);
		
		//wall sensor
		shape.setAsBox(5 / PPM, 50 / PPM, new Vector2(30 / PPM, 0), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensor") );
		
		//coin collector sensor
		CircleShape circleShape = new CircleShape();
    	circleShape.setRadius( this.coinFixtureRadius /PPM);
		fixtureDef.shape = circleShape;
		fixtureDef = Materials.characterSensor;
		body.createFixture(fixtureDef).setUserData( new UserData("coinCollectorSensor") );
		
		//foot sensor
		shape.setAsBox(25 / PPM, 25 / PPM, new Vector2(-10 / PPM, -80 / PPM), 0);
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
	
	private void createMirrorFixtures(FixtureDef fixtureDef, Vector2 bodySize, PolygonShape shape){
		
		//wall sensor body
		shape.setAsBox(0.5f / PPM, 54.5f / PPM, new Vector2(24 / PPM, -1/PPM), 0);
		fixtureDef = Materials.wallSensorBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensorBody") );
		
		//sliding fixture
		shape.setAsBox(bodySize.y -5/PPM, bodySize.x, new Vector2(-bodySize.x -4/PPM, 32/PPM), 0);
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("mainBoddy") );
		body.getFixtureList().get(2).setSensor(true);
		
		//wall sensor
		shape.setAsBox(5 / PPM, 50 / PPM, new Vector2(30 / PPM, 0), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensor") );
		
		//coin collector sensor
		CircleShape circleShape = new CircleShape();
    	circleShape.setRadius(64/PPM);
		fixtureDef.shape = circleShape;
		fixtureDef = Materials.characterSensor;
		body.createFixture(fixtureDef).setUserData( new UserData("coinCollectorSensor") );
	
		//foot sensor
		shape.setAsBox(25 / PPM, 25 / PPM, new Vector2(-10 / PPM, 80 / PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("footSensor") );
		
		//jump sensor
		shape.setAsBox(70 / PPM, 40 / PPM, new Vector2(-40 / PPM, 80 / PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("jumpSensor") );
		
		//standup sensor
		shape.setAsBox(bodySize.x-5/PPM, bodySize.y - 6/PPM, new Vector2(0, -8/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("standupSensor") );
		
		//head sensor
		shape.setAsBox(bodySize.x-5/PPM, 15/PPM, new Vector2(0, -60/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("headSensor") );
		
		//Left sensor
		shape.setAsBox(5/PPM, 5/PPM, new Vector2(-25/PPM, 55/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("leftRotationSensor") );
		
		//Right sensor
		shape.setAsBox(5/PPM, 5/PPM, new Vector2(25/PPM, 55/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("rightRotationSensor") );
		
		body.getFixtureList().get(11).setSensor(true);
		body.getFixtureList().get(12).setSensor(true);
		for(int i = 11; i<=20; i++){
			((UserData)body.getFixtureList().get(i).getUserData()).ignoreContact = true;
		}
	}
	
	public void start()
	{
		if(flags.isCanBegin())
		{		
			if(!stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
			flags.setBegan(true);
			if(flags.isOnGround()){
				animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			}
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
		}
	}
	
	public void jump(float xMultiplier, float yMultiplier, float xAdd, float yAdd){
		if(flags.isCanDoubleJump()){
			doubleJump();
		}
		else if(flags.isCanJump()){
			normalJump(xMultiplier, yMultiplier, xAdd, yAdd);	
		}
	}
	
	private void normalJump(float xMultiplier, float yMultiplier, float xAdd, float yAdd)
	{
		flags.setBoostedOnce(false);
		if(flags.isSliding())
			speedPlayerBy(0.2f);
		flags.setSliding(false);
		flags.setQueuedBoost(0);
		flags.setJumped(true);
		layFixtures(false);

		float y = (float) sqrt(-world.getGravity().y * 8);
		float x = body.getLinearVelocity().x;
		body.setLinearVelocity(x*xMultiplier + xAdd, (y*yMultiplier + yAdd) * gravityModificator);
		animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
		
		if(stepSoundPlayed){
			sounds.get(CharacterSound.STEPS).stop();
			stepSoundPlayed = false;
		}
		sounds.get(CharacterSound.JUMP).play();
		
		NotificationManager.getInstance().notifyJump(getBody().getPosition(), 1, 1, 0, 0, false);
	}	
	
	private void doubleJump(){
		float y = (float) sqrt(-world.getGravity().y * 8);
		float x = body.getLinearVelocity().x;
		flags.setQueuedBoost(0);
		body.setLinearVelocity(x * 0.8f, gravityModificator * y * 0.7f );
		flags.setDoubleJumped(true);
	}
	
	private void lift(){
		if(flags.isCanBeLifted()){
			flags.setBoostedOnce(false);
			if(flags.isSliding())
				speedPlayerBy(0.2f);
			flags.setSliding(false);
			flags.setJumped(true);
			layFixtures(false);
	
			float y = (float) sqrt(-world.getGravity().y * 8);
			float x = body.getLinearVelocity().x;
			body.setLinearVelocity(-10, y * gravityModificator);
			animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			//sounds.get(CharacterSound.JUMP).play();
			
			flags.setQueuedBoost(0);
			flags.setDoubleJumped(true);
		}
	}
	
	public void land()
	{
		if(flags.isCanLand())
		{
			flags.setDoubleJumped(false);
			flags.setJumped(false);
			if(!stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
			
			if( flags.isSliding() )
				standUp();
			else
			{
				if(speed > 0.05f){
					animationManager.setCurrentAnimationState(CharacterAnimationState.LANDING);
				}
				else{
					animationManager.setCurrentAnimationState(CharacterAnimationState.LANDINGIDLE);
					flags.update();
				}
			}
			if(!flags.isSliding())
				sounds.get(CharacterSound.LAND).play(0.3f);
		}
	}
	
	public void boostAfterLand(){
		if(flags.isCanBoost()){
			body.setLinearVelocity(flags.getQueuedBoost(), 0);
			flags.setBoostedOnce(true);
			flags.setQueuedBoost(0);
		}
	}
	
	private void layFixtures(boolean layFixture){
		if(layFixture){
			body.getFixtureList().get(0).setSensor(true); 
			body.getFixtureList().get(1).setSensor(true);
			body.getFixtureList().get(2).setSensor(false);
		}
		else{
			body.getFixtureList().get(0).setSensor(false); 
			body.getFixtureList().get(1).setSensor(false);
			body.getFixtureList().get(2).setSensor(true);
		}
	}
	
	public void slide()
	{
		if(flags.isCanSlide())
		{
			flags.setSliding(true);
			layFixtures(true);
			flags.setMinimumSlidingTimePassed(false);
			
			animationManager.setCurrentAnimationState(CharacterAnimationState.BEGINSLIDING);
			slowPlayerBy(0.2f);
			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			sounds.get(CharacterSound.SLIDE).play();
			
			customActionManager.registerAction(new CustomAction(0.5f) {
				@Override
				public void perform() {
					flags.setMinimumSlidingTimePassed(true);
					if(!flags.isSlideButtonPressed()) standUp();
				}
			});
			NotificationManager.getInstance().notifySlide(getBody().getPosition());
		}
		else if(!flags.isOnGround()){
			body.setLinearVelocity( body.getLinearVelocity().x, -30f * gravityModificator );
		}
	}
	
	public void standUp()
	{
		if(flags.isCanStandUp()){
			flags.setSliding(false);
			layFixtures(false);

			animationManager.setCurrentAnimationState(CharacterAnimationState.STANDINGUP);
			speedPlayerBy(0.2f);

			if(speed > 0.01f && !stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
			NotificationManager.getInstance().notifyStandUp(getBody().getPosition());
		}
		else{
			flags.setForceStandUp(true);
		}
	}

	public void dieTop()
	{
		if(flags.isCanDie()){
			sounds.get(CharacterSound.DEATH).play();
			
			die();
			animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGTOP);
		}
	}

	public void dieBottom()
	{
		if(flags.isCanDie()){
			sounds.get(CharacterSound.DEATH).play();
			
			die();
			animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGBOTTOM);
		}
	}

	public void dieDismemberment()
	{
		if(flags.isCanDie()){
			getBody().setLinearVelocity(0,0);
			sounds.get(CharacterSound.EXPLODE).play();
			for(BodyMember bodyMember : bodyMembers){
				bodyMember.init();
			}  
			
			setVisible(false);
			
			die();
		}
	}
	
	private void die()
	{
		playerSlowAmmount = 0;
		if(flags.isAlive())
		{
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			flags.setAlive(false);
			flags.setSliding(false);
			flags.setFlying(false);
			
			//flags.setBegan(false);
			
			deathPosition = new Vector2(body.getPosition());
			customActionManager.registerAction(new CustomAction(1f) {
				@Override
				public void perform() {
					respawn();	
				}
			});
		}
	}
	
	public void respawn()
	{
		setVisible(true);
		flags.setImmortal(true);
		flags.setAlive(true);
		flags.update();
		if(flags.isOnGround())
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
		
		body.setTransform(deathPosition, 0);
		start();
		
		final CustomAction blinkAction = new CustomAction(0.1f, 0) {
			@Override
			public void perform()
			{
				if(blinkShow) setVisible(true);
				else          setVisible(false);
				
				blinkShow = !blinkShow;
			}
		};
		
		customActionManager.registerAction(blinkAction);
		
		customActionManager.registerAction(new CustomAction(2f) {
			@Override
			public void perform() {
				
				flags.setImmortal(false);
				
				flags.update();

				blinkAction.setFinished(true);
				
				blinkShow = false;
								
				setVisible(true);				
			}
		});
	}
	
	abstract public void useAbility(CharacterAbilityType abilityType);
	
	public void slowPlayerBy(float percent){
		if(!(percent < 0 || percent > 1))
			playerSlowAmmount += playerSpeedLimit * percent;
	}
	public void speedPlayerBy(float percent){
		if(!(percent < 0 || percent > 1))
			playerSlowAmmount -= playerSpeedLimit * percent;
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
	private void handleQueuedActions(){
		if(flags.isQueuedJump()){
			character.jump(1, 1, 0, 0);
			flags.setQueuedJump(false);
		}
		
		if(flags.getQueuedBoost() > 0){
			boostAfterLand();
		}
		
		if(flags.isQueuedLift()){
			lift();
			flags.setQueuedLift(false);
		}
	}
	
	private void handleActions(){
		land();
	}
	
	private void handleStopping(){
		if(flags.isShouldStop()){	
			flags.setStopped(true);
			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		}
		if(flags.isShouldStart()){
			flags.setStopped(false);
		}
	}

	private void handleRunning(){
		speed = body.getLinearVelocity().x;
		if(flags.isCanRun()){
			if(flags.isOnGround()){
				body.applyForceToCenter(new Vector2(3000, 0), true); 
			}
			else if(this.getBody().getLinearVelocity().x <= 1){
					body.setLinearVelocity( (playerSpeedLimit - playerSlowAmmount) * 0.5f, body.getLinearVelocity().y);
			}
		}
		//Logger.log(this, flags.isShouldChangeToRunningState());
		if(flags.isShouldChangeToRunningState())
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
	}
	
	private void handleStepSoundSpeed(){
		if(stepSoundId != 0)
			sounds.get(CharacterSound.STEPS).setPitch(stepSoundId, getSpeed()/12);
	}

	private void handleStandingUp(){
		if(flags.isForceStandUp() && flags.isCanStandUp()){
			standUp();
			flags.setForceStandUp(false);
		}
	}
	
	private void handleDying(){
		if(flags.isDieBottom()){
			dieBottom();
			flags.setDieBottom(false);
		}
		else if(flags.isDieTop()){
			dieTop();
			flags.setDieTop(false);
		}
		else if(flags.isDieDismemberment()){
			dieDismemberment();
			flags.setDieDismemberment(false);
		}
	}
	
	private void handleFlying(){
		if(flags.isShouldFly()){
			CustomAction action = new CustomAction(0.1f, 1, this) {
				@Override
				public void perform() {
					if(flags.isShouldFly()){
						animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
						flags.setBoostedOnce(false);
						if(stepSoundPlayed){
							sounds.get(CharacterSound.STEPS).stop();
							stepSoundPlayed = false;
						}
					}
				}
			};
			
			customActionManager.registerAction(action);
			if(flags.isStopFlyingAction()){
				action.setFinished(true);
			}
		}
	}
	
	private void handleInversedGravity(){

		if(flags.isGravityInversed()){
			body.applyForceToCenter(new Vector2(0, body.getMass()*120f), true);
		}
		
		if(flags.isGravityInversed() && flags.isGravityRotationSwitch()){
			
			//normal na nie-sensory
			body.getFixtureList().get(1).setSensor(false);
			body.getFixtureList().get(2).setSensor(false);

			//mirror na sensory
			body.getFixtureList().get(11).setSensor(true);
			body.getFixtureList().get(12).setSensor(true);
			
			for(int i = 1; i<=10; i++){
				((UserData)body.getFixtureList().get(i).getUserData()).ignoreContact = false;
			}
			for(int i = 11; i<=20; i++){
				((UserData)body.getFixtureList().get(i).getUserData()).ignoreContact = true;
			}
			
			flags.setGravityRotationSwitch(false);
			flags.setGravityInversed(false);
			gravityModificator = 1;
			
			customActionManager.registerAction(new CustomAction(0.0001f, 0, this) {
				@Override
				public void perform() {
					if(((Character)args[0]).getRotation() > 0)
						((Character)args[0]).setRotation(((Character)args[0]).getRotation()-20f);
					else{
						this.setFinished(true);
						flipX = false;
					}
				}
			});
			
			if(flags.getFootSensor() > 0){
				flags.decrementFootSensor();
			}
			if(flags.getJumpSensor() > 0){
				flags.decrementJumpSensor();
			}
			if(flags.getStandupSensor() < 0){
				flags.incrementStandupSensor();
			}
			if(flags.getHeadSensor() < 0){
				flags.incrementHeadSensor();
			}
		}
		else if(!flags.isGravityInversed() && flags.isGravityRotationSwitch()){
			
			//normal na sensory
			body.getFixtureList().get(1).setSensor(true);
			body.getFixtureList().get(2).setSensor(true);

			//mirror na nie-sensory
			body.getFixtureList().get(11).setSensor(false);
			body.getFixtureList().get(12).setSensor(false);

			for(int i = 1; i<=10; i++){
				((UserData)body.getFixtureList().get(i).getUserData()).ignoreContact = true;
			}
			for(int i = 11; i<=20; i++){
				((UserData)body.getFixtureList().get(i).getUserData()).ignoreContact = false;
			}
			
			flags.setGravityRotationSwitch(false);
			flags.setGravityInversed(true);
			gravityModificator = -1;
			
			customActionManager.registerAction(new CustomAction(0.0001f, 0, this) {
				@Override
				public void perform() {
					if(((Character)args[0]).getRotation() < 180)
						((Character)args[0]).setRotation(((Character)args[0]).getRotation()+20f);
					else{
						this.setFinished(true);
						flipX = true;
					}
				}
			});
			
			if(flags.getFootSensor() > 0){
				flags.decrementFootSensor();
			}
			if(flags.getJumpSensor() > 0){
				flags.decrementJumpSensor();
			}
			if(flags.getStandupSensor() < 0){
				flags.incrementStandupSensor();
			}
			if(flags.getHeadSensor() < 0){
				flags.incrementHeadSensor();
			}
		}
		
		if(!flags.isGravityInversed() && flags.isOnGround()){
			//this.setRotation(0);
			flipX = false;
		}
		else if(flags.isGravityInversed() && flags.isOnGround()){
			//this.setRotation(180);
			flipX = true;
		}
	}
	
	long coinsManager = 0;
	long flagsUpdate = 0;
	long queuedActions = 0;
	long actions = 0;
	long running = 0;
	long stopping = 0;
	long standingUp = 0;
	long stepSoundSpeed = 0;
	long dying = 0;
	long flying = 0;
	long inversedGravity = 0;
	long animationManagerr = 0;
	long size = 0;
	
	@Override
	public void act(float delta) {
    	if(flags.isMe()) if(CoinsManager.getInstance() != null) CoinsManager.getInstance().update();

		flags.update();
		handleQueuedActions();
		handleActions();
		handleRunning();
		handleStopping();
		handleStandingUp();
		handleStepSoundSpeed();
		handleDying();
		handleFlying();
		handleInversedGravity();

		currentFrame = animationManager.animate(delta);
		
        setPosition(body.getPosition().x + 10/PPM, body.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        //setRotation(body.getAngle() * MathUtils.radiansToDegrees);            
	}
	
//	public void handleGameFinished()
//	{
//		sounds.get(CharacterSound.VICTORY).play(0.4f);
//		gameIsEnded = true;
//	}
//	
	public Body getBody(){ return this.body; }
	public float getSpeed(){ return this.speed; }

	public abstract CharacterType getCharacterType();	

	
	//BUTTONS
	public CharacterButton getJumpButton(String buttonName){	
		CharacterButton button = new CharacterButton(buttonName, Runner.SCREEN_WIDTH - 20, 20);	
		button.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				flags.setQueuedJump(true);
		        return true;
		    }
		});
		button.setPosition(button.getX() - button.getWidth(), button.getY());
		
		return button;
	}
	
	public CharacterButton getSlideButton(String buttonName){	
		CharacterButton button = new CharacterButton(buttonName, 20, 20);	
		button.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				flags.setSlideButtonPressed(true);
				character.slide();
		        return true;
		    }
		});
		
		return button;
	}
	
	public CharacterButton getTempButton(String buttonName){	
		CharacterButton button = new CharacterButton(buttonName, Runner.SCREEN_WIDTH - 20, Runner.SCREEN_HEIGHT - 200);	
		button.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				if(!flags.isTempRunFlag())
					flags.setTempRunFlag(true);
				else
					flags.setTempRunFlag(false);
				
		        return true;
		    }
		});
		button.setPosition(button.getX() - button.getWidth(), button.getY());
		
		return button;
	}
	
	private Array<Button> initializePowerupButtons()
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
					if(flags.isCanUseAbility()) 
						character.usePowerup( powerupType );
						//tutaj powinna byc wyslana notyfikacja z typem umiejetnosci, wlascicielem i pozycja odpalenia
			        return true;
			    }
			});
			
			this.powerupButtons.add(button);
		}
		
		return this.powerupButtons;
	}
	
	public void usePowerup(PowerupType powerupType) 
	{
		if( powerupType == PowerupType.NONE )
		{
			//pass
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
			
			//removePowerup(PowerupType.ABILITY1); - to jest wykomentowane do testów ale ma tu byc
		}
		
		flags.setPowerupSet(false);
	}

	public PowerupType currentPowerupSet; //zmienna pomocnicza do sterowania klawiatura
	public void setPowerup(PowerupType powerupType) 
	{
		currentPowerupSet = powerupType;
		for(Button button: powerupButtons){
			if( ( (PowerupType)button.getUserObject() ) == powerupType ){
				button.setVisible(true);
			}
		}
		flags.setPowerupSet(true);
	}
	
	public void removePowerup(PowerupType powerupType) 
	{
		for(Button button: powerupButtons){
			if( ( (PowerupType)button.getUserObject() ) == powerupType ){
				button.setVisible(false);
			}
		}
		flags.setPowerupSet(false);
	}
	
	public void incrementCoinCounter(){ 
		coinCounter++;
	}
	public int getCoinCounter(){ return coinCounter; }
}

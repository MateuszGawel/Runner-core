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
import com.apptogo.runner.handlers.FlagsHandler;
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
	
	public FlagsHandler flags;
	
	private World world;
	private CustomActionManager customActionManager = CustomActionManager.getInstance();
	protected Body body;
	protected TextureRegion currentFrame;
	protected Character character = this;
	public AnimationManager animationManager;
	protected ArrayList<BodyMember> bodyMembers;
	
	protected HashMap<CharacterSound, Sound> sounds = new HashMap<CharacterSound, Sound>();
	protected boolean stepSoundPlayed;
	protected long stepSoundId;
	
	public float speed = 0;
	public float playerSpeedLimit = 12;
	public float playerMinSpeed = 3;
	public float playerSlowAmmount = 0;
	public float speedBeforeLand;
	
	protected Vector2 deathPosition;
	
	protected Skin guiSkin;
	protected String jumpButtonStyleName;
	protected String slideButtonStyleName; 
	protected String slowButtonStyleName;
	protected Array<Button> powerupButtons;
	
	protected boolean blinkShow = false;
	
	public Character(World world, String atlasName, String jumpButtonStyleName, String slideButtonStyleName, String slowButtonStyleName)
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
	}
	
	
    private void addSounds(){
    	ResourcesManager rm = ResourcesManager.getInstance();
    	BaseScreen cs = ScreensManager.getInstance().getCurrentScreen();
    	
    	sounds.put(CharacterSound.STEPS, (Sound)rm.getResource(cs, "mfx/game/characters/steps.ogg"));
    	sounds.put(CharacterSound.LAND, (Sound)rm.getResource(cs, "mfx/game/characters/land.ogg"));
    	sounds.put(CharacterSound.SLIDE, (Sound)rm.getResource(cs, "mfx/game/characters/slide.ogg"));
    }
    
	protected void createBody(){
		bodyMembers = new ArrayList<BodyMember>();
		
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
		shape.setAsBox(0.5f / PPM, 54.5f / PPM, new Vector2(24 / PPM, 1/PPM), 0);
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
	
	public void start()
	{Logger.log(this, flags.isBegan() );
	Logger.log(this, flags.isAlive() );
	Logger.log(this, flags.isFinished() );
	
		if(flags.isCanBegin())
		{		Logger.log(this, "START");
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
		flags.setSliding(false);
		
		layFixtures(false);

		float y = (float) sqrt(-world.getGravity().y * 8);
		float x = body.getLinearVelocity().x;
		body.setLinearVelocity(x*xMultiplier + xAdd, y*yMultiplier + yAdd);
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
		body.setLinearVelocity(x * 0.8f, y * 0.7f );
		flags.setDoubleJumped(true);
	}
	
	public void land()
	{
		if(flags.isCanLand())
		{
			flags.setDoubleJumped(false);
			
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
				else
					animationManager.setCurrentAnimationState(CharacterAnimationState.LANDINGIDLE);
			}
			sounds.get(CharacterSound.LAND).play(0.3f);
		}
	}
	
	public void boostAfterLand(){
		if(flags.isCanBoost()){
			body.setLinearVelocity(speedBeforeLand, 0);
			flags.setBoostedOnce(true);
			Logger.log(this, "BOOST! " + String.valueOf(speedBeforeLand));
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
			
			customActionManager.registerAction(new CustomAction(0.3f) {
				@Override
				public void perform() {
					flags.setMinimumSlidingTimePassed(true);
					if(!flags.isSlideButtonPressed()) standUp();
				}
			});
			NotificationManager.getInstance().notifySlide(getBody().getPosition());
		}
		else if(!flags.isOnGround()){
			body.setLinearVelocity( body.getLinearVelocity().x, -30f );
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
	{Logger.log(this, "DIE");
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
			
			flags.setBegan(false);
			
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
		
		final CustomAction blinkAction = new CustomAction(0.3f, 0) {
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
		
		flags.setImmortal(true);
		flags.setAlive(true);
		flags.update();
		
		body.setTransform(deathPosition, 0);
		start();
		
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
		
		if(flags.isQueuedBoost()){
			boostAfterLand();
			flags.setQueuedBoost(false);
		}
	}
	
	private void handleActions(){
		land();
	}
	
	private void handleStopping(){
		if(flags.isShouldStop()){		
			flags.setStopped(true);
			
			this.slowPlayerBy(1.0f);
			
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
		}
	}

	private void handleRunning(){
		speed = body.getLinearVelocity().x;
		if(flags.isCanRun()){ Logger.log(this, "CAN RUN");
			if(flags.isOnGround()){
				body.applyForceToCenter(new Vector2(3000, 0), true); Logger.log(this, "ON GROUND"); }
			else if(this.getBody().getLinearVelocity().x <= 0.01){
					body.setLinearVelocity( (playerSpeedLimit - playerSlowAmmount) * 0.5f, body.getLinearVelocity().y); Logger.log(this, "DUPA"); }
//					body.applyForceToCenter(new Vector2(3000 - 20*getBody().getLinearVelocity().x*getBody().getLinearVelocity().x, 0), true);
		}
		if(flags.isShouldChangeToRunningState())
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING); //czy to potrzebne?
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
	
	@Override
	public void act(float delta) 
	{
		flags.update();
		handleQueuedActions();
		handleActions();
		handleRunning();
		handleStopping();
		handleStandingUp();
		handleStepSoundSpeed();
		handleDying();
		handleFlying();
		//handleRotation();
		
		currentFrame = animationManager.animate(delta);

        setPosition(body.getPosition().x + 10/PPM, body.getPosition().y + 20/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        
        
        //Logger.log(this,  "SPEED = " + String.valueOf(body.getLinearVelocity().x));
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

	
	//POWERUPS
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
				flags.setQueuedJump(true);
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
				flags.setSlideButtonPressed(true);
				character.slide();
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
			{
				flags.setSlideButtonPressed(false);
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
					if(flags.isCanUseAbility()) 
						character.usePowerup( powerupType );
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
		
		flags.setPowerupSet(false);
	}

	public void setPowerup(PowerupType powerupType) 
	{
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
}

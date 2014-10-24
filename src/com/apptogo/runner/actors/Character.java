package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterSound;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.CharacterAction;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
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
	
	protected boolean me = false;
	protected boolean alive = true;
	protected boolean touchGround = false;
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
	protected boolean started = false;
	protected boolean dismemberment = false;
	protected boolean actDismemberment = false; //zdarzenie smierci odpala sie z contact listenera podczas world.step() a wtedy nie mozna korzystac z poola lub tworzyc obiektow
	protected boolean forceStandup = false;
	protected boolean shouldLand = false;

	protected HashMap<CharacterSound, Sound> sounds = new HashMap<CharacterSound, Sound>();
	protected HashMap<String, Boolean> soundInstances = new HashMap<String, Boolean>();
	protected boolean stepSoundPlayed = false;
	protected long stepSoundId = 0;
	
	protected boolean gameIsEnded = false;
	
	protected int wallSensor = 0;
	protected int footSensor = 0;
	protected int standupSensor = 0;
	protected int barrelSensor = 0;
	protected float speed = 0;
	protected float jumpHeight = 4;
	
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
	private List<CharacterAction> actions = new ArrayList<CharacterAction>();
	private List<CharacterAction> actionsCreated = new ArrayList<CharacterAction>();
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
		Vector2 bodySize = new Vector2(25 / PPM, 55 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
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
		
		//sliding fixture
		shape.setAsBox(bodySize.y -5/PPM, bodySize.x, new Vector2(-bodySize.x, -30/PPM), 0);
		fixtureDef = Materials.characterBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("player") );
		body.getFixtureList().get(1).setSensor(true);
		
		//wall sensor
		shape.setAsBox(5 / PPM, 50 / PPM, new Vector2(30 / PPM, 0), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("wallSensor") );
		
		
		//foot sensor
		shape.setAsBox(25 / PPM, 20 / PPM, new Vector2(-10 / PPM, -70 / PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("footSensor") );
		
		//slide sensor
		shape.setAsBox(bodySize.x-5/PPM, bodySize.y - 6/PPM, new Vector2(0, 8/PPM), 0);
		fixtureDef = Materials.characterSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( new UserData("standupSensor") );
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
			if(touchGround || touchBarrel)
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
			return true;
		}
		else return false;
	}
	
	public boolean jump()
	{
		if(/*started && */alive && canStandup && (touchWall || touchGround || touchBarrel || !me))
		{
			sliding = false;
			jumped = true;
			float v0 = (float) sqrt(-world.getGravity().y*2 * jumpHeight );
			body.setLinearVelocity( body.getLinearVelocity().x, v0 ); //wczesniej x bylo 0 i stad widoczny lag przy skoku :) ale teraz leci chyba za daleko jak sie rozpedzi z gorki :( - trzeba sprawdzic
			
			animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			sounds.get(CharacterSound.JUMP).play();
			Logger.log(this, "JUMP");
			return true;
		}
		else return false;
	}	
	public void land()
	{
		if(shouldLand)
		{
			Logger.log(this, "LAND");
			touchGround = true;
			jumped = false;
			animationManager.setCurrentAnimationState(CharacterAnimationState.LANDING);
			sounds.get(CharacterSound.LAND).play(0.3f);
			if(body.getLinearVelocity().x > 0.01f && !stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
		}
	}
	
	public void boostAfterLand(){
		if(shouldLand && running && !touchWall){
			body.setLinearVelocity(((UserData)getBody().getUserData()).speedBeforeLand, 0);
		}
	}
	
	public boolean slide()
	{
		if(started && alive && (touchGround || !me) && !sliding)
		{
			sliding = true;
			body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
			animationManager.setCurrentAnimationState(CharacterAnimationState.BEGINSLIDING);
			slowPlayerBy(0.5f);
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			sounds.get(CharacterSound.SLIDE).play();
			return true;
		}
		else return false;
	}
	
	public boolean standUp()
	{
		if(alive && sliding && canStandup){
			sliding = false;
			body.getFixtureList().get(0).setSensor(false);
			body.getFixtureList().get(1).setSensor(true);
			animationManager.setCurrentAnimationState(CharacterAnimationState.STANDINGUP);
			speedPlayerBy(0.5f);
			if(body.getLinearVelocity().x > 0.01f && !stepSoundPlayed){
				stepSoundId = sounds.get(CharacterSound.STEPS).loop();
				stepSoundPlayed = true;
			}
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
			body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
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
			body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
			body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
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
			registerAction(new CharacterAction(1f) {
				
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
		body.getFixtureList().get(0).setSensor(false);
		body.getFixtureList().get(1).setSensor(true);
		
		handleImmortality(2.0f);
		
		alive = true;
		body.setTransform(deathPosition, 0);
		
		playerSlowAmmount = 0;
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
	
	public void decrementFootSensor(){
		footSensor--;
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
	
	public void superJump()
	{
		float v0 = (float) sqrt(-world.getGravity().y*2 * (jumpHeight * 3) );
		body.setLinearVelocity( body.getLinearVelocity().x, v0 );
	}
	private void superRun()
	{
		body.applyForceToCenter(new Vector2(60000, 0), true);
	}
	
	
	/*--- HANDLERS ---*/
	public void handleImmortality(float immortalityLenght)
	{
		immortal = true;
		blinking = true;
		registerAction(new CharacterAction(immortalityLenght) {
			
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
				registerAction(new CharacterAction(0.08f) {
					@Override
					public void perform() {
						visible = false;
					}
				});
			}
			else if(blinking && !visible){
				registerAction(new CharacterAction(0.08f) {
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
		if(alive && Math.abs(speed) < 1f && !stopped && touchGround && !sliding && (animationManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING)){
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			if(stepSoundPlayed){
				sounds.get(CharacterSound.STEPS).stop();
				stepSoundPlayed = false;
			}
			stopped = true;
		}
		else if(alive && Math.abs(speed) > 3f && stopped && !sliding){
			if(touchGround){
				if(!stepSoundPlayed){
					stepSoundId = sounds.get(CharacterSound.STEPS).loop();
					stepSoundPlayed = true;
				}
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			}
			else
				animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);
			stopped = false;
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
		
		if(standupSensor > 0)
			canStandup = false;
		else
			canStandup = true;
		
		if(barrelSensor > 0){
			touchBarrel = true;
		}
		else
			touchBarrel = false;
	}
	
	/**Dodaj akcje ktora wykona perform po ustalonym delay*/
	public void registerAction(CharacterAction action){
		this.actionsCreated.add(action);
	}
	
	/**metoda obslugujaca slowy z ContactListnera*/
	private void handleSlowing(){
		playerSwampSlowAmmount = playerSpeedLimit * ((UserData)body.getUserData()).slowPercent;
	}
	
	private void handleActions(float delta){
		ListIterator<CharacterAction> iter = actions.listIterator();
		while(iter.hasNext()) {
		    CharacterAction action = iter.next();
		    action.act(delta);
		    if (action.isFinished()) {
		        iter.remove();
		    }
		}
	}
	
	private void handleRunning(){
		speed = body.getLinearVelocity().x;
		if(running && (speed < playerSpeedLimit - playerSlowAmmount - playerSwampSlowAmmount || speed <= playerMinSpeed))
			//body.setLinearVelocity(playerSpeedLimit, 0);
			body.applyForceToCenter(new Vector2(3000, 0), true);
	}
	
	private void handleStepSoundSpeed(){
		sounds.get(CharacterSound.STEPS).setPitch(stepSoundId, getSpeed()/10);
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
		if(alive && !sliding && (touchGround || touchBarrel) && animationManager.getCurrentAnimationState() == CharacterAnimationState.JUMPING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYBOMB)
			shouldLand = true;
		else
			shouldLand = false;
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
		if(!touchGround && !touchWall && !touchBarrel){
			registerAction(new CharacterAction(this, 0.1f, 1) {
				
				@Override
				public void perform() {
					if(!touchGround && !touchWall && !touchBarrel && alive){
						animationManager.setCurrentAnimationState(CharacterAnimationState.FLYING);
						if(stepSoundPlayed){
							sounds.get(CharacterSound.STEPS).stop();
							stepSoundPlayed = false;
						}
					}
						
				}
			});
		}
	}

	@Override
	public void act(float delta) 
	{
		for(CharacterAction action : actionsCreated){
			actions.add(action);
		}
		actionsCreated.clear();
		
		handleBlinking();
		handleStopping();
		handleSensors();
		handleSlowing();
		handleRunning();
		handleDismemberment();
		handleActions(delta);
		handleStandingUp();
		handleStepSoundSpeed();
		handleDying();
		handleShouldLand();
		handleFlying();
		
		currentFrame = animationManager.animate(delta);
		
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
		
		jumpButton.setPosition(Runner.SCREEN_WIDTH - jumpButton.getWidth() - 20, jumpButton.getHeight() + 20 + 40);
		jumpButton.setSize(jumpButton.getWidth(), jumpButton.getHeight());
		jumpButton.setBounds(jumpButton.getX(), jumpButton.getY(), jumpButton.getWidth(), jumpButton.getHeight());
		
		jumpButton.addListener(new InputListener() 
		{
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
			{
				if( character.jump() )
				{
					NotificationManager.getInstance().notifyJump(getBody().getPosition());
				}
				
		        return true;
		    }
		});
		
		return jumpButton;
	}
	public Button getSlideButton()
	{
		Button slideButton = new Button(guiSkin, this.slideButtonStyleName);
		
		slideButton.setPosition(Runner.SCREEN_WIDTH - slideButton.getWidth() - 20, 20);
		slideButton.setSize(slideButton.getWidth(), slideButton.getHeight());
		slideButton.setBounds(slideButton.getX(), slideButton.getY(), slideButton.getWidth(), slideButton.getHeight());
		
		slideButton.addListener(new InputListener() {
			@Override
		    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) 
			{
				if( character.slide() )
				{
					NotificationManager.getInstance().notifySlide(getBody().getPosition());
				}
		        return true;
		    }
			@Override
		    public void touchUp (InputEvent event, float x, float y, int pointer, int button) 
			{
				if( character.standUp() )
				{
					NotificationManager.getInstance().notifyStandUp(getBody().getPosition());
				}
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
		else if( powerupType == PowerupType.SUPERJUMP )
		{
			character.superJump();
		}
		else if( powerupType == PowerupType.SUPERSPEED )
		{
			character.superRun();
		}
		
		character.setPowerup(PowerupType.NONE);
		this.isPowerupSet  = false;
	}

	public void setPowerup(PowerupType powerupType) 
	{
		for(Button button: powerupButtons)
		{
			if( ( (PowerupType)button.getUserObject() ) == powerupType )
			{
				button.toFront();
			}
		}
		
		if( powerupType != PowerupType.NONE )
		{
			this.isPowerupSet  = true;
		}
	}

	public boolean isPowerupSet() 
	{
		return this.isPowerupSet;
	}
	public void setMe(boolean me){ this.me = me; }

}

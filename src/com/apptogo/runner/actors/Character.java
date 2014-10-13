package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.appwarp.NotificationManager;
import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.CharacterAnimationState;
import com.apptogo.runner.enums.CharacterType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.handlers.CharacterAction;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.userdata.UserData;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public abstract class Character extends Actor{
	private World world;
	
	protected boolean me = false;
	protected boolean alive = true;
	protected boolean touchGround = false;
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
	
	protected boolean gameIsEnded = false;
	
	protected int wallSensor = 0;
	protected int footSensor = 0;
	protected int standupSensor = 0;
	protected float speed = 0;
	protected float jumpHeight = 4;
	
	protected float playerSpeedLimit = 12;
	protected float playerMinSpeed = 2;
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
		if(!running && alive && touchGround && !gameIsEnded)
		{			
			running = true;
			started = true;
			animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
			return true;
		}
		else return false;
	}
	
	private void testMethod(){
		registerAction(new CharacterAction(this, 2f) {
			@Override
			public void perform() {
				Logger.log(this, "akcja 2");
			}
		});
		registerAction(new CharacterAction(this, 5f) {
			@Override
			public void perform() {
				Logger.log(this, "akcja 5");
			}
		});
		registerAction(new CharacterAction(this, 8f) {
			@Override
			public void perform() {
				Logger.log(this, "akcja 8");
			}
		});
	}
	
	public boolean jump()
	{
		//testMethod();
		if(/*started && */alive && (touchWall || touchGround || !me))
		{
			sliding = false;
			jumped = true;
			float v0 = (float) sqrt(-world.getGravity().y*2 * jumpHeight );
			body.setLinearVelocity( body.getLinearVelocity().x, v0 ); //wczesniej x bylo 0 i stad widoczny lag przy skoku :) ale teraz leci chyba za daleko jak sie rozpedzi z gorki :( - trzeba sprawdzic
			
			animationManager.setCurrentAnimationState(CharacterAnimationState.JUMPING);

			return true;
		}
		else return false;
	}	
	public void land()
	{
		if(alive && !sliding && footSensor > 0)
		{
			if(animationManager.getCurrentAnimationState() == CharacterAnimationState.JUMPING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYING || animationManager.getCurrentAnimationState() == CharacterAnimationState.FLYBOMB)
			{
				touchGround = true;
				jumped = false;
				animationManager.setCurrentAnimationState(CharacterAnimationState.LANDING);
			}
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
			
			return true;
		}
		else if(!canStandup){
			forceStandup = true;
			return true;
		}
		else return false;
	}

	public boolean dieTop()
	{
		boolean success = die();
		animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGTOP);
		body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
		body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
		return success;
	}

	public boolean dieBottom()
	{
		boolean success = die();
		animationManager.setCurrentAnimationState(CharacterAnimationState.DIEINGBOTTOM);
		body.getFixtureList().get(0).setSensor(true); //wy³¹cz kolizje stoj¹cego body
		body.getFixtureList().get(1).setSensor(false); //w³¹cz kolizjê le¿¹cego body
		return success;
	}

	public boolean dieDismemberment()
	{
		dismemberment = true;
		visible = false;
		actDismemberment = true;   
		boolean success = die();
			
		return success;
	}
	
	private boolean die()
	{
		if(alive)
		{
			alive = false;
			running = false;
			sliding = false;
			jumped = false;

			deathPosition = new Vector2(body.getPosition());
			
			Timer.schedule(new Task() {
				@Override
				public void run() {
					dismemberment = false;
					respawn();
				}
			}, 1);
			
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
		
		start();
	}
	
	abstract public void useAbility(CharacterAbilityType abilityType);
		
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
	public void incrementStandupSensor(){
		standupSensor++;
	}
	
	public void decrementStandupSensor(){
		standupSensor--;
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
	/*--- HANDLERS ---*/
	public void handleImmortality(float immortalityLenght)
	{
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
	
	private void handleBlinking()
	{
		setVisible(visible);
		
		if(!dismemberment){
			if(blinking && visible){
				Timer.schedule(new Task() {
					@Override
					public void run() {
						visible = false;
					}
				}, 0.08f);
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
	}
	
	private void handleStopping(){
		if(alive && speed < 0.001f && !stopped && touchGround && !sliding && (animationManager.getCurrentAnimationState() == CharacterAnimationState.RUNNING)){
			animationManager.setCurrentAnimationState(CharacterAnimationState.IDLE);
			stopped = true;
		}
		else if(alive && speed > 0.001f && stopped && !sliding){
			if(touchGround)
				animationManager.setCurrentAnimationState(CharacterAnimationState.RUNNING);
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
	}
	
	/**Dodaj akcje ktora wykona perform po ustalonym delay*/
	public void registerAction(CharacterAction action){
		this.actions.add(action);
	}
	
	/**metoda obslugujaca slowy z ContactListnera*/
	private void handleSlowing(){
		playerSwampSlowAmmount = playerSpeedLimit * ((UserData)body.getUserData()).slowPercent;
	}
	
	private void handleActions(float delta){
		Iterator<CharacterAction> iter = actions.listIterator();
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
			body.applyForceToCenter(new Vector2(3000, 0), true);
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

	@Override
	public void act(float delta) 
	{
		handleBlinking();
		handleStopping();
		handleSensors();
		handleSlowing();
		handleRunning();
		handleDismemberment();
		handleActions(delta);
		handleStandingUp();
		
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
		gameIsEnded = true;
	}
	
	public abstract CharacterType getCharacterType();	

	/* do zaimplementowania potem przy umiejetnosciach klasowych
	public abstract Button getAbilityButton();
	*/
	
	public Button getJumpButton()
	{
		Button jumpButton = new Button(guiSkin, this.jumpButtonStyleName);
		
		jumpButton.setPosition(Runner.SCREEN_WIDTH/PPM - jumpButton.getWidth()/PPM - 20/PPM, jumpButton.getHeight()/PPM + 20/PPM + 40/PPM);
		jumpButton.setSize(jumpButton.getWidth()/PPM, jumpButton.getHeight()/PPM);
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
		
		slideButton.setPosition(Runner.SCREEN_WIDTH/PPM - slideButton.getWidth()/PPM - 20/PPM, 20/PPM);
		slideButton.setSize(slideButton.getWidth()/PPM, slideButton.getHeight()/PPM);
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

package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Boar extends Actor implements Poolable{

	public boolean alive;
	private Body bodylvl1, bodylvl2, bodylvl3;
	private AnimationManager animationManager;
	
	//parameters
	private int level;
	private AtlasRegion currentFrame;
	private TextureAtlas atlas;
	private CustomAction despawnAction;
	private World world;
	private CustomAction jumpAction, forceJumpAction;
	private float direction = 1;
	
	public enum BoarAnimationState{
		LVL1, LVL2, LVL3
	}

	private void setAnimationState(){
		switch(level){
			case 1:
				animationManager.setCurrentAnimationState(BoarAnimationState.LVL1);
				break;
			case 2:
				animationManager.setCurrentAnimationState(BoarAnimationState.LVL2);
				break;
			case 3:
				animationManager.setCurrentAnimationState(BoarAnimationState.LVL3);
				break;
		}
	}
	
	private Body getBody(){
		switch(level){
		case 1:
			return bodylvl1;
		case 2:
			return bodylvl2;
		case 3: 
			return bodylvl3;
		default:
			return bodylvl1;
		}
	}

	private float getBodyHeight(){
		switch(level){
		case 1:
			return 0.5f;
		case 2:
			return 0.25f;
		case 3: 
			return 1f;
		default:
			return 0.5f;
		}
	}
	
	private Vector2 getSpeed(){
		switch(level){
		case 1:
			return new Vector2(12f, getBody().getLinearVelocity().y);
		case 2:
			return new Vector2(new Random().nextInt(10)+14, getBody().getLinearVelocity().y);
		case 3: 
			return new Vector2(5f, getBody().getLinearVelocity().y);
		default:
			return new Vector2(12f, getBody().getLinearVelocity().y);
		}
	}
	
	public Boar(World world, GameWorld gameWorld){
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        animationManager = new AnimationManager();
        this.world = world;
		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL1, animationManager.createFrames(19, "boar1lvl"), true));	
		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL2, animationManager.createFrames(19, "boar1lvl"), true));	
		animationManager.createAnimation(new MyAnimation(0.03f, BoarAnimationState.LVL3, animationManager.createFrames(19, "boar1lvl"), true));	
		setAnimationState();	
		
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		bodylvl1 = createBody(Materials.boarBody, 1f, 0.5f);
		createFixture(Materials.boarSensor, 0.1f, 0.4f, 1f, bodylvl1);
		createFixture(Materials.boarSensor, 0.1f, 0.4f, -1f, bodylvl1);

		bodylvl2 = createBody(Materials.boarBody, 0.5f, 0.25f);
		createFixture(Materials.boarSensor, 0.1f, 0.2f, 0.5f, bodylvl2);
		createFixture(Materials.boarSensor, 0.1f, 0.2f, -0.5f, bodylvl2);
		
		bodylvl3 = createBody(Materials.boarBody, 1.5f, 1f);
		createFixture(Materials.boarSensor, 0.1f, 0.8f, 1.5f, bodylvl3);
		createFixture(Materials.boarSensor, 0.1f, 0.8f, -1.5f, bodylvl3);
	
		setScale(0);
		gameWorld.getWorldStage().addActor(this);
		
		jumpAction = new CustomAction((new Random().nextFloat()+1), 0) {
			@Override
			public void perform() {
				getBody().setLinearVelocity(new Vector2(getBody().getLinearVelocity().x, 20f+new Random().nextInt(10)));
			}
		};
		
		forceJumpAction = new CustomAction(0.5f, 0) {
			@Override
			public void perform() {
				getBody().setLinearVelocity(new Vector2(getBody().getLinearVelocity().x, 30f));
			}
		};
	}
	
	
	public Body createBody(FixtureDef fixtureDef, float boxWidth, float boxHeight)
	{
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.fixedRotation = true;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight);
		UserData userData = new UserData("boar");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;

		userData.bodyWidth = shapeWidth;
		
		fixtureDef.shape = shape;
		
		Body body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData( userData );
		body.setUserData(new UserData(userData));
		
		return body;
	}
	
	public void createFixture(FixtureDef fixtureDef, float boxWidth, float boxHeight, float xOffset, Body body)
	{

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(boxWidth, boxHeight, new Vector2(xOffset, 0), 0);
		fixtureDef.shape = shape;
		
		Fixture fixture = body.createFixture(fixtureDef);
		fixture.setUserData( new UserData("boarSensor") );
	}
	
    private void disappear(){
  	
    	CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 10) {
    		float scale = getScaleX();
			
			@Override
			public void perform() {
				scale -= 0.1f;
				if(scale>=0){
					setScale(scale);

				}
			}
			@Override
			public void onFinish(){
				setVisible(false);
				reset();
				setScale(0);
			}
		});
    }
    
    private void appear(){
    	CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 10) {
			float scale = 0;
			
			@Override
			public void perform() {
				scale += 0.1f;
				setScale(scale);
			}
		});
    }

    public void init(Character characterOwner, int level) {
    	this.level = level;
    	setAnimationState();
    	currentFrame = (AtlasRegion)animationManager.animate(0f);
    	alive = true;
    	((UserData)getBody().getFixtureList().get(0).getUserData()).abilityLevel = level;
    	((UserData)getBody().getFixtureList().get(0).getUserData()).playerName = characterOwner.playerName;
    	if(level==2) getBody().setTransform(characterOwner.getX()+new Random().nextInt(6)-3, characterOwner.getY()+getBodyHeight(), 0);
    	else getBody().setTransform(characterOwner.getX()+0.5f, characterOwner.getY()+getBodyHeight(), 0);
    	setVisible(true);
		appear();
		getBody().setLinearVelocity(new Vector2(0, 0));

        CustomActionManager.getInstance().registerAction(new CustomAction(4) {	
			@Override
			public void perform() {
				disappear();
			}
		});
    	if(level == 2){
        	CustomActionManager.getInstance().registerAction(jumpAction);
    	}
    	if(level == 3){
    		direction = -1;
    	}
    }
    
    private float xSpeed;
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(alive){
    		Vector2 speed = getSpeed();
    		if(xSpeed==0)
    			xSpeed = speed.x;
    		getBody().setLinearVelocity(new Vector2(xSpeed*direction, speed.y));
    	}
    	else if(level == 2){
            CustomActionManager.getInstance().unregisterAction(jumpAction);
    	}
    	currentFrame = (AtlasRegion)animationManager.animate(delta);
    	if(currentFrame != null){
	    	setPosition(getBody().getPosition().x, getBody().getPosition().y);
	        setWidth(currentFrame.getRegionWidth() / PPM);
	        setHeight(currentFrame.getRegionHeight() / PPM);
	        setOrigin(getWidth()/2, getHeight()/2);
    	}
    	handleSwitchingDirection();
    	handleForceJumping();
    }
    
    private boolean switchBlocked;
    private void handleSwitchingDirection(){
    	if(alive && !switchBlocked && ((UserData)getBody().getUserData()).changeDirection && level != 2){
    		((UserData)getBody().getUserData()).changeDirection = false;
    		switchBlocked = true;
    		direction*=-1;
    		
        	CustomActionManager.getInstance().registerAction(new CustomAction(0.5f) {
    			@Override
    			public void perform() {
    				switchBlocked = false;
    			}
    		});		
    	}
    }
    
    private boolean forceJumpRegistered;
    private void handleForceJumping(){
		if(!forceJumpRegistered && alive && level == 2 && ((UserData)getBody().getUserData()).changeDirection){
			CustomActionManager.getInstance().registerAction(forceJumpAction);
			forceJumpRegistered = true;
		}
		else if(forceJumpRegistered && level == 2 && !((UserData)getBody().getUserData()).changeDirection){
			CustomActionManager.getInstance().unregisterAction(forceJumpAction);
			forceJumpRegistered = false;
		}
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
		AtlasRegion currentRegion = (AtlasRegion)currentFrame;
		batch.draw(currentFrame.getTexture(),  //Texture texture
				   getX() + ( (currentRegion.offsetX) / PPM) - currentRegion.originalWidth/2/PPM, //float x
                   getY() + ( (currentRegion.offsetY) / PPM) - currentRegion.originalHeight/2/PPM, //float y
                   getOriginX(),  //float originX
                   getOriginY(),  //float originY
                   getWidth(),    //float width
                   getHeight(),   //float height
                   getScaleX(),             //float scaleX
                   getScaleY(),             //float scaleY
                   getRotation(), //float rotation
                   currentFrame.getRegionX(), //int srcX
                   currentFrame.getRegionY(), //int srcY
                   currentFrame.getRegionWidth(), //int srcWidth
                   currentFrame.getRegionHeight(),//int srcHeight 
                   direction==-1, //boolean flipX
                   false  //boolean flipY
                  );
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public void reset() {
		setScale(0);
		animationManager.getCurrentAnimation().resetLoops();
		getBody().setTransform(-100, 0, 0);
        alive = false;
        direction = 1;
	}
}

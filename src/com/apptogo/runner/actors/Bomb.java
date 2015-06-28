package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.actors.Mushroom.MushroomAnimationState;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Bomb extends Obstacle implements Poolable{

	public boolean alive;
	private Body currentExplodeSensor;
	private Body explodeSensorOne, explodeSensorTwo, explodeSensorThree;
	private boolean exploding;
	
	//parameters
	private int level = 3;
	private float timeToExplode = 2;
	
	private CustomAction explodeAction, explodingAction;
	public ParticleEffectActor currentExplosionParticle;
	public ParticleEffectActor explosionParticleOne, explosionParticleTwo, explosionParticleThree;
	public enum BombAnimationState{
		LVL1, LVL2, LVL3
	}
	
	
	private String getRegionName(){
		if(level==1)
			return "bomblvlone";
		else if(level==2)
			return "bomblvltwo";	
		else if(level==3)
			return "bomblvlthree";
		else return "bomblvlone";
	}
	
	private ParticleEffectActor getParticleEffectActor(){
		switch(level){
			case 1:
				return explosionParticleOne;
			case 2:
				return explosionParticleTwo;
			case 3:
				return explosionParticleThree;
			default:
				return explosionParticleOne;
			
		}
	}
	
	private Body getExplosionSensor(){
		switch(level){
			case 1:
				return explodeSensorOne;
			case 2:
				return explodeSensorTwo;
			case 3:
				return explodeSensorThree;
			default:
				return explodeSensorOne;
			
		}
	}
	
	private void setAnimationState(){
		switch(level){
			case 1:
				animationManager.setCurrentAnimationState(BombAnimationState.LVL1);
				break;
			case 2:
				animationManager.setCurrentAnimationState(BombAnimationState.LVL2);
				break;
			case 3:
				animationManager.setCurrentAnimationState(BombAnimationState.LVL3);
				break;
		}
	}
	
	public Bomb(World world, GameWorld gameWorld){
		super(new EllipseMapObject(0,0,20,20), world, "gfx/game/characters/charactersAtlas.pack");
		
		this.gameWorld = gameWorld;
		setOffset(-10/PPM, -8/PPM);
		

//		explosionParticleOne = new ParticleEffectActor("explosion_lvl1.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
//		explosionParticleTwo = new ParticleEffectActor("explosion_lvl2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
//		explosionParticleThree = new ParticleEffectActor("explosion_lvl3.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		explosionParticleOne = new ParticleEffectActor("explosion_lvl1.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		explosionParticleTwo = new ParticleEffectActor("explosion_lvl2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));
		explosionParticleThree = new ParticleEffectActor("explosion_lvl3.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack"));

		
		createBody(BodyType.DynamicBody, Materials.bombBody, "bomb");
		createExplosions();
		
		animationManager.createAnimation(new MyAnimation(0.1f, BombAnimationState.LVL1, animationManager.createFrames(5, "bomblvl1"), true));
		animationManager.createAnimation(new MyAnimation(0.1f, BombAnimationState.LVL2, animationManager.createFrames(5, "bomblvl2"), true));
		animationManager.createAnimation(new MyAnimation(0.1f, BombAnimationState.LVL3, animationManager.createFrames(5, "bomblvl3"), true));
		
		
		
		
		gameWorld.getWorldStage().addActor(this);
		
		
		currentFrame = animationManager.animate(0f);
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	
		if(this.level == 1)
			setOrigin(6/PPM, 5/PPM);
		else
			setOrigin(10/PPM, 8/PPM);
		
        body.setTransform(characterOwner.getX()-20/PPM, characterOwner.getY(), 0);
        gameWorld.getWorldStage().addActor(getParticleEffectActor());
        setAnimationState();
        body.setLinearVelocity(characterOwner.getSpeed()/3, 0);
        body.getFixtureList().get(0).setUserData(new UserData("bomb", characterOwner.playerName));
        getExplosionSensor().getFixtureList().get(0).setUserData(new UserData("bombExplosion", characterOwner.playerName));
        alive = true;
        setVisible(true);
        explodingAction = new CustomAction(0.5f) {	
			@Override
			public void perform() {
				exploding=false;
	    		reset();
			}
		};
		explodeAction = new CustomAction(timeToExplode) {	
			@Override
			public void perform() {
				exploding=true;
				setVisible(false);
				CustomActionManager.getInstance().registerAction(explodingAction);
				getParticleEffectActor().obtainAndStart(body.getPosition().x+10/PPM, body.getPosition().y+10/PPM, 0);
			}
		};
		
		CustomActionManager.getInstance().registerAction(explodeAction);	
		
    }
    
    private void explode(){
    	if(!exploding){
	    	CustomActionManager.getInstance().unregisterAction(explodeAction);
	    	CustomActionManager.getInstance().registerAction(explodingAction);	
	    	setVisible(false);
	    	getParticleEffectActor().obtainAndStart(body.getPosition().x+10/PPM, body.getPosition().y+10/PPM, 0);
	    	exploding = true;
    	}
    }
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(exploding){ 		 		
    		Vector2 offset = new Vector2((float)Math.sqrt(2)*10/PPM, 0).setAngle((body.getAngle())*(float)(180/Math.PI)+45);
    		getExplosionSensor().setTransform(body.getPosition().x+offset.x, body.getPosition().y+offset.y, body.getAngle());
    	}
    	else if(getExplosionSensor().getPosition().x > -100)
    		getExplosionSensor().setTransform(-100, 0, 0);
    	
    	if(((UserData)body.getUserData()).collected){
    		explode();
    		((UserData)body.getUserData()).collected = false;
    	}
    }
    
    private void createExplosions(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		FixtureDef fixtureDef = Materials.obstacleSensor;
		CircleShape shape = new CircleShape();
		shape.setPosition(new Vector2(0, 0));
		shape.setRadius(2);
		fixtureDef.shape = shape;
		
		explodeSensorOne = world.createBody(bodyDef);
		explodeSensorOne.createFixture(fixtureDef);
		explodeSensorOne.setTransform(-100, 0, 0);
		
		shape.setRadius(3);
		explodeSensorTwo = world.createBody(bodyDef);
		explodeSensorTwo.createFixture(fixtureDef);
		explodeSensorTwo.setTransform(-100, 0, 0);
		
		shape.setRadius(5);
		explodeSensorThree = world.createBody(bodyDef);
		explodeSensorThree.createFixture(fixtureDef);
		explodeSensorThree.setTransform(-100, 0, 0);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		body.setTransform(position, 0);
		getParticleEffectActor().remove();
        alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}

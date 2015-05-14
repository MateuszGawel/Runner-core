package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class BlackHole extends Actor implements Poolable{

	public boolean alive;
	private Body bodyLvl1, bodyLvl2, bodyLvl3;
	private TextureRegion currentRegion;
	
	//parameters
	private int level = 1;
	private TextureAtlas atlas;
	private ParticleEffectActor particleLvl2, particleLvl3;
	private ParticleEffect pooledEffect;
	private CustomAction despawnAction;
	
	private ParticleEffectActor getParticleEffect(){
		switch(level){
		case 1:
			return null;
		case 2:
			return particleLvl2;
		case 3:
			return particleLvl3;
		default:
			return particleLvl2;
			
		}
	}
	
	private Body getCurrentBody(){
		switch(level){
		case 1:
			return bodyLvl1;
		case 2:
			return bodyLvl2;
		case 3:
			return bodyLvl3;
		default:
			return bodyLvl1;
			
		}
	}
	
	private float getSize(){
		switch(level){
		case 1:
			return 220;
		case 2:
			return 300;
		case 3:
			return 440;
		default:
			return 220;
			
		}
	}
	private float getSpeed(){
		switch(level){
		case 1:
			return 20;
		case 2:
			return 23;
		case 3:
			return 13;
		default:
			return 20;
			
		}
	}
	
	public BlackHole(World world, GameWorld gameWorld){
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        currentRegion = atlas.findRegion("blackhole");
		
        gameWorld.getWorldStage().addActor(this);
        
        BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		CircleShape shape = new CircleShape();
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		UserData userData = new UserData("blackhole");
		userData.bodyWidth = shapeWidth;
		FixtureDef fixtureDef;
		fixtureDef = Materials.obstacleSensor;
		fixtureDef.shape = shape;
		
		shape.setRadius(1.5f);
		bodyLvl1 = world.createBody(bodyDef);
		bodyLvl1.createFixture(fixtureDef).setUserData(userData);
		
		shape.setRadius(2f);
		bodyLvl2 = world.createBody(bodyDef);
		bodyLvl2.createFixture(fixtureDef).setUserData(userData);
		
		shape.setRadius(3f);
		bodyLvl3 = world.createBody(bodyDef);
		bodyLvl3.createFixture(fixtureDef).setUserData(userData);
		
		particleLvl2 = new ParticleEffectActor("blackHole2.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		particleLvl2.setCulling(false);
		gameWorld.getWorldStage().addActor(particleLvl2);
		
		particleLvl3 = new ParticleEffectActor("blackHole3.p", 1, 4, 1, 1/PPM, (TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), GameWorldType.convertToAtlasPath(gameWorld.gameWorldType)));
		gameWorld.getWorldStage().addActor(particleLvl3);
		particleLvl3.setCulling(false);
		
	}
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	setScale(1);
    	alive = true;
    	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).alive = true;
    	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).playerName = characterOwner.playerName;
    	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).abilityLevel = level;
    	getCurrentBody().setTransform(characterOwner.getX(), characterOwner.getY(), 0);
    	getCurrentBody().setLinearVelocity(getSpeed(), 0);
    	getCurrentBody().setAngularVelocity(3f);
    	setWidth(getSize() / PPM);
        setHeight(getSize() / PPM);
    	setOrigin(getWidth()/2, getHeight()/2);
    	
    	if(level != 1){
	    	pooledEffect = getParticleEffect().obtainAndStart(getCurrentBody().getPosition().x, getCurrentBody().getPosition().y, 0);
	    	getParticleEffect().toFront();
    	}
    	
    	
    	despawnAction = new CustomAction(4.f, 1) {
			
			@Override
			public void perform() {
				registerReset();
			}
		};
    	
    	CustomActionManager.getInstance().registerAction(despawnAction);
    }
    
    private void registerReset(){
    	CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 0) {
			float scale = 1;
			
			@Override
			public void perform() {
				
				if(scale >= 0){
					scale -= 0.1f;
					setScale(scale);
				}
				else{
					setFinished(true);		
					reset();
				}
			}
		});
    }
    //unregister despawn nie dziala
    //nie pojawia sie teleport wroga, pewnie pozycja zla
    @Override
    public void act(float delta){
    	super.act(delta);
    	setPosition(getCurrentBody().getPosition().x - getWidth()/2, getCurrentBody().getPosition().y - getHeight()/2);
    	if(alive){
	        setRotation((float)Math.toDegrees(getCurrentBody().getAngle()));
	        if(level!=1){ 
	        	getParticleEffect().setPosition(getCurrentBody().getPosition().x, getCurrentBody().getPosition().y);
	        	pooledEffect.setPosition(getCurrentBody().getPosition().x, getCurrentBody().getPosition().y);
	        }
	        if(((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).active){
	        	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).active = false;
	        	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).alive = false;
	        	CustomActionManager.getInstance().unregisterAction(despawnAction);
	        	despawnAction.resetAction();
	        	registerReset();
	        }
    	}
    }
	
    @Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
  
	@Override
	public void reset() {
		getCurrentBody().setTransform(-100, 0, 0);
        alive = false;
        getParticleEffect().freeEffect((PooledEffect)pooledEffect);
	}

	public void setLevel(int level) {
		this.level = level;
	}
}

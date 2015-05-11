package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

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
	private TextureRegion regionLvl1, regionLvl2, regionLvl3;
	
	//parameters
	private int level = 1;
	private TextureAtlas atlas;
	
	private TextureRegion getCurrentRegion(){
		switch(level){
		case 1:
			return regionLvl1;
		case 2:
			return regionLvl2;
		case 3:
			return regionLvl3;
		default:
			return regionLvl1;
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
	
	private float getSpeed(){
		switch(level){
		case 1:
			return 20;
		case 2:
			return 23;
		case 3:
			return 28;
		default:
			return 20;
			
		}
	}
	
	public BlackHole(World world, GameWorld gameWorld){
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        regionLvl1 = atlas.findRegion("blackhole");
        regionLvl2 = atlas.findRegion("blackhole");
        regionLvl3 = atlas.findRegion("blackhole");
		
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
    	
    	setOrigin(getCurrentRegion().getRegionWidth()/2/PPM, getCurrentRegion().getRegionHeight()/2/PPM);
    }
    

    @Override
    public void act(float delta){
    	super.act(delta);
    	setPosition(getCurrentBody().getPosition().x - getCurrentRegion().getRegionWidth()/2/PPM, getCurrentBody().getPosition().y - getCurrentRegion().getRegionHeight()/2/PPM);
    	if(alive){
	        setWidth(getCurrentRegion().getRegionWidth() / PPM);
	        setHeight(getCurrentRegion().getRegionHeight() / PPM);
	        setRotation((float)Math.toDegrees(getCurrentBody().getAngle()));
	        if(((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).active){
	        	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).active = false;
	        	((UserData)getCurrentBody().getFixtureList().get(0).getUserData()).alive = false;
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
    	}
    }
	
    @Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(getCurrentRegion(), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	@Override
	public void reset() {
		getCurrentBody().setTransform(-100, 0, 0);
        alive = false;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}

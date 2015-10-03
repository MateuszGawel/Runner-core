package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.AnimationManager;
import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.MyTiledMapRendererActorFrontLayer;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Snares extends Obstacle implements Poolable{

	public boolean alive;
	private Body body;
	
	//parameters
	private int level = 1;
	private AnimationManager animationManager;
	private AtlasRegion currentRegion;
	private boolean animate;
	private boolean active;
	
	public enum SnaresAnimationState{
		LVL1, LVL2, LVL3
	}

	private void setAnimationState(){
		switch(level){
			case 1:
				animationManager.setCurrentAnimationState(SnaresAnimationState.LVL1);
				break;
			case 2:
				animationManager.setCurrentAnimationState(SnaresAnimationState.LVL2);
				break;
			case 3:
				animationManager.setCurrentAnimationState(SnaresAnimationState.LVL3);
				break;
		}
	}
	
	public Snares(World world, GameWorld gameWorld){
		this.gameWorld = gameWorld;
		animationManager = new AnimationManager();
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.25f, 0.10f);
		UserData userData = new UserData("snares");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;
		
		FixtureDef fixtureDef = Materials.snaresBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( userData );
		
		fixtureDef = Materials.snaresSensor;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( userData );
		
		body.setUserData( userData );
		
		
		animationManager.createAnimation(new MyAnimation(0.02f, SnaresAnimationState.LVL1, animationManager.createFrames(8, "snareslvl1"), false));	
		animationManager.createAnimation(new MyAnimation(0.02f, SnaresAnimationState.LVL2, animationManager.createFrames(8, "snareslvl2"), false));	
		animationManager.createAnimation(new MyAnimation(0.02f, SnaresAnimationState.LVL3, animationManager.createFrames(8, "snareslvl3"), false));	
		setAnimationState();
		gameWorld.getWorldStage().addActor(this);
		
		
	}
	
	
    public void init(Character characterOwner, int level) {
    	this.level = level;
    	alive = true;
    	((UserData)body.getFixtureList().get(1).getUserData()).abilityLevel = level;
    	((UserData)body.getFixtureList().get(1).getUserData()).playerName = characterOwner.playerName;
        body.setTransform(characterOwner.getX()-10/PPM, characterOwner.getY()+50/PPM, 0);
        setAnimationState();
        currentRegion = (AtlasRegion)animationManager.animate(0f);
        for (Actor actor : gameWorld.getWorldStage().getActors()){
        	if(actor instanceof MyTiledMapRendererActorFrontLayer){
        		this.setZIndex(actor.getZIndex()-1);
        	}
        }
    }
    
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	setPosition(body.getPosition().x - currentRegion.getRegionWidth()/2/PPM, body.getPosition().y - currentRegion.getRegionHeight()/2/PPM + 38/PPM - (currentRegion.originalHeight/PPM - currentRegion.getRegionHeight()/2/PPM));
        setWidth(currentRegion.getRegionWidth() / PPM);
        setHeight(currentRegion.getRegionHeight() / PPM);
        setRotation((float)Math.toDegrees(body.getAngle()));
        setOrigin(currentRegion.getRegionWidth()/2/PPM, currentRegion.getRegionHeight()/2/PPM);
        if(animate) currentRegion = (AtlasRegion)animationManager.animate(delta);
        
        if(((UserData)body.getFixtureList().get(0).getUserData()).active && !active)
        	active = true;
        
        if(active){
        	animate = true;
        	active = false;
			CustomActionManager.getInstance().registerAction(new CustomAction(level) {
				@Override
				public void perform() {
					setAnimationState();
					animate = false;
					((UserData)body.getFixtureList().get(0).getUserData()).active = false;
					Logger.log(this, "CA LEVEL : " + level);
					animationManager.getCurrentAnimation().resetLoops();
					currentRegion = (AtlasRegion)animationManager.animate(0f);
				}
			});
        }
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentRegion, getX(), getY()+10/PPM, getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
}

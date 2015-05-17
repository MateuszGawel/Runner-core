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
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ForceField extends Actor implements Poolable{

	public boolean alive;
	private Body body;
	
	//parameters
	private int level = 1;
	private AtlasRegion currentRegion;
	private TextureAtlas atlas;
	private CustomAction pulseAction;
	
	public ForceField(World world, GameWorld gameWorld){
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        currentRegion = atlas.findRegion("forcefield");
        
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		body = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.3f, 1.5f);
		UserData userData = new UserData("forceField");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;
		
		FixtureDef fixtureDef = Materials.obstacleBody;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData( userData );
		
		body.setUserData( userData );
		
		setScale(0);
		gameWorld.getWorldStage().addActor(this);
		
		pulseAction = new CustomAction(0.01f, 0) {
			float scale = 1;
			boolean growing;
			@Override
			public void perform() {
				
				if(growing){
					scale+=0.01f;
					if(scale >= 1)
						growing = false;
				}
				else{
					scale -= 0.01f;
					if(scale <= 0.85)
						growing = true;
				}
				setScale(scale);
			}
		};
	}
    private void appear(){
    	CustomActionManager.getInstance().registerAction(new CustomAction(0.01f, 10) {
			float scale = 0;
			
			@Override
			public void perform() {
				scale += 0.1f;
				setScale(scale);
			}
			@Override
			public void onFinish(){
				CustomActionManager.getInstance().registerAction(pulseAction);
			}
		});
    }

    public void init(Character characterOwner, int level) {
    	this.level = level;
    	alive = true;
    	((UserData)body.getFixtureList().get(0).getUserData()).abilityLevel = level;
    	((UserData)body.getFixtureList().get(0).getUserData()).playerName = characterOwner.playerName;
        body.setTransform(characterOwner.getX()-0.5f, characterOwner.getY()+1, 0);
		appear();
    	setWidth(0.6f);
        setHeight(3);
        setOrigin(getWidth()/2, getHeight()/2);
    }
    
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	setPosition(body.getPosition().x - currentRegion.getRegionWidth()/2/PPM, body.getPosition().y - currentRegion.getRegionHeight()/2/PPM);
        setWidth(currentRegion.getRegionWidth() / PPM);
        setHeight(currentRegion.getRegionHeight() / PPM);
        
    }
    
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(currentRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
	}
	
	public void setLevel(int level) {
		this.level = level;
	}
	
	@Override
	public void reset() {
		setScale(0);
		body.setTransform(-100, 0, 0);
        alive = false;
        CustomActionManager.getInstance().unregisterAction(pulseAction);
	}
}

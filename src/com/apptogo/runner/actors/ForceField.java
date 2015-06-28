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
	private Body bodylvl1, bodylvl2, bodylvl3;
	
	//parameters
	private int level = 1;
	private AtlasRegion currentRegion;
	private TextureAtlas atlas;
	private CustomAction pulseAction, despawnAction;
	
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
			return 1f;
		case 2:
			return 1.5f;
		case 3: 
			return 2f;
		default:
			return 1f;
		}
	}

	public ForceField(World world, GameWorld gameWorld){
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        
        
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodylvl1 = world.createBody(bodyDef);
		bodylvl2 = world.createBody(bodyDef);
		bodylvl3 = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(0.3f, 1f);
		UserData userData = new UserData("forceField");
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		userData.bodyWidth = shapeWidth;
		
		FixtureDef fixtureDef = Materials.obstacleBody;
		fixtureDef.shape = shape;
		bodylvl1.createFixture(fixtureDef).setUserData( userData );
		shape.setAsBox(0.3f, 1.5f);
		bodylvl2.createFixture(fixtureDef).setUserData( userData );
		shape.setAsBox(0.3f, 2f);
		bodylvl3.createFixture(fixtureDef).setUserData( userData );
		
		bodylvl1.setUserData( userData );
		bodylvl2.setUserData( userData );
		bodylvl3.setUserData( userData );
		
		setScale(0);
		gameWorld.getWorldStage().addActor(this);
		
		pulseAction = new CustomAction(0.01f, 0) {
			float scale = 1;
			boolean growing;
			@Override
			public void perform() {
				
				if(growing){
					scale+=0.003f;
					if(scale >= 1)
						growing = false;
				}
				else{
					scale -= 0.003f;
					if(scale <= 0.85)
						growing = true;
				}
				setScale(scale);
				
			}
		};

	}
	
    private void disappear(){
    	pulseAction.setFinished(true);
    	
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
			@Override
			public void onFinish(){
				CustomActionManager.getInstance().registerAction(pulseAction);
			}
		});
    }

    public void init(Character characterOwner, int level) {
    	this.level = level;
    	pulseAction.resetAction();
    	currentRegion = atlas.findRegion("forcefieldlvl"+level);
    	alive = true;
    	((UserData)getBody().getFixtureList().get(0).getUserData()).abilityLevel = level;
    	((UserData)getBody().getFixtureList().get(0).getUserData()).playerName = characterOwner.playerName;
    	getBody().setTransform(characterOwner.getX()-0.5f, characterOwner.getY()+getBodyHeight(), 0);
    	setVisible(true);
		appear();
		

        CustomActionManager.getInstance().registerAction(new CustomAction(5) {	
			@Override
			public void perform() {
				disappear();
			}
		});
    }
    
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	
    	setPosition(getBody().getPosition().x-0.5f, 
    			    getBody().getPosition().y-currentRegion.getRegionHeight()/2/PPM);
        setWidth(currentRegion.getRegionWidth() / PPM);
        setHeight(currentRegion.getRegionHeight() / PPM);
        setOrigin(getWidth()/2, getHeight()/2);
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
		getBody().setTransform(-100, 0, 0);
        alive = false;
	}
}

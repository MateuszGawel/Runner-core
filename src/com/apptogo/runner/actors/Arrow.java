package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.Random;

import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Arrow extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body arrowBody;

	private float timeToDisappear = 2;
	private TextureAtlas atlas;
	private TextureRegion arrowRegion;
	private CustomAction despawnAction;
	
	private Random random;
	
	public Arrow(World world, GameWorld gameWorld){
		this.position = new Vector2();
        
        atlas = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/charactersAtlas.pack");
        arrowRegion = atlas.findRegion("arrow");

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		
		CircleShape shape = new CircleShape();
		shape.setRadius(0.08f);
		
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		
		UserData userData = new UserData("arrow");
		userData.bodyWidth = shapeWidth;
		
		arrowBody = world.createBody(bodyDef);
				
		FixtureDef fixtureDef;
		fixtureDef = Materials.arrowBody;
		fixtureDef.shape = shape;
		
		arrowBody.createFixture(fixtureDef).setUserData( userData );
		fixtureDef = Materials.arrowSensor;
		fixtureDef.shape = shape;
		arrowBody.createFixture(fixtureDef).setUserData( userData );
		
		arrowBody.setUserData( userData );
		
		random = new Random();
		despawnAction = new CustomAction(timeToDisappear) {		
			@Override
			public void perform() {
				reset();
			}
		};
	}

    public void init(Character character, int arrowNumber) {
    	alive = true;
        CustomActionManager.getInstance().registerAction(new CustomAction(arrowNumber*0.1f, 1, character) {		
			@Override
			public void perform() {
				((UserData)arrowBody.getFixtureList().get(0).getUserData()).playerName = ((Character)args[0]).playerName;
				((UserData)arrowBody.getFixtureList().get(1).getUserData()).playerName = ((Character)args[0]).playerName;
		    	position.set(((Character)args[0]).getX()+10/PPM, ((Character)args[0]).getY()+40/PPM);
		    	((UserData)arrowBody.getUserData()).active = true;
		    	arrowBody.setActive(true);
		    	arrowBody.setTransform(position, 0.785f);
		    	arrowBody.setLinearVelocity(20 + ((Character)args[0]).getBody().getLinearVelocity().x + random.nextInt(5), 15 +random.nextInt(5));    	
			}
		});

		setOrigin(arrowRegion.getRegionWidth()/2/PPM,  arrowRegion.getRegionHeight()/2/PPM);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		arrowBody.setTransform(position, 45);
        alive = false;
	}
	
	@Override
	public void act(float delta)
	{
		setPosition(arrowBody.getPosition().x - arrowRegion.getRegionWidth()/2/PPM, arrowBody.getPosition().y - arrowRegion.getRegionHeight()/2/PPM);
		if(alive){
	        setWidth(arrowRegion.getRegionWidth() / PPM);
	        setHeight(arrowRegion.getRegionHeight() / PPM);
	        if(arrowBody.isActive() && !((UserData)arrowBody.getUserData()).active){
	        	arrowBody.setActive(false);
	    		despawnAction.resetAction();
	        	CustomActionManager.getInstance().registerAction(despawnAction);
	        }
	        else if(((UserData)arrowBody.getUserData()).active){
	        	setRotation(arrowBody.getLinearVelocity().angle());
	        }
	        	
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(arrowRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}

package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.animation.MyAnimation;
import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Bomb extends Obstacle implements Poolable, Ability{

	public boolean alive;
	private Body explodeSensor;
	private boolean exploding;
	
	//parameters
	private int level = 1;
	private float timeToExplode = 2;
	private float explosionRange = 2;
	
	private CustomAction explodeAction, explodingAction;
	
	public enum BombAnimationState{
		NORMAL
	}
	
	public Bomb(World world, GameWorld gameWorld){
		super(new EllipseMapObject(0,0,32,32), world, "coin", 5, 0.03f, BombAnimationState.NORMAL, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		
		createBody(BodyType.DynamicBody, Materials.bombBody, "bomb");
		createExplosion();
		
//		CircleShape shape = new CircleShape();
//		shape.setRadius(explosionRange);
//		shape.setPosition(new Vector2(0.25f, 0.25f));
//		explodeSensor = createFixture(Materials.obstacleSensor, shape, "bombExplosion");
		//((UserData)explodeSensor.getUserData()).ignoreContact = true;
		
		
		gameWorld.getWorldStage().addActor(this);

		animationManager.setCurrentAnimationState(BombAnimationState.NORMAL);
		currentFrame = animationManager.animate(0f);
	}
	
    public void init(Character characterOwner) {

        body.setTransform(characterOwner.getX()-20/PPM, characterOwner.getY(), 0);
        body.setLinearVelocity(characterOwner.getSpeed()/3, 0);
        body.setUserData(new UserData("bomb", characterOwner.playerName));
        explodeSensor.getFixtureList().get(0).setUserData(new UserData("bombExplosion", characterOwner.playerName));
        alive = true;
        
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
				CustomActionManager.getInstance().registerAction(explodingAction);
			}
		};
		
		CustomActionManager.getInstance().registerAction(explodeAction);	
		
    }
    
    private void explode(){
    	if(!exploding){
	    	CustomActionManager.getInstance().unregisterAction(explodeAction);
	    	CustomActionManager.getInstance().registerAction(explodingAction);	
	    	exploding = true;
    	}
    }
    
    @Override
    public void act(float delta){
    	super.act(delta);
    	if(exploding){ 		
    		explodeSensor.setTransform(body.getPosition().x+0.25f, body.getPosition().y+0.25f, 0);
    	}
    	else if(explodeSensor.getPosition().x > -100)
    		explodeSensor.setTransform(-100, 0, 0);
    	
    	if(((UserData)body.getUserData()).collected){
    		explode();
    		((UserData)body.getUserData()).collected = false;
    	}
    }
    
    private void createExplosion(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		FixtureDef fixtureDef = Materials.obstacleSensor;
		CircleShape shape = new CircleShape();
		shape.setRadius(explosionRange);
		shape.setPosition(new Vector2(0, 0));
		
		fixtureDef.shape = shape;
		
		explodeSensor = world.createBody(bodyDef);
		explodeSensor.createFixture(fixtureDef);
		explodeSensor.setTransform(-100, 0, 0);
    }
    
	@Override
	public void reset() {
		position.set(-100, 0);
		body.setTransform(position, 0);
        alive = false;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
		
		switch(level){
			case 1:
				timeToExplode = 2;
				explosionRange = 2;
				break;
			case 2:
				timeToExplode = 3;
				explosionRange = 3;
				break;
			case 3:
				timeToExplode = 4;
				explosionRange = 4;
				break;
			default:
				throw new RuntimeException("Wrong ability level: " + level + " is not allowed. Choose 1, 2 or 3");
		}
	}
}

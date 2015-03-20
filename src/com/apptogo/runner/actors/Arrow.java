package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.CustomAction;
import com.apptogo.runner.handlers.CustomActionManager;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.main.Runner;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Box2DVars;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class Arrow extends Obstacle implements Poolable, Ability{

	private Vector2 position;
	public boolean alive;
	private Body arrowBody;
	private GameWorld gameWorld;

	private float timeToDisappear = 2;
	private TextureRegion arrowRegion;
	
	//parameters
	private int level = 1;
	
	//arrow things
	float dragConstant;
	Vector2 pointingDirection;
	Vector2 flightDirection;
	float flightSpeed;
	float dot;
	float dragForceMagnitude;
	Vector2 arrowTailPosition;
	
	public Arrow(World world, GameWorld gameWorld){
		super(((TextureAtlas)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/characters.pack")).findRegion("arrow"));
		this.position = new Vector2();
        this.alive = false;
        this.gameWorld = gameWorld;

        Vector2 bodySize = new Vector2(25 / PPM, 2 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(bodySize.x, bodySize.y);
		
		arrowBody = world.createBody(bodyDef);
				
		FixtureDef fixtureDef;
		fixtureDef = Materials.arrowBody;
		fixtureDef.shape = shape;

		dragConstant = 0.007f;
		arrowBody.setAngularDamping(10);

	}

    public void init(Character character) {
    	position.set(character.getX()+10/PPM, character.getY()+50/PPM);
    	//http://www.iforce2d.net/b2dtut/sticky-projectiles
    	arrowBody.setActive(true);
    	//dla potomnych
    	//nadaj¹c si³ê dla strza³y w x i y musimy dobraæ odpowiedni k¹t
    	//trzeba policzyc x/y i potem sprawdziæ w necie dla ilu stopni tangens=to co nam wyjdzie
    	//tym sposobem mamy k¹t, jeszcze trzeba go zamieniæ na radiany bo setTransform przyjmuje radiany
    	
    	arrowBody.setUserData(new UserData("arrow", character.playerName));
    	arrowBody.setTransform(position, 0.28f);
    	arrowBody.setLinearVelocity(45, 10);
        alive = true;
		
		CustomActionManager.getInstance().registerAction(new CustomAction(timeToDisappear) {	
			@Override
			public void perform() {
				reset();
			}
		});	
		setOrigin(currentFrame.getRegionWidth()/2/PPM,  currentFrame.getRegionHeight()/2/PPM);
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
		pointingDirection = arrowBody.getWorldVector(new Vector2(1/PPM, 0));
		arrowTailPosition = arrowBody.getWorldPoint(new Vector2(-25/PPM, 0));
		flightDirection = arrowBody.getLinearVelocity();
		flightSpeed = flightDirection.len();
		dot = Vector2.dot(flightDirection.x, flightDirection.y, pointingDirection.x, pointingDirection.y); //dot to iloczyn skalarny
		dragForceMagnitude = (1 - Math.abs(dot)) * flightSpeed * flightSpeed * dragConstant * arrowBody.getMass();
	    arrowBody.applyForce(new Vector2(dragForceMagnitude * -flightDirection.x, dragForceMagnitude * -flightDirection.y), arrowTailPosition, true);
        
	    setPosition(arrowBody.getPosition().x - currentFrame.getRegionWidth()/2/PPM, arrowBody.getPosition().y - currentFrame.getRegionHeight()/2/PPM);
        setWidth(currentFrame.getRegionWidth() / PPM);
        setHeight(currentFrame.getRegionHeight() / PPM);
        setRotation(arrowBody.getAngle() * MathUtils.radiansToDegrees);
       // if(((UserData)arrowBody.getUserData()).active)
        	//arrowBody.setActive(false);
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
	}
}

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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
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

public class Arrow extends Actor implements Poolable{

	private Vector2 position;
	public boolean alive;
	private Body arrowBody;

	private float timeToDisappear = 2;
	
	private Texture arrowTexture;
	private TextureRegion arrowRegion;
	
	//arrow things
	float dragConstant;
	Vector2 pointingDirection;
	Vector2 flightDirection;
	float flightSpeed;
	float dot;
	float dragForceMagnitude;
	Vector2 arrowTailPosition;
	
	private Random random;
	
	//jesli zajdzie potrzeba zeby strzala sie wbijala do ruchomego obiektu to trzeba bedzie weldjointa zrobic.
	public Arrow(World world, GameWorld gameWorld){
		this.position = new Vector2();
        this.alive = false;
        
        arrowTexture = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/arrow.png");

        Vector2 bodySize = new Vector2(25 / PPM, 2 / PPM);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(Runner.SCREEN_WIDTH / 2 / PPM, 800 / PPM));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(bodySize.x, bodySize.y);
		
		float shapeWidth = Box2DVars.getShapeWidth(shape);
		
		UserData userData = new UserData("arrow");
		userData.bodyWidth = shapeWidth;
		
		arrowBody = world.createBody(bodyDef);
				
		FixtureDef fixtureDef;
		fixtureDef = Materials.arrowBody;
		fixtureDef.shape = shape;
		
		arrowBody.createFixture(fixtureDef).setUserData( userData );
		arrowBody.setUserData( userData );
		
		arrowRegion = new TextureRegion(arrowTexture);
		
		//shape.setAsBox(3/PPM, 3/PPM, new Vector2(12/PPM, 0), 0);
		//fixtureDef = Materials.arrowheadBody;
		//fixtureDef.shape = shape;
		//arrowBody.createFixture(fixtureDef).setUserData( new UserData("player") );
		
		random = new Random();

		//Logger.log(this, "Koniec: " + arrowTailPosition.x + " " + arrowTailPosition.y);
		//Logger.log(this, "player: " + player.getX());

	}

    public void init(Character character, int arrowNumber) {

//        CustomActionManager.getInstance().registerAction(new CustomAction(timeToDisappear) {		
//			@Override
//			public void perform() {
//				reset();
//			}
//		});
        
        CustomActionManager.getInstance().registerAction(new CustomAction(arrowNumber*0.1f, 1, character) {		
			@Override
			public void perform() {
		    	position.set(((Character)args[0]).getX()+10/PPM, ((Character)args[0]).getY()+40/PPM);
		    	//http://www.iforce2d.net/b2dtut/sticky-projectiles
		    	((UserData)arrowBody.getUserData()).active = true;
		    	arrowBody.setActive(true);
		    	//dla potomnych
		    	//nadaj�c si�� dla strza�y w x i y musimy dobra� odpowiedni k�t
		    	//trzeba policzyc x/y i potem sprawdzi� w necie dla ilu stopni tangens=to co nam wyjdzie
		    	//tym sposobem mamy k�t, jeszcze trzeba go zamieni� na radiany bo setTransform przyjmuje radiany
		    	
		    	arrowBody.setTransform(position, 0.785f);
		    	//arrowBody.setLinearVelocity(55 + ((Character)args[0]).getBody().getLinearVelocity().x/2 + random.nextInt(10), 14 + random.nextInt(10));
		    	
				dragConstant = 0.1f;
				//arrowBody.setAngularDamping(10);
		    	arrowBody.setLinearVelocity(25, 25);
		    	alive = true;
			}
		});

		setOrigin(arrowTexture.getWidth()/2/PPM,  arrowTexture.getHeight()/2/PPM);
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
		if(alive){
			pointingDirection = arrowBody.getWorldVector(new Vector2(1/PPM, 0));
			arrowTailPosition = arrowBody.getWorldPoint(new Vector2(-25/PPM, 0));
			flightDirection = arrowBody.getLinearVelocity();
			flightSpeed = flightDirection.nor().len();
			dot = Vector2.dot(flightDirection.x, flightDirection.y, pointingDirection.x, pointingDirection.y); //dot to iloczyn skalarny
			dragForceMagnitude = (1 - Math.abs(dot)) * flightSpeed * flightSpeed * dragConstant * arrowBody.getMass();
		    //arrowBody.applyForce(new Vector2(dragForceMagnitude * -flightDirection.x, dragForceMagnitude * -flightDirection.y), arrowTailPosition, true);
	        setPosition(arrowBody.getPosition().x - arrowTexture.getWidth()/2/PPM, arrowBody.getPosition().y - arrowTexture.getHeight()/2/PPM);
	        setWidth(arrowTexture.getWidth() / PPM);
	        setHeight(arrowTexture.getHeight() / PPM);
	        setRotation(arrowBody.getAngle() * MathUtils.radiansToDegrees);
	        if(!((UserData)arrowBody.getUserData()).active){
	        	arrowBody.setActive(false);
	        }
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		batch.draw(arrowRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
}

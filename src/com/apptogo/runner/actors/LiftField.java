package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import java.util.ArrayList;
import java.util.List;

import com.apptogo.runner.vars.Materials;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class LiftField extends Actor{

	private Body fieldBody;
	private Alien player;
	private float timeToDisappear = 2;
	private boolean active = false;
	private List<Body> bodiesToLift = new ArrayList<Body>();
	//private Texture arrowTexture;
	//private TextureRegion arrowRegion;
	private float timeElapsed = 0;
	private float stateTime = 0;
	private float speedOfGrowing = 0.02f;
	private Vector2 enemyForceStrength = new Vector2(-20f, 150f);
	private Vector2 obstacleForceStrength = new Vector2(-10f, 50f);
	public float initialBoost = 50f;
	
	public LiftField(Alien player, World world){
        this.player = player;
        
        //arrowTexture = ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "gfx/game/characters/arrow.png");

        
        BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		
		CircleShape shape = new CircleShape();
		FixtureDef fixtureDef;
		
		fieldBody = world.createBody(bodyDef);
		fieldBody.setUserData("liftField");

		shape.setRadius(200/PPM);
		fixtureDef = Materials.worldObjectBody;
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		fieldBody.createFixture(fixtureDef).setUserData("liftField");
		fieldBody.setTransform(-100f, 0, 0);
	}

    public void init() {
    	active = true;
    	stateTime = 0;
    	timeElapsed = 0;
    	fieldBody.getFixtureList().get(0).getShape().setRadius(200/PPM);
		Timer.schedule(new Task() {
			@Override
			public void run() {
				reset();
				active = false;
			}
		}, timeToDisappear);
		
		/*
		Timer.schedule(new Task() {
			@Override
			public void run() {
				reset();
				fieldBody.getFixtureList().get(0).getShape().setRadius(fieldBody.getFixtureList().get(0).getShape().getRadius()+1/PPM);
				if(fieldBody.getFixtureList().get(0).getShape().getRadius() > 200 / PPM)
					this.cancel();
			}
		}, 0.01f, 0.01f);
		*/
    }
    
	public void reset() {
		fieldBody.setTransform(-100f, 0, 45);
	}
	
	public void addBodyToLift(Body bodyToLift){
		bodiesToLift.add(bodyToLift);
		//Logger.log(this, "dodalem body: " + bodyToLift.getUserData());
	}
	public void removeBodyToLift(Body bodyToLift){
		bodiesToLift.remove(bodyToLift);
	}

	
	@Override
	public void act(float delta){
		
		stateTime += delta;
		
		if((stateTime - timeElapsed)/speedOfGrowing >= 1){
			timeElapsed+=speedOfGrowing;
			fieldBody.getFixtureList().get(0).getShape().setRadius(fieldBody.getFixtureList().get(0).getShape().getRadius()+1/PPM);
		}
		
		
		if(active)fieldBody.setTransform(player.getX() + 50/PPM, player.getY() + 5/PPM, 0);
		//Logger.log(this, "player " + player.getX() + "  " + player.getY());
        setPosition(player.getX(), player.getY());
        //setWidth(arrowTexture.getWidth() / PPM);
        //setHeight(arrowTexture.getHeight() / PPM);
        //setRotation(fieldBody.getAngle() * MathUtils.radiansToDegrees);
        for(Body bodyToLift : bodiesToLift){
        	if(bodyToLift.getUserData() == "enemy")
        		bodyToLift.applyForceToCenter(enemyForceStrength, true);
        	else
        		bodyToLift.applyForceToCenter(obstacleForceStrength, true);
        }
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		//batch.draw(arrowRegion, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1, 1, getRotation());
	}
	
	public void setActive(boolean active){ this.active = active; }
	public boolean isActive(){ return this.active; }
}

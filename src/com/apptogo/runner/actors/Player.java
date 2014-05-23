package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.handlers.Logger;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Player extends Actor{

	private World world;
	private float stateTime = 0f;
	private PlayerState currentState = PlayerState.IDLE;
	
	private Body playerBody;
	
	public Player(World world){
		this.world = world;
		createPlayerBody();
	}
	
	public enum PlayerState{
		IDLE, RUNNING, JUMPING
	}
	
	private void createPlayerBody(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(50 / PPM, 200 / PPM));
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(10 / PPM, 10 / PPM);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.8f;
		fixtureDef.restitution = 0.0f;
		fixtureDef.shape = shape;
		
		playerBody = world.createBody(bodyDef);
		playerBody.createFixture(fixtureDef);
	}
	
	public void jump(){
		playerBody.applyForceToCenter(0, 10, true);
	}
	
	public void startRunning(){
		currentState = PlayerState.RUNNING;
		
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
		if(currentState == PlayerState.RUNNING)
			playerBody.setLinearVelocity(1, playerBody.getLinearVelocity().y);
        setPosition(playerBody.getPosition().x, playerBody.getPosition().y);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
	
	public PlayerState getCurrentState(){ return this.currentState; }
	public Body getPlayerBody(){ return this.playerBody; }
}

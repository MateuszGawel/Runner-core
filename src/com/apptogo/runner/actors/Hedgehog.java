package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Hedgehog extends Obstacle{

	private float direction = -1;
	private float prevDirection = 1;
	
	public enum HedgehogAnimationState{
		WALKING
	}
	
	
	public Hedgehog(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/hedgehog.pack", "hedgehog", 12, 0.05f, HedgehogAnimationState.WALKING);
		super.animate = true;
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.DynamicBody, Materials.obstacleGhostBody, "hedgehog");
		createFixture(Materials.obstacleSensor, "hedgehog");
		setOffset(-10f/PPM, -15f/PPM);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		
		if(Math.abs(getBody().getLinearVelocity().x) < 0.1f && direction!=prevDirection){
			prevDirection = direction;
			direction*= -1;
		}
		
		if(direction == 1 && !currentFrame.isFlipX())
			currentFrame.flip(true, false);
		else if(direction == -1 && currentFrame.isFlipX())
			currentFrame.flip(true, false);
		
		getBody().setLinearVelocity(1f * direction, 0);
	}
}

package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Hedgehog extends Obstacle{
	
	public enum HedgehogAnimationState{
		WALKING
	}
	
	private float direction = 1;
	
	public Hedgehog(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/hedgehog.pack", "hedgehog", 12, 0.05f, HedgehogAnimationState.WALKING);
		setAnimate(true);
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.DynamicBody, Materials.obstacleGhostBody, "hedgehog");
		createFixture(Materials.obstacleSensor, "killingBottom");
		setOffset(-60f/PPM, -30f/PPM);
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		if(((UserData)getBody().getUserData()).changeDirection){
			((UserData)getBody().getUserData()).changeDirection = false;
			direction*=-1;
		}
		if(direction == 1 && !currentFrame.isFlipX())
			currentFrame.flip(true, false);
		else if(direction == -1 && currentFrame.isFlipX())
			currentFrame.flip(true, false);
		getBody().setLinearVelocity(1f * direction, 0);
	}
}

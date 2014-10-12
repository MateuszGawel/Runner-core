package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.CharacterAbilityType;
import com.apptogo.runner.enums.PowerupType;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class RockBig extends Obstacle{

	private float stateTime = 0;
	private float timeElapsed = 0;
	private float interval = 3f;
	private Vector2 velocity = new Vector2(0, 0.4f);

	
	public RockBig(MapObject object, World world, GameWorld gameWorld)
	{
		super(object, world, "gfx/game/levels/rockBig.png");
				
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.KinematicBody, Materials.obstacleBody, "active");
		setOffset(-5/PPM, -currentFrame.getTexture().getHeight()/PPM);
	}
	
	
	@Override
	public void act(float delta){
		super.act(delta);	
		stateTime += delta;
		if((stateTime - timeElapsed)/interval >= 1){
			timeElapsed+=interval;
			velocity.y *= -1;
		}
		getBody().setLinearVelocity(velocity);
	}
}

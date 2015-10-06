package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
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
		super(object, world, "rockBig", GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		if(object.getProperties().containsKey("delay"))
			timeElapsed = Float.valueOf(object.getProperties().get("delay").toString());
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.KinematicBody, Materials.worldObjectBody, "nonkilling");
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
		setPosition(body.getPosition().x - 15/PPM, body.getPosition().y - currentFrame.getRegionHeight()/PPM);
	}
}

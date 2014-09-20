package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Bush extends Obstacle{

	private float initialX, initialY;
	
	public Bush(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/bush.png");
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.DynamicBody, Materials.bushBody, "bush");
		initialX = getBody().getPosition().x;
		initialY = getBody().getPosition().y;
		setOffset(-12/PPM, -12/PPM);
	}
	@Override
	public void act(float delta){
		super.act(delta);
		if(getBody().getAngularVelocity() < 15)
			getBody().applyTorque(9f, true);
		if(getBody().getPosition().x < 0)
			getBody().setTransform(initialX, initialY, 0);
	}
}

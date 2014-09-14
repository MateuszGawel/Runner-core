package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Bonfire extends Obstacle{

	public enum BonfireAnimationState{
		NORMAL
	}
	
	
	public Bonfire(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/bonfire.pack", "bonfire", 33, 0.05f, BonfireAnimationState.NORMAL);
		super.animate = true;
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleBody, "killing");
		setOffset(-15f/PPM, -15f/PPM);
	}
}

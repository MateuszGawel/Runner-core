package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;

public class Flag extends Obstacle{

	public enum FlagAnimationState{
		NORMAL
	}
	
	public Flag(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "flag", 35, 0.03f, FlagAnimationState.NORMAL, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		setAnimate(true);
		gameWorld.getWorldStage().addActor(this);
		createShape();
		setUpdatePosition(true);
		setOffset(-35f/PPM, -30f/PPM);		
	}
}

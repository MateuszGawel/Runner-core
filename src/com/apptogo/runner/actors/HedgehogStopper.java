package com.apptogo.runner.actors;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class HedgehogStopper extends Obstacle{
	
	public HedgehogStopper(MapObject object, World world, GameWorld gameWorld){
		super(object, world, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		setAnimate(false);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "hedgehogStopper");
	}
}

package com.apptogo.runner.actors;

import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class HedgehogStopper extends Obstacle{
	
	public HedgehogStopper(MapObject object, World world, GameWorld gameWorld){
		super(object, world);
		setAnimate(false);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "hedgehogStopper");
	}
}

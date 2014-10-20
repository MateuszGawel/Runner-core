package com.apptogo.runner.actors;

import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Barrel extends Obstacle{

	/* W tym wypadku nie jest to konieczne
	public enum BarrelAnimationState{
		NORMAL
	}
	*/
	
	public Barrel(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/barrelSmall.png");
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleBody, "barrel");
	}
	
	public void act(float delta){
		super.act(delta);
		if(((UserData)getBody().getUserData()).active && getBody().getType() != BodyType.DynamicBody)
			getBody().setType(BodyType.DynamicBody);
	}
}

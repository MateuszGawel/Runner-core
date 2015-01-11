package com.apptogo.runner.actors;

import com.apptogo.runner.userdata.UserData;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Pool.Poolable;

public class PoolableBody implements Poolable{

	public Body body;

	
	public PoolableBody(BodyDef bodyDef, FixtureDef fixtureDef, String userData, World world){
		body = world.createBody(bodyDef);
		body.createFixture(fixtureDef).setUserData( new UserData("coin") );
		body.setUserData( new UserData("coin") );
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}

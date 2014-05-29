package com.apptogo.runner.vars;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.apptogo.runner.vars.Box2DVars;
public class Materials {
	
	public static FixtureDef playerBody;
	public static FixtureDef enemyBody;
	public static FixtureDef groundBody;
	
	static
	{
		playerBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_PLAYER, Box2DVars.BIT_GROUND);
		enemyBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_ENEMY, Box2DVars.BIT_GROUND);
		groundBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_GROUND, (short)(Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER));
	}
	
	private static FixtureDef createFixtureDef(float density, float friction, float restitution, short categoryBits, short maskBits)
	{
		FixtureDef f = new FixtureDef();
		
		f.density = density;
		f.friction = friction;
		f.restitution = restitution;
		f.filter.categoryBits = categoryBits;
		f.filter.maskBits = maskBits;
		
		return f;
	}

}

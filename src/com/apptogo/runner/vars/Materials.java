package com.apptogo.runner.vars;

import com.badlogic.gdx.physics.box2d.FixtureDef;
public class Materials {
	
	public static FixtureDef playerBody;
	public static FixtureDef wallSensorBody;
	public static FixtureDef enemyBody;
	public static FixtureDef groundBody;
	public static FixtureDef killingBody;
	public static FixtureDef footSensorBody;
	
	static
	{
		playerBody = createFixtureDef(30f, 0.2f, 0f, Box2DVars.BIT_PLAYER, (short)(Box2DVars.BIT_GROUND | Box2DVars.BIT_KILLING), false);
		wallSensorBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_WALLSENSOR, Box2DVars.BIT_GROUND, false);
		footSensorBody = createFixtureDef(0f, 0f, 0f, Box2DVars.BIT_FOOTSENSOR, Box2DVars.BIT_GROUND, true);
		enemyBody = createFixtureDef(30f, 10f, 0f, Box2DVars.BIT_ENEMY, Box2DVars.BIT_GROUND, false);
		groundBody = createFixtureDef(30f, 0.5f, 0f, Box2DVars.BIT_GROUND, (short)(Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALLSENSOR | Box2DVars.BIT_FOOTSENSOR), false);
		killingBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_KILLING, (short)(Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER), true);
	}
	
	private static FixtureDef createFixtureDef(float density, float friction, float restitution, short categoryBits, short maskBits, boolean sensor)
	{
		FixtureDef f = new FixtureDef();
		
		f.density = density;
		f.friction = friction;
		f.restitution = restitution;
		f.filter.categoryBits = categoryBits;
		f.filter.maskBits = maskBits;
		f.isSensor = sensor;
		
		return f;
	}

}

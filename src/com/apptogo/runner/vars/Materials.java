package com.apptogo.runner.vars;

import com.badlogic.gdx.physics.box2d.FixtureDef;
public class Materials {
	
	public static FixtureDef playerBody;
	public static FixtureDef playerSlidingBody;
	public static FixtureDef wallSensorBody;
	public static FixtureDef enemyBody;
	public static FixtureDef groundBody;
	public static FixtureDef objectBody;
	public static FixtureDef obstacleBody;
	public static FixtureDef footSensorBody;
	public static FixtureDef bombBody;
	public static FixtureDef barrelBody;
	public static FixtureDef arrowBody;
	public static FixtureDef bodyMemberBody;
	public static FixtureDef mushroomBody;
	
	static
	{
		playerBody = createFixtureDef(12f, 0.2f, 0f, Box2DVars.BIT_PLAYER, (short)(Box2DVars.BIT_NONKILLING | Box2DVars.BIT_KILLING | Box2DVars.BIT_BARREL), false);
		playerSlidingBody = createFixtureDef(5f, 0.15f, 0f, Box2DVars.BIT_PLAYER, (short)(Box2DVars.BIT_NONKILLING | Box2DVars.BIT_KILLING), false);
		wallSensorBody = createFixtureDef(0f, 0f, 0f, Box2DVars.BIT_WALLSENSOR, Box2DVars.BIT_NONKILLING, true);
		footSensorBody = createFixtureDef(0f, 0f, 0f, Box2DVars.BIT_FOOTSENSOR, Box2DVars.BIT_NONKILLING, true);
		enemyBody = createFixtureDef(30f, 10f, 0f, Box2DVars.BIT_ENEMY, Box2DVars.BIT_NONKILLING, false);
		groundBody = createFixtureDef(30f, 0.5f, 0f, Box2DVars.BIT_NONKILLING, (short)(Box2DVars.BIT_BODYMEMBER | Box2DVars.BIT_BARREL | Box2DVars.BIT_ARROW |Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALLSENSOR | Box2DVars.BIT_FOOTSENSOR | Box2DVars.BIT_BOMB), false);
		objectBody = createFixtureDef(30f, 0.5f, 0f, Box2DVars.BIT_NONKILLING, (short)(Box2DVars.BIT_BODYMEMBER | Box2DVars.BIT_BARREL | Box2DVars.BIT_ARROW | Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALLSENSOR | Box2DVars.BIT_FOOTSENSOR | Box2DVars.BIT_BOMB), false);
		obstacleBody = createFixtureDef(30f, 0f, 0f, Box2DVars.BIT_KILLING, (short)(Box2DVars.BIT_BODYMEMBER | Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER), true);
		bombBody = createFixtureDef(10f, 0.1f, 0.5f, Box2DVars.BIT_BOMB, Box2DVars.BIT_NONKILLING, false);
		barrelBody = createFixtureDef(30f, 1f, 0f, Box2DVars.BIT_BARREL, (short)(Box2DVars.BIT_BARREL | Box2DVars.BIT_NONKILLING | Box2DVars.BIT_PLAYER), false);
		arrowBody = createFixtureDef(1f, 0.1f, 0.1f, Box2DVars.BIT_ARROW, (short)(Box2DVars.BIT_NONKILLING | Box2DVars.BIT_KILLING), false);
		bodyMemberBody = createFixtureDef(10f, 0.5f, 0.1f, Box2DVars.BIT_BODYMEMBER, (short)(Box2DVars.BIT_BODYMEMBER | Box2DVars.BIT_NONKILLING | Box2DVars.BIT_KILLING), false);
		mushroomBody = createFixtureDef(10f, 0.5f, 0.1f, Box2DVars.BIT_NONKILLING, (short)(Box2DVars.BIT_BODYMEMBER | Box2DVars.BIT_BARREL | Box2DVars.BIT_ARROW |Box2DVars.BIT_ENEMY | Box2DVars.BIT_PLAYER | Box2DVars.BIT_WALLSENSOR | Box2DVars.BIT_FOOTSENSOR | Box2DVars.BIT_BOMB), false);
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

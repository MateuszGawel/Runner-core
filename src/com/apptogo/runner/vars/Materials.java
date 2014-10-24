package com.apptogo.runner.vars;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import static com.apptogo.runner.vars.Box2DVars.*;
public class Materials {
	
	public static FixtureDef characterBody;
	public static FixtureDef characterSensor;
	public static FixtureDef bodyMemberBody;
	
	public static FixtureDef terrainBody;
	public static FixtureDef worldObjectBody;
	
	public static FixtureDef obstacleGhostBody;
	public static FixtureDef obstacleBody;
	public static FixtureDef obstacleSensor;
	
	public static FixtureDef bombBody;
	public static FixtureDef arrowBody;
	public static FixtureDef fieldBody;
	
	static
	{
		//player
		characterBody = createFixtureDef(12f, 0.2f, 0f, BIT_CHARACTER, (short)(BIT_TERRAIN | BIT_WORLD_OBJECT | BIT_ABILITY), false); //postaæ
		characterSensor = createFixtureDef(0f, 0f, 0f, BIT_CHARACTER_SENSOR, (short)(BIT_WORLD_OBJECT | BIT_TERRAIN), true); //obydwa sensory
		bodyMemberBody = createFixtureDef(10f, 0.5f, 0.1f, BIT_WORLD_OBJECT, (short)(BIT_WORLD_OBJECT | BIT_TERRAIN), false); //czesci cia³a
		
		//world (podzial dla busha)
		terrainBody = createFixtureDef(30f, 0.5f, 0f, BIT_TERRAIN, (short)(BIT_WORLD_OBJECT | BIT_CHARACTER | BIT_CHARACTER_SENSOR | BIT_ABILITY), false); //statyczny ground
		worldObjectBody = createFixtureDef(30f, 0.5f, 0f, BIT_WORLD_OBJECT, (short)(BIT_CHARACTER | BIT_WORLD_OBJECT | BIT_CHARACTER_SENSOR | BIT_ABILITY), false); //statyczne obiekty
		
		//obstacle
		obstacleGhostBody = createFixtureDef(1000f, 0.1f, 0.1f, BIT_WORLD_OBJECT, (short)(BIT_TERRAIN | BIT_WORLD_OBJECT), false); //bez kolizji z playerem, trzeba sensor jesli ma byc wykrywane
		obstacleBody = createFixtureDef(500f, 1f, 0f, BIT_WORLD_OBJECT, (short)(BIT_TERRAIN | BIT_WORLD_OBJECT | BIT_CHARACTER | BIT_CHARACTER_SENSOR), false); //z kolizja z playerem, nie trzeba sensora
		obstacleSensor = createFixtureDef(0.1f, 1f, 0.8f, BIT_WORLD_OBJECT, (short)(BIT_CHARACTER | BIT_CHARACTER_SENSOR), true); //wystarczy jesli przeszkoda jest statyczna
		
		bombBody = createFixtureDef(10f, 0.1f, 0.5f, BIT_ABILITY, (short)(BIT_CHARACTER | BIT_TERRAIN | BIT_WORLD_OBJECT), false);
		arrowBody = createFixtureDef(1f, 0.1f, 0.1f, BIT_ABILITY, (short)(BIT_CHARACTER | BIT_TERRAIN | BIT_WORLD_OBJECT), false);
		fieldBody = createFixtureDef(1f, 0.1f, 0.1f, BIT_ABILITY, (short)(BIT_CHARACTER | BIT_WORLD_OBJECT), true);
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

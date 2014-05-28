package com.apptogo.runner.vars;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Materials {
	
	public static FixtureDef HumanBody;
	
	static
	{
		HumanBody = createFixtureDef(30f, 0f, 0f);
	}
	
	private static FixtureDef createFixtureDef(float density, float friction, float restitution)
	{
		FixtureDef f = new FixtureDef();
		
		f.density = density;
		f.friction = friction;
		f.restitution = restitution;
		
		return f;
	}

}

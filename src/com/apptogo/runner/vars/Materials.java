package com.apptogo.runner.vars;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Materials {
	
	private class MaterialProperties
	{
		protected float density     = 1.0f;
		protected float friction    = 0.8f;
		protected float restitution = 0.0f;
		
		protected MaterialProperties() {}
		
		protected MaterialProperties(float density, float friction, float restitution)
		{
			this.density = density;
			this.friction = friction;
			this.restitution = restitution;
		}
	}
	
	//singleton
	private static final Materials INSTANCE = new Materials();
	public static Materials getInstance(){ return INSTANCE;	}
	
	Map <String, MaterialProperties> materials;
	
	private Materials()
	{
		materials = new HashMap<String, MaterialProperties>();
		
		//list of materials:
		materials.put( "ALUMINIUM", new MaterialProperties( 2720, 0, 0  ) );
		materials.put( "CORC", new MaterialProperties( 700, 0, 0  ) );
		materials.put( "WOOD", new MaterialProperties( 700, 0, 0  ) );
		materials.put( "ICE", new MaterialProperties( 700, 0, 0  ) );
		materials.put( "PLASTIC", new MaterialProperties( 700, 0, 0  ) );
	}
	
	public FixtureDef getMaterial(String materialName)
	{
		FixtureDef fixture = new FixtureDef();
		MaterialProperties mProperties = new MaterialProperties();
		
		if( materials.containsKey(materialName) )
		{
			mProperties = materials.get(materialName);
		}
		//if not keep default values
		
		fixture.density = mProperties.density;
		fixture.friction = mProperties.friction;
		fixture.restitution = mProperties.restitution;
		
		return fixture;
	}

}

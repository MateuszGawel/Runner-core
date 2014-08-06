package com.apptogo.runner.levels;

import com.apptogo.runner.world.GameWorld.GameWorldType;
import com.badlogic.gdx.utils.Array;

public class LevelWorld 
{
	public String name;
	public Array<Level> levels = new Array<Level>();
	GameWorldType worldType;
	
	public LevelWorld(String name, GameWorldType worldType)
	{
		this.name = name;
		this.worldType = worldType;
	}
	
	public void setLevels(Array<Level> levels)
	{
		this.levels = levels;
	}
}

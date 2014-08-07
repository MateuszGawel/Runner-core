package com.apptogo.runner.levels;

import com.apptogo.runner.world.GameWorld.GameWorldType;
import com.badlogic.gdx.utils.Array;

public class LevelWorld 
{
	public String name;
	private Array<Level> levels = new Array<Level>();
	private GameWorldType worldType;
	
	public LevelWorld(String name, GameWorldType worldType)
	{
		this.name = name;
		this.worldType = worldType;
	}
	
	public GameWorldType getWorldType()
	{
		return this.worldType;
	}
	
	public void setLevels(Array<Level> levels)
	{
		this.levels = levels;
		
		for(int i = 0; i < this.levels.size; i++)
		{
			this.levels.get(i).worldType = this.worldType;
		}
	}
	public Array<Level> getLevels()
	{
		return this.levels;
	}
}

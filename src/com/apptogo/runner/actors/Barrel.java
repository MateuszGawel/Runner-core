package com.apptogo.runner.actors;

import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Barrel extends Obstacle{

	private UserData userData;
	private Sound sound;
	
	public Barrel(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/barrelSmall.png");
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleBody, "barrel");
		
		userData = ((UserData)getBody().getUserData());
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/barrel.ogg");
	}
	
	@Override
	public void act(float delta) {
    	long startTime = System.nanoTime();
    	super.act(delta);
		if(((UserData)getBody().getUserData()).active && getBody().getType() != BodyType.DynamicBody)
			getBody().setType(BodyType.DynamicBody);
		
		if(userData.playSound){
			userData.playSound = false;
			sound.play(getSoundVolume());
		}
    	long endTime = System.nanoTime();
    	if(gameWorld!=null) gameWorld.barrelArray.add(endTime - startTime);
		
	}
}

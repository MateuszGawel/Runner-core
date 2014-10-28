package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.ScreenType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.screens.GameScreen;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Bonfire extends Obstacle{

	public enum BonfireAnimationState{
		NORMAL
	}
	
	private Sound sound;
	private long soundId;
	
	public Bonfire(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/bonfire.pack", "bonfire", 33, 0.05f, BonfireAnimationState.NORMAL);
		super.animate = true;
		gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "bonfire");
		setOffset(-15f/PPM, -15f/PPM);
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/bonfire.ogg");
		soundId = sound.loop(getSoundVolume());
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		sound.setVolume(soundId, getSoundVolume());
	}
}

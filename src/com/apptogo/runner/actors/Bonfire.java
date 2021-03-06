package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.userdata.UserData;
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
		super(object, world, "bonfire", 33, 0.05f, BonfireAnimationState.NORMAL, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType) );
		setAnimate(true);
		gameWorld.getWorldStage().addActor(this);
		this.setZIndex(1000);
		createBody(BodyType.StaticBody, Materials.obstacleSensor, "obstacle");
		( (UserData) body.getFixtureList().first().getUserData() ).killingDismemberment = true;
		setOffset(-15f/PPM, -15f/PPM);
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/bonfire.ogg");
		
	}
	@Override
	public void act(float delta) {		
		if(soundId == 0)
			soundId = sound.loop(getSoundVolume());
		
		super.act(delta);
		sound.setVolume(soundId, getSoundVolume());
		
	}
}

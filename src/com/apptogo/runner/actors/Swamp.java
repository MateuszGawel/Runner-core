package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.enums.GameWorldType;
import com.apptogo.runner.handlers.ResourcesManager;
import com.apptogo.runner.handlers.ScreensManager;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;

public class Swamp extends Obstacle{

	public enum SwampAnimationState{
		ANIMATING
	}
	
	private int segmentCount;
	private float objectWidth;
	
	private Sound sound;
	private long soundId;
	
	public Swamp(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "swamp", 45, 0.05f, SwampAnimationState.ANIMATING, GameWorldType.convertToAtlasPath(gameWorld.gameWorldType));
		setAnimate(false);
		createBody(BodyType.StaticBody, Materials.worldObjectBody, "swamp");
		gameWorld.getWorldStage().addActor(this);
		setVisible(false);
		objectWidth = ((RectangleMapObject)object).getRectangle().width;
		segmentCount = Math.round((objectWidth - 128f) / 64f);
		
		for(int i=0; i<segmentCount; i++){
			SwampSegment segment = new SwampSegment();
			float offset = getBody().getPosition().x - (objectWidth/2 - (i+1)*64f)/PPM;
			segment.setPosition(offset, getBody().getPosition().y + 10f/PPM);
			gameWorld.getWorldStage().addActor(segment);
		}	
		sound = (Sound)ResourcesManager.getInstance().getResource(ScreensManager.getInstance().getCurrentScreen(), "mfx/game/levels/swamp.ogg");
		soundId = sound.loop(getSoundVolume());
	}
	
	@Override
	public void act(float delta){
		super.act(delta);
		sound.setVolume(soundId, getSoundVolume());
	}
}

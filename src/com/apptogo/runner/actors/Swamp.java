package com.apptogo.runner.actors;

import static com.apptogo.runner.vars.Box2DVars.PPM;

import com.apptogo.runner.logger.Logger;
import com.apptogo.runner.vars.Materials;
import com.apptogo.runner.world.GameWorld;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public class Swamp extends Obstacle{

	public enum SwampAnimationState{
		ANIMATING
	}
	
	private int segmentCount;
	private float objectWidth;
	
	public Swamp(MapObject object, World world, GameWorld gameWorld){
		super(object, world, "gfx/game/levels/swamp.pack", "swamp", 45, 0.05f, SwampAnimationState.ANIMATING);
		super.animate = false;
		//gameWorld.getWorldStage().addActor(this);
		createBody(BodyType.StaticBody, Materials.worldObjectBody, "swamp");
		//setOffset(-((RectangleMapObject)object).getRectangle().width/PPM/2+64/PPM, 10f/PPM);
		
		objectWidth = ((RectangleMapObject)object).getRectangle().width;
		segmentCount = Math.round((objectWidth - 128f) / 64f);
		
		for(int i=0; i<segmentCount; i++){
			SwampSegment segment = new SwampSegment();
			segment.setPosition(getBody().getPosition().x - objectWidth/2/PPM + (i+1)*64f/PPM, getBody().getPosition().y + 10f/PPM);
			gameWorld.getWorldStage().addActor(segment);
		}
		
		Logger.log(this, "width: " + ((RectangleMapObject)object).getRectangle().width);
		Logger.log(this, "bede tworzyc: " + segmentCount);
	}
}
